/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.services.dicomstorage.backend;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import trencadis.infrastructure.services.dicomstorage.backend.file_system.File_System;
import trencadis.infrastructure.services.dicomstorage.backend.grid_ftp.Grid_FTP;

/**
 *
 * @author root
 */
public class BackEnd {
    
    //private Enumeration<String> type_support = {FILE_SYSTEM, GRID_FTP};
        
    private String BackEndType;
    private Object backend;
    private String strError;
    
    public BackEnd(String xmlBackEndConfig){
        try{
            strError = "";                
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            java.io.ByteArrayInputStream abDoc = new java.io.ByteArrayInputStream(xmlBackEndConfig.getBytes());
            Document doc = dBuilder.parse(abDoc);                                          

            BackEndType = doc.getElementsByTagName("BACKEND_TYPE").item(0).getTextContent();
            
            if (BackEndType.equals("FILE_SYSTEM")){            	
                backend = new File_System(doc.getElementsByTagName("HOME_DIR").item(0).getTextContent());  
            }
            if (BackEndType.equals("GRID_FTP")){            	
                backend = new Grid_FTP(doc.getElementsByTagName("HOST_NAME").item(0).getTextContent(),
                					   doc.getElementsByTagName("HOST_PORT").item(0).getTextContent(),
                					   doc.getElementsByTagName("HOME_DIR").item(0).getTextContent());  
          }
                    
        }catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public String strGetError(){
        return strError;
    }
  
    public String iGetBackEndParameters() {
    	try{
            if (BackEndType.equals("FILE_SYSTEM")){
                return ((File_System)(this.backend)).getBackEndParameters();
            }
            if (BackEndType.equals("GRID_FTP")){
                return ((Grid_FTP)(this.backend)).getBackEndParameters();
            }
            return null;
        }catch(Exception ex){
            strError = ex.toString();
            return null;
        }
    }
    
    public int iAddFile(String strIDFile, File f, String credential)throws Exception{
        try{
            if (BackEndType.equals("FILE_SYSTEM")){
                ((File_System)(this.backend)).addFile(strIDFile, f);                
            }
            if (BackEndType.equals("GRID_FTP")){
                ((Grid_FTP)(this.backend)).addFile(strIDFile, f, credential);                
            }            
            return 0;
        }catch(Exception ex){
            strError = ex.toString();
            return -1;
        }
    }        
    
    
    public int iDeleteFile(String IDFile, String credential) throws Exception {
        try{
            if (BackEndType.equals("FILE_SYSTEM")){
                ((File_System)(this.backend)).deleteFile(IDFile);
            }
            if (BackEndType.equals("GRID_FTP")){
                ((Grid_FTP)(this.backend)).deleteFile(IDFile, credential);
            }            
            return 0;
        }catch(Exception ex){
            strError = ex.toString();
            return -1;
        }        
    }
    
    public String xmlGetDICOMSRFile(String IDFile, String credential)
    		throws Exception {
        try{
            if (BackEndType.equals("FILE_SYSTEM")){
                return ((File_System)(this.backend)).xmlGetDICOMSRFile(IDFile);
            }
            if (BackEndType.equals("GRID_FTP")){
                return ((Grid_FTP)(this.backend)).xmlGetDICOMSRFile(IDFile, credential);
            }
            return null;
        }catch(Exception ex){
            strError = ex.toString();
            return null;
        } 
    }
    
    public String xmlGetAllDICOMSRFiles(String IDFiles, String credential) throws Exception {
        try{
            if (BackEndType.equals("FILE_SYSTEM")){
                return ((File_System)(this.backend)).xmlGetAllDICOMSRFiles(IDFiles);
            }
            if (BackEndType.equals("GRID_FTP")){
                return ((Grid_FTP)(this.backend)).xmlGetAllDICOMSRFiles(IDFiles, credential);
            }
            return null;
        }catch(Exception ex){
            strError = ex.toString();
            return null;
        }
        
    }
  
}
