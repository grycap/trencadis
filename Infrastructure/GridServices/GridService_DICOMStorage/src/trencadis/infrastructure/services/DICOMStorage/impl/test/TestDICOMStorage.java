/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.services.DICOMStorage.impl.test;


import java.io.File;
import org.apache.commons.io.FileUtils;
import trencadis.infrastructure.services.DICOMStorage.impl.DICOMStorageResource;

/**
 *
 * @author root
 */
public class TestDICOMStorage {
         
    public static void main(String[] args) {
        try{
            String xmlOntology   = "";
            String strURI        = "";            
            String strURIFactory = "";
            String xmlInput      = "";
            String xmlOutput     = "";
            String xmlDICOMSR    = "";
                                                            
            DICOMStorageResource resource4 = new DICOMStorageResource();
            DICOMStorageResource resource5 = new DICOMStorageResource();
            resource5.bIsDebug = true;
            resource4.bIsDebug = true;
            
            //String strCertificado =  FileUtils.readFileToString(new File("/home/locamo/proxy.txt"));         

            /******************************************************************/
            /***************** TEST Inicialize Ontology 4 *********************          
            xmlOntology    = FileUtils.readFileToString(new File("/home/trencadis/middleware/test/01_Exploration_of_Breast.xml")); 
            strURI        = "";
            strURIFactory = "";
            resource4.initialize(strCertificado,null, strURI, strURIFactory, "4", "Exploración of Breast",xmlOntology);
            System.out.println("TEST Inicialize Ontology 4 SUCCESFULLY");
            /******************************************************************/
            
            /******************************************************************/
            /***************** TEST Inicialize Ontology 5 *********************
            xmlOntology    = FileUtils.readFileToString(new File("/home/trencadis/middleware/test/02_Mamography.xml")); 
            strURI         = "";
            strURIFactory  = "";
            resource5.initialize(strCertificado,null, strURI, strURIFactory, "5", "Mamography",xmlOntology);
            System.out.println("TEST Inicialize Ontology 5 SUCCESFULLY");            
            /******************************************************************/
            
            
            /******************************************************************/
            /***************** TEST xmlAddReport from IDOntology 4 ************
            xmlDICOMSR = FileUtils.readFileToString(new File("/opt/trencadis/files/reports/ECO_12_750.xml"));
            xmlDICOMSR = xmlDICOMSR.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
                    
            xmlInput = "<INPUT>" +
                         "<CERTIFICATE>" + strCertificado + "</CERTIFICATE>"  +
                         "<IDONTOLOGY>6</IDONTOLOGY>"   +
                         "<IDTRENCADISREPORT>31D92320-B0D6-11E2-8ADE-E5F9126E9D9D</IDTRENCADISREPORT>" +
                         "<DICOM_SR> " +                             
                                "<xml_DICOM_SR>" + xmlDICOMSR + "</xml_DICOM_SR>" +                            
                         "</DICOM_SR>" +
                    "</INPUT>";      
            xmlOutput = resource4.xmlAddReport(xmlInput);
            System.out.println(xmlOutput);
            
            
            /******************************************************************/
            /***************** TEST xmlAddReport from IDOntology 5 ************
            xmlDICOMSR    = FileUtils.readFileToString(new File("/home/trencadis/DICOMStorage/BackEnd/backup/MAMO_1_1.xml"));         
            xmlDICOMSR = xmlDICOMSR.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
                    
            xmlInput = "<INPUT>" +
                         "<CERTIFICATE>" + strCertificado + "</CERTIFICATE>"  +
                         "<IDONTOLOGY>5</IDONTOLOGY>"   +
                         "<IDTRENCADISREPORT>31D92320-B0D6-11E2-8ADE-E5F9126E9D9C</IDTRENCADISREPORT>" +
                         "<DICOM_SR> " +                             
                                "<xml_DICOM_SR>" + xmlDICOMSR + "</xml_DICOM_SR>" +                            
                         "</DICOM_SR>" +
                    "</INPUT>";               
            xmlOutput = resource5.xmlAddReport(xmlInput);
            System.out.println("Add Report");
            System.out.println(xmlOutput);
            
            
            /******************************************************************/
            /***************** TEST xmlRemoveReport from IDOntology 4 *********        
            xmlInput = "<INPUT>" +
                         "<CERTIFICATE>" + strCertificado + "</CERTIFICATE>"  +
                         "<IDONTOLOGY>6</IDONTOLOGY>"   +
                         "<IDTRENCADISREPORT>33173300-C400-11E4-8D35-9DE56F0C43B3</IDTRENCADISREPORT>" +                         
                    "</INPUT>";               
            xmlOutput = resource4.xmlRemoveReport(xmlInput);
            System.out.println("Remove Report");
            System.out.println(xmlOutput);                                    
            /******************************************************************/
            
            /******************************************************************/
            /***************** TEST xmlRemoveReport from IDOntology 5 *********            
            xmlInput = "<INPUT>" +
                         "<CERTIFICATE>" + strCertificado + "</CERTIFICATE>"  +
                         "<IDONTOLOGY>5</IDONTOLOGY>"   +
                         "<IDTRENCADISREPORT>31D92320-B0D6-11E2-8ADE-E5F9126E9D9C</IDTRENCADISREPORT>" +                         
                    "</INPUT>";               
            xmlOutput = resource5.xmlRemoveReport(xmlInput);
            System.out.println("Remove Report");
            System.out.println(xmlOutput);                                    
            /******************************************************************/
            
            /******************************************************************/
            /***************** TEST xmlDownloadReport from IDOntology 4 *******           
            xmlInput = "<INPUT>" +
                         "<CERTIFICATE>" + strCertificado + "</CERTIFICATE>"  +
                         "<IDONTOLOGY>4</IDONTOLOGY>"   +
                         "<IDTRENCADISREPORT>31D92320-B0D6-11E2-8ADE-E5F9126E9D9D</IDTRENCADISREPORT>" +                         
                    "</INPUT>";               
            xmlOutput = resource4.xmlDownloadReport(xmlInput);
            System.out.println("Download Report");
            System.out.println(xmlOutput);                                    
            /******************************************************************/
            
            
            /******************************************************************/
            /***************** TEST xmlDownloadReport from IDOntology 6 *******
            xmlInput = "<INPUT>\n" +
                         "\t<CERTIFICATE>\n\t" + strCertificado + "\t</CERTIFICATE>\n"  +
                         "\t<IDONTOLOGY>6</IDONTOLOGY>\n"   +
                         "\t<IDTRENCADISREPORT>1D30DF20-C70A-11E4-8D35-E68FFB6C6EED</IDTRENCADISREPORT>\n" +                         
                    "</INPUT>";   
            xmlOutput = resource5.xmlDownloadReport(xmlInput);
            System.out.println("Download Report");
            System.out.println(xmlOutput);                                    
            /******************************************************************/
            

            
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
