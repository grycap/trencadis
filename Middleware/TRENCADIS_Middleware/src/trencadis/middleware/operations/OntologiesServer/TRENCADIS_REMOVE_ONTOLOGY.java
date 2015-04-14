package trencadis.middleware.operations.OntologiesServer;

import java.net.URI;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This class is used to remove an ontology from the infrastructure
 */
public class TRENCADIS_REMOVE_ONTOLOGY extends
		TRENCADIS_OPERATION_ONTOLOGIESSERVER_SERVICE {

	private String _idOntology;

	/**
	 * This constructor is used when we have the OntologiesServer URI and we do
	 * not need to ask for it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @param ontologiesServerURI The URI of the OntologiesServer service
	 * @param idOntology Identifier with the reportType of the ontology to remove
	 * @throws Exception
	 */
	public TRENCADIS_REMOVE_ONTOLOGY(TRENCADIS_SESSION session,
			URI ontologiesServerURI, String idOntology) throws Exception {
		super(session, ontologiesServerURI);

		_idOntology = idOntology;
	}

	/**
	 * This constructor is used when we do not have the URI of the
	 * OntologiesServer and we have to ask for it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @param idOntology Identifier with the reportType of the ontology to remove
	 * @throws Exception
	 */
	public TRENCADIS_REMOVE_ONTOLOGY(TRENCADIS_SESSION session,
			String idOntology) throws Exception {
		super(session);

		_idOntology = idOntology;
	}

	/**
	 * This method removes a DICOM-SR structure file from the OntologiesServer.
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {

		String xmlInputRemoveOntology = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>"
				+ "<IDONTOLOGY>"
				+ _idOntology
				+ "</IDONTOLOGY>"
				+ "</INPUT>";
		String xmlOutputRemoveOntology = ontologiesServer.xmlRemoveOntology(xmlInputRemoveOntology);

		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputRemoveOntology.XmlWrapper removeOntology_wrapper = new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputRemoveOntology.XmlWrapper(
				xmlOutputRemoveOntology, false);
		removeOntology_wrapper.wrap();

		if (removeOntology_wrapper.get_OUTPUT().get_STATUS().compareTo("-1") == 0)
			throw new Exception("Can not remove ontology: "
					+ removeOntology_wrapper.get_OUTPUT().get_DESCRIPTION());

	}

}
