/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.services.dicomstorage.indexer.test;


import java.io.File;
import java.util.Collection;

import trencadis.infrastructure.services.dicomstorage.indexer.Indexer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;


/**
 *
 * @author root
 */
public class TestAMGA {
         
     public static void main(String[] args) {
        try{
            
            int iErr           = 0;
            String xmlOntology = "";
            String xmlReport   = "";
            String strConfig = "<INDEXER_PARAMETERS>" +
                                    "<INDEXER_TYPE>AMGA</INDEXER_TYPE>" +
                                    "<AMGA_PARAMETERS>" +
                                        "<HOME_DIR>Hospital_Peset</HOME_DIR>" +
                                        "<HOST_NAME>trencadisv07.i3m.upv.es</HOST_NAME>" +
                                        "<HOST_PORT>8822</HOST_PORT>" +
                                        //"<TMP_DIR>/home/trencadis//DICOMStorage/tmp/</TMP_DIR>" +
                                        "<TMP_DIR>/opt/trencadis/tmp/</TMP_DIR>" + 
                                     "</AMGA_PARAMETERS>"+
                                "</INDEXER_PARAMETERS>";
            
            Indexer ind = new Indexer(strConfig);                                                                               
            String[] extensions = {"xml"};
            boolean recursive = true;
            long iIni = 0;
            long iFin = 0;
            long iSum = 0;
            int iCont = 0;
            
            //INSERT 10.000 DATA //            
            /*
            //java.util.Collection files = FileUtils.listFiles(new File("/root/TRENCADIS/Test/DICOM_SR/ECO"),extensions, recursive);            
            //java.util.Collection files = FileUtils.listFiles(new File("/root/TRENCADIS/Test/DICOM_SR/MAMO"),extensions, recursive);
            java.util.Collection files = FileUtils.listFiles(new File("/root/TRENCADIS/Test/DICOM_SR/RESO"),extensions, recursive);
            //java.util.Collection files = FileUtils.listFiles(new File("/root/TRENCADIS/Test/DICOM_SR/EQ"),extensions, recursive);            
            
            
            File file = (File) files.iterator().next();
            xmlReport = FileUtils.readFileToString(file);
            iIni = System.currentTimeMillis();
            iErr = ind.iAddReport(xmlReport);
            iFin = System.currentTimeMillis();
            
            for (java.util.Iterator iterator = files.iterator(); iterator.hasNext();) {
                file = (File) iterator.next();
                xmlReport = FileUtils.readFileToString(file);
                iIni = System.currentTimeMillis();
                iErr = ind.iAddReport(xmlReport);
                iFin = System.currentTimeMillis();
                if (iErr != 0){
                    System.out.println(ind.strGetError());                    
                }else{
                    iCont ++;
                    System.out.println("INSERT File number " + iCont + "(" + file.getAbsolutePath() + ") COMPLETED IN + " + (iFin - iIni) + "!!!");
                }                                                
                iSum = iSum + (iFin - iIni);
            }
    
            //System.out.println("INSERTADOS EN TOTAL " + iCont + " REGISTROS");            
            System.out.println("LA MEDIA ES  " + (iSum/iCont));            
                                      
        */
            
            /**************/
            /***************** TEST CREATE STRUCTURE ONTOLOGY 4 ***************
            xmlOntology = FileUtils.readFileToString(new File("/home/trencadis/middleware/test/01_Exploration_of_Breast.xml"));
            iErr = ind.iAddStructure(xmlOntology);
            if (iErr != 0){
                System.out.println(ind.strGetError());
            }else{
                System.out.println("TEST CREATE STRUCTURE ONTOLOGY 4 SUCCESFULLY");
            }
            
            /******************************************************************/
            /***************** TEST CREATE STRUCTURE ONTOLOGY 5 ***************
            xmlOntology = FileUtils.readFileToString(new File("/home/trencadis/middleware/test/02_Mamography.xml"));
            iErr = ind.iAddStructure(xmlOntology);
            if (iErr != 0){
                System.out.println(ind.strGetError());
            }else{
                System.out.println("TEST CREATE STRUCTURE ONTOLOGY 5 SUCCESFULLY");
            }
            
            /******************************************************************/
            /***************** TEST CREATE STRUCTURE ONTOLOGY 6 ***************
            xmlOntology = FileUtils.readFileToString(new File("/home/trencadis/middleware/test/03_UltrasoundScan.xml"));
            iErr = ind.iAddStructure(xmlOntology);
            if (iErr != 0){
                System.out.println(ind.strGetError());
            }else{
                System.out.println("TEST CREATE STRUCTURE ONTOLOGY 6 SUCCESFULLY");
            }
            
            /******************************************************************/
            /***************** TEST CREATE STRUCTURE ONTOLOGY 7 ***************
            xmlOntology = FileUtils.readFileToString(new File("/home/trencadis/middleware/test/04_RM.xml"));
            iErr = ind.iAddStructure(xmlOntology);
            if (iErr != 0){
                System.out.println(ind.strGetError());
            }else{
                System.out.println("TEST CREATE STRUCTURE ONTOLOGY 7 SUCCESFULLY");
            }
            
            
            /******************************************************************/
            /***************** TEST REMOVE STRUCTURE ONTOLOGY 4 ***************               
            iErr = ind.iDeleteStructure("4");
            if (iErr != 0){
                System.out.println(ind.strGetError());
            }else{
                System.out.println("TEST REMOVE STRUCTURE ONTOLOGY 4 SUCCESFULLY");
            }
            /******************************************************************/
            
            /******************************************************************/
            /***************** TEST REMOVE STRUCTURE ONTOLOGY 5 ***************   
            iErr = ind.iDeleteStructure("5");
            if (iErr != 0){
                System.out.println(ind.strGetError());
            }else{
                System.out.println("TEST REMOVE STRUCTURE ONTOLOGY 5 SUCCESFULLY");
            }
            
            /******************************************************************/
            /***************** TEST ADD DICOMSR FROM STRUCTURE ONTOLOGY 4 *****
            xmlReport = FileUtils.readFileToString(new File("/opt/trencadis/files/reports/ECO_6_750.xml"));
            iErr = ind.iAddReport(xmlReport);
            if (iErr != 0){
                System.out.println(ind.strGetError());
            }else{
                System.out.println("TEST ADD DICOMSR FROM ONTOLOGY 6 SUCCESFULLY");
            }
             
            /******************************************************************/
            /***************** TEST ADD DICOMSR FROM STRUCTURE ONTOLOGY 5 *****/  
            //xmlReport = FileUtils.readFileToString(new File("/opt/trencadis/files/reports/MAMO_1_500.xml"));
//            IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
//        	File dir = new File("/opt/trencadis/files/reports_def/MAMO/firsts");
//        	Collection<File> files = FileUtils.listFiles(dir, fileFilter, (IOFileFilter) null);
//        	for (File report : files) {
//        		System.out.println("IDReport: " + report.getName());
//        		xmlReport = FileUtils.readFileToString(report);
//	            iErr = ind.iAddReport(xmlReport);
//	            if (iErr != 0){
//	                System.out.println(ind.strGetError());
//	            }else{
//	                System.out.println("TEST ADD DICOMSR FROM ONTOLOGY 5 SUCCESFULLY");
//	            }
//        	}
			xmlReport = FileUtils.readFileToString(new File(
					"/opt/trencadis/files/reports/ECO_11_750.xml"));

			iErr = ind.iAddReport(xmlReport);
			if (iErr != 0) {
				System.out.println(ind.strGetError());
			} else {
				System.out.println("TEST ADD DICOMSR FROM ONTOLOGY 5 SUCCESFULLY");
			}
        	
            
            /******************************************************************/
            /***************** TEST LIST DICOMSR FROM ONTOLOGY 4 **************
            xmlOntology = FileUtils.readFileToString(new File("/home/trencadis/middleware/test/01_Exploration_of_Breast.xml"));
            iErr = ind.iListReport("31D92320-B0D6-11E2-8ADE-E5F9126E9D9D",xmlOntology);
            if (iErr != 0){
                System.out.println(ind.strGetError());
            }else{
                System.out.println("TEST LIST DICOMSR FROM ONTOLOGY 4 SUCCESFULLY");
            }
            
                        
            /******************************************************************/
            /***************** TEST LIST DICOMSR FROM ONTOLOGY 5 **************
            xmlOntology = FileUtils.readFileToString(new File("/home/trencadis/middleware/test/02_Mamography.xml"));
            iErr = ind.iListReport("31D92320-B0D6-11E2-8ADE-E5F9126E9D9C",xmlOntology);
            if (iErr != 0){
                System.out.println(ind.strGetError());
            }else{
                System.out.println("TEST LIST DICOMSR FROM ONTOLOGY 5 SUCCESFULLY");
            }
                        
            /******************************************************************/
            /***************** TEST REMOVE DICOMSR FROM ONTOLOGY 4 ************ 
            xmlOntology = FileUtils.readFileToString(new File("/home/trencadis/middleware/test/01_Exploration_of_Breast.xml"));
            iErr = ind.iDeleteReport("31D92320-B0D6-11E2-8ADE-E5F9126E9D9D", xmlOntology);
            if (iErr != 0){
                System.out.println(ind.strGetError());
            }else{
                System.out.println("TEST REMOVE DICOMSR FROM ONTOLOGY 4SUCCESFULLY");
            }
            */
            /******************************************************************/
            /*********************** TEST GET DICOMSR IDs *********************/
            //String res = ind.iGetDICOMIDs();
            //System.out.println(res);
            //System.out.println(ind.strGetError());
            
			/******************************************************************/
            /**************** TEST GET DICOMSR IDs BY ONTOLOGY ****************/
            String res = ind.iGetDICOMIDsByOntology("7");
            System.out.println(res);
            System.out.println(ind.strGetError());
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}