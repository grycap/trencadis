package trencadis.infrastructure.services.KeyServer.impl.wrapper.config;

/******************************************************************* 
 *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER ********** 
 ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----******* 
 ********************************************************************/ 


import org.w3c.dom.*;

/**
 * Contiene los m�todos que permiten obtener y cambiar la informaci�n
 * de los elementos <STRONG>SERVICE_PARAMETERS </STRONG> del documento XML.
 */
public class SERVICE_PARAMETERS extends Wrapper{

/**
 * Crea un Wrapper que trabajar� a partir de un elemento SERVICE_PARAMETERS
 * @param e El elemento sobre el cual trabaja el Wrapper
*/
public SERVICE_PARAMETERS(Element e){
 super(e);
}

/**
 * Obtiene el valor del elemento IDENTITY
 * @return El valor del elemento IDENTITY
 */
public String get_IDENTITY(){
 try {
 String unparsedValue = getValueByTagName("IDENTITY");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento IDENTITY
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento IDENTITY
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_IDENTITY(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("IDENTITY", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("IDENTITY");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento DOMAIN
 * @return El valor del elemento DOMAIN
 */
public String get_DOMAIN(){
 try {
 String unparsedValue = getValueByTagName("DOMAIN");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento DOMAIN
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento DOMAIN
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_DOMAIN(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("DOMAIN", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("DOMAIN");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento BASE_PATH
 * @return El valor del elemento BASE_PATH
 */
public String get_BASE_PATH(){
 try {
 String unparsedValue = getValueByTagName("BASE_PATH");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento BASE_PATH
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento BASE_PATH
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_BASE_PATH(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("BASE_PATH", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("BASE_PATH");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento PATH_CATRUSTCERT
 * @return El valor del elemento PATH_CATRUSTCERT
 */
public String get_PATH_CATRUSTCERT(){
 try {
 String unparsedValue = getValueByTagName("PATH_CATRUSTCERT");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento PATH_CATRUSTCERT
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento PATH_CATRUSTCERT
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_PATH_CATRUSTCERT(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("PATH_CATRUSTCERT", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("PATH_CATRUSTCERT");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento TMP_DIR
 * @return El valor del elemento TMP_DIR
 */
public String get_TMP_DIR(){
 try {
 String unparsedValue = getValueByTagName("TMP_DIR");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento TMP_DIR
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento TMP_DIR
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_TMP_DIR(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("TMP_DIR", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("TMP_DIR");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

}