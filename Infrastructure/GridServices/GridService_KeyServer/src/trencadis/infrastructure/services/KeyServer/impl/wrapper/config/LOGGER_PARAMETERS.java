package trencadis.infrastructure.services.KeyServer.impl.wrapper.config;

/******************************************************************* 
 *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER ********** 
 ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----******* 
 ********************************************************************/ 


import org.w3c.dom.*;

/**
 * Contiene los m�todos que permiten obtener y cambiar la informaci�n
 * de los elementos <STRONG>LOGGER_PARAMETERS </STRONG> del documento XML.
 */
public class LOGGER_PARAMETERS extends Wrapper{

/**
 * Crea un Wrapper que trabajar� a partir de un elemento LOGGER_PARAMETERS
 * @param e El elemento sobre el cual trabaja el Wrapper
*/
public LOGGER_PARAMETERS(Element e){
 super(e);
}

/**
 * Obtiene el valor del elemento LOG_PATH
 * @return El valor del elemento LOG_PATH
 */
public String get_LOG_PATH(){
 try {
 String unparsedValue = getValueByTagName("LOG_PATH");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento LOG_PATH
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento LOG_PATH
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_LOG_PATH(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("LOG_PATH", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("LOG_PATH");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

}