package trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputUpdateStorageServices;/*******************************************************************  *********** AUTOMATICALLY GENERATED CLASS BY XmlWrappers  **********  ********-----German Molto  (gmolto@dsic.upv.es) -----*******  ********************************************************************/ import org.w3c.dom.*;/** * Contains the methods that allow to obtain and change the information of * the elements <STRONG>OUTPUT </STRONG> from the XML document. */public class OUTPUT extends Wrapper{/** * Creates a Wrapper that works starting from the element OUTPUT * @param e The elemenent the Wrapper works with*/public OUTPUT(Element e){ super(e);}/** * Obtains the value of element STATUS * @return The value of element STATUS */public String get_STATUS(){ try { String unparsedValue = getValueByTagName("STATUS"); String parsedValue = unparsedValue; return parsedValue; }catch (Exception e) { e.printStackTrace(); return null;}}/** * Sets the value to element STATUS * If the element does not exist it is automatically created * @param value The new value of element STATUS * @throws IllegalArgumentException If the value violates *         any scheme restriction */public void set_STATUS(String value){ String unparsedValue = value; try{ setValueByTagName("STATUS", unparsedValue); } catch(Exception e) { //Si no existe el elemento lo creamos   Element el = doc.createElement("STATUS");   el.appendChild(doc.createTextNode(unparsedValue));   elem.appendChild(el); }}/** * Obtains the value of element DESCRIPTION * @return The value of element DESCRIPTION */public String get_DESCRIPTION(){ try { String unparsedValue = getValueByTagName("DESCRIPTION"); String parsedValue = unparsedValue; return parsedValue; }catch (Exception e) { e.printStackTrace(); return null;}}/** * Sets the value to element DESCRIPTION * If the element does not exist it is automatically created * @param value The new value of element DESCRIPTION * @throws IllegalArgumentException If the value violates *         any scheme restriction */public void set_DESCRIPTION(String value){ String unparsedValue = value; try{ setValueByTagName("DESCRIPTION", unparsedValue); } catch(Exception e) { //Si no existe el elemento lo creamos   Element el = doc.createElement("DESCRIPTION");   el.appendChild(doc.createTextNode(unparsedValue));   elem.appendChild(el); }}}