/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.middleware.operations.DICOMStorage.test;

import java.io.File;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReportsID.DICOM_SR_ID;
import trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_XMLDSR_DOWNLOAD;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_XMLDSR_DOWNLOAD_ALL_ONTOLOGY;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_XMLDSR_REMOVE;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_XMLDSR_UPLOAD;

/**
 *
 * @author root
 */
public class TestDICOM {
    public static void main(String[] args) {
        try{

/*            
            System.setProperty("X509_USER_PROXY", "/tmp/damian.proxy");
                                                            
            Vector    _v_urls_iis = new Vector(); 
            trencadis.middleware.wrapper.config.URL_IIS URL_IIS;
            org.globus.axis.util. Util.registerTransport();
            trencadis.middleware.wrapper.config.XmlWrapper inputWrapper =
			new trencadis.middleware.wrapper.config.XmlWrapper(new File("/home/trencadis/middleware/trencadis.config"), false);
	    inputWrapper.wrap();
                                    
            
            Iterator it_urls = inputWrapper.get_CONFIGURATION().get_INDEX_SERVICE_PARAMETERS().getAll_URL_IIS();
                while (it_urls.hasNext()){
                    URL_IIS = (trencadis.middleware.wrapper.config.URL_IIS) it_urls.next();
                    _v_urls_iis.add(URL_IIS.getValue());                     
                }
                        
            IIS_Query_RPs query_iis = new IIS_Query_RPs();
                                                                                                
            //Recupero la URL los datos del recurso con los que sean de la ontologia 5.
            String strxPathQuery = "//*[local-name()='DICOMStorageServiceInfo' and boolean(./*[local-name()='Ontology' and IDOntology=5]/*)]/*[local-name()='URIResource']/*";
            
            
            QueryResourcePropertiesResponse response = query_iis.QueryRPs(_v_urls_iis, strxPathQuery);                   
            MessageElement[] responseContents = response.get_any();
            for (int i=0;i<responseContents.length;i++){
                System.out.println(responseContents[i].getValue());
            }
*/
            File xmlDSR = null;
            
            TRENCADIS_XML_DICOM_SR_FILE xmlDsr1 = null;
            TRENCADIS_XML_DICOM_SR_FILE xmlDsr2 = null;
            TRENCADIS_XML_DICOM_SR_FILE xmlDsr3 = null;
            TRENCADIS_XML_DICOM_SR_FILE xmlDsrDownload = null;
            TRENCADIS_XMLDSR_UPLOAD gs = null;
            TRENCADIS_XMLDSR_REMOVE gs2 = null;
            TRENCADIS_XMLDSR_DOWNLOAD gs3 = null;
            
    		String backend_config =   "<BACKEND_PARAMETERS>"
					+ "	 <BACKEND_TYPE>GRID_FTP</BACKEND_TYPE>"
					+ "  <GRIDFTP_PARAMETERS>"
					+ "     <HOST_NAME>trencadisv06.i3m.upv.es</HOST_NAME>"
					+ "     <HOST_PORT>2811</HOST_PORT>"
					+ "     <HOME_DIR>/home/gridftp</HOME_DIR>"
					+ "  </GRIDFTP_PARAMETERS>"
					+ "</BACKEND_PARAMETERS>";
            
            /******************************* CREATE TRENCADIS_SESSION **********/
            File configFile = new File("/opt/trencadis/trencadis.config");
            TRENCADIS_SESSION session = new TRENCADIS_SESSION(configFile, "1234567890");
            /*******************************************************************/
            
            /******************* TEST - TRENCADIS_XMLDSR_UPLOAD - CONSTRUCTOR 1 *******
            xmlDSR    = new File("/opt/trencadis/files/reports/ECO_13_750.xml");
            xmlDsr1   = new TRENCADIS_XML_DICOM_SR_FILE(session, xmlDSR);            
            //xmlDsr1.generatePDFReport("/home/trencadis/DICOMStorage/BackEnd/backup/EQ_1_3.xml");
            gs = new TRENCADIS_XMLDSR_UPLOAD(session,xmlDsr1);
            gs.execute();
            System.out.println("  > " + xmlDsr1.getIDReport() + " - " + xmlDsr1.getIDTRENCADISReport());
            System.out.println("TEST Succesfully - UPLOAD DICOMSR Exploration Breast to any Center that support the Ontology - CONSTRUCTOR 1");
            /*******************************************************************/
            
            /******************* TEST - TRENCADIS_XMLDSR_UPLOAD - CONSTRUCTOR 1 *******
            
            System.out.println("TEST - TRENCADIS_XMLDSR_UPLOAD");
            IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
        	File homeDir = new File("/opt/trencadis/files/reports_test/MAMO/");
        	Collection<File> files = FileUtils.listFiles(homeDir, fileFilter, (IOFileFilter) null);
            for (File xmlDsr: files) {            
	            xmlDsr2   = new TRENCADIS_XML_DICOM_SR_FILE(session, xmlDsr);            
	      		gs = new TRENCADIS_XMLDSR_UPLOAD(session, xmlDsr2, "1");
	      		gs.execute();
	      		System.out.println("  > " + xmlDsr2.getIDReport() + " - " + xmlDsr2.getIDTRENCADISReport());
            }
            System.out.println("TEST Succesfully");
            /*******************************************************************/
                        
            /******************* TEST - TRENCADIS_XMLDSR_UPLOAD - CONSTRUCTOR 2 *******      
            xmlDSR      = new File("/home/trencadis/DICOMStorage/BackEnd/backup/EQ_1_3.xml");
            xmlDsr3   = new TRENCADIS_XML_DICOM_SR_FILE(session, xmlDSR);
            gs = new TRENCADIS_XMLDSR_UPLOAD(session,xmlDsr3,"1");
            gs.execute();
            System.out.println("TEST Succesfully - UPLOAD DICOMSR Exploration Breast to IDCenter 1 - CONSTRUCTOR 1");
            /*******************************************************************/
             
            /******************* TEST - TRENCADIS_XMLDSR_REMOVE - CONSTRUCTOR 1 *******                  
            gs2 = new TRENCADIS_XMLDSR_REMOVE(session,xmlDsr3, "1");
            gs2.execute();
            System.out.println("TEST Succesfully - REMOVE DICOMSR Exploration Breast from IDCenter 1 - CONSTRUCTOR 1");
            /*******************************************************************/ 
             
            /******************* TEST - TRENCADIS_XMLDSR_REMOVE - CONSTRUCTOR 2 *******
            gs2 = new TRENCADIS_XMLDSR_REMOVE(session, "6",  "33173300-C400-11E4-8D35-9DE56F0C43B3", "1");
            gs2.execute();
            System.out.println("TEST Succesfully - REMOVE DICOMSR Exploration Breast from IDCenter 1 and IDOntology " + xmlDsr2.getIDOntology() + " - CONSTRUCTOR 1");
            /*******************************************************************/ 

            /******************* TEST - TRENCADIS_XMLDSR_DOWNLOAD - CONSTRUCTOR 1 *******           
            gs3 = new TRENCADIS_XMLDSR_DOWNLOAD(session, xmlDsr1.getIDOntology(),  xmlDsr1.getIDTRENCADISReport(), "1");
            xmlDsrDownload = gs3.execute();
            System.out.println("TEST Succesfully - DOWNLOAD DICOMSR Exploration Breast from IDCenter 1 and IDOntology " + xmlDsrDownload.getIDOntology() + " - CONSTRUCTOR 1");            

            /******************* TEST - TRENCADIS_XMLDSR_DOWNLOAD - CONSTRUCTOR 1 ******
            System.out.println("TEST DOWNLOAD DICOMSR from IDCenter 1");
            gs3 = new TRENCADIS_XMLDSR_DOWNLOAD(session, "6", "1D30DF20-C70A-11E4-8D35-E68FFB6C6EED" ,"1");
            xmlDsrDownload = gs3.execute();
            System.out.println("   ID Ontology: " + xmlDsrDownload.getIDOntology());
            System.out.println("   ID Report: " + xmlDsrDownload.getIDReport());
            FileUtils.writeStringToFile(new File("/opt/trencadis/reports/"
        			+ xmlDsrDownload.getIDReport() + ".xml")
        			, xmlDsrDownload.getContents());
            System.out.println("TEST Succesfully");
            
            /*******************************************************************
            xmlDsr1 = new TRENCADIS_XML_DICOM_SR_FILE(session,new File("/home/trencadis/DICOMStorage/BackEnd/backup/EQ_1_3.xml"));
            xmlDsr1.generateDICOMSR();
            /*******************************************************************/
            
            /**************** TEST - TRENCADIS_XMLDSR_DOWNLOAD_ALL *************
            System.out.println("TEST DOWNLOAD_ALL_DICOMSR");
            TRENCADIS_XMLDSR_DOWNLOAD_ALL gs6 = new TRENCADIS_XMLDSR_DOWNLOAD_ALL(session);
            Vector<TRENCADIS_XML_DICOM_SR_FILE> v_xmldsr = gs6.execute();            
            for (TRENCADIS_XML_DICOM_SR_FILE xmldsr : v_xmldsr) {
            	FileUtils.writeStringToFile(new File("/opt/trencadis/reports/" 
            			+ xmldsr.getIDReport() + ".xml")
            			, xmldsr.getContents());
            	System.out.println(" -> Download report with ontology: " + xmldsr.getIDOntology()
            			+ " and report ID " + xmldsr.getIDTRENCADISReport());
            }        	
            System.out.println("TEST Succesfully");            
            /*******************************************************************/
            
            /************ TEST - TRENCADIS_XMLDSR_DOWNLOAD_ALL_BY_ONTOLOGY *****/
            System.out.println("TEST DOWNLOAD_ALL_DICOMSR_BY_ONTOLOGY");
            TRENCADIS_XMLDSR_DOWNLOAD_ALL_ONTOLOGY gs6 = new TRENCADIS_XMLDSR_DOWNLOAD_ALL_ONTOLOGY(session, 1, "7");
            Vector<TRENCADIS_XML_DICOM_SR_FILE> v_xmldsr = gs6.execute();            
            for (TRENCADIS_XML_DICOM_SR_FILE xmldsr : v_xmldsr) {
            	FileUtils.writeStringToFile(new File("/opt/trencadis/reports/" 
            			+ xmldsr.getIDReport() + ".xml")
            			, xmldsr.getContents());
            	System.out.println(" -> Download report with ontology: " + xmldsr.getIDOntology()
            			+ " and report ID " + xmldsr.getIDReport());
            }        	
            System.out.println("TEST Succesfully");            
            /*******************************************************************/
            
            /*************** TEST - TRENCADIS_XMLDSR_DOWNLOAD FROM BACKEND ******
            System.out.println("TEST DOWNLOAD DICOMSR from Backend");
            BackEnd backend = new BackEnd(backend_config);
            String idReport = "2B61AF20-C70A-11E4-8D35-E1B00A2ABE40";
            String report = backend.xmlGetDICOMSRFile(idReport, session.getX509VOMSCredential());
            FileUtils.writeStringToFile(new File("/opt/trencadis/reports/"
        			+ idReport + ".xml")
        			, report);
            System.out.println("TEST Succesfully");
            
            /*******************************************************************/
            
            /**** TEST - TRENCADIS_XMLDSR_DOWNLOAD_INDEXER_IDs *****
            System.out.println("TEST DOWNLOAD_ALL_DICOMSR_IDs");
            TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS gs7 = new TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS(session, 1);
            for (TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE dicomStorageIDS : gs7.getDICOMStorageIDS()) {
            	System.out.println(dicomStorageIDS.getCenterName());
            	//System.out.println(dicomStorageIDS.getBackend().toString());
            	for (DICOM_SR_ID id : dicomStorageIDS.getDICOM_DSR_IDS())
            		System.out.println("\t" + id.getValue());
            }
            System.out.println("TEST Succesfully");            
            /*******************************************************************/
            
            /**** TEST - TRENCADIS_XMLDSR_DOWNLOAD_INDEXER_IDs (ontology and center) *****
            System.out.println("TEST DOWNLOAD_ALL_DICOMSR_IDs);
            TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS gs8 = new TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS(session, 1, "6");
            for (TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE dicomStorageIDS : gs8.getDICOMStorageIDS()) {
            	System.out.println(dicomStorageIDS.getBackend().toString());
            	for (DICOM_SR_ID id : dicomStorageIDS.getDICOM_DSR_IDS())
            		System.out.println(id.getValue());
            }
            System.out.println("TEST Succesfully");            
            /*******************************************************************/
            
            
            
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
/*
<DICOMStorageServiceInfo>
    <Domain>domi</Domain>
    <IDCenter>1</IDCenter>      <CenterName>Hospital_Doctor_Peset</CenterName> 
    <URIResource>
        <URI>hola</URI> 
        <Key>5</Key>
    </URIResource>
    <Ontology>
        <IDOntology>5</IDOntology>
        <Description>Mamografía</Description>
    </Ontology>
</DICOMStorageServiceInfo>
*/ 