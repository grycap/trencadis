package trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputSign;

/******************************************************************* 
 *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER ********** 
 ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----******* 
 ********************************************************************/ 


import org.w3c.dom.*;

/**
 * Contiene los m�todos que permiten obtener y cambiar la informaci�n
 * de los elementos <STRONG>INPUT </STRONG> del documento XML.
 */
public class INPUT extends Wrapper{

/**
 * Crea un Wrapper que trabajar� a partir de un elemento INPUT
 * @param e El elemento sobre el cual trabaja el Wrapper
*/
public INPUT(Element e){
 super(e);
}

/**
 * Obtiene el valor del elemento CERTIFICATE
 * @return El valor del elemento CERTIFICATE
 */
public String get_CERTIFICATE(){
 try {
 String unparsedValue = getValueByTagName("CERTIFICATE");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento CERTIFICATE
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento CERTIFICATE
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_CERTIFICATE(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("CERTIFICATE", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("CERTIFICATE");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento EOUID
 * @return El valor del elemento EOUID
 */
public String get_EOUID(){
 try {
 String unparsedValue = getValueByTagName("EOUID");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento EOUID
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento EOUID
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_EOUID(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("EOUID", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("EOUID");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene un Wrapper a partir del elemento ONTOLOGIES
 * @param value El Wrapper obtenido 
*/
 public ONTOLOGIES get_ONTOLOGIES(){
  Element el = getElementByTagName("ONTOLOGIES"); 
  return new ONTOLOGIES(el);
 }
  
}