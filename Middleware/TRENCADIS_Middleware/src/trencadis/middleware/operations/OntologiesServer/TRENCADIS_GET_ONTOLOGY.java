package trencadis.middleware.operations.OntologiesServer;

import java.net.URI;
import org.globus.axis.util.Util;

import trencadis.middleware.files.TRENCADIS_XML_ONTOLOGY_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;
import trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputGetOntology.XmlWrapper;

/**
 * This class is used to download an ontology from the grid infrastructure
 */
public class TRENCADIS_GET_ONTOLOGY extends
		TRENCADIS_OPERATION_ONTOLOGIESSERVER_SERVICE {
	static { Util.registerTransport(); }
	private String _idOntology;

	/**
	 * This constructor is used when we have the OntologiesServer URI and we do
	 * not need to ask for it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @param ontologiesServerURI The URI of the OntologiesServer service
	 * @param idOntology Identifier with the reportType of the ontology to download
	 * @throws Exception
	 */
	public TRENCADIS_GET_ONTOLOGY(TRENCADIS_SESSION session,
			URI ontologiesServerURI, String idOntology) throws Exception {
		super(session, ontologiesServerURI);

		_idOntology = idOntology;

	}

	/**
	 * This constructor is used when we do not have the URI of the
	 * OntologiesServer and we have to ask for it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @param idOntology Identifier with the reportType of the ontology to download
	 * @throws Exception
	 */
	public TRENCADIS_GET_ONTOLOGY(TRENCADIS_SESSION session, String idOntology)
			throws Exception {
		super(session);

		_idOntology = idOntology;

	}

	/**
	 * This method downloads a DICOM-SR structure file from the infrastructure.
	 * 
	 * @throws Exception
	 */
	public TRENCADIS_XML_ONTOLOGY_FILE execute() throws Exception {

		String xmlInputGetOntology = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>"
				+ "   <CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>"
				+ "   <IDONTOLOGY>"
				+ _idOntology
				+ "</IDONTOLOGY>"
				+ "</INPUT>";
		String xmlOutputGetOntology = ontologiesServer
				.xmlGetOntology(xmlInputGetOntology);

		XmlWrapper getOntology_wrapper = new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputGetOntology.XmlWrapper(
				xmlOutputGetOntology, false);
		getOntology_wrapper.wrap();

		if (getOntology_wrapper.get_OUTPUT().get_STATUS().compareTo("-1") == 0) {

			throw new Exception("Can not get ontology: "
					+ getOntology_wrapper.get_OUTPUT().get_DESCRIPTION());

		} else {

			String DICOM_SR_STR = getOntology_wrapper.get_OUTPUT()
					.get_ONTOLOGY().get_xml_DICOM_SR_Template();

			// No se uriliza la herramienta de German, porque al ser este campo
			// un XML esta no funciona
			// String DICOM_SR_STR =
			// getOntology_wrapper.get_OUTPUT().get_ONTOLOGY().replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");

			TRENCADIS_XML_ONTOLOGY_FILE dicom_sr_structure = new TRENCADIS_XML_ONTOLOGY_FILE(
					_session, DICOM_SR_STR);

			return dicom_sr_structure;

		}

	}

}
