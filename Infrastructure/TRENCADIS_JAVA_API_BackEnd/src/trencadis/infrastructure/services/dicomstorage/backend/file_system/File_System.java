/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.services.dicomstorage.backend.file_system;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;



/**
 *
 * @author root
 */
public class File_System {
    private String home_dir;
    
    public File_System(String home_dir){
        this.home_dir = home_dir;
    }
    
    public String getBackEndParameters() {
    	String config = "";
    	config += "\t\t<BACKEND_TYPE>FILE_SYSTEM</BACKEND_TYPE>\n";
    	config += "\t\t<HOME_DIR>";
    	config += home_dir;
    	config += "</HOME_DIR>\n";    	
    	return config;
    }
    
    public void addFile(String strIDFile, File  f){  
        try{
            File nf = new File(home_dir + "/" + strIDFile);
            if (nf.exists()){
                throw new RuntimeException("The file (IDFile=" + strIDFile + ") exists. The file can not be rewrited.");
            }else{
                FileUtils.copyFile(f, nf);        
            }
        }catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
            
    }
    
    public void deleteFile(String strIDFile){
        try{                        
            FileUtils.forceDelete(new File(home_dir + "/" + strIDFile));        
        }catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public String xmlGetDICOMSRFile(String strIDFile){
        try{
        	return FileUtils.readFileToString(new File(home_dir + "/" + strIDFile));
        }catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public String xmlGetAllDICOMSRFiles(String strIDFiles){
    	try{        	
        	String retval = "";
        	String[] idFiles = strIDFiles.split(",");
        	for(String strIDFile : idFiles) {
        		retval += "\t<xml_DICOM_SR>";
        		String aux = this.xmlGetDICOMSRFile(strIDFile);
        		retval += aux.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        		retval += "</xml_DICOM_SR>\n";
        	}
        	return retval;
        }catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public String xmlGetAllDICOMSRIDs() {
    	try{
        	IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
        	File homeDir = new File(home_dir + "/");
        	Collection<File> files = FileUtils.listFiles(homeDir, fileFilter, (IOFileFilter) null);
        	String retval = "";
        	for(File f : files) {
        		retval += f.getName();
        		retval += ",";
        	}
        	retval = retval.substring(0, retval.length() - 1);
        	return retval;
        }catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
}
