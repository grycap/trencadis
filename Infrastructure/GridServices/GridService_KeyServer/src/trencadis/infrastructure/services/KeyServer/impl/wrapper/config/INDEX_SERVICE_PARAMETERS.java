package trencadis.infrastructure.services.KeyServer.impl.wrapper.config;

/******************************************************************* 
 *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER ********** 
 ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----******* 
 ********************************************************************/ 


import org.w3c.dom.*;

/**
 * Contiene los m�todos que permiten obtener y cambiar la informaci�n
 * de los elementos <STRONG>INDEX_SERVICE_PARAMETERS </STRONG> del documento XML.
 */
public class INDEX_SERVICE_PARAMETERS extends Wrapper{

/**
 * Crea un Wrapper que trabajar� a partir de un elemento INDEX_SERVICE_PARAMETERS
 * @param e El elemento sobre el cual trabaja el Wrapper
*/
public INDEX_SERVICE_PARAMETERS(Element e){
 super(e);
}

/**
 * Obtiene el valor del elemento REGISTRATION_PATH
 * @return El valor del elemento REGISTRATION_PATH
 */
public String get_REGISTRATION_PATH(){
 try {
 String unparsedValue = getValueByTagName("REGISTRATION_PATH");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento REGISTRATION_PATH
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento REGISTRATION_PATH
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_REGISTRATION_PATH(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("REGISTRATION_PATH", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("REGISTRATION_PATH");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

}