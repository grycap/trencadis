package trencadis.middleware.operations.OntologiesServer;

import java.net.URI;
import java.util.Iterator;
import java.util.Vector;

import trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputGetAllOntologies.*;
import trencadis.middleware.files.TRENCADIS_XML_ONTOLOGY_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;

public class TRENCADIS_GET_ALL_ONTOLOGIES extends TRENCADIS_OPERATION_ONTOLOGIESSERVER_SERVICE {

	/**
	 * This constructor is used when we do not have the URI of the
	 * OntologiesServer and we have to ask for it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @throws Exception
	 */	
	public TRENCADIS_GET_ALL_ONTOLOGIES(TRENCADIS_SESSION session)
			throws Exception {
		super(session);
		
	}
	
	/**
	 * This constructor is used when we have the OntologiesServer URI and we do
	 * not need to ask for it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @param ontologiesServerURI The URI of the OntologiesServer service
	 * @throws Exception
	 */
	public TRENCADIS_GET_ALL_ONTOLOGIES (TRENCADIS_SESSION session,
			URI ontologiesServerURI) throws Exception {
		super(session, ontologiesServerURI);

	}
	
	/**
	 * This method downloads all DICOM-SR structure files from the infrastructure.
	 * 
	 * @throws Exception
	 */
	public Vector<TRENCADIS_XML_ONTOLOGY_FILE> execute() throws Exception {
		Vector<TRENCADIS_XML_ONTOLOGY_FILE> ontology_files = null;
		String xmlInputGetOntology = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>"
				+ "   <CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>"
				+ "</INPUT>";
		String xmlOutputGetOntology = ontologiesServer
				.xmlGetAllOntologies(xmlInputGetOntology);
		
		XmlWrapper getOntology_wrapper = new XmlWrapper(xmlOutputGetOntology, false);
		getOntology_wrapper.wrap();

		if (getOntology_wrapper.get_OUTPUT().get_STATUS().compareTo("-1") == 0) {
			throw new Exception("Can not get ontologies: "
					+ getOntology_wrapper.get_OUTPUT().get_DESCRIPTION());
		} else {
			Iterator<ONTOLOGY> it = getOntology_wrapper.get_OUTPUT().get_ONTOLOGIES().getAll_ONTOLOGY();
			ontology_files = new Vector<TRENCADIS_XML_ONTOLOGY_FILE>();
			while (it.hasNext()) {
				ONTOLOGY ontology = (ONTOLOGY) it.next();
				String DICOM_SR_STR = ontology.get_xml_DICOM_SR_Template();
				TRENCADIS_XML_ONTOLOGY_FILE dicom_sr_structure = new TRENCADIS_XML_ONTOLOGY_FILE(
						_session, DICOM_SR_STR);
				ontology_files.add(dicom_sr_structure);
			}
		}		
		return ontology_files;
	}
}
