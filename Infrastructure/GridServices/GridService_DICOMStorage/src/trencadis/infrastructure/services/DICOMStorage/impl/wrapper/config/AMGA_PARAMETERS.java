package trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config;/*******************************************************************  *********** CLASE GENERADA AUTOM�TICAMENTE POR XMLWRAPPER **********  ********-----Germ�n Molt� Mart�nez (gmolto@dsic.upv.es) -----*******  ********************************************************************/ import org.w3c.dom.*;/** * Contiene los m�todos que permiten obtener y cambiar la informaci�n * de los elementos <STRONG>AMGA_PARAMETERS </STRONG> del documento XML. */public class AMGA_PARAMETERS extends Wrapper{/** * Crea un Wrapper que trabajar� a partir de un elemento AMGA_PARAMETERS * @param e El elemento sobre el cual trabaja el Wrapper*/public AMGA_PARAMETERS(Element e){ super(e);}/** * Obtiene el valor del elemento HOME_DIR * @return El valor del elemento HOME_DIR */public String get_HOME_DIR(){ try { String unparsedValue = getValueByTagName("HOME_DIR"); String parsedValue = unparsedValue; return parsedValue; }catch (Exception e) { e.printStackTrace(); return null;}}/** * Asocia un valor al elemento HOME_DIR * Si el elemento no existe, lo crea previamente * @param value El valor a establecer al elemento HOME_DIR * @throws IllegalArgumentException Si el valor incumple alguna *         de las restricciones del schema */public void set_HOME_DIR(String value){ String unparsedValue = value; try{ setValueByTagName("HOME_DIR", unparsedValue); } catch(Exception e) { //Si no existe el elemento lo creamos   Element el = doc.createElement("HOME_DIR");   el.appendChild(doc.createTextNode(unparsedValue));   elem.appendChild(el); }}/** * Obtiene el valor del elemento HOST_NAME * @return El valor del elemento HOST_NAME */public String get_HOST_NAME(){ try { String unparsedValue = getValueByTagName("HOST_NAME"); String parsedValue = unparsedValue; return parsedValue; }catch (Exception e) { e.printStackTrace(); return null;}}/** * Asocia un valor al elemento HOST_NAME * Si el elemento no existe, lo crea previamente * @param value El valor a establecer al elemento HOST_NAME * @throws IllegalArgumentException Si el valor incumple alguna *         de las restricciones del schema */public void set_HOST_NAME(String value){ String unparsedValue = value; try{ setValueByTagName("HOST_NAME", unparsedValue); } catch(Exception e) { //Si no existe el elemento lo creamos   Element el = doc.createElement("HOST_NAME");   el.appendChild(doc.createTextNode(unparsedValue));   elem.appendChild(el); }}/** * Obtiene el valor del elemento HOST_PORT * @return El valor del elemento HOST_PORT */public String get_HOST_PORT(){ try { String unparsedValue = getValueByTagName("HOST_PORT"); String parsedValue = unparsedValue; return parsedValue; }catch (Exception e) { e.printStackTrace(); return null;}}/** * Asocia un valor al elemento HOST_PORT * Si el elemento no existe, lo crea previamente * @param value El valor a establecer al elemento HOST_PORT * @throws IllegalArgumentException Si el valor incumple alguna *         de las restricciones del schema */public void set_HOST_PORT(String value){ String unparsedValue = value; try{ setValueByTagName("HOST_PORT", unparsedValue); } catch(Exception e) { //Si no existe el elemento lo creamos   Element el = doc.createElement("HOST_PORT");   el.appendChild(doc.createTextNode(unparsedValue));   elem.appendChild(el); }}/** * Obtiene el valor del elemento TMP_DIR * @return El valor del elemento TMP_DIR */public String get_TMP_DIR(){ try { String unparsedValue = getValueByTagName("TMP_DIR"); String parsedValue = unparsedValue; return parsedValue; }catch (Exception e) { e.printStackTrace(); return null;}}/** * Asocia un valor al elemento TMP_DIR * Si el elemento no existe, lo crea previamente * @param value El valor a establecer al elemento TMP_DIR * @throws IllegalArgumentException Si el valor incumple alguna *         de las restricciones del schema */public void set_TMP_DIR(String value){ String unparsedValue = value; try{ setValueByTagName("TMP_DIR", unparsedValue); } catch(Exception e) { //Si no existe el elemento lo creamos   Element el = doc.createElement("TMP_DIR");   el.appendChild(doc.createTextNode(unparsedValue));   elem.appendChild(el); }}}