/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS.Test;

import java.security.Security;

import org.apache.axis.message.MessageElement;
import org.globus.axis.util.Util;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;

import trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS.IIS_Query_RPs;

/**
 *
 * @author root
 */
public class Test {
        
    public static void main(String[] args) {
        try{
            
            args = new String[2];
            
            args[0] = "https://trencadisv02.i3m.upv.es:8443/wsrf/services/DefaultIndexService";
            //args[1] = "/tmp/damian.proxy";
            args[1] = "/opt/trencadis/tmp/x509up_uTRENCADIS_MIDDLEWARE_1422532782736";
            
            //Secure Conections
            Security.removeProvider("SunPKCS11-Solaris");
            Util.registerTransport();	 
            
            System.setProperty("X509_USER_PROXY", args[1]);
            System.setProperty("CADIR", "/etc/grid-security/certificates");
            System.setProperty("X509_CERT_DIR", "/etc/grid-security/certificates");
            
            //System.setProperty( "X509_USER_CERT", "/etc/grid-security/usercert.pem");
           // System.setProperty( "X509_USER_KEY",  "/etc/grid-security/userkey.pem");
            
            System.setProperty( "X509_USER_CERT", "/opt/trencadis/usercert.pem");
            System.setProperty( "X509_USER_KEY",  "/opt/trencadis/userkey.pem");
            
            IIS_Query_RPs query_iis = new IIS_Query_RPs();                
            String strxPathQuery ="//*[namespace-uri()='http://services.infrastructure.trencadis/EOUIDGenerator' and local-name()='EOUIDGeneratorServiceInfo']//*[local-name()='URI']";                
            String  strURL_IIS = args[0].toString();
                        
            QueryResourcePropertiesResponse response = query_iis.QueryRPs(strURL_IIS, strxPathQuery);
            if (response == null){
                throw new Exception(query_iis.strGetError());
            }

            MessageElement[] responseContents = response.get_any();
                                    
    
            System.out.println(responseContents.length);
            

        }catch (Exception ex){
            ex.printStackTrace();
        }
    
    }
}
