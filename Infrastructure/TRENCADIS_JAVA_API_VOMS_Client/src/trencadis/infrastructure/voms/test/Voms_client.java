/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.voms.test;


import trencadis.infrastructure.voms.contact.VOMSProxyInit;
import trencadis.infrastructure.voms.contact.VOMSRequestOptions;
import trencadis.infrastructure.voms.contact.VOMSServerInfo;

/**
 *
 * @author root
 */
public class Voms_client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            
           // TODO code application logic here
           //System.setProperty( "X509_USER_CERT", "/etc/grid-security/usercert.pem");
           //System.setProperty( "X509_USER_KEY",  "/etc/grid-security/userkey.pem");
           System.setProperty( "X509_USER_CERT", "/opt/trencadis/usercert.pem");
           System.setProperty( "X509_USER_KEY",  "/opt/trencadis/userkey.pem");
        	
           //Esto no funciona.....
           //System.setProperty("PKCS12_USER_CERT", "/etc/grid-security/91_TRENCADIS_certificate.p12");                                 
           //System.setProperty("PKCS12_USER_KEY_PASSWORD", "1234567890");           
           
            
           System.setProperty("VOMSES_LOCATION", "/etc/grid-security/vomses_trencadis"); 
           System.setProperty("VOMSDIR","/etc/grid-security/vomses");
           System.setProperty("CADIR", "/etc/grid-security/certificates");
           System.out.println("X509_CERT_DIR value: " + System.getProperty("X509_CERT_DIR"));               
           VOMSProxyInit vpi = VOMSProxyInit.instance("1234567890");
                                          
           vpi.setProxyOutputFile("/tmp/damian.proxy");            
           vpi.setProxyType(trencadis.infrastructure.voms.contact.VOMSProxyBuilder.GT4_PROXY);                   
           vpi.setDelegationType(trencadis.infrastructure.voms.contact.VOMSProxyConstants.DELEGATION_FULL);      
                                                                                             
           System.out.println("1......" + vpi.getProxyOutputFile());
           System.out.println("2......" + vpi.getProxyType());
           System.out.println("3......" + vpi.getDelegationType());
           System.out.println("4......" + vpi.getProxyLifetime());
           System.out.println("5......" + vpi.getProxyKeySize());
           System.out.println("6......" + vpi.getPolicyType());
           
           
           VOMSServerInfo vsi = new VOMSServerInfo();
           vsi.setVoName("TRENCADIS");
           vsi.setHostName("trencadisv01.i3m.upv.es"); 
           vsi.setPort(15000);
           vsi.setHostDn("/C=ES/O=UPV-GRyCAP-CA/OU=UPV-I3M/OU=GRyCAP/CN=host/trencadisv01.i3m.upv.es");            
           vsi.setAlias("TRENCADIS"); 
           
           VOMSRequestOptions vrp = new VOMSRequestOptions();
           vrp.setVoName("TRENCADIS");
           vrp.setLifetime(10000);
           java.util.ArrayList<VOMSRequestOptions> alvrp = new java.util.ArrayList<VOMSRequestOptions>();
           alvrp.add(vrp);                                           
           
           vpi.addVomsServer(vsi);
           vpi.getVomsProxy(alvrp);
           System.out.println("FIN");
            
        }catch(Exception ex){            
            ex.printStackTrace();
            System.out.println(ex.toString());
        }
    }
}