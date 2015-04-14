package trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config;import org.w3c.dom.*;import java.io.*;import org.apache.xerces.parsers.DOMParser;import org.apache.xml.serialize.OutputFormat;import org.apache.xml.serialize.XMLSerializer;public class XmlWrapper extends Wrapper {	private String fileName;	Document doc = null;	private boolean inValidationMode;	/**	 * Creates a Wrapper for the XML document. It will be an entry point to the	 * document, as acess will be provided from here to the root element of the	 * XML document	 * 	 * @param filename	 *            The XML file name	 */	public XmlWrapper(File file, boolean inValidationMode) {		this.fileName = file.getAbsolutePath();		this.inValidationMode = inValidationMode;	}	/**	 * Creates a Wrapper for the XML document. It will be an entry point to the	 * document, as acess will be provided from here to the root element of the	 * XML document	 * 	 * @param filename	 *            The XML file name	 */	public XmlWrapper(String xmlDocument, boolean inValidationMode) {		try {			File f = File.createTempFile("XmlWrapper", ".xml");			this.fileName = f.getAbsolutePath();			FileOutputStream os = new FileOutputStream(f);			os.write(xmlDocument.getBytes());			os.close();		} catch (IOException ex) {			System.err.println("Unable to create file: " + fileName);		}		this.inValidationMode = inValidationMode;	}	/**	 * Starts the XML document parsing in validation mode (checks that the XML	 * document satisfies the Schema. If required, shows the errors Besides, it	 * obtains the root element of the document.	 * 	 * @param filename	 *            The XML file name.	 * @return false if an error occurs during parsing.	 */	public boolean wrap() {		return wrap(this.fileName, this.inValidationMode);	}	/**	 * Parses the XML document in validation mode (checks that the XML document	 * satisfies the Schema. If required, shows the errors Besides, it obtains	 * the root element of the document.	 * 	 * @param filename	 *            The XML file name.	 * @inValidationMode A boolean to indicate if the document should be read in	 *                   validation mode.	 * @return false if an error occurs during parsing.	 */	private boolean wrap(String filename, boolean inValidationMode) {		try {			DOMParser parser = new DOMParser();			if (inValidationMode) {				parser.setFeature("http://xml.org/sax/features/validation",						true);				parser.setFeature(						"http://apache.org/xml/features/validation/schema",						true);				parser.setProperty(						"http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",						"xmlInputAddReport.xsd");			}			parser.setErrorHandler(new CustomErrorHandler());			parser.parse(filename);			doc = parser.getDocument();		} catch (Exception sxe) {			sxe.printStackTrace();			return false;		}		elem = doc.getDocumentElement();		return true;	}	/**	 * Obtains the current XML document (perhaps after the modifications you	 * made) in the form of a String	 * 	 * @return The current XML document as a String	 */	public String to_XMLString() {		String xmlCode = null;		File f = null;		try {			f = File.createTempFile("XmlWrapper", ".xml");			to_XML(f.getAbsolutePath());			xmlCode = fileToString(f.getAbsolutePath());		} catch (IOException ex) {			System.err.println(ex.getMessage());			return null;		}		return xmlCode;	}	/**	 * Obtains a String representation of a text file	 * 	 * @return The text file as a String	 */	private String fileToString(String textFilePath) throws IOException {		FileInputStream fis = new FileInputStream(textFilePath);		byte[] b = new byte[fis.available()];		fis.read(b);		fis.close();		return new String(b);	}	/**	 * Stores the changes in the same XML document	 */	public void to_XML() {		to_XML(fileName);	}	/**	 * Stores the changes in an XML document	 * 	 * @param filename	 *            The name of the file	 */	public void to_XML(String filename) {		try {			FileOutputStream out = new FileOutputStream(filename);			OutputFormat format = new OutputFormat(doc, "UTF-8", true);			XMLSerializer s = new XMLSerializer(out, format);			s.serialize(doc);		} catch (Exception e) {			e.printStackTrace();		}	}	/**	 * Verifies that all the changes produces to DOM satisfy the Schema	 * 	 * @return True If the current DOM is coherent with the schema. False if any	 *         modification violates the schema. A description of the error will	 *         be shown.	 */	public boolean validate() {		to_XML(fileName + ".tmp.xml");		if (!wrap(fileName + ".tmp.xml", true))			return false;		else			return true;	}	/************* M�todos de entrada al documento XML *************/	/**	 * Obtiene el valor del elemento HOME_DIR	 * 	 * @return El valor del elemento HOME_DIR	 */	public String get_HOME_DIR() {		try {			String unparsedValue = getValueByTagName("HOME_DIR");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento HOME_DIR Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento HOME_DIR	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_HOME_DIR(String value) {		String unparsedValue = value;		try {			setValueByTagName("HOME_DIR", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("HOME_DIR");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento HOME_DIR y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_HOME_DIR() {		try {			Element e = getElementByTagName("HOME_DIR");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene el valor del elemento TMP_DIR	 * 	 * @return El valor del elemento TMP_DIR	 */	public String get_TMP_DIR() {		try {			String unparsedValue = getValueByTagName("TMP_DIR");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento TMP_DIR Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento TMP_DIR	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_TMP_DIR(String value) {		String unparsedValue = value;		try {			setValueByTagName("TMP_DIR", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("TMP_DIR");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento TMP_DIR y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_TMP_DIR() {		try {			Element e = getElementByTagName("TMP_DIR");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene un Wrapper a partir del elemento INDEXER_PARAMETERS	 * 	 * @param value	 *            El Wrapper obtenido	 */	public INDEXER_PARAMETERS get_INDEXER_PARAMETERS() {		return new INDEXER_PARAMETERS(elem);	}	/**	 * Obtiene el valor del elemento HOST_PORT	 * 	 * @return El valor del elemento HOST_PORT	 */	public String get_HOST_PORT() {		try {			String unparsedValue = getValueByTagName("HOST_PORT");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento HOST_PORT Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento HOST_PORT	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_HOST_PORT(String value) {		String unparsedValue = value;		try {			setValueByTagName("HOST_PORT", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("HOST_PORT");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento HOST_PORT y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_HOST_PORT() {		try {			Element e = getElementByTagName("HOST_PORT");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene el valor del elemento USER	 * 	 * @return El valor del elemento USER	 */	public String get_USER() {		try {			String unparsedValue = getValueByTagName("USER");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento USER Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento USER	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_USER(String value) {		String unparsedValue = value;		try {			setValueByTagName("USER", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("USER");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento USER y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_USER() {		try {			Element e = getElementByTagName("USER");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene el valor del elemento CENTERNAME	 * 	 * @return El valor del elemento CENTERNAME	 */	public String get_CENTERNAME() {		try {			String unparsedValue = getValueByTagName("CENTERNAME");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento CENTERNAME Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento CENTERNAME	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_CENTERNAME(String value) {		String unparsedValue = value;		try {			setValueByTagName("CENTERNAME", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("CENTERNAME");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento CENTERNAME y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_CENTERNAME() {		try {			Element e = getElementByTagName("CENTERNAME");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene el valor del elemento DB_NAME	 * 	 * @return El valor del elemento DB_NAME	 */	public String get_DB_NAME() {		try {			String unparsedValue = getValueByTagName("DB_NAME");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento DB_NAME Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento DB_NAME	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_DB_NAME(String value) {		String unparsedValue = value;		try {			setValueByTagName("DB_NAME", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("DB_NAME");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento DB_NAME y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_DB_NAME() {		try {			Element e = getElementByTagName("DB_NAME");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene el valor del elemento URL_IIS	 * 	 * @return El valor del elemento URL_IIS	 */	public String get_URL_IIS() {		try {			String unparsedValue = getValueByTagName("URL_IIS");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento URL_IIS Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento URL_IIS	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_URL_IIS(String value) {		String unparsedValue = value;		try {			setValueByTagName("URL_IIS", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("URL_IIS");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento URL_IIS y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_URL_IIS() {		try {			Element e = getElementByTagName("URL_IIS");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene el valor del elemento IDCENTER	 * 	 * @return El valor del elemento IDCENTER	 */	public String get_IDCENTER() {		try {			String unparsedValue = getValueByTagName("IDCENTER");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento IDCENTER Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento IDCENTER	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_IDCENTER(String value) {		String unparsedValue = value;		try {			setValueByTagName("IDCENTER", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("IDCENTER");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento IDCENTER y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_IDCENTER() {		try {			Element e = getElementByTagName("IDCENTER");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene el valor del elemento INDEXER_TYPE	 * 	 * @return El valor del elemento INDEXER_TYPE	 */	public String get_INDEXER_TYPE() {		try {			String unparsedValue = getValueByTagName("INDEXER_TYPE");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento INDEXER_TYPE Si el elemento no existe, lo	 * crea previamente	 * 	 * @param value	 *            El valor a establecer al elemento INDEXER_TYPE	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_INDEXER_TYPE(String value) {		String unparsedValue = value;		try {			setValueByTagName("INDEXER_TYPE", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("INDEXER_TYPE");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento INDEXER_TYPE y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_INDEXER_TYPE() {		try {			Element e = getElementByTagName("INDEXER_TYPE");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene un Wrapper a partir del elemento LIST_OF_URL_IIS	 * 	 * @param value	 *            El Wrapper obtenido	 */	public LIST_OF_URL_IIS get_LIST_OF_URL_IIS() {		return new LIST_OF_URL_IIS(elem);	}	/**	 * Obtiene el valor del elemento REGISTRATION_PATH	 * 	 * @return El valor del elemento REGISTRATION_PATH	 */	public String get_REGISTRATION_PATH() {		try {			String unparsedValue = getValueByTagName("REGISTRATION_PATH");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento REGISTRATION_PATH Si el elemento no existe,	 * lo crea previamente	 * 	 * @param value	 *            El valor a establecer al elemento REGISTRATION_PATH	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_REGISTRATION_PATH(String value) {		String unparsedValue = value;		try {			setValueByTagName("REGISTRATION_PATH", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("REGISTRATION_PATH");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento REGISTRATION_PATH y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_REGISTRATION_PATH() {		try {			Element e = getElementByTagName("REGISTRATION_PATH");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene un Wrapper a partir del elemento INDEX_SERVICE_PARAMETERS	 * 	 * @param value	 *            El Wrapper obtenido	 */	public INDEX_SERVICE_PARAMETERS get_INDEX_SERVICE_PARAMETERS() {		return new INDEX_SERVICE_PARAMETERS(elem);	}	/**	 * Obtiene un Wrapper a partir del elemento GATEKEEPER_PARAMETERS	 * 	 * @param value	 *            El Wrapper obtenido	 */	public GATEKEEPER_PARAMETERS get_GATEKEEPER_PARAMETERS() {		return new GATEKEEPER_PARAMETERS(elem);	}	/**	 * Obtiene un Wrapper a partir del elemento GRIDFTP_PARAMETERS	 * 	 * @param value	 *            El Wrapper obtenido	 */	public GRIDFTP_PARAMETERS get_GRIDFTP_PARAMETERS() {		return new GRIDFTP_PARAMETERS(elem);	}	/**	 * Obtiene un Wrapper a partir del elemento LOGGER_PARAMETERS	 * 	 * @param value	 *            El Wrapper obtenido	 */	public LOGGER_PARAMETERS get_LOGGER_PARAMETERS() {		return new LOGGER_PARAMETERS(elem);	}	/**	 * Obtiene el valor del elemento PASSWORD	 * 	 * @return El valor del elemento PASSWORD	 */	public String get_PASSWORD() {		try {			String unparsedValue = getValueByTagName("PASSWORD");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento PASSWORD Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento PASSWORD	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_PASSWORD(String value) {		String unparsedValue = value;		try {			setValueByTagName("PASSWORD", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("PASSWORD");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento PASSWORD y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_PASSWORD() {		try {			Element e = getElementByTagName("PASSWORD");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene el valor del elemento BASE_PATH	 * 	 * @return El valor del elemento BASE_PATH	 */	public String get_BASE_PATH() {		try {			String unparsedValue = getValueByTagName("BASE_PATH");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento BASE_PATH Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento BASE_PATH	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_BASE_PATH(String value) {		String unparsedValue = value;		try {			setValueByTagName("BASE_PATH", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("BASE_PATH");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento BASE_PATH y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_BASE_PATH() {		try {			Element e = getElementByTagName("BASE_PATH");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene un Wrapper a partir del elemento FILE_SYSTEM_PARAMETERS	 * 	 * @param value	 *            El Wrapper obtenido	 */	public FILE_SYSTEM_PARAMETERS get_FILE_SYSTEM_PARAMETERS() {		return new FILE_SYSTEM_PARAMETERS(elem);	}	/**	 * Obtiene un Wrapper a partir del elemento AMGA_PARAMETERS	 * 	 * @param value	 *            El Wrapper obtenido	 */	public AMGA_PARAMETERS get_AMGA_PARAMETERS() {		return new AMGA_PARAMETERS(elem);	}	/**	 * Obtiene un Wrapper a partir del elemento CONFIGURATION	 * 	 * @param value	 *            El Wrapper obtenido	 */	public CONFIGURATION get_CONFIGURATION() {		return new CONFIGURATION(elem);	}	/**	 * Obtiene el valor del elemento PATH_CATRUSTCERT	 * 	 * @return El valor del elemento PATH_CATRUSTCERT	 */	public String get_PATH_CATRUSTCERT() {		try {			String unparsedValue = getValueByTagName("PATH_CATRUSTCERT");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento PATH_CATRUSTCERT Si el elemento no existe, lo	 * crea previamente	 * 	 * @param value	 *            El valor a establecer al elemento PATH_CATRUSTCERT	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_PATH_CATRUSTCERT(String value) {		String unparsedValue = value;		try {			setValueByTagName("PATH_CATRUSTCERT", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("PATH_CATRUSTCERT");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento PATH_CATRUSTCERT y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_PATH_CATRUSTCERT() {		try {			Element e = getElementByTagName("PATH_CATRUSTCERT");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene el valor del elemento HOST_NAME	 * 	 * @return El valor del elemento HOST_NAME	 */	public String get_HOST_NAME() {		try {			String unparsedValue = getValueByTagName("HOST_NAME");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento HOST_NAME Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento HOST_NAME	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_HOST_NAME(String value) {		String unparsedValue = value;		try {			setValueByTagName("HOST_NAME", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("HOST_NAME");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento HOST_NAME y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_HOST_NAME() {		try {			Element e = getElementByTagName("HOST_NAME");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}	/**	 * Obtiene un Wrapper a partir del elemento SERVICE_PARAMETERS	 * 	 * @param value	 *            El Wrapper obtenido	 */	public SERVICE_PARAMETERS get_SERVICE_PARAMETERS() {		return new SERVICE_PARAMETERS(elem);	}	/**	 * Obtiene un Wrapper a partir del elemento BACKEND_PARAMETERS	 * 	 * @param value	 *            El Wrapper obtenido	 */	public BACKEND_PARAMETERS get_BACKEND_PARAMETERS() {		return new BACKEND_PARAMETERS(elem);	}	/**	 * Obtiene el valor del elemento LOG_PATH	 * 	 * @return El valor del elemento LOG_PATH	 */	public String get_LOG_PATH() {		try {			String unparsedValue = getValueByTagName("LOG_PATH");			String parsedValue = unparsedValue;			return parsedValue;		} catch (Exception e) {			e.printStackTrace();			return null;		}	}	/**	 * Asocia un valor al elemento LOG_PATH Si el elemento no existe, lo crea	 * previamente	 * 	 * @param value	 *            El valor a establecer al elemento LOG_PATH	 * @throws IllegalArgumentException	 *             Si el valor incumple alguna de las restricciones del schema	 */	public void set_LOG_PATH(String value) {		String unparsedValue = value;		try {			setValueByTagName("LOG_PATH", unparsedValue);		} catch (Exception e) { // Si no existe el elemento lo creamos			Element el = doc.createElement("LOG_PATH");			el.appendChild(doc.createTextNode(unparsedValue));			elem.appendChild(el);		}	}	/**	 * Elimina el elemento LOG_PATH y sus descendientes	 * 	 * @throws java.util.NoSuchElementException	 *             Si el atributo no se encuentra	 * @throws IllegalStateException	 *             Si el schema impide eliminar m�s elementos	 */	public void del_LOG_PATH() {		try {			Element e = getElementByTagName("LOG_PATH");			e.getParentNode().removeChild(e);		} catch (Exception ex) {			throw new java.util.NoSuchElementException(					" Se ha intentado borrar un elemento inexistente");		}	}}/** * Clase que proporciona un comportamiento personalizado frente a los errores al * analizar el documento XML. */class CustomErrorHandler implements org.xml.sax.ErrorHandler {	public void warning(org.xml.sax.SAXParseException exception)			throws org.xml.sax.SAXException {		System.out.println(">> Warning << " + "  Linea: "				+ exception.getLineNumber() + " " + "  Archivo:     "				+ exception.getSystemId() + " " + "  Mensaje: "				+ exception.getMessage());		throw new org.xml.sax.SAXException("Warning  ");	}	public void error(org.xml.sax.SAXParseException exception)			throws org.xml.sax.SAXException {		System.out.println(">> Error de Sintaxis << " + "  Linea: "				+ exception.getLineNumber() + " " + "  Archivo:     "				+ exception.getSystemId() + " " + "  Mensaje: "				+ exception.getMessage());		throw new org.xml.sax.SAXException("Error de Sintaxis");	}	public void fatalError(org.xml.sax.SAXParseException exception)			throws org.xml.sax.SAXException {		System.out.println(">> Error Fatal << " + "  Linea: "				+ exception.getLineNumber() + " " + "  Archivo:     "				+ exception.getSystemId() + " " + "  Mensaje: "				+ exception.getMessage());		throw new org.xml.sax.SAXException("Error fatal");	}}