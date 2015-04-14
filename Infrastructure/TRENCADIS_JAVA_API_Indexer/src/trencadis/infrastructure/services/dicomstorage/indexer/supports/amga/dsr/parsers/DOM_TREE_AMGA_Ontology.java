/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.parsers;

import arda.md.javaclient.MDClient;
import arda.md.javaclient.MDServerConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.ByteArrayInputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.objects.*;

/**
 *
 * @author root
 */
public class DOM_TREE_AMGA_Ontology {
    
    private boolean bIsDebug = true;
    private boolean bIstest  = true;
    
    private String strError           = new String();
    
    private String strBaseDir         = new String();
    
    private String IDTRENCADISReport  = new String();
    
    private boolean bIsCreateOntology = false;  //To create a ontology from xmlOntology
    private boolean bIsRemoveOntology = false;  //To remove a ontology from xmlOntology
    private boolean bIsListDICOMSR    = false;  //List all Tree data from AMGA of IDTRENCADISReport based on xmlOntology
    private boolean bIsRemoveDICOMSR  = false;  //Remove all tree data of IDTRENCADISreport based on xmlOntology 
    private boolean bIsInsertDICOMSR  = false;  //Remove all tree data of IDTRENCADISreport based on xmlOntology 
        
   
    private AMGAOperationsHelper parserHelper = null;
    
        
    public DOM_TREE_AMGA_Ontology(MDServerConnection serverConn, MDClient mdClient, String base_dir) {
		this.parserHelper      = new AMGAOperationsHelper(serverConn, mdClient);
		this.strBaseDir          = base_dir;
                this.bIsCreateOntology = false;
                this.bIsRemoveOntology = false;
                this.bIsListDICOMSR    = false;
                this.bIsRemoveDICOMSR  = false;
                this.bIsInsertDICOMSR  = false; 
    }
                        
    public String strGetError(){
            return strError;
    }
                    
        
    public int iParserAMGAAddStructureOntology(String xmlOntology){
            this.bIsCreateOntology = true;
            this.bIsRemoveOntology = false;
            this.bIsListDICOMSR    = false;
            this.bIsRemoveDICOMSR  = false;
            this.bIsInsertDICOMSR  = false;
            return iParserAMGAStructureOntology(xmlOntology);
    }
    
    public int iParserAMGAListDICOMSR(String xmlOntology, String IDTRENCADISReport){
            this.bIsCreateOntology = false;
            this.bIsRemoveOntology = false;
            this.bIsListDICOMSR    = true;
            this.bIsRemoveDICOMSR  = false;
            this.bIsInsertDICOMSR  = false;
            this.IDTRENCADISReport = IDTRENCADISReport;
            return iParserAMGAStructureOntology(xmlOntology);
    }
    
    public int iParserAMGAremoveDICOMSR(String xmlOntology, String IDTRENCADISReport){
            this.bIsCreateOntology = false;
            this.bIsRemoveOntology = false;
            this.bIsListDICOMSR    = false;
            this.bIsRemoveDICOMSR  = true;
            this.bIsInsertDICOMSR  = false;
            this.IDTRENCADISReport = IDTRENCADISReport;
            return iParserAMGAStructureOntology(xmlOntology);
    }
    
                
    public int iParserAMGAInsertDICOMSR(String xmlDICOMSR){
            try{
                
                this.bIsCreateOntology = false;
                this.bIsRemoveOntology = false;
                this.bIsListDICOMSR    = false;
                this.bIsRemoveDICOMSR  = false;
                this.bIsInsertDICOMSR  = true;
                
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new ByteArrayInputStream(xmlDICOMSR.getBytes("UTF-8")));
        
            
                NodeList nList = doc.getChildNodes(); //<NODE DICOM_SR>
            
                Node nNode = nList.item(0);
                            
                if (bIsDebug){
                    System.out.println("IDOntology:"  + nNode.getAttributes().getNamedItem("IDOntology").getNodeValue());
                    if (!this.bIstest) System.out.println("IDTRENCADISReport:" + nNode.getAttributes().getNamedItem("IDTRENCADISReport").getNodeValue());
                }
        
		                        
                String strIDOntology   = nNode.getAttributes().getNamedItem("IDOntology").getNodeValue();
                if (!this.bIstest) this.IDTRENCADISReport = nNode.getAttributes().getNamedItem("IDTRENCADISReport").getNodeValue();
                else{
                    this.IDTRENCADISReport = String.valueOf(System.currentTimeMillis());
                }
                System.out.println("IDOntology: " + strIDOntology);
                System.out.println("IDTRENCADISReport: " + this.IDTRENCADISReport);
                parserHelper.addEntryHeaderDirectory(this.strBaseDir + "/ONTO_" +  strIDOntology, this.IDTRENCADISReport,strIDOntology);
                                                                               
                int iErr = iParserRecursiveXMLStructureOntologyToAMGA(nNode,this.strBaseDir + "/ONTO_" +  strIDOntology);
                if (iErr == 0) return 0;
                else 
                	return iErr;
                
    
            }catch(Exception ex){
            	ex.printStackTrace();
                strError = ex.toString();
                return -1;
            }    
    }  
        
    private int iParserAMGAStructureOntology(String xmlOntology){
            try{
                
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new ByteArrayInputStream(xmlOntology.getBytes("UTF-8")));
        
            
                NodeList nList = doc.getChildNodes(); //<NODE DICOM_SR>
            
                Node nNode = nList.item(0);
                            
                if (bIsDebug){
                    System.out.println("IDOntology:"  + nNode.getAttributes().getNamedItem("IDOntology").getNodeValue());
                    System.out.println("Description:" + nNode.getAttributes().getNamedItem("Description").getNodeValue());
                }
        
                        
                String strIDOntology = nNode.getAttributes().getNamedItem("IDOntology").getNodeValue();
                
                if (bIsCreateOntology){
                    try{
                            parserHelper.createBaseDirectory(this.strBaseDir);
                            parserHelper.createDirectory(this.strBaseDir + "/ONTO_" +  strIDOntology);                            
                            parserHelper.createHeaderDirectory(this.strBaseDir + "/ONTO_" +  strIDOntology);
                    }catch(Exception ex){
                        strError = "The ontology " + strIDOntology + " has been created in AMGA Server Previosly.";
                        return 0;
                    }
                }
                        
                if (bIsListDICOMSR) {                    
                    parserHelper.listEntryHeaderDirectory(this.strBaseDir + "/ONTO_" +  strIDOntology, IDTRENCADISReport);
                }
                
                if (bIsRemoveDICOMSR) {
                    parserHelper.removeEntryHeaderDirectory(this.strBaseDir + "/ONTO_" +  strIDOntology, IDTRENCADISReport);
                }
                                
                
                int iErr = iParserRecursiveXMLStructureOntologyToAMGA(nNode,this.strBaseDir + "/ONTO_" +  strIDOntology);
                if (iErr == 0) return 0;
                else return iErr;                
    
            }catch(Exception ex){
            	ex.printStackTrace();
                strError = ex.toString();
                return -1;
            }    
    }
    
    private  int iParserRecursiveXMLStructureOntologyToAMGA(Node nNode, String strPathbase){
            try{
                String strCNcodeschema      = new String();
                String strCNcodevalue       = new String();
                String strCNcodemeaning     = new String();
                String strCNcodeschema_ant  = new String();
                String strCNcodevalue_ant   = new String();
                String strCNcodemeaning_ant = new String();                
                DSRNode node                = null;
                

                int    iCardMin           = -1;
                int    iCardMax           = -1;
                int    iErr               =  0;  
                int    iIndex             = -1;   
                String strCurrentPathBase = new String(); 
                                                
                NodeList nList = nNode.getChildNodes(); 

                for (int temp = 0; temp < nList.getLength(); temp++) {
                      nNode = nList.item(temp);
                      
                      iCardMin           = -2;
                      iCardMax           = -1;         
                      
                                           
                      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                           //NODOS
                           if  (  nNode.getNodeName().equals("TEXT") || nNode.getNodeName().equals("DATE") || 
                                  nNode.getNodeName().equals("CODE") || nNode.getNodeName().equals("NUM")  || 
                                  nNode.getNodeName().equals("CONTAINER") ){

                                  if (nNode.getNodeName().equals("TEXT"))           node = new TEXT();
                                  else if (nNode.getNodeName().equals("DATE"))      node = new DATE();
                                  else if (nNode.getNodeName().equals("CODE"))      node = new CODE();
                                  else if (nNode.getNodeName().equals("NUM"))       node = new NUM();
                                  else if (nNode.getNodeName().equals("CONTAINER")) node = new CONTAINER();
                                                              
                                  if (this.bIsDebug) System.out.println("TYPE :" + nNode.getNodeName());                             
                                  
                                  /***************************************************************/
                                  /*************** CONCEPT NAME **********************************/
                                  
                                  Node node_ch = Recupera_SUBTAG("CONCEPT_NAME","CODE_SCHEMA",nNode);
                                  if (node_ch == null) return -1;
                                  if (this.bIsDebug) System.out.println("   CODE SCHEMA :" + node_ch.getTextContent());
                                  strCNcodeschema       = node_ch.getTextContent();            

                                  Node node_cv = Recupera_SUBTAG("CONCEPT_NAME","CODE_VALUE", nNode);
                                  if (node_cv == null) return -1;
                                  if (this.bIsDebug) System.out.println("   CODE VALUE :" + node_cv.getTextContent());
                                  strCNcodevalue        = node_cv.getTextContent();            
                                                                 
                                  Node node_cm = Recupera_SUBTAG("CONCEPT_NAME","CODE_MEANING", nNode);
                                  if (node_cm == null) return -1;
                                  if (this.bIsDebug) System.out.println("   CODE MEANING :" + node_cm.getTextContent());
                                  strCNcodemeaning      = node_cm.getTextContent();            
                                  
                                  
                                  node.setCodeSchema(strCNcodeschema);
                                  node.setCodeValue(strCNcodevalue);
                                  node.setCodeMeaning(strCNcodemeaning);

                                  

                                  if (this.bIsInsertDICOMSR) {
                                    if (strCNcodeschema_ant.equals("") && 
                                        strCNcodevalue_ant.equals("")  &&
                                         strCNcodemeaning_ant.equals("") ) {
                                        iIndex = 1;                                        
                                    }else{
                                        if (node.getCodeSchema().equals(strCNcodeschema_ant) &&
                                            node.getCodeValue().equals(strCNcodevalue_ant)   &&
                                            node.getCodeMeaning().equals(strCNcodemeaning_ant))    
                                            iIndex++;   
                                        else iIndex = 1;
                                    }
                                  }
                                  
                                  
                                  strCurrentPathBase = strPathbase + "/" + strCNcodeschema + "_" + strCNcodevalue;
                                                                                                                                                                                                             
                                 if ((this.bIsCreateOntology)||(this.bIsListDICOMSR)||(this.bIsRemoveDICOMSR)){
                                        /***************************************************************/
                                        /*************** GENERAL PROPERTIES   ***************************/
                                        Node node_card = Recupera_SUBTAG("PROPERTIES", "CARDINALITY", nNode);
                                        if (node_card == null) return -1;
                                        if (this.bIsDebug){
                                            System.out.println("   CARD MIN :" + node_card.getAttributes().getNamedItem("min").getNodeValue());
                                            System.out.println("   CARD MAX :" + node_card.getAttributes().getNamedItem("max").getNodeValue());
                                        }                                                                   
                                        iCardMin     = Integer.parseInt(node_card.getAttributes().getNamedItem("min").getNodeValue());
                                        iCardMax     = Integer.parseInt(node_card.getAttributes().getNamedItem("max").getNodeValue());  
                                        if (iCardMin == 0)  iCardMin =   1;
                                        if (iCardMax == -1) iCardMax =   30;                                                                                                     
 
                                        //ITERO CARPETAS EN AMGA DE LOS NODOS SEGUN CARDINALIDADES                                  
                                        for (int i=iCardMin; i<=iCardMax; i++){
                                            node.setPath(strCurrentPathBase + "_" + i);
                                            if (bIsCreateOntology){                                                
                                                parserHelper.createDirectory(node);
                                                parserHelper.addAtributes(node);
                                            }
                                            if (bIsListDICOMSR){                                                
                                                parserHelper.listEntry(node, this.IDTRENCADISReport);
                                            }
                                            if (bIsRemoveDICOMSR){
                                                parserHelper.removeEntry(node, this.IDTRENCADISReport);
                                            }                                      
                                      }                                              
                                  }
                                                                   
                                                                    
                                  if (this.bIsInsertDICOMSR){
                                        /***************************************************************/
                                        /*************** VALUE *****************************************/                                        
                                        Node nNodeValue = Recupera_TAG(nNode,"VALUE");
                                        if (nNodeValue == null) return -1;                                        
                                        if (nNode.getNodeName().equals("CONTAINER")){                                            
                                        }else if (nNode.getNodeName().equals("CODE")){                                            
                                            Node node_EntrySchema = Recupera_TAG(nNodeValue,"CODE_SCHEMA");                                            
                                            if (node_EntrySchema == null) return -1;
                                            ((CODE)node).setEntrySchema(node_EntrySchema.getTextContent());
                                            Node node_EntryValue = Recupera_TAG(nNodeValue,"CODE_VALUE");                                            
                                            if (node_EntryValue == null) return -1;
                                            ((CODE)node).setEntryValue(node_EntryValue.getTextContent());
                                            Node node_EntryMeaning = Recupera_TAG(nNodeValue,"CODE_MEANING");                                            
                                            if (node_EntryMeaning == null) return -1;
                                            ((CODE)node).setEntryMeaning(node_EntryMeaning.getTextContent());                                                                                        
                                        }else if (nNode.getNodeName().equals("TEXT")) {                                            
                                            ((TEXT)node).setTextValue(nNodeValue.getTextContent());                                            
                                        }else if (nNode.getNodeName().equals("DATE")){                                            
                                            ((DATE)node).setDateValue(nNodeValue.getTextContent());                                            
                                        }else if  (nNode.getNodeName().equals("NUM")){                                            
                                            ((NUM)node).setNumericValue(nNodeValue.getTextContent());
                                            
                                            Node nNodeUnits = Recupera_TAG(nNode,"UNIT_MEASUREMENT");
                                            if (nNodeUnits == null) return -1;
                                            
                                            Node node_UnitsSchema = Recupera_TAG(nNode,"CODE_SCHEMA");                                            
                                            if (node_UnitsSchema == null) return -1;                                            
                                            ((NUM)node).setUnitsSchema(node_UnitsSchema.getTextContent());
                                            
                                            Node node_UnitsValue = Recupera_TAG(nNode,"CODE_VALUE");                                            
                                            if (node_UnitsValue == null) return -1;                                            
                                            ((NUM)node).setUnitsValue(node_UnitsValue.getTextContent());
                                            
                                            Node node_UnitsMeaning = Recupera_TAG(nNode,"CODE_MEANING");                                            
                                            if (node_UnitsMeaning == null) return -1;                                            
                                            ((NUM)node).setUnitsMeaning(node_UnitsMeaning.getTextContent());
                                            
                                        }
                                        
                                        node.setPath(strCurrentPathBase + "_" + iIndex);
                                        
                                        if (strCurrentPathBase.matches("Hospital_Peset/ONTO_6/RADLEX_RID10326_1/RADLEX_RID29897_1/RADLEX_RID3874_1/RADLEX_RID5972_1")){
                                        	int tt = 0;
                                        }
                                        
                                        parserHelper.addEntry(node, IDTRENCADISReport);
                                      
                                  }
                                  
                                  
                                  //LOCALIZO CHILDREN si existe y lanzo lo recursivo tandas veces como cardinalidades
                                  Node nNodeChilds = Recupera_TAG(nNode,"CHILDREN");
                                  if (nNodeChilds == null) return -1;
                                  if (nNodeChilds.getNodeName().equals("CHILDREN")){
                                    if ((this.bIsCreateOntology)||(this.bIsListDICOMSR)||(this.bIsRemoveDICOMSR)){
                                       if (nNodeChilds.getNodeName().equals("CHILDREN")){
                                            for (int i=iCardMin; i<=iCardMax; i++){                                
                                                iErr = iParserRecursiveXMLStructureOntologyToAMGA(nNodeChilds, strCurrentPathBase + "_" + i);
                                                if (iErr != 0) return iErr;                                             
                                            }                                              
                                        }                                  
                                    }                                 
                                    if (this.bIsInsertDICOMSR){
                                        iErr = iParserRecursiveXMLStructureOntologyToAMGA(nNodeChilds, strCurrentPathBase + "_" + iIndex);
                                        if (iErr != 0) return iErr;  
                                        strCNcodeschema_ant  = node.getCodeSchema();
                                        strCNcodevalue_ant   = node.getCodeValue();
                                        strCNcodemeaning_ant = node.getCodeMeaning();
                                    }
                                  }
                                  
                            }
                        }
                }
                
                return 0;
            }catch(Exception ex){
                ex.printStackTrace();
                strError = ex.toString();
                return -1;
            }
    }
    
    private Node Recupera_TAG(Node nNode, String strTAG){  
        try{
            Node nNodeChild = null;        
            NodeList nList = nNode.getChildNodes();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                nNodeChild = nList.item(temp);            
                if (nNodeChild.getNodeName().equals(strTAG)){                    
                    return nNodeChild;         
                }
            }            
            return nNodeChild; //Si no lo encuentra devuelve el ultimo
            
        }catch(Exception ex){
        	ex.printStackTrace();
             strError = ex.toString();
             return null;
        }
    }
      
    //Recupera un nodo corrspondiente a un subtag dentro de un tag padre  
    private Node Recupera_SUBTAG(String strTAG, String strSubTAG, Node nNode){
         try{
            Node nNodecnn = null; 
            NodeList nList = nNode.getChildNodes();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNodecn = nList.item(temp);
                if (nNodecn.getNodeName().equals(strTAG)){
                    NodeList nList2 = nNodecn.getChildNodes();
                    for (int j = 0; j < nList2.getLength(); j++) {
                        nNodecnn = nList2.item(j);
                        if (nNodecnn.getNodeName().equals(strSubTAG)){
                            return   nNodecnn;  
                        }  
                    }
                }
            }
            strError = "SUBTAG " + strSubTAG + " inside of TAG " + strTAG + " not found.";
            return null;
         }catch(Exception ex){
        	 ex.printStackTrace();
             strError = ex.toString();
             return null;
         }
    }

   
}
