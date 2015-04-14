package trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlOutputRetrieve;

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
 * Obtiene el valor del elemento MAC
 * @return El valor del elemento MAC
 */
public String get_MAC(){
 try {
 String unparsedValue = getValueByTagName("MAC");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento MAC
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento MAC
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_MAC(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("MAC", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("MAC");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento PART_KEY_SHARE
 * @return El valor del elemento PART_KEY_SHARE
 */
public String get_PART_KEY_SHARE(){
 try {
 String unparsedValue = getValueByTagName("PART_KEY_SHARE");
 String parsedValue = unparsedValue;
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Asocia un valor al elemento PART_KEY_SHARE
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento PART_KEY_SHARE
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_PART_KEY_SHARE(String value){
 String unparsedValue = value;
 try{
 setValueByTagName("PART_KEY_SHARE", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("PART_KEY_SHARE");
   el.appendChild(doc.createTextNode(unparsedValue));
   elem.appendChild(el);
 }
}

/**
 * Obtiene el valor del elemento ID_PART_KEY_SHARE
 * @return El valor del elemento ID_PART_KEY_SHARE
 */
public int get_ID_PART_KEY_SHARE(){
 try {
 String unparsedValue = getValueByTagName("ID_PART_KEY_SHARE");
 int parsedValue = Integer.parseInt(unparsedValue);
 return parsedValue;
 }catch (Exception e) { e.printStackTrace(); return -999;}
}

/**
 * Asocia un valor al elemento ID_PART_KEY_SHARE
 * Si el elemento no existe, lo crea previamente
 * @param value El valor a establecer al elemento ID_PART_KEY_SHARE
 * @throws IllegalArgumentException Si el valor incumple alguna
 *         de las restricciones del schema
 */
public void set_ID_PART_KEY_SHARE(int value){
 String unparsedValue = String.valueOf(value);
 try{
 setValueByTagName("ID_PART_KEY_SHARE", unparsedValue);
 } catch(Exception e) { //Si no existe el elemento lo creamos
   Element el = doc.createElement("ID_PART_KEY_SHARE");
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