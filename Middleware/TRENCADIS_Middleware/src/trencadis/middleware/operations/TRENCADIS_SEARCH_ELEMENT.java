package trencadis.middleware.operations;

import java.io.IOException;

import trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE;

/**
 * This class is used to store the information of a search element.
 */
public class TRENCADIS_SEARCH_ELEMENT {

	private String _reportId;

	private TRENCADIS_XML_DICOM_SR_FILE _xmlDsr = null;

	/**
	 * The constructor stores the element information parameters in the class.
	 * 
	 * @param reportId Identifier of the report
	 * @throws IOException
	 */
	TRENCADIS_SEARCH_ELEMENT(String reportId) throws IOException {
		_reportId = reportId;
	}
	
	/**
	 * Getter method for the report id
	 * @return The report id
	 */
	public String getReportId() {
		return _reportId;
	}

	/**
	 * Setter method called by {@link trencadis.middleware.operations.TRENCADIS_XMLDSR_DOWNLOAD TRENCADIS_XMLDSR_DOWNLOAD}
	 * when the {@link trencadis.middleware.operations.TRENCADIS_SEARCH_RESULTS TRENCADIS_SEARCH_RESULTS} are used to download a DICOM-SR.
	 * @param xmlDsr The {@link trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE TRENCADIS_XML_DICOM_SR_FILE} to be associated with this {@link trencadis.middleware.operations.TRENCADIS_SEARCH_ELEMENT TRENCADIS_SEARCH_ELEMENT}
	 */
	public void setXmlDsr(TRENCADIS_XML_DICOM_SR_FILE xmlDsr) {
		_xmlDsr = xmlDsr;
	}

	/**
	 * Getter method for the local file that can be obtained by download.
	 * @return A {@link trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE TRENCADIS_XML_DICOM_SR_FILE} element if the report has been downloaded, else returns null.
	 */

	public TRENCADIS_XML_DICOM_SR_FILE getXmlDsr() {
		return _xmlDsr;
	}

}
