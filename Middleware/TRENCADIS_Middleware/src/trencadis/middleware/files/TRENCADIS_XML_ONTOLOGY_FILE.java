package trencadis.middleware.files;

import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;

import trencadis.middleware.files.TRENCADIS_GENERIC_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * File manager class with support for attributes and operations related to the
 * xml DICOM-SR structure files.
 */
public class TRENCADIS_XML_ONTOLOGY_FILE extends TRENCADIS_GENERIC_FILE {

	private String _idontology = null;
	private String _description = null;

	/**
	 * This constructor loads the contents of the file from the filesystem.
	 * 
	 * @param session An established
	 *            {@link trencadis.middleware.login.TRENCADIS_SESSION
	 *            TRENCADIS_SESSION}
	 * @param file File from which the contents will be read
	 * @throws Exception
	 */
	public TRENCADIS_XML_ONTOLOGY_FILE(TRENCADIS_SESSION session, File file)
			throws Exception {
		super(session, file);
		this._contents = this._contents.replace(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		getParams();
	}

	/**
	 * This constructor loads the contents of the file from a string object.
	 * 
	 * @param middleware_config Middleware configuration (contains tmp dir)
	 * @param contents A string containing all the contents.
	 * @throws Exception
	 */
	public TRENCADIS_XML_ONTOLOGY_FILE(TRENCADIS_SESSION middleware_config,
			String contents) throws Exception {
		super(middleware_config, contents);
		getParams();
	}

	/**
	 * Reads the contents and extracts the reportType of the XML representation
	 * of the DICOM-SR ontology.
	 * 
	 * @throws Exception
	 */
	private void getParams() throws Exception {
		try {
			super.dsr_helper.getReportParams(super._contents);
		} catch (Exception e) {
		}

		_idontology = super.dsr_helper.getIDOntology();
		_description = super.dsr_helper.getDescription();

		if (_idontology == null)
			throw new Exception("Can not read report parameters");
	}

	/**
	 * Getter method for the report type
	 * 
	 * @return The reportType
	 */
	public String getIDOntology() {
		return _idontology;
	}

	/**
	 * Getter method for the report type
	 * 
	 * @return The reportType
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Prints on screen a tree representation of the ontology structure.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SAXException
	 */
	public void listStructure() throws IOException, InterruptedException,
			SAXException {
		super.dsr_helper.listStructure(super._contents);
	}

}
