package trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputRetrieve;

import org.w3c.dom.*;
import java.io.*;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

public class XmlWrapper extends Wrapper {
 private String fileName;
 Document doc = null;
 private boolean inValidationMode;

/**
 * Creates a Wrapper for the XML document. It will be an entry point to the document, 
 * as acess will be provided from here to the root element of the XML document
 * @param filename The XML file name 
 */
 public XmlWrapper(File file, boolean inValidationMode){
  this.fileName = file.getAbsolutePath(); 
  this.inValidationMode = inValidationMode; 
 }
 
/**
 * Creates a Wrapper for the XML document. It will be an entry point to the document, 
 * as acess will be provided from here to the root element of the XML document
 * @param filename The XML file name 
 */
 public XmlWrapper(String xmlDocument, boolean inValidationMode){
  try{
   File f = File.createTempFile("XmlWrapper",".xml");
   this.fileName = f.getAbsolutePath();
   FileOutputStream os = new FileOutputStream(f);
   os.write(xmlDocument.getBytes());
   os.close();
   }catch(IOException ex){
    System.err.println("Unable to create file: " + fileName);
   }
   this.inValidationMode = inValidationMode;
 }
/**
 * Starts the XML document parsing in validation mode (checks 
 * that the XML document satisfies the Schema. If required, shows the errors
 * Besides, it obtains the root element of the document.
 * @param filename The XML file name.                              
 * @return false if an error occurs during parsing.      
 */
 public boolean wrap(){
  return wrap(this.fileName, this.inValidationMode); 
 }

/**
 * Parses the XML document in validation mode (checks that 
 * the XML document satisfies the Schema. If required, shows the errors
 * Besides, it obtains the root element of the document.
 * @param filename The XML file name.                              
 * @inValidationMode A boolean to indicate if the document should be read in validation mode.
 * @return false if an error occurs during parsing.      
 */
 private boolean wrap(String filename, boolean inValidationMode){
  try {
   org.apache.xerces.parsers.DOMParser parser = new org.apache.xerces.parsers.DOMParser();
   if (inValidationMode) {
   parser.setFeature("http://xml.org/sax/features/validation", true);
   parser.setFeature("http://apache.org/xml/features/validation/schema", true);
   parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "ontologiesServerConfig.xsd");
   }
   parser.setErrorHandler(new trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputRetrieve.CustomErrorHandler());
   parser.parse(filename);
   doc = parser.getDocument();
  }catch(Exception sxe){
   return false;
  }
 elem = doc.getDocumentElement(); 
 return true;
}

/**
 * Obtains the current XML document (perhaps after the modifications
 * you made) in the form of a String
 * @return The current XML document as a String  
 */
public String to_XMLString(){
 String xmlCode=null;
 File f = null;
 try{
  f = File.createTempFile("XmlWrapper",".xml");
  to_XML(f.getAbsolutePath());
  xmlCode = fileToString(f.getAbsolutePath());
 }catch(IOException ex){
  System.err.println(ex.getMessage());
  return null;
 }
 return xmlCode;
}

/**
 * Obtains a String representation of a text file
 * @return The text file as a String  
 */
private String fileToString(String textFilePath) throws IOException{
 FileInputStream fis = new FileInputStream(textFilePath);
 byte[] b = new byte[fis.available()];
 fis.read(b);
 fis.close();
 return new String(b);
}

/**
 * Stores the changes in the same XML document
 */
public void to_XML(){
  to_XML(fileName);
}

/**
 * Stores the changes in an XML document
 * @param filename The name of the file
 */
 public void to_XML(String filename){
 try{
  FileOutputStream out = new FileOutputStream(filename); 
  org.apache.xml.serialize.OutputFormat format = new org.apache.xml.serialize.OutputFormat(doc, "UTF-8", true); 
  org.apache.xml.serialize.XMLSerializer s = new org.apache.xml.serialize.XMLSerializer(out,format);
  s.serialize(doc);
 }catch(Exception e) {e.printStackTrace();} 
}

/**
 * Verifies that all the changes produces to DOM satisfy the Schema
 * @return True If the current DOM is coherent with the schema.
 *         False if any modification violates the schema.
 *         A description of the error will be shown.
 */
public boolean validate(){
  to_XML(fileName + ".tmp.xml");
  if (!wrap(fileName + ".tmp.xml", true)) return false;  else return true;
}

 /************* M�todos de entrada al documento XML *************/


/**
 * Obtiene un Wrapper a partir del elemento INPUT
 * @param value El Wrapper obtenido 
*/
 public INPUT get_INPUT(){
   return new INPUT(elem);
}

}/**
 * Clase que proporciona un comportamiento personalizado frente
 * a los errores al analizar el documento XML.
 */
class CustomErrorHandler implements org.xml.sax.ErrorHandler{
 public void warning(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
   System.out.println(">> Warning << " +
   "  Linea: " +   exception.getLineNumber() + " " + 
   "  Archivo:     " +   exception.getSystemId() + " " +
   "  Mensaje: " +   exception.getMessage());
 throw new org.xml.sax.SAXException("Warning  ");
}
 public void error(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
   System.out.println(">> Error de Sintaxis << " +
   "  Linea: " +   exception.getLineNumber() + " " + 
   "  Archivo:     " +   exception.getSystemId() + " " +
   "  Mensaje: " +   exception.getMessage());
 throw new org.xml.sax.SAXException("Error de Sintaxis");
}
 public void fatalError(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
   System.out.println(">> Error Fatal << " +
   "  Linea: " +   exception.getLineNumber() + " " + 
   "  Archivo:     " +   exception.getSystemId() + " " +
   "  Mensaje: " +   exception.getMessage());
 throw new org.xml.sax.SAXException("Error fatal");
 }
}


