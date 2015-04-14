package trencadis.infrastructure.services.KeyServer.impl;

import java.io.File;
import java.rmi.RemoteException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.globus.axis.message.addressing.AttributedURIType;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.impl.ReflectionResourceProperty;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.utils.AddressingUtils;

import trencadis.common.util.CertUtils;
import trencadis.common.util.CryptUtils;
import trencadis.infrastructure.TRENCADIS_JAVA_API_SQL_Keys_Database.TRENCADIS_JAVA_API_SQL_Keys_Database;
import trencadis.infrastructure.gateKeeper.CGateKeeper;
import trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS.IIS_Register;
import trencadis.infrastructure.services.stubs.KeyServer.*;

public class KeyServerResource implements Resource, ResourceProperties {
	        
        //It indicates if is in debug mode
        public static boolean bIsDebug = false;
	   
        /*******************************************************************/
        /********** Local parameters ***************************************/
        private String identity;
        private String domain;
	private String base_path;
	private String path_cattrustcert;
	private String tmp_dir;
        /*******************************************************************/  
                        
        /*******************************************************************/
        /****************** IIS Parameters *********************************/
        private String registration_path;
        /*******************************************************************/
        
        /*******************************************************************/
        /****************** GateKeeper Component **************************/
	private CGateKeeper gateKeeper;
        /*******************************************************************/
                                    
        /*******************************************************************/
        /****************** Logger Component *******************************/
        private Logger logger = Logger.getLogger(KeyServerResource.class.getName());
        /*******************************************************************/
        
        /*******************************************************************/
        /****** TRENCADIS Java IIS Register ********************************/
        IIS_Register API_IIS_Register;
        /*******************************************************************/
   
         /*******************************************************************/
        /****** TRENCADIS Java API SQL Keys Database Component *******/
        TRENCADIS_JAVA_API_SQL_Keys_Database API_Keys_db;
        /*******************************************************************/
        
        
        /* Resource Property set */
	private ResourcePropertySet propSet;

	/* Resource properties */
	private KeyServerServiceInfo RPs_KeyServerServiceInfo;
	
	
	/* Initializes RPs */
	public void initialize() throws Exception {
            
              File confPath = new File("/home/trencadis/KeyServer/KeyServerConfig.xml");
               trencadis.infrastructure.services.KeyServer.impl.wrapper.config.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.KeyServer.impl.wrapper.config.XmlWrapper(confPath, false);
                    inputWrapper.wrap();
            
              /***********************************************************************************/
              /****** Parametros Generales de Configuración del Servicio Ontologies Server *******/
              /***********************************************************************************/               
              trencadis.infrastructure.services.KeyServer.impl.wrapper.config.SERVICE_PARAMETERS localParameters = inputWrapper.get_CONFIGURATION().get_SERVICE_PARAMETERS();
              this.identity          = localParameters.get_IDENTITY();
              this.domain            = localParameters.get_DOMAIN();
              this.base_path         = localParameters.get_BASE_PATH();
              this.path_cattrustcert = localParameters.get_PATH_CATRUSTCERT();
              this.tmp_dir           = localParameters.get_TMP_DIR();                				
              /************************************************************************************/
            
                /**************************************************************/
               /********************  COMPONENT GATEKEEPER *******************/
               trencadis.infrastructure.services.KeyServer.impl.wrapper.config.GATEKEEPER_PARAMETERS gatekeeperParameters = inputWrapper.get_CONFIGURATION().get_GATEKEEPER_PARAMETERS();		
               //Initialize the gatekeeper
               this.gateKeeper = new CGateKeeper(
				"org.postgresql.Driver",
				"jdbc:ontologiesServerGatekeeperPool:",
				"jdbc:postgresql://",
				gatekeeperParameters.get_HOST_NAME(),
				gatekeeperParameters.get_HOST_PORT(),
				gatekeeperParameters.get_DB_NAME(),
				gatekeeperParameters.get_USER(), 
                                gatekeeperParameters.get_PASSWORD(),
				this.path_cattrustcert
               );
               /**************************************************************/
              
               /**************************************************************/
               /********************  IIS Parameters   ***********************/                
	       trencadis.infrastructure.services.KeyServer.impl.wrapper.config.INDEX_SERVICE_PARAMETERS indexServiceParameters = inputWrapper.get_CONFIGURATION().get_INDEX_SERVICE_PARAMETERS();	       
               this.registration_path = indexServiceParameters.get_REGISTRATION_PATH();
               /****************************************************************/
              
               /**************************************************************/
               /********************  LOGGER COMPONENT ***********************/                
	       trencadis.infrastructure.services.KeyServer.impl.wrapper.config.LOGGER_PARAMETERS  loggerParameters = inputWrapper.get_CONFIGURATION().get_LOGGER_PARAMETERS() ;
	       FileHandler logFile = new FileHandler(loggerParameters.get_LOG_PATH());
	       logger.addHandler(logFile);
	       logger.setLevel(Level.ALL);
	       SimpleFormatter formatter = new SimpleFormatter();
	       logFile.setFormatter(formatter);
               /**************************************************************/
  
                  /**************************************************************/
                /***** TRENCADIS_JAVA_API_SQL_Keys_Database Component ************/                
		//Initialize connection pool wrapping driver for ontologies                     
                trencadis.infrastructure.services.KeyServer.impl.wrapper.config.BACKEND_PARAMETERS backendParameters = inputWrapper.get_CONFIGURATION().get_BACKEND_PARAMETERS();		                
                API_Keys_db = new TRENCADIS_JAVA_API_SQL_Keys_Database(
                                                                 backendParameters.get_HOST_NAME(), 
                                                                 backendParameters.get_HOST_PORT(), 
                                                                 backendParameters.get_DB_NAME(), 
                                                                 backendParameters.get_USER(),
                                                                 backendParameters.get_PASSWORD());                                
                /**************************************************************/
               
               
if (!bIsDebug){                          
		this.propSet = new SimpleResourcePropertySet(KeyServerQNames.RESOURCE_PROPERTIES);
                
                 try{
                    ResourceProperty keyServerServiceInfo = new ReflectionResourceProperty(
				KeyServerQNames.RP_KEYSERVERSERVICEINFO, "KeyServerServiceInfo", this);
                    this.propSet.add(keyServerServiceInfo);
                } catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		ResourceContext ctx;
		try {
			ctx = ResourceContext.getResourceContext();
                } catch(ResourceContextException e) {
                    logger.log(Level.ALL, "Could not get ResourceContext: " + e);	    	
                    return;
                }
                                                                            
                EndpointReferenceType endpoint;
                try {
                    endpoint = AddressingUtils.createEndpointReference(ctx, null);
                } catch(Exception e) {            
                    logger.log(Level.ALL, "Could not form EPR: " + e);
                    return;
                }		
		
		try {                                                                            
  
                    X509Certificate uUIDCertChainX509 = CertUtils.getServCertificateChain();
			                                                
                    RPs_KeyServerServiceInfo = new KeyServerServiceInfo();
                    RPs_KeyServerServiceInfo.setDomain(CertUtils.getPublicSubjectOU(uUIDCertChainX509));
                    
                    if (identity.equals(new String("-"))) {
    				identity = CertUtils.getPublicSubjectCN(uUIDCertChainX509);
                    }
                    RPs_KeyServerServiceInfo.setID(identity);
			                                                
                    AttributedURIType address = endpoint.getAddress();
                    RPs_KeyServerServiceInfo.setURI(address.toString());
                                                                                                                    
		} catch(Exception e) {  
                    logger.log(Level.ALL, "Exception when trying to initializing this EOUIDGeneratorService: " + e);			
		}        
                

		
/*
		try {
			org.globus.KeyService.wrappers.wrapper_conf_keyservice.XmlWrapper xmlWrapper = 
				new org.globus.KeyService.wrappers.wrapper_conf_keyservice.XmlWrapper(confPath);
			
			X509Certificate keyCertChainX509 = CertUtils.getServCertificateChain();
			String keyCertChainStr = CertUtils.getServExportedCredentials();
			
			setKeyCertChain(keyCertChainStr);
			
			KeyServInfo tmpKeyServInfo = new KeyServInfo();
			tmpKeyServInfo.setKeyServDomain(
					CertUtils.getPublicSubjectOU(keyCertChainX509));
			
			String identity = xmlWrapper.get_config().get_service().get_identity();
			if (identity.equals(new String("-"))) {
				identity = CertUtils.getPublicSubjectCN(keyCertChainX509);
			}
			tmpKeyServInfo.setKeyServID(identity);
			
	        AttributedURI address = endpoint.getAddress();			
			tmpKeyServInfo.setKeyServURI(address.toString());
			setKeyServInfo(tmpKeyServInfo);
		} catch(Exception e) {
			logger.error("Exception when trying to initializing this KeyService", e);
		}
                
 */
}
	}

	/* Get/Setters for the RPs */
	public KeyServerServiceInfo getKeyServerServiceInfo() {
		return RPs_KeyServerServiceInfo;
	}

	public synchronized void setKeyServerServiceInfo(KeyServerServiceInfo RPs_KeyServerServiceInfo) {
		this.RPs_KeyServerServiceInfo = RPs_KeyServerServiceInfo;
	}
		
	
	/* Required by interface ResourceProperties */
	public ResourcePropertySet getResourcePropertySet() {
		return this.propSet;
	}
        
        public void IIS_Register(){
            try{
                //Inizialize IIS Register component ald register the RP
                API_IIS_Register = new  IIS_Register();
                API_IIS_Register.register(this.registration_path);
            }catch(Exception e){
                logger.log(Level.ALL, "Exception when trying to register in the IIS " + e);            
            }
        }
	
        /* Implementation of store, retrieve, and update operations */

	public String xmlStore(String xmlInputStore) throws RemoteException {
            String strDN          = "";
            String strOutputStore = "";
            try{
                
                trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputStore.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputStore.XmlWrapper(xmlInputStore, false);
		inputWrapper.wrap();
                                
                String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();   
                strDN = gateKeeper.strExtractDN(CERTIFICATE);
		String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
		if (!gkResult.contentEquals("OK")) throw new Exception("Connecting to gatekeeper: " + gkResult);
                
                String EOUID             = inputWrapper.get_INPUT().get_EOUID();
                String MAC               = inputWrapper.get_INPUT().get_MAC();
                String PART_KEY_SHARE    = inputWrapper.get_INPUT().get_PART_KEY_SHARE();
                int    ID_PART_KEY_SHARE = inputWrapper.get_INPUT().get_ID_PART_KEY_SHARE();
       
                // ontology
                Iterator it_ontologies = inputWrapper.get_ONTOLOGIES().getAll_IDONTOLOGY();
                trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputStore.IDONTOLOGY tmpOntID;      
                Vector<Integer> ONTOLOGIES = new <Integer>Vector();          
                while (it_ontologies.hasNext()) {
                        tmpOntID = (trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputStore.IDONTOLOGY) it_ontologies.next();
                        ONTOLOGIES.add(new Integer(tmpOntID.getValue()));
                }	
		
                
                //API SQL KEYS BACKEND               
                int iErr = this.API_Keys_db.iInsertSharePair(EOUID, ONTOLOGIES, ID_PART_KEY_SHARE, PART_KEY_SHARE, strDN, MAC);
                if (iErr == 0){
                            strOutputStore += "<OUTPUT>\n";
                            strOutputStore += "\t<STATUS>0</STATUS>\n";
                            strOutputStore += "\t<DESCRIPTION>\n\tOK\t</DESCRIPTION>\n";
                            strOutputStore += "</OUTPUT>\n";			
                                                        
                            
                            logger.log(Level.ALL, "*****************************************************");
                            logger.log(Level.ALL, strDN + " -> Method: KeyServer.xmlStore()");
                            logger.log(Level.ALL, xmlInputStore);
                            logger.log(Level.ALL, "*****************************************************");                                                                                    
                }else{
                            strOutputStore += "<OUTPUT>\n";
                            strOutputStore += "\t<STATUS>-1</STATUS>\n";
                            strOutputStore += "\t<DESCRIPTION>\n\t" + API_Keys_db.strGetError() + "\t</DESCRIPTION>\n";
                            strOutputStore += "</OUTPUT>\n";
                            
                            logger.log(Level.ALL, "*****************************************************");
                            logger.log(Level.ALL, strDN + "-> Error in Method: KeyServer.xmlStore()");
                            logger.log(Level.ALL, strOutputStore);                        
                            logger.log(Level.ALL, "*****************************************************");		
                }
                
                return strOutputStore;
                                
            }catch(Exception ex){
                 strOutputStore += "<OUTPUT>\n";
                 strOutputStore += "\t<STATUS>-1</STATUS>\n";
                 strOutputStore += "\t<DESCRIPTION>\n\t" + ex.toString() + "</DESCRIPTION>\n";                 	
                 strOutputStore += "</OUTPUT>\n";                        
                    
                 logger.log(Level.ALL, "*****************************************************");
                 logger.log(Level.ALL, "strDN -> Error in Method: KeyServer.xmlStore()");
                 logger.log(Level.ALL, strOutputStore);                        
                 logger.log(Level.ALL, "*****************************************************");    
                 
                 return strOutputStore;
            }
                                                  					
	}
        
        
        public String xmlRetrieve(String xmlInputRetrieve) throws RemoteException {
            String strDN             = "";
            String strOutputRetrieve = "";
            try{
		 trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputRetrieve.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputRetrieve.XmlWrapper(xmlInputRetrieve, false);
		inputWrapper.wrap();
                                
                String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();   
                strDN = gateKeeper.strExtractDN(CERTIFICATE);
		String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
		if (!gkResult.contentEquals("OK")) throw new Exception("Connecting to gatekeeper: " + gkResult);
                
                String IDONTOLOGY = inputWrapper.get_INPUT().get_IDONTOLOGY();
                String EOUID      = inputWrapper.get_INPUT().get_EOUID();
                
                //API SQL KEYS BACKEND               
                String strResult  = this.API_Keys_db.strGetSharePair(EOUID, IDONTOLOGY);
                if (strResult != null){
                    
                            String [] strValues = strResult.split(",");
                    
                            strOutputRetrieve += "<OUTPUT>\n";
                            strOutputRetrieve += "\t<STATUS>0</STATUS>\n";
                            strOutputRetrieve += "\t<MAC>"               + strValues[2]  + "</MAC>\n";
                            strOutputRetrieve += "\t<PART_KEY_SHARE>"    + strValues[1]  + "</PART_KEY_SHARE>\n";
                            strOutputRetrieve += "\t<ID_PART_KEY_SHARE>" + strValues[0]  + "</ID_PART_KEY_SHARE>\n";                            
                            strOutputRetrieve += "\t<DESCRIPTION>\n\tOK\t</DESCRIPTION>\n";
                            strOutputRetrieve += "</OUTPUT>\n";			
                                                            
                            logger.log(Level.ALL, "*****************************************************");
                            logger.log(Level.ALL, strDN + " -> Method: KeyServer.xmlRetrieve()");
                            logger.log(Level.ALL, xmlInputRetrieve);
                            logger.log(Level.ALL, "*****************************************************");                                                                                    
                }else{
                            strOutputRetrieve += "<OUTPUT>\n";
                            strOutputRetrieve += "\t<STATUS>-1</STATUS>\n";
                            strOutputRetrieve += "\t<DESCRIPTION>\n\t" + API_Keys_db.strGetError() + "\t</DESCRIPTION>\n";
                            strOutputRetrieve += "</OUTPUT>\n";
                            
                            logger.log(Level.ALL, "*****************************************************");
                            logger.log(Level.ALL, strDN + "-> Error in Method: KeyServer.xmlRetrieve()");
                            logger.log(Level.ALL, strOutputRetrieve);                        
                            logger.log(Level.ALL, "*****************************************************");		
                }
                
                return strOutputRetrieve;
                
            }catch(Exception ex){
                 strOutputRetrieve += "<OUTPUT>\n";
                 strOutputRetrieve += "\t<STATUS>-1</STATUS>\n";
                 strOutputRetrieve += "\t<DESCRIPTION>\n\t" + ex.toString() + "</DESCRIPTION>\n";
                 strOutputRetrieve += "\t<EOUID></EOUID>\n";	
                 strOutputRetrieve += "</OUTPUT>\n";                        
                    
                 logger.log(Level.ALL, "*****************************************************");
                 logger.log(Level.ALL, "strDN -> Error in Method: KeyServer.xmlRetrieve()");
                 logger.log(Level.ALL, strOutputRetrieve);                        
                 logger.log(Level.ALL, "*****************************************************");    
                 
                 return strOutputRetrieve;
            }
			
	}
        
        
        public String xmlUpdate(String xmlInputUpdate) throws RemoteException {
            String strDN           = "";
            String strOutputUpdate = "";
            try{
                
                trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputUpdate.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputUpdate.XmlWrapper(xmlInputUpdate, false);
		inputWrapper.wrap();
                                
                String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();   
                strDN = gateKeeper.strExtractDN(CERTIFICATE);
		String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
		if (!gkResult.contentEquals("OK")) throw new Exception("Connecting to gatekeeper: " + gkResult);
                
                String EOUID             = inputWrapper.get_INPUT().get_EOUID();
                String MAC               = inputWrapper.get_INPUT().get_MAC();
                String PART_KEY_SHARE    = inputWrapper.get_INPUT().get_PART_KEY_SHARE();
                int    ID_PART_KEY_SHARE = inputWrapper.get_INPUT().get_ID_PART_KEY_SHARE();
                String IDONTOLOGY        = inputWrapper.get_INPUT().get_IDONTOLOGY();
                       		
                
                //API SQL KEYS BACKEND               
                int iErr = this.API_Keys_db.iUpdateSharePair(EOUID, IDONTOLOGY, ID_PART_KEY_SHARE, PART_KEY_SHARE, strDN, MAC);
                if (iErr == 0){
                            strOutputUpdate += "<OUTPUT>\n";
                            strOutputUpdate += "\t<STATUS>0</STATUS>\n";
                            strOutputUpdate += "\t<DESCRIPTION>\n\tOK\t</DESCRIPTION>\n";
                            strOutputUpdate += "</OUTPUT>\n";			
                                                        
                            
                            logger.log(Level.ALL, "*****************************************************");
                            logger.log(Level.ALL, strDN + " -> Method: KeyServer.xmlUpdate()");
                            logger.log(Level.ALL, xmlInputUpdate);
                            logger.log(Level.ALL, "*****************************************************");                                                                                    
                }else{
                            strOutputUpdate += "<OUTPUT>\n";
                            strOutputUpdate += "\t<STATUS>-1</STATUS>\n";
                            strOutputUpdate += "\t<DESCRIPTION>\n\t" + API_Keys_db.strGetError() + "\t</DESCRIPTION>\n";
                            strOutputUpdate += "</OUTPUT>\n";
                            
                            logger.log(Level.ALL, "*****************************************************");
                            logger.log(Level.ALL, strDN + "-> Error in Method: KeyServer.xmlUpdate()");
                            logger.log(Level.ALL, strOutputUpdate);                        
                            logger.log(Level.ALL, "*****************************************************");		
                }
                
                return strOutputUpdate;
                                
            }catch(Exception ex){
                 strOutputUpdate += "<OUTPUT>\n";
                 strOutputUpdate += "\t<STATUS>-1</STATUS>\n";
                 strOutputUpdate += "\t<DESCRIPTION>\n\t" + ex.toString() + "</DESCRIPTION>\n";                 	
                 strOutputUpdate += "</OUTPUT>\n";                        
                    
                 logger.log(Level.ALL, "*****************************************************");
                 logger.log(Level.ALL, "strDN -> Error in Method: KeyServer.xmlUpdate()");
                 logger.log(Level.ALL, strOutputUpdate);                        
                 logger.log(Level.ALL, "*****************************************************");    
                 
                 return strOutputUpdate;
            }	

	}
        
        
	public String xmlSign(String xmlInputSign) throws RemoteException {
            String xmlOutputSign = "";
            String strDN         = "";
            try{
                
                trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputSign.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputSign.XmlWrapper(xmlInputSign, false);
		inputWrapper.wrap();
                                
                String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();   
                strDN = gateKeeper.strExtractDN(CERTIFICATE);
		String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
		if (!gkResult.contentEquals("OK")) throw new Exception("Connecting to gatekeeper: " + gkResult);
                
                String EOUID             = inputWrapper.get_INPUT().get_EOUID();               
                // ontology
                Iterator it_ontologies = inputWrapper.get_ONTOLOGIES().getAll_IDONTOLOGY();
                trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputStore.IDONTOLOGY tmpOntID;      
                Vector<Integer> ONTOLOGIES = new <Integer>Vector();          
                while (it_ontologies.hasNext()) {
                        tmpOntID = (trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputStore.IDONTOLOGY) it_ontologies.next();
                        ONTOLOGIES.add(new Integer(tmpOntID.getValue()));
                }	
                
                
                PrivateKey privKey = CertUtils.getServPrivateKey();                
                String signedMessage = CryptUtils.getSignedMsg(privKey, CryptUtils.getMsgStr(EOUID, ONTOLOGIES));
                
                if (signedMessage != null){
                    xmlOutputSign += "<OUTPUT>\n";
                    xmlOutputSign += "\t<STATUS>0</STATUS>\n";
                    xmlOutputSign += "\t<SIGNEDMESSAGE>" + signedMessage +"</SIGNEDMESSAGE>\n";
                    xmlOutputSign += "\t<DESCRIPTION>\n\tOK\t</DESCRIPTION>\n";
                    xmlOutputSign += "</OUTPUT>\n";			
                            
                    logger.log(Level.ALL, "*****************************************************");
                    logger.log(Level.ALL, strDN + " -> Method: KeyServer.xmlSign()");
                    logger.log(Level.ALL, xmlInputSign);
                    logger.log(Level.ALL, "*****************************************************");
                }else{
                     xmlOutputSign += "<OUTPUT>\n";
                     xmlOutputSign += "\t<STATUS>-1</STATUS>\n";
                     xmlOutputSign += "\t<DESCRIPTION>\n\tCould no generate signed message\t</DESCRIPTION>\n";
                     xmlOutputSign += "</OUTPUT>\n";
                            
                     logger.log(Level.ALL, "*****************************************************");
                     logger.log(Level.ALL, strDN + "-> Error in Method: KeyServer.xmlSign()");
                     logger.log(Level.ALL, xmlOutputSign);                        
                     logger.log(Level.ALL, "******************************************************");                    
                }
                return xmlOutputSign;
                                                                

            }catch(Exception ex){
                xmlOutputSign += "<OUTPUT>\n";
                xmlOutputSign += "\t<STATUS>-1</STATUS>\n";
                xmlOutputSign += "\t<DESCRIPTION>\n\t" + ex.toString() + "</DESCRIPTION>\n";                 	
                xmlOutputSign += "</OUTPUT>\n";                        
                    
                logger.log(Level.ALL, "*****************************************************");
                logger.log(Level.ALL, "strDN -> Error in Method: EOUIDGenerator.xmlSign()");
                logger.log(Level.ALL, xmlOutputSign);                        
                logger.log(Level.ALL, "*****************************************************");    
                 
                return xmlOutputSign;
            }
        }
}
