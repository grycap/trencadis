package trencadis.infrastructure.services.KeyServer.impl.wrapper.config;

/******************************************************************* 
 *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER ********** 
 ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----******* 
 ********************************************************************/ 


import org.w3c.dom.*;

/**
 * Contiene los m�todos que permiten obtener y cambiar la informaci�n
 * de los elementos <STRONG>BACKEND_PARAMETERS </STRONG> del documento XML.
 */
public class BACKEND_PARAMETERS extends Wrapper{

/**
 * Crea un Wrapper que trabajar� a partir de un elemento BACKEND_PARAMETERS
 * @param e El elemento sobre el cual trabaja el Wrapper
*/
public BACKEND_PARAMETERS(Element e){
 super(e);
}

/**
 * Obtiene el valor del elemento HOST_NAME
 * @return El valor del elemento HOST_NAME
 */
public String get_HOST_NAME(){
 try {
 String unparsedValue = getValueByTagName("HOST_NAME");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento HOST_NAME
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento HOST_NAME
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_HOST_NAME(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("HOST_NAME", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("HOST_NAME");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento HOST_PORT
 * @return El valor del elemento HOST_PORT
 */
public String get_HOST_PORT(){
 try {
 String unparsedValue = getValueByTagName("HOST_PORT");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento HOST_PORT
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento HOST_PORT
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_HOST_PORT(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("HOST_PORT", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("HOST_PORT");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento DB_NAME
 * @return El valor del elemento DB_NAME
 */
public String get_DB_NAME(){
 try {
 String unparsedValue = getValueByTagName("DB_NAME");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento DB_NAME
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento DB_NAME
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_DB_NAME(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("DB_NAME", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("DB_NAME");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento USER
 * @return El valor del elemento USER
 */
public String get_USER(){
 try {
 String unparsedValue = getValueByTagName("USER");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento USER
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento USER
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_USER(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("USER", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("USER");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento PASSWORD
 * @return El valor del elemento PASSWORD
 */
public String get_PASSWORD(){
 try {
 String unparsedValue = getValueByTagName("PASSWORD");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento PASSWORD
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento PASSWORD
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_PASSWORD(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("PASSWORD", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("PASSWORD");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

}