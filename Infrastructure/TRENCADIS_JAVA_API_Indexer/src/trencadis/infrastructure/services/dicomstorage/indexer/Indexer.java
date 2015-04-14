/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.services.dicomstorage.indexer;


import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.DSRAMGAHelper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import arda.md.javaclient.AttributeSet;
import arda.md.javaclient.AttributeSetList;

import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author root
 */
public class Indexer {

   // private Enumeration<String> typr_support = {AMGA, NEO4JAVA, POSTGRESQL};
        
    private String IndexerType;
    private Object indexer_backend;
    private String strError;
        
    public Indexer(String xmlIndexerConfig){
        try{
            strError = "";                
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            java.io.ByteArrayInputStream abDoc = new java.io.ByteArrayInputStream(xmlIndexerConfig.getBytes());
            Document doc = dBuilder.parse(abDoc);                                          

            IndexerType = doc.getElementsByTagName("INDEXER_TYPE").item(0).getTextContent();
            
            if (IndexerType.equals("AMGA")){                                                                 
                
                 indexer_backend = new DSRAMGAHelper(
                                        doc.getElementsByTagName("HOST_NAME").item(0).getTextContent(),
					doc.getElementsByTagName("HOST_PORT").item(0).getTextContent(),					
                                        doc.getElementsByTagName("HOME_DIR").item(0).getTextContent().replace(" ","_"),										
					doc.getElementsByTagName("TMP_DIR").item(0).getTextContent());  
           }                                                                                                                                       
                    
        }catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    public String strGetError(){
        return strError;
    }
    /*
     * Add xmdlDICOMSR structure (Ontology) to the System
     */
    public int iAddStructure(String xmlDsrStr) throws Exception {
        try{
            if (IndexerType.equals("AMGA")){                
                ((DSRAMGAHelper)(this.indexer_backend)).addStructure(xmlDsrStr);
            }
            
            return 0;
        }catch(Exception ex){
            strError = ex.toString();
            return -1;
        }
    }
    /*
     * Remove DICOMSR structure (IDOntology) from the System. This method remove strucuture and all data.
     */
    public int iDeleteStructure(String IDOntology) throws Exception {
        try{
            if (IndexerType.equals("AMGA")){
                ((DSRAMGAHelper)(this.indexer_backend)).deleteStructure(IDOntology);
            }
            return 0;
        }catch(Exception ex){
            strError = ex.toString();
            return -1;
        }
    }        
    /*
     * Add report data complaint to DICOMSR structure (Ontology) to the System
     */
    public int iAddReport(String xmlDsr)throws Exception{
        try{
            if (IndexerType.equals("AMGA")){                
                ((DSRAMGAHelper)(this.indexer_backend)).addReport(xmlDsr);                
            }
            
            return 0;
        }catch(Exception ex){
            strError = ex.toString();
            return -1;
        }
    }            
    /*
     * Remove report data complaint to DICOMSR structure (Ontology) to the System
    */
    public int iDeleteReport(String IDTRENCADISReport, String xmlOntology) throws Exception {
        try{
            if (IndexerType.equals("AMGA")){
                ((DSRAMGAHelper)(this.indexer_backend)).deleteReport(xmlOntology, IDTRENCADISReport);
            }
            return 0;
        }catch(Exception ex){
            strError = ex.toString();
            return -1;
        }
    }    
    /*
     * List report data complaint to DICOMSR structure (Ontology) to the System
    */
    public int iListReport(String IDTRENCADISReport, String xmlOntology) throws Exception {
        try{
            if (IndexerType.equals("AMGA")){
                ((DSRAMGAHelper)(this.indexer_backend)).listReport(xmlOntology, IDTRENCADISReport);
            }
            return 0;
        }catch(Exception ex){
            strError = ex.toString();
            return -1;
        }
    }
    
    public Vector vQueryDICOMIds(String query, String reportType) throws Exception {
        try{
            Vector vResult = null;
            if (IndexerType.equals("AMGA")){
                //vResult = ((DSRAMGAHelper)(this.indexer_backend)).TO DO
            }
            return vResult;
        }catch(Exception ex){
            strError = ex.toString();
            return null;
        }
			
     }
    
    public String iGetDICOMIDs() {
    	try{
            String result = "";
            if (IndexerType.equals("AMGA")){
            	result = ((DSRAMGAHelper)(this.indexer_backend)).getDICOMSRIDs();
            }
            return result;
        }catch(Exception ex){
            strError = ex.toString();
            return null;
        }
    }
    
    public String iGetDICOMIDsByOntology(String ontology) {
    	try{
            String result = "";
            if (IndexerType.equals("AMGA")){
            	result = ((DSRAMGAHelper)(this.indexer_backend)).getDICOMSRIDsByOntology(ontology);
            }
            return result;
        }catch(Exception ex){
            strError = ex.toString();
            return null;
        }
    }
       
        
}
