package trencadis.middleware.operations.DICOMStorage;

import java.util.Iterator;
import java.util.Vector;

import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReports.DICOM_SR;
import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReports.XmlWrapper;
import trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This class is used to download an XML representation of a DICOM-SR from the
 * grid infrastructure
 */
public class TRENCADIS_XMLDSR_DOWNLOAD_ALL extends
		TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE {
	
	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the Storage DICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param idCenter Identifier of the hospital replica
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_DOWNLOAD_ALL(TRENCADIS_SESSION session, int idCenter)
			throws Exception {
		super(session, idCenter);
		
	}

	/**
	 * This method starts the download, also returns the downloaded files in a vector.
	 * Each file is associated with the corresponding
	 * {@link trencadis.middleware.operations.TRENCADIS_XML_DICOM_SR_FILE
	 * TRENCADIS_XML_DICOM_SR_FILE}
	 * 
	 * @return The downloaded files into a
	 *         {@link trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE
	 *         TRENCADIS_XML_DICOM_SR_FILE} vector
	 * @throws Exception
	 */
	public Vector<TRENCADIS_XML_DICOM_SR_FILE> execute() throws Exception {
		
		Vector<TRENCADIS_XML_DICOM_SR_FILE> v_reports = null;
		String xmlInputDownloadReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "</INPUT>";	

		String xmlOutputDownloadReport = DICOMStorage.xmlDownloadAllReports(xmlInputDownloadReport);	
		
		XmlWrapper downloadReport_wrapper = new XmlWrapper(xmlOutputDownloadReport, false);
		downloadReport_wrapper.wrap();
		
		if (!downloadReport_wrapper.get_OUTPUT().get_STATUS().equals("0")) {
			throw new Exception("Can not download report: "
					+ downloadReport_wrapper.get_OUTPUT().get_DESCRIPTION());
		} else {
			
			Iterator it_reports = downloadReport_wrapper.get_OUTPUT().get_DICOM_REPORTS().getAll_DICOM_SR();
			v_reports = new Vector<TRENCADIS_XML_DICOM_SR_FILE>();
			while (it_reports.hasNext()){
				DICOM_SR obj = (DICOM_SR)it_reports.next();
				String dicom_sr = obj.getValue();
				TRENCADIS_XML_DICOM_SR_FILE xmldsr = new TRENCADIS_XML_DICOM_SR_FILE(_session, dicom_sr);
				v_reports.add(xmldsr);
			}			
			return v_reports;

		}

	}

}
