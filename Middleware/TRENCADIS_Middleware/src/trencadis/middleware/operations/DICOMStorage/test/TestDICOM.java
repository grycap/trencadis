/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.middleware.operations.DICOMStorage.test;

import java.io.File;

import org.apache.commons.io.FileUtils;

import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReportsID.DICOM_SR_ID;
import trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_XMLDSR_DOWNLOAD;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_XMLDSR_REMOVE;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_XMLDSR_UPLOAD;

/**
 *
 * @author root
 */
public class TestDICOM {
	
	
    public static void main(String[] args) {
    	
        try{

            File xmlDSR = null;
            
            TRENCADIS_XML_DICOM_SR_FILE xmlDsr1 = null;
            TRENCADIS_XML_DICOM_SR_FILE xmlDsr2 = null;
            TRENCADIS_XML_DICOM_SR_FILE xmlDsrTest1 = null;
            TRENCADIS_XML_DICOM_SR_FILE xmlDsrTest2 = null;
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
            
            /************************* CREATE TRENCADIS_SESSION **********************/
            File configFile = new File("/opt/trencadis/trencadis.config");
            TRENCADIS_SESSION session = new TRENCADIS_SESSION(configFile, "1234567890");
            /*************************************************************************/
            
            /******************* TEST - TRENCADIS_XMLDSR_UPLOAD - CONSTRUCTOR 1 *******
            System.out.println("TEST - TRENCADIS_XMLDSR_UPLOAD");
            xmlDSR    = new File("/opt/trencadis/files/reports/ECO_1_500.xml");
            xmlDsr1   = new TRENCADIS_XML_DICOM_SR_FILE(session, xmlDSR);            
            gs = new TRENCADIS_XMLDSR_UPLOAD(session,xmlDsr1);
            gs.execute();
            System.out.println("  > " + xmlDsr1.getIDReport() + " - " + xmlDsr1.getIDTRENCADISReport());
            System.out.println("TEST Succesfully - UPLOAD DICOMSR Ecography to any Center that support the Ontology");
            /*******************************************************************/
            
            /******************* TEST - TRENCADIS_XMLDSR_UPLOAD - CONSTRUCTOR 1 *******
            System.out.println("TEST - TRENCADIS_XMLDSR_UPLOAD");
            IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
        	File homeDir = new File("/opt/trencadis/files/reports_duplicate");
        	Collection<File> files = FileUtils.listFiles(homeDir, fileFilter, (IOFileFilter) null);
        	for (int i = 0; i < 400; i++) {
        		int j = 0;
	            for (File xmlDsr: files) {            
//	            	xmlDsrTest1   = new TRENCADIS_XML_DICOM_SR_FILE(session, xmlDsr);            
//		      		gs = new TRENCADIS_XMLDSR_UPLOAD(session, xmlDsrTest1, 1);
//		      		gs.execute();
//		      		Thread.sleep(1000l);
		      		xmlDsrTest2   = new TRENCADIS_XML_DICOM_SR_FILE(session, xmlDsr);
		      		gs = new TRENCADIS_XMLDSR_UPLOAD(session, xmlDsrTest2, 2);
		      		gs.execute();
		      		System.out.println("  > " + xmlDsrTest2.getIDReport()
		      						   //+ "\t- trencadisv06: " + xmlDsrTest1.getIDTRENCADISReport()
		      						   + "\t- vmblade17: " + xmlDsrTest2.getIDTRENCADISReport());
		      		if (j == 50 || j == 100) {
		      			File tmpDir = new File("/tmp");
		      			String[] extension = new String[] { "xml" };
		      			for (final File file : FileUtils.listFiles(tmpDir, extension, true)) {
		      				file.delete();
		      			}
		      			Thread.sleep(360000l);
		      		}
		      		Thread.sleep(1000l);
		      		j++;
	            }
	            //System.out.println("Iteration " + i + " completed.");
	            Thread.sleep(300000l);
        	}
            System.out.println("TEST Succesfully");
            /*******************************************************************/
                        
            /******************* TEST - TRENCADIS_XMLDSR_UPLOAD - CONSTRUCTOR 2 *******      
            xmlDSR  = new File("/home/trencadis/DICOMStorage/BackEnd/backup/EQ_1_3.xml");
            xmlDsr3 = new TRENCADIS_XML_DICOM_SR_FILE(session, xmlDSR);
            gs = new TRENCADIS_XMLDSR_UPLOAD(session,xmlDsr3,"1");
            gs.execute();
            System.out.println("TEST Succesfully - UPLOAD DICOMSR Exploration Breast to IDCenter 1 - CONSTRUCTOR 1");
            /*******************************************************************/
             
            /******************* TEST - TRENCADIS_XMLDSR_REMOVE - CONSTRUCTOR 1 *******                  
            gs2 = new TRENCADIS_XMLDSR_REMOVE(session, xmlDsr3, "1");
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
            gs3 = new TRENCADIS_XMLDSR_DOWNLOAD(session, 1, "6", "1D30DF20-C70A-11E4-8D35-E68FFB6C6EED");
            xmlDsrDownload = (TRENCADIS_XML_DICOM_SR_FILE) gs3.execute();
            System.out.println("   ID Ontology: " + xmlDsrDownload.getIDOntology());
            System.out.println("   ID Report: " + xmlDsrDownload.getIDReport());
            FileUtils.writeStringToFile(new File(TEST_OUTPUT_DIR + File.separator
        			+ xmlDsrDownload.getIDReport() + ".xml")
        			, xmlDsrDownload.getContents());
            System.out.println("TEST Succesfully");
            
            /*******************************************************************
            xmlDsr1 = new TRENCADIS_XML_DICOM_SR_FILE(session,new File("/home/trencadis/DICOMStorage/BackEnd/backup/EQ_1_3.xml"));
            xmlDsr1.generateDICOMSR();
            /*******************************************************************/
            
            /**************** TEST - TRENCADIS_XMLDSR_DOWNLOAD_ALL *************
            System.out.println("TEST DOWNLOAD_ALL_DICOMSR");
            TRENCADIS_XMLDSR_DOWNLOAD gs6 = new TRENCADIS_XMLDSR_DOWNLOAD(session, 1);
            Vector<TRENCADIS_XML_DICOM_SR_FILE> v_xmldsr = (Vector<TRENCADIS_XML_DICOM_SR_FILE>) gs6.execute();            
            for (TRENCADIS_XML_DICOM_SR_FILE xmldsr : v_xmldsr) {
            	FileUtils.writeStringToFile(new File(TEST_OUTPUT_DIR + File.separator
            			+ xmldsr.getIDReport() + ".xml")
            			, xmldsr.getContents());
            	System.out.println(" -> Download report with ontology: " + xmldsr.getIDOntology()
            			+ " and report ID " + xmldsr.getIDTRENCADISReport());
            }        	
            System.out.println("TEST Succesfully");            
            /*******************************************************************/
            
            /************ TEST - TRENCADIS_XMLDSR_DOWNLOAD_ALL_BY_ONTOLOGY *****
            System.out.println("TEST DOWNLOAD_ALL_DICOMSR_BY_ONTOLOGY");
            TRENCADIS_XMLDSR_DOWNLOAD gs7 = new TRENCADIS_XMLDSR_DOWNLOAD(session, 1, "7");
            Vector<TRENCADIS_XML_DICOM_SR_FILE> v_xmldsr_onto = (Vector<TRENCADIS_XML_DICOM_SR_FILE>)gs7.execute();            
            for (TRENCADIS_XML_DICOM_SR_FILE xmldsr : v_xmldsr_onto) {
            	FileUtils.writeStringToFile(new File(TEST_OUTPUT_DIR + File.separator
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
            FileUtils.writeStringToFile(new File(TEST_OUTPUT_DIR + File.separator
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
            
            /**** TEST - TRENCADIS_XMLDSR_DOWNLOAD_INDEXER_IDs (ontology and center) *****/
            System.out.println("TEST DOWNLOAD_ALL_DICOMSR_IDs");
            TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS gs8 = new TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS(session);
            int i = 0;
            for (TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE dicomStorageIDS : gs8.getDICOMStorageIDS()) {
            	String data = "";
            	data += dicomStorageIDS.getCenterName() + "\n";
            	for (DICOM_SR_ID id : dicomStorageIDS.getDICOM_DSR_IDS())
            		data += "\t- " + id.getValue() + "\n";
            	FileUtils.writeStringToFile(new File("/home/locamo/", (dicomStorageIDS.getCenterName().replaceAll(" ", "")) + i), data );
            	i++;
            }
            System.out.println("TEST Succesfully");            
            /*******************************************************************/
            
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
