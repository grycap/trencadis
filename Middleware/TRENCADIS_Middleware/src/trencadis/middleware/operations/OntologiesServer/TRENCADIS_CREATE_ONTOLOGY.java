package trencadis.middleware.operations.OntologiesServer;

import java.net.URI;

import trencadis.middleware.files.TRENCADIS_XML_ONTOLOGY_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This class is used to create a new ontology in the grid infrastructure, an XML representation of the structure of a DICOM-SR
 */
public class TRENCADIS_CREATE_ONTOLOGY extends TRENCADIS_OPERATION_ONTOLOGIESSERVER_SERVICE {

	private TRENCADIS_XML_ONTOLOGY_FILE _xmlDsrStr;
	protected String _description;
	protected String _idontology;

	/**
	 * This constructor is used when we have the OntologiesServer URI and we do not need to ask for it to the IIS. 
	 *
	 * @param session Session used by this operation
	 * @param ontologiesServerURI The URI of the OntologiesServer service
	 * @param xmlDsrStr Structure file with the ontology
	 * @param description Description field of the ontology
	 * @param restrictive Restriction field of the ontology
	 * @throws Exception 
	 */
	public TRENCADIS_CREATE_ONTOLOGY(TRENCADIS_SESSION session, URI ontologiesServerURI, TRENCADIS_XML_ONTOLOGY_FILE xmlDsrStr, String idontology, String description) throws Exception {
		super(session, ontologiesServerURI);
		
		_xmlDsrStr = xmlDsrStr;
		
		if (description.isEmpty()) _description = " ";
		else                       _description = description;
		if (idontology.isEmpty()) {
                    throw new Exception("Can not create ontology becaus de IDOntology is void");                    
                }
		else   _idontology = idontology;
	}

	/**
	 * This constructor is used when we do not have the URI of the OntologiesServer and we have to
	 * ask for it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @param xmlDsrStr Structure file with the ontology
	 * @param description Description field of the ontology
	 * @param restrictive Restriction field of the ontology
	 * @throws Exception
	 */
	public TRENCADIS_CREATE_ONTOLOGY(TRENCADIS_SESSION session, TRENCADIS_XML_ONTOLOGY_FILE xmlDsrStr, String idontology, String description) throws Exception {
		super(session);

		_xmlDsrStr = xmlDsrStr;

		if (description.isEmpty()) _description = " ";
		else                       _description = description;
		if (idontology.isEmpty()) {
                    throw new Exception("Can not create ontology becaus de IDOntology is void");                    
                }
		else   _idontology = idontology;
	}
	
	/**
	 * This method starts the upload of the DICOM-SR structure file to the infrastructure.
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {

		String xmlInputCreateOntology = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>"
				+ "    <CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>"
				+ "    <IDONTOLOGY>"
				+ _idontology
				+ "</IDONTOLOGY>"
				+ "    <DESCRIPTION>"
				+ _description
				+ "</DESCRIPTION>"
				+ "    <ONTOLOGY>"
				+ "          <xml_DICOM_SR_Template>"
				+ _xmlDsrStr.getContents()
				+ "</xml_DICOM_SR_Template>"
				+ "    </ONTOLOGY>" + "</INPUT>";
                                                                               
		String xmlOutputCreateOntology = ontologiesServer.xmlCreateOntology(xmlInputCreateOntology);                                
                                 
                trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputCreateOntology.XmlWrapper createOntology_wrapper =
			new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputCreateOntology.XmlWrapper(xmlOutputCreateOntology, false);
		createOntology_wrapper.wrap();    
                

		if (createOntology_wrapper.get_OUTPUT().get_STATUS().compareTo("-1") == 0)
			throw new Exception("Can not create ontology: " + createOntology_wrapper.get_OUTPUT().get_DESCRIPTION());

	}

}
