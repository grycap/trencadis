package trencadis.infrastructure.services.KeyServer.impl.wrapper.xmlInputStore;

/******************************************************************* 
 *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER ********** 
 ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----******* 
 ********************************************************************/ 


import org.w3c.dom.*;

/**
 * Contiene los m�todos que permiten obtener y cambiar la informaci�n
 * de los elementos <STRONG>IDONTOLOGY </STRONG> del documento XML.
 */
public class IDONTOLOGY extends Wrapper{

/**
 * Crea un Wrapper que trabajar� a partir de un elemento IDONTOLOGY
 * @param e El elemento sobre el cual trabaja el Wrapper
*/
public IDONTOLOGY(Element e){
 super(e);
}

/**
 * Obtiene el valor del elemento
 * @return El valor del elemento
 */
public String getValue(){
 try {
 String unparsedValue = elem.getFirstChild().getNodeValue();
 String parsedValue = unparsedValue;
 return parsedValue.trim();
 }catch (Exception e) { e.printStackTrace(); return null;}
}

/**
 * Establece el valor del elemento
 * @param El nuevo valor del elemento
 * @throws java.lang.IllegalArgumentException. Si el valor 
 *         incumple alguna de las restricciones del Schema
 */
public void setValue(String value){
 String unparsedValue = value;
 try{
 elem.getFirstChild().setNodeValue(unparsedValue);
 } catch(Exception e) { e.printStackTrace();} 
}

}