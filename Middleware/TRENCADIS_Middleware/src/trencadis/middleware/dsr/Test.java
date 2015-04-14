package trencadis.middleware.dsr;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;

import trencadis.middleware.login.TRENCADIS_SESSION;
import trencadis.middleware.operations.DICOMStorage.TRENCADIS_XMLDSR_UPLOAD;
import trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE;


/**
 *
 * @author root
 */
public class Test {
   public static void main(String[] args) {
   
       try{
        TRENCADIS_SESSION session = 
            new TRENCADIS_SESSION(new File("/root/TRENCADIS/Test/trencadis.config"),"1234567890");
        
        
        TRENCADIS_XML_DICOM_SR_FILE xmlDsr = new TRENCADIS_XML_DICOM_SR_FILE(session, new File("/root/TRENCADIS/Test/damian.input"));
        
        TRENCADIS_XMLDSR_UPLOAD tu = new TRENCADIS_XMLDSR_UPLOAD(session,xmlDsr,1);
        
        
        tu.execute();
        
        System.out.println("FIN");
        
        }catch(Exception ex){
            ex.printStackTrace();
        }
   }
}
