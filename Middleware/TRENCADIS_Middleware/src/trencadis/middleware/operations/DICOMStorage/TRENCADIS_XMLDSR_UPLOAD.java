package trencadis.middleware.operations.DICOMStorage;

import trencadis.middleware.operations.DICOMStorage.TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE;
import trencadis.middleware.operations.EOUIDGeneratorService.TRENCADIS_NEW_EOUID;

import java.io.File;
import java.net.URI;

import org.apache.commons.io.FileUtils;

import trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This class is used to upload an XML representation of a DICOM-SR into the
 * grid infrastructure
 */
public class TRENCADIS_XMLDSR_UPLOAD extends
		TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE {

	private TRENCADIS_XML_DICOM_SR_FILE _xmlDsr;

	/**
	 * This constructor need to ask it to the IIS.
	 *
	 * @param session
	 *            Session used by this operation
	 * @param xmlDsr
	 *            File to be uploaded
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_UPLOAD(TRENCADIS_SESSION session,
			TRENCADIS_XML_DICOM_SR_FILE xmlDsr) throws Exception {
		super(session, xmlDsr.getIDOntology());

		_xmlDsr = xmlDsr;
	}

	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the Storage DICOM URI.
	 *
	 * @param session Session used by this operation
	 * @param xmlDsr File to be uploaded
	 * @param id_hospital Identifier of the hospital replica
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_UPLOAD(TRENCADIS_SESSION session,
			TRENCADIS_XML_DICOM_SR_FILE xmlDsr, int id_center)
			throws Exception {
		super(session, id_center, xmlDsr.getIDOntology());

		_xmlDsr = xmlDsr;
	}

	/**
	 * This constructor is used when we have the StorageDICOM URI and we do not
	 * need to ask it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @param xmlDsr File to be uploaded
	 * @param storageDICOMURI The URI of the StorageDICOM service where the file is
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_UPLOAD(TRENCADIS_SESSION session,
			TRENCADIS_XML_DICOM_SR_FILE xmlDsr, URI DICOMStorageURI, String Key)
			throws Exception {
		super(session, DICOMStorageURI, xmlDsr.getIDOntology());

		_xmlDsr = xmlDsr;
	}

	/**
	 * This method starts the upload of the DICOM-SR representation.
	 *
	 * @throws Exception
	 */
	public void execute() throws Exception {

		if (!_xmlDsr.getIDTRENCADISReport().equals("")) {
			// Si el documento tienen un IDTRENCADISReport asignado, significa
			// que ya ha sido subido.
			throw new Exception("Can not add (IDTRENCADISReport="
					+ _xmlDsr.getIDTRENCADISReport()
					+ " because has a IDTRENCADISReport assigned.");
		} else {
			// Generate an IDTRENCADISReport
			TRENCADIS_NEW_EOUID new_eouid = new TRENCADIS_NEW_EOUID(_session);
			String eouid = new_eouid.execute();
			_xmlDsr.setIDTRENCADISReport(eouid);
		}

		String xmlInputAddReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "<IDONTOLOGY>"
				+ _xmlDsr.getIDOntology()
				+ "</IDONTOLOGY>\n"
				+ "<IDTRENCADISREPORT>"
				+ _xmlDsr.getIDTRENCADISReport()
				+ "</IDTRENCADISREPORT>\n"
				+ "<DICOM_SR>\n"
				+ "<xml_DICOM_SR>"
				+ _xmlDsr.getContents().replace(
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
				+ "</xml_DICOM_SR>\n" + "</DICOM_SR>\n" + "</INPUT>";
		
		
		String xmlOutputAddReport = DICOMStorage.xmlAddReport(xmlInputAddReport);

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputAddReport.XmlWrapper addReport_wrapper = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputAddReport.XmlWrapper(
				xmlOutputAddReport, false);
		addReport_wrapper.wrap();

		if (!addReport_wrapper.get_OUTPUT().get_STATUS().equals("0"))
			throw new Exception("Can not add report: "
					+ addReport_wrapper.get_OUTPUT().get_DESCRIPTION());

	}

}
