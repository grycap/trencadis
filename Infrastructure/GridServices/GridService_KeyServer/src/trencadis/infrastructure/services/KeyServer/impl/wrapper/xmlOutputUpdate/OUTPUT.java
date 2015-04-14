package trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlOutputUpdate;

/******************************************************************* 
 *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER ********** 
 ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----******* 
 ********************************************************************/ 


import org.w3c.dom.*;

/**
 * Contiene los m�todos que permiten obtener y cambiar la informaci�n
 * de los elementos <STRONG>OUTPUT </STRONG> del documento XML.
 */
public class OUTPUT extends Wrapper{

/**
 * Crea un Wrapper que trabajar� a partir de un elemento OUTPUT
 * @param e El elemento sobre el cual trabaja el Wrapper
*/
public OUTPUT(Element e){
 super(e);
}

/**
 * Obtiene el valor del elemento STATUS
 * @return El valor del elemento STATUS
 */
public String get_STATUS(){
 try {
 String unparsedValue = getValueByTagName("STATUS");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento STATUS
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento STATUS
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_STATUS(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("STATUS", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("STATUS");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento DESCRIPTION
 * @return El valor del elemento DESCRIPTION
 */
public String get_DESCRIPTION(){
 try {
 String unparsedValue = getValueByTagName("DESCRIPTION");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento DESCRIPTION
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento DESCRIPTION
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_DESCRIPTION(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("DESCRIPTION", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("DESCRIPTION");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

}