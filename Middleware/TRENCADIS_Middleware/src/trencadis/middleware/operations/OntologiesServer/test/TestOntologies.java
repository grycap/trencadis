/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.middleware.operations.OntologiesServer.test;

import java.io.File;
import java.net.URI;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import trencadis.middleware.files.TRENCADIS_XML_ONTOLOGY_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;
import trencadis.middleware.operations.OntologiesServer.TRENCADIS_CREATE_ONTOLOGY;
import trencadis.middleware.operations.OntologiesServer.TRENCADIS_GET_ALL_ONTOLOGIES;

/**
 *
 * @author root
 */
public class TestOntologies {
    public static void main(String[] args) {
        try{
            URI uIIS           = null;
            File xmlDSR        = null;            
            String idontology  = null;
            String description = null;
            TRENCADIS_XML_ONTOLOGY_FILE xmlDsrStr = null;
                                   
            
            File configFile = new File("/opt/trencadis/trencadis.config");
            TRENCADIS_SESSION session = new TRENCADIS_SESSION(configFile, "1234567890");
                                    
            
            /******************* TEST - Create ONTOLOGY - CONSTRUCTOR 1 *******
            uIIS        = new URI("https://trencadisv04.i3m.upv.es:8443/wsrf/services/trencadis/infrastructure/services/OntologiesServer");                                    
            xmlDSR      = new File("/home/trencadis/middleware/test/01_Exploration_of_Breast.xml");
            xmlDsrStr   = new TRENCADIS_XML_ONTOLOGY_FILE(session, xmlDSR);
            idontology  = "4";            
            description = "Exploration of Breast";
                        
            //TRENCADIS_CREATE_ONTOLOGY create_onto_4 = new TRENCADIS_CREATE_ONTOLOGY(session,uIIS,xmlDsrStr,idontology,description);
            //create_onto_4.execute();
            TRENCADIS_CREATE_ONTOLOGY create_onto = new TRENCADIS_CREATE_ONTOLOGY(session,xmlDsrStr,idontology,description);
            create_onto.execute();
            
            System.out.println("TEST Succesfully - Create Exploration Breast - CONSTRUCTOR 1");
            /******************************************************************/
          
            /******************* TEST - Create ONTOLOGY - CONSTRUCTOR 1 *******
            uIIS        = new URI("https://trencadisv04.i3m.upv.es:8443/wsrf/services/trencadis/infrastructure/services/OntologiesServer");                                    
            xmlDSR      = new File("/opt/trencadis/files/templates/05_Mammography_BIRADS_5th.xml");
            xmlDsrStr   = new TRENCADIS_XML_ONTOLOGY_FILE(session, xmlDSR);
            idontology  = "5";            
            description = "Mammography";
                        
            TRENCADIS_CREATE_ONTOLOGY create_onto_5 = new TRENCADIS_CREATE_ONTOLOGY(session,uIIS,xmlDsrStr,idontology,description);
            create_onto_5.execute();
            System.out.println("TEST Succesfully - Create Mammography - CONSTRUCTOR 1");
            /******************************************************************/
            
            /******************* TEST - Create ONTOLOGY - CONSTRUCTOR 1 *******
            uIIS        = new URI("https://trencadisv04.i3m.upv.es:8443/wsrf/services/trencadis/infrastructure/services/OntologiesServer");                                    
            xmlDSR      = new File("/opt/trencadis/files/templates/06_Ecography_BIRADS_5th.xml");
            xmlDsrStr   = new TRENCADIS_XML_ONTOLOGY_FILE(session, xmlDSR);
            idontology  = "6";            
            description = "Ultrasound Scan";
                        
            TRENCADIS_CREATE_ONTOLOGY create_onto_6 = new TRENCADIS_CREATE_ONTOLOGY(session,uIIS,xmlDsrStr,idontology,description);
            create_onto_6.execute();
            System.out.println("TEST Succesfully - Create Ultrasound Scan - CONSTRUCTOR 1");
            /******************************************************************/
          
            /******************* TEST - Create ONTOLOGY - CONSTRUCTOR 1 *******
            uIIS        = new URI("https://trencadisv04.i3m.upv.es:8443/wsrf/services/trencadis/infrastructure/services/OntologiesServer");                                    
            xmlDSR      = new File("/opt/trencadis/files/templates/07_MRI_BIRADS_5th.xml");
            xmlDsrStr   = new TRENCADIS_XML_ONTOLOGY_FILE(session, xmlDSR);
            idontology  = "7";            
            description = "Magnetic Resonance";
                        
            TRENCADIS_CREATE_ONTOLOGY create_onto_7 = new TRENCADIS_CREATE_ONTOLOGY(session,uIIS,xmlDsrStr,idontology,description);
            create_onto_7.execute();
            System.out.println("TEST Succesfully - Create Magnetic Resonance - CONSTRUCTOR 1");
            /******************************************************************/
                                                           
            /******************* TEST - DESTROY ONTOLOGY  *********************
            TRENCADIS_REMOVE_ONTOLOGY remove_onto = new TRENCADIS_REMOVE_ONTOLOGY(session, "7");
            remove_onto.execute();                        
            System.out.println("TEST Succesfully - Remove 7");
            /******************************************************************/
                        
            /******************* TEST - Create ONTOLOGY - CONSTRUCTOR 2 *******        
            xmlDSR      = new File("/home/trencadis/middleware/test/01_Exploration_of_Breast.xml");
            xmlDsrStr   = new TRENCADIS_XML_O
            File configFile = new File("/home/trencadis/middleware/trencadis.config");
            TRENCADIS_SESSIONTOLOGY_FILE(session, xmlDSR);
            idontology  = "4";            
            description = "Exploration of Breast";
                        
            TRENCADIS_CREATE_ONTOLOGY create_onto2 = new TRENCADIS_CREATE_ONTOLOGY(session,xmlDsrStr,idontology,description);
            create_onto2.execute();
            System.out.println("TEST Succesfully - Create Exploration Breast -  CONSTRUCTOR 2");
            /******************************************************************/
            
            /******************* TEST - DESTROY ONTOLOGY  *******************
            TRENCADIS_REMOVE_ONTOLOGY remove_onto5 = new TRENCADIS_REMOVE_ONTOLOGY(session, "5");
            remove_onto5.execute();    
            TRENCADIS_REMOVE_ONTOLOGY remove_onto6 = new TRENCADIS_REMOVE_ONTOLOGY(session, "6");
            remove_onto6.execute();
            TRENCADIS_REMOVE_ONTOLOGY remove_onto7 = new TRENCADIS_REMOVE_ONTOLOGY(session, "7");
            remove_onto7.execute();
            System.out.println("TEST Destroy Onolgies Succesfully");
            /******************************************************************/
            
            /******************* TEST - GET ONTOLOGY - CONSTRUCTOR 1 *********                                  
            TRENCADIS_GET_ONTOLOGY get_onto = new TRENCADIS_GET_ONTOLOGY(session, "5");
            TRENCADIS_XML_ONTOLOGY_FILE trencadis_dicoms_sr = get_onto.execute();            
            System.out.println(trencadis_dicoms_sr.getContents());
            System.out.println(trencadis_dicoms_sr.getIDOntology());
            System.out.println(trencadis_dicoms_sr.getDescription());
            System.out.println("TEST Succesfully - Get Ontology 5 Mammography");
            /******************************************************************/
            
            /***************************** TEST - GET ALL ONTOLOGIES ******************************/
            System.out.println("TEST - Get All Ontologies");
            TRENCADIS_GET_ALL_ONTOLOGIES get_all_onto = new TRENCADIS_GET_ALL_ONTOLOGIES(session);
            Vector<TRENCADIS_XML_ONTOLOGY_FILE> trencadis_all_dicoms_sr = get_all_onto.execute();            
            for(TRENCADIS_XML_ONTOLOGY_FILE ontology_file : trencadis_all_dicoms_sr) {
            	FileUtils.writeStringToFile(new File("/opt/trencadis/ontologies/"
            								+ ontology_file.getDescription() + ".xml"),
            								ontology_file.getContents());
            	System.out.println(" -> Get ontology " + ontology_file.getDescription());
            }
            System.out.println("TEST Succesfully");
            /**************************************************************************************/
                                                
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
