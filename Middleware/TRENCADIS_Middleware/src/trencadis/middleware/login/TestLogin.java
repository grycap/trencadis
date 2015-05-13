/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.middleware.login;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 *
 * @author root
 */
public class TestLogin {
     public static void main(String[] args) {
        
    	 try{

        	File configFile = new File("/opt/trencadis/trencadis.config");
            TRENCADIS_SESSION session = new TRENCADIS_SESSION(configFile, "1234567890");            
            
            System.out.println(session.getX509VOMSProxyFile().getAbsolutePath().toString());
            FileUtils.writeStringToFile(new File("/opt/trencadis/proxy.txt"), session.getX509VOMSCredential());
        
    	 }catch(Exception ex){
    		 ex.printStackTrace();
    	 }
     }
}
