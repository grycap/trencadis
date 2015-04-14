package trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config;/*******************************************************************  *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER **********  ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----*******  ********************************************************************/ import org.w3c.dom.*;/** * Contiene los m�todos que permiten obtener y cambiar la informaci�n * de los elementos <STRONG>FILE_SYSTEM_PARAMETERS </STRONG> del documento XML. */public class FILE_SYSTEM_PARAMETERS extends Wrapper{/** * Crea un Wrapper que trabajar� a partir de un elemento FILE_SYSTEM_PARAMETERS * @param e El elemento sobre el cual trabaja el Wrapper*/public FILE_SYSTEM_PARAMETERS(Element e){ super(e);}/** * Obtiene el valor del elemento HOME_DIR * @return El valor del elemento HOME_DIR */public String get_HOME_DIR(){ try { String unparsedValue = getValueByTagName("HOME_DIR"); String parsedValue = unparsedValue; return parsedValue; }catch (Exception e) { e.printStackTrace(); return null;}}/** * Asocia un valor al elemento HOME_DIR * Si el elemento no existe, lo crea previamente * @param value El valor a establecer al elemento HOME_DIR * @throws IllegalArgumentException Si el valor incumple alguna *         de las restricciones del schema */public void set_HOME_DIR(String value){ String unparsedValue = value; try{ setValueByTagName("HOME_DIR", unparsedValue); } catch(Exception e) { //Si no existe el elemento lo creamos   Element el = doc.createElement("HOME_DIR");   el.appendChild(doc.createTextNode(unparsedValue));   elem.appendChild(el); }}}