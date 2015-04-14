package trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputMetadataQuery;/*******************************************************************  *********** AUTOMATICALLY GENERATED CLASS BY XmlWrappers  **********  ********-----German Molto  (gmolto@dsic.upv.es) -----*******  ********************************************************************/import org.w3c.dom.*;/** * Contains the methods that allow to obtain and change the information of the * elements <STRONG>OUTPUT </STRONG> from the XML document. */public class OUTPUT extends Wrapper {	/**	 * Creates a Wrapper that works starting from the element OUTPUT	 * 	 * @param e	 *            The elemenent the Wrapper works with	 */	public OUTPUT(Element e) {		super(e);	}	/**	 * Obtains the value of element URI	 * 	 * @return The value of element URI	 */	public String get_URI() {		try {			String unparsedValue = getValueByTagName("URI");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Sets the value to element URI If the element does not exist it is	 * automatically created	 * 	 * @param value	 *            The new value of element URI	 * @throws IllegalArgumentException	 *             If the value violates any scheme restriction	 */	public void set_URI(String value) {		String unparsedValue = value;		try {			setValueByTagName("URI", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("URI");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Obtains the value of element DSR_TYPE	 * 	 * @return The value of element DSR_TYPE	 */	public String get_DSR_TYPE() {		try {			String unparsedValue = getValueByTagName("DSR_TYPE");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Sets the value to element DSR_TYPE If the element does not exist it is	 * automatically created	 * 	 * @param value	 *            The new value of element DSR_TYPE	 * @throws IllegalArgumentException	 *             If the value violates any scheme restriction	 */	public void set_DSR_TYPE(String value) {		String unparsedValue = value;		try {			setValueByTagName("DSR_TYPE", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("DSR_TYPE");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Obtains the value of element HOSPITAL	 * 	 * @return The value of element HOSPITAL	 */	public String get_HOSPITAL() {		try {			String unparsedValue = getValueByTagName("HOSPITAL");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Sets the value to element HOSPITAL If the element does not exist it is	 * automatically created	 * 	 * @param value	 *            The new value of element HOSPITAL	 * @throws IllegalArgumentException	 *             If the value violates any scheme restriction	 */	public void set_HOSPITAL(String value) {		String unparsedValue = value;		try {			setValueByTagName("HOSPITAL", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("HOSPITAL");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Obtains the value of element STATUS	 * 	 * @return The value of element STATUS	 */	public String get_STATUS() {		try {			String unparsedValue = getValueByTagName("STATUS");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Sets the value to element STATUS If the element does not exist it is	 * automatically created	 * 	 * @param value	 *            The new value of element STATUS	 * @throws IllegalArgumentException	 *             If the value violates any scheme restriction	 */	public void set_STATUS(String value) {		String unparsedValue = value;		try {			setValueByTagName("STATUS", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("STATUS");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Obtains the value of element DESCRIPTION	 * 	 * @return The value of element DESCRIPTION	 */	public String get_DESCRIPTION() {		try {			String unparsedValue = getValueByTagName("DESCRIPTION");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Sets the value to element DESCRIPTION If the element does not exist it is	 * automatically created	 * 	 * @param value	 *            The new value of element DESCRIPTION	 * @throws IllegalArgumentException	 *             If the value violates any scheme restriction	 */	public void set_DESCRIPTION(String value) {		String unparsedValue = value;		try {			setValueByTagName("DESCRIPTION", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("DESCRIPTION");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Enables to iterate through all the elements ID	 * 	 * @return A java.util.iterator to ease the iteration <BR>	 *         Usage example: <BR>	 *         Iterator it = getAll_ID(); <BR>	 *         while (it.hasNext()){ <BR>	 *         <UL>	 *         ID obj = (ID) it.next(); <BR>	 *         }	 */	public java.util.Iterator getAll_ID() {		java.util.Vector v = getChildsByTagName("ID");		int length = v.size();		for (int i = 0; i < length; i++) {			v.setElementAt(new ID((Element) v.elementAt(i)), i);		}		return v.iterator();	}	/**	 * Adds a new element ID. The content of the new element is void and must be	 * stablished through the methods of the obtained wrapper	 * 	 * @return A wrapper to the newly created element	 */	public ID add_ID() {		Element newID = doc.createElement("ID");		elem.appendChild(newID);		return new ID(newID);	}}