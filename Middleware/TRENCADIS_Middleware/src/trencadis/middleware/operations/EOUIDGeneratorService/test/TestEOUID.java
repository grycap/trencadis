
package trencadis.middleware.operations.EOUIDGeneratorService.test;

import java.io.File;
import java.net.URI;

import trencadis.middleware.login.TRENCADIS_SESSION;
import trencadis.middleware.operations.EOUIDGeneratorService.TRENCADIS_NEW_EOUID;

/**
 *
 * @author root
 */
public class TestEOUID {
      public static void main(String[] args) {
        try{
                               
            File configFile = new File("/opt/trencadis/trencadis.config");
            TRENCADIS_SESSION session = new TRENCADIS_SESSION(configFile, "1234567890");
            
            /******************* TEST - EOUIDGeneratorService - CONSTRUCTOR 1 *******/
            TRENCADIS_NEW_EOUID new_eouid = new TRENCADIS_NEW_EOUID(session);
            String eouid = new_eouid.execute();
            
            System.out.println(eouid);
            /******************************************************************/                                   
            
         
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
