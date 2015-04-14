package trencadis.middleware.operations.DICOMStorage;

import java.io.File;

import org.apache.commons.io.FileUtils;

import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadReport.XmlWrapper;
import trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This class is used to download an XML representation of a DICOM-SR from the
 * grid infrastructure
 */
public class TRENCADIS_XMLDSR_DOWNLOAD extends
		TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE {

	private String _IDTRENCADISReport;
	private String _IDOntology;

	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the Storage DICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param id_center Identifier of the hospital replica
	 * @param reportType The report type of the DICOM-SR to download
	 * @param reportID The report identifier of the DICOM-SR to download
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_DOWNLOAD(TRENCADIS_SESSION session,
			TRENCADIS_XML_DICOM_SR_FILE xmlDsr, int id_center)
			throws Exception {
		super(session, id_center, xmlDsr.getIDOntology());

		_IDOntology = xmlDsr.getIDOntology();
		_IDTRENCADISReport = xmlDsr.getIDTRENCADISReport();
	}

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
	public TRENCADIS_XMLDSR_DOWNLOAD(TRENCADIS_SESSION session,
			String IDOntology, String IDTRENCADISReport, int idCenter)
			throws Exception {
		super(session, idCenter, IDOntology);

		_IDTRENCADISReport = IDTRENCADISReport;
		_IDOntology = IDOntology;
	}

	/**
	 * This method starts the download, also returns the downloaded file.
	 * @return The downloaded file into a
	 *         {@link trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE
	 *         TRENCADIS_XML_DICOM_SR_FILE} object
	 * @throws Exception
	 */
	public TRENCADIS_XML_DICOM_SR_FILE execute() throws Exception {

		String xmlInputDownloadReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "<IDONTOLOGY>"
				+ _IDOntology
				+ "</IDONTOLOGY>"
				+ "<IDTRENCADISREPORT>"
				+ _IDTRENCADISReport
				+ "</IDTRENCADISREPORT>\n" + "</INPUT>";

		String xmlOutputDownloadReport = DICOMStorage.xmlDownloadReport(xmlInputDownloadReport);

		XmlWrapper downloadReport_wrapper = new XmlWrapper(xmlOutputDownloadReport, false);
		downloadReport_wrapper.wrap();
		
		if (!downloadReport_wrapper.get_OUTPUT().get_STATUS().equals("0")) {
			throw new Exception("Can not download report: "
					+ downloadReport_wrapper.get_OUTPUT().get_DESCRIPTION());
		} else {

			String DICOM_SR = downloadReport_wrapper.get_OUTPUT().get_DICOM_SR().get_xml_DICOM_SR();
			TRENCADIS_XML_DICOM_SR_FILE dicom_sr = new TRENCADIS_XML_DICOM_SR_FILE(_session, DICOM_SR);
			return dicom_sr;

		}

	}

}
