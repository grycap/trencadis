package trencadis.middleware.operations.DICOMStorage;

import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputRemoveReport.XmlWrapper;
import trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This class is used to remove an XML representation of a DICOM-SR in the grid
 * infrastructure
 */
public class TRENCADIS_XMLDSR_REMOVE extends
		TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE {

	private String _IDOntology;
	private String _IDTRENCADISReport;

	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the Storage DICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param id_hospital Identifier of the hospital replica
	 * @param reportType The report type of the DICOM-SR to download
	 * @param reportID The report identifier of the DICOM-SR to download
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_REMOVE(TRENCADIS_SESSION session,
			TRENCADIS_XML_DICOM_SR_FILE xmlDsr, int id_center)
			throws Exception {
		super(session, id_center, xmlDsr.getIDOntology());

		_IDOntology = xmlDsr.getIDOntology();
		_IDTRENCADISReport = xmlDsr.getIDTRENCADISReport();
	}

	public TRENCADIS_XMLDSR_REMOVE(TRENCADIS_SESSION session,
			String IDOntology, String IDTRENCADISReport, int id_center)
			throws Exception {
		super(session, id_center, IDOntology);
		_IDOntology = IDOntology;
		_IDTRENCADISReport = IDTRENCADISReport;
	}

	/**
	 * This method deletes the DICOM-SR representation from the infrastructure.
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {

		if (_IDTRENCADISReport.equals("")) {
			// Si el documento no tiene un IDTRENCADISReport asignado, significa
			// que no ha sido subido.
			throw new Exception("Can not remove (IDTRENCADISReport="
					+ _IDTRENCADISReport
					+ " because has a IDTRENCADISReport assigned.");
		}

		String xmlInputRemoveReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "<IDONTOLOGY>"
				+ _IDOntology
				+ "</IDONTOLOGY>\n"
				+ "<IDTRENCADISREPORT>"
				+ _IDTRENCADISReport
				+ "</IDTRENCADISREPORT>\n" + "</INPUT>";

		String xmlOutputRemoveReport = DICOMStorage
				.xmlRemoveReport(xmlInputRemoveReport);

		XmlWrapper removeReport_wrapper = new XmlWrapper(xmlOutputRemoveReport, false);
		removeReport_wrapper.wrap();

		if (!removeReport_wrapper.get_OUTPUT().get_STATUS().equals("0"))
			throw new Exception("Can not remove report: "
					+ removeReport_wrapper.get_OUTPUT().get_DESCRIPTION());

	}

}
