package trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlOutputRetrieve;

/******************************************************************* 
 *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER ********** 
 ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----******* 
 ********************************************************************/ 


import org.w3c.dom.*;
import java.io.*;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

public class Wrapper {
/**
 * Elemento a partir del cual todo wrapper trabaja
*/ 
 protected org.w3c.dom.Element elem;
 protected org.w3c.dom.Document doc;

/**
 * Construye un Wrapper que trabajar� a partir de un elemento
 * @param e El org.w3c.dom.Element base para el Wrapper
 */
 public Wrapper(org.w3c.dom.Element e){ 
  elem=e;
  doc=e.getOwnerDocument();
 }

 public Wrapper(){}

/**
 * Devuelve el elemento que tiene una determinada etiqueta y que es hijo inmediato del elemento 
 * a partir del cual trabaja cada Wrapper.
 * @param name El nombre del elemento buscado
 * @return El elemento obtenido
 */
protected org.w3c.dom.Element getElementByTagName(String name){
try{
 NodeList nl = elem.getChildNodes();
 for ( int i = 0; i < nl.getLength() ; i++ ){
  Node n = nl.item(i);
  if ((n.getNodeType() == Node.ELEMENT_NODE) && n.getNodeName().equalsIgnoreCase(name))
    return (Element) n ;
 }//for
return null;
}catch(Exception e) {e.printStackTrace(); return null;}
}

/**
 * Obtiene un conjunto de elementos que son hijos directos del elemento 
 * a partir del cual trabaja cada Wrapper y con un nombre determinado. 
 * @param name El nombre de los elementos buscados 
 * @return Un java.util.Vector con los elementos encontrados 
 */
protected java.util.Vector getChildsByTagName(String name){
 return getChildsByTagName( elem , name );
}

/**
 * Obtiene un conjunto de elementos con un nombre determinado y que 
 * son hijos directos de un nodo especificado
 * @param node El nodo, los hijos del cual queremos encontrar
 * @param name El nombre de los elementos que estamos buscando
 * @return Un java.util.Vector con los elementos encontrados
 */
protected java.util.Vector getChildsByTagName(org.w3c.dom.Node node, String name){
 java.util.Vector v = new java.util.Vector();
 try{
 NodeList nl = node.getChildNodes();
 int length = nl.getLength();
 for (int i = 0; i < length ; i++){
  Node n = nl.item(i);
  if ( (n.getNodeType() == Node.ELEMENT_NODE) && n.getNodeName().equalsIgnoreCase(name))
     v.addElement(n);
}//for
return v;
}catch(Exception e) {e.printStackTrace(); return null;}
}

/**
 * Obtiene el valor de un elemento, hijo del elemento a partir del cual 
 * trabaja el Wrapper
 * @param name El nombre del elemento 
 * @return El valor del nodo texto del elemento 
 */
protected String getValueByTagName(String name){ 
 Element el = getElementByTagName(name); 
 return el.getFirstChild().getNodeValue();
}

/**
 * Establece el valor a un elemento, hijo del elemento a 
 * partir del cual trabaja el Wrapper
 * @param name El nombre del elemento
 * @param value El nuevo valor del elemento
 */
protected void setValueByTagName(String name, String value){
 Element el = getElementByTagName(name); 
 el.getFirstChild().setNodeValue(value);
 }

public String str_to_XML(){
 try{
  StringWriter out = new StringWriter();
  OutputFormat format = new OutputFormat(doc,"UTF-8",true);
  XMLSerializer s = new XMLSerializer(out,format);
  s.serialize(elem);
  return out.getBuffer().toString();
 }catch(Exception ex){
  ex.printStackTrace(); return null;
 }
}
}
