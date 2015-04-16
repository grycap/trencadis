package trencadis.infrastructure.services.KeyServer.impl.wrapper.config;

/******************************************************************* 
 *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER ********** 
 ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----******* 
 ********************************************************************/ 


import org.w3c.dom.*;

/**
 * Contiene los m�todos que permiten obtener y cambiar la informaci�n
 * de los elementos <STRONG>CONFIGURATION </STRONG> del documento XML.
 */
public class CONFIGURATION extends Wrapper{

/**
 * Crea un Wrapper que trabajar� a partir de un elemento CONFIGURATION
 * @param e El elemento sobre el cual trabaja el Wrapper
*/
public CONFIGURATION(Element e){
 super(e);
}

/**
 * Obtiene un Wrapper a partir del elemento SERVICE_PARAMETERS
 * @param value El Wrapper obtenido 
*/
 public SERVICE_PARAMETERS get_SERVICE_PARAMETERS(){
  Element el = getElementByTagName("SERVICE_PARAMETERS"); 
  return new SERVICE_PARAMETERS(el);
 }
  
/**
 * Obtiene un Wrapper a partir del elemento GATEKEEPER_PARAMETERS
 * @param value El Wrapper obtenido 
*/
 public GATEKEEPER_PARAMETERS get_GATEKEEPER_PARAMETERS(){
  Element el = getElementByTagName("GATEKEEPER_PARAMETERS"); 
  return new GATEKEEPER_PARAMETERS(el);
 }
  
/**
 * Obtiene un Wrapper a partir del elemento BACKEND_PARAMETERS
 * @param value El Wrapper obtenido 
*/
 public BACKEND_PARAMETERS get_BACKEND_PARAMETERS(){
  Element el = getElementByTagName("BACKEND_PARAMETERS"); 
  return new BACKEND_PARAMETERS(el);
 }
  
/**
 * Obtiene un Wrapper a partir del elemento LOGGER_PARAMETERS
 * @param value El Wrapper obtenido 
*/
 public LOGGER_PARAMETERS get_LOGGER_PARAMETERS(){
  Element el = getElementByTagName("LOGGER_PARAMETERS"); 
  return new LOGGER_PARAMETERS(el);
 }
  
/**
 * Obtiene un Wrapper a partir del elemento INDEX_SERVICE_PARAMETERS
 * @param value El Wrapper obtenido 
*/
 public INDEX_SERVICE_PARAMETERS get_INDEX_SERVICE_PARAMETERS(){
  Element el = getElementByTagName("INDEX_SERVICE_PARAMETERS"); 
  return new INDEX_SERVICE_PARAMETERS(el);
 }
  
}