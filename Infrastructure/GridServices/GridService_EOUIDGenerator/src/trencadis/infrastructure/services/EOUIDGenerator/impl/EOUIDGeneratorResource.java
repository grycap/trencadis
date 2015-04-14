package trencadis.infrastructure.services.EOUIDGenerator.impl;

import java.io.File;
import java.rmi.RemoteException;
import java.security.cert.X509Certificate;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.apache.axis.components.uuid.SimpleUUIDGen;


import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.impl.ReflectionResourceProperty;



import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.config.ContainerConfig;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.utils.AddressingUtils;

import org.globus.axis.message.addressing.EndpointReferenceType;
import org.globus.axis.message.addressing.AttributedURIType;

import trencadis.common.util.CertUtils;

import trencadis.infrastructure.services.stubs.EOUIDGenerator.EOUIDGeneratorServiceInfo;

import trencadis.infrastructure.gateKeeper.CGateKeeper;
import trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS.IIS_Register;

public class EOUIDGeneratorResource implements Resource, ResourceProperties {

        //It indicates if is in debug mode
        public static boolean bIsDebug = false;
    
        /*******************************************************************/
	/********** Local parameters ***************************************/
	private String identity;
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
        private Logger logger = Logger.getLogger(EOUIDGeneratorResource.class.getName());
        /*******************************************************************/

        /*******************************************************************/
        /****** TRENCADIS Java API SQL Ontologies Database Component *******/
        IIS_Register API_IIS_Register;
        /*******************************************************************/

        /*******************************************************************/
        /****************** EOUID Generator Component **********************/
	private SimpleUUIDGen eouidGen = null;
        /*******************************************************************/
        
	/* Resource Property set */
	private ResourcePropertySet propSet;

	/* Resource properties */
	private EOUIDGeneratorServiceInfo RPs_EOUIDGeneratorServiceInfo;                                          
                                          
	
	/* Initializes RPs */
	public void initialize() throws Exception {
            
               File confPath = new File("/home/trencadis/EOUIDGeneratorService/EOUIDGeneratorConfig.xml");
               trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.config.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.config.XmlWrapper(confPath, false);
                    inputWrapper.wrap();
            
               /***********************************************************************************/
               /****** Parametros Generales de Configuración del Servicio Ontologies Server *******/
               /***********************************************************************************/               
               trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.config.SERVICE_PARAMETERS localParameters = inputWrapper.get_CONFIGURATION().get_SERVICE_PARAMETERS();
               this.identity          = localParameters.get_IDENTITY();
               this.base_path         = localParameters.get_BASE_PATH();
               this.path_cattrustcert = localParameters.get_PATH_CATRUSTCERT();
               this.tmp_dir           = localParameters.get_TMP_DIR();                				
               /************************************************************************************/
                    
               /**************************************************************/
               /********************  COMPONENT GATEKEEPER *******************/
               trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.config.GATEKEEPER_PARAMETERS gatekeeperParameters = inputWrapper.get_CONFIGURATION().get_GATEKEEPER_PARAMETERS();		
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
	       trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.config.INDEX_SERVICE_PARAMETERS indexServiceParameters = inputWrapper.get_CONFIGURATION().get_INDEX_SERVICE_PARAMETERS();	       
               this.registration_path = indexServiceParameters.get_REGISTRATION_PATH();
               /****************************************************************/

               /**************************************************************/
               /********************  LOGGER COMPONENT ***********************/                
	       trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.config.LOGGER_PARAMETERS  loggerParameters = inputWrapper.get_CONFIGURATION().get_LOGGER_PARAMETERS() ;
	       FileHandler logFile = new FileHandler(loggerParameters.get_LOG_PATH());
	       logger.addHandler(logFile);
	       logger.setLevel(Level.ALL);
	       SimpleFormatter formatter = new SimpleFormatter();
	       logFile.setFormatter(formatter);
               /**************************************************************/

               /**************************************************************/
               /********************  eouuid gENERATOR cOMPONENT *************/                                 
               eouidGen = new SimpleUUIDGen();
               /**************************************************************/
                
                
                
if (!bIsDebug){  		                				                              
		this.propSet = new SimpleResourcePropertySet(EOUIDGeneratorQNames.RESOURCE_PROPERTIES);
                                
                try{
                    ResourceProperty eOUIDGeneratorServiceInfo = new ReflectionResourceProperty(
				EOUIDGeneratorQNames.RP_EOUIDGENERATORSERVICEINFO, "EOUIDGeneratorServiceInfo", this);
                    this.propSet.add(eOUIDGeneratorServiceInfo);
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
			                                                
                    RPs_EOUIDGeneratorServiceInfo = new EOUIDGeneratorServiceInfo();
                    RPs_EOUIDGeneratorServiceInfo.setDomain(CertUtils.getPublicSubjectOU(uUIDCertChainX509));
                    
                    if (identity.equals(new String("-"))) {
    				identity = CertUtils.getPublicSubjectCN(uUIDCertChainX509);
                    }
                    RPs_EOUIDGeneratorServiceInfo.setID(identity);
			                                                
                    AttributedURIType address = endpoint.getAddress();
                    RPs_EOUIDGeneratorServiceInfo.setURI(address.toString());
                                                                                                                    
		} catch(Exception e) {  
                    logger.log(Level.ALL, "Exception when trying to initializing this EOUIDGeneratorService: " + e);			
		}        
}
                                               
	}

	/* Get/Setters for the RPs */
	public EOUIDGeneratorServiceInfo getEOUIDGeneratorServiceInfo() {
		return RPs_EOUIDGeneratorServiceInfo;
	}

	public synchronized void setEOUIDGeneratorServiceInfo(EOUIDGeneratorServiceInfo RP_EOUIDGeneratorServiceInfo) {
		this.RPs_EOUIDGeneratorServiceInfo = RP_EOUIDGeneratorServiceInfo;
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
                       
        public String xmlGetNext(String xmlInputGetNext) throws RemoteException {
            String xmlOutputgetNext = "";
            String strDN = "";
            try{                
            
                trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.xmlInputGetNext.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.xmlInputGetNext.XmlWrapper(xmlInputGetNext, false);
		inputWrapper.wrap();
                
                String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();   
                strDN = gateKeeper.strExtractDN(CERTIFICATE);
		String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
		if (!gkResult.contentEquals("OK")) throw new Exception("Connecting to gatekeeper: " + gkResult);
                
                                                
		String eouid = eouidGen.nextUUID();
		if ((eouid instanceof String)&&(eouid != null)&&(!eouid.equals("-"))) {
                    xmlOutputgetNext += "<OUTPUT>\n";
                    xmlOutputgetNext += "\t<STATUS>0</STATUS>\n";
                    xmlOutputgetNext += "\t<DESCRIPTION></DESCRIPTION>\n";		
                    xmlOutputgetNext += "\t<EOUID>" + eouid + "</EOUID>\n";			
                    xmlOutputgetNext += "</OUTPUT>\n";               
                    
                    logger.log(Level.ALL, "*****************************************************");
                    logger.log(Level.ALL, strDN + " -> Method: xmlGetNext()");
                    logger.log(Level.ALL, xmlInputGetNext);
                    logger.log(Level.ALL, "*****************************************************");   
                    
		} else {
                    xmlOutputgetNext += "<OUTPUT>\n";
                    xmlOutputgetNext += "\t<STATUS>-1</STATUS>\n";
                    xmlOutputgetNext += "\t<DESCRIPTION>\n\tInternal Error.</DESCRIPTION>\n";
                    xmlOutputgetNext += "\t<EOUID></EOUID>\n";	
                    xmlOutputgetNext += "</OUTPUT>\n";                        
                    
                    logger.log(Level.ALL, "*****************************************************");
                    logger.log(Level.ALL, "strDN -> Error in Method: EOUIDGenerator.xmlGetNext()");
                    logger.log(Level.ALL, xmlOutputgetNext);                        
                    logger.log(Level.ALL, "*****************************************************");                    
		}				
                return xmlOutputgetNext;
            }catch(Exception ex){
                 xmlOutputgetNext += "<OUTPUT>\n";
                 xmlOutputgetNext += "\t<STATUS>-1</STATUS>\n";
                 xmlOutputgetNext += "\t<DESCRIPTION>\n\t" + ex.toString() + "</DESCRIPTION>\n";
                 xmlOutputgetNext += "\t<EOUID></EOUID>\n";	
                 xmlOutputgetNext += "</OUTPUT>\n";                        
                    
                 logger.log(Level.ALL, "*****************************************************");
                 logger.log(Level.ALL, "strDN -> Error in Method: EOUIDGenerator.xmlGetNext()");
                 logger.log(Level.ALL, xmlOutputgetNext);                        
                 logger.log(Level.ALL, "*****************************************************");    
                 
                 return xmlOutputgetNext;
            }
	}              
        public String xmlIsActive(String xmlInputIsActive) throws RemoteException {
            String xmlOutputIsActive = "";
            try{                
            
                trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.xmlInputIsActive.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper.xmlInputIsActive.XmlWrapper(xmlInputIsActive, false);
		inputWrapper.wrap();
                
                String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();                
		String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
		if (!gkResult.contentEquals("OK")) throw new Exception("Connecting to gatekeeper: " + gkResult);
                                                                		
                    xmlOutputIsActive += "<OUTPUT>\n";
                    xmlOutputIsActive += "\t<STATUS>0</STATUS>\n";
                    xmlOutputIsActive += "\t<DESCRIPTION></DESCRIPTION>\n";		                
                    xmlOutputIsActive += "</OUTPUT>\n";                                            
		
                    return xmlOutputIsActive;
            }catch(Exception ex){
                 xmlOutputIsActive += "<OUTPUT>\n";
                 xmlOutputIsActive += "\t<STATUS>-1</STATUS>\n";
                 xmlOutputIsActive += "\t<DESCRIPTION>\n\t" + ex.toString() + "</DESCRIPTION>\n";    
                 xmlOutputIsActive += "</OUTPUT>\n";                        
                    
                 logger.log(Level.ALL, "*****************************************************");
                 logger.log(Level.ALL, "strDN -> Error in Method: EOUIDGenerator.xmlGetNext()");
                 logger.log(Level.ALL, xmlOutputIsActive);                        
                 logger.log(Level.ALL, "*****************************************************");    
                 
                 return xmlOutputIsActive;
            }
	}              
                
}