package trencadis.middleware.files;

import trencadis.middleware.files.pdfs.PDFBreastReport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.xml.sax.SAXException;

import trencadis.middleware.files.TRENCADIS_GENERIC_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;

import org.apache.commons.io.FileUtils;

/**
 * File manager class with support for attributes and operations related to the
 * xml DICOM-SR files.
 */
public class TRENCADIS_XML_DICOM_SR_FILE extends TRENCADIS_GENERIC_FILE {

	private String _id_trencadis_report = null;
	private String _id_report = null;
	private String _id_ontology = null;
	private String _date_time_start = null;
	private String _date_time_end = null;
	
	/**
	 * This constructor loads the contents of the file from the filesystem.
	 *
	 * @param session An established
	 *            {@link trencadis.middleware.login.TRENCADIS_SESSION
	 *            TRENCADIS_SESSION}
	 * @param file File from which the contents will be read
	 * @throws Exception
	 */
	public TRENCADIS_XML_DICOM_SR_FILE(TRENCADIS_SESSION session, File file)
			throws Exception {
		super(session, file);
		getReportParams();
	}

	/**
	 * This constructor loads the contents of the file from a string object.
	 * 
	 * @param middleware_config Middleware configuration (contains tmp dir)
	 * @param contents A string containing all the contents.
	 * @throws Exception
	 */
	public TRENCADIS_XML_DICOM_SR_FILE(TRENCADIS_SESSION middleware_config,
			String contents) throws Exception {
		super(middleware_config, contents);
		getReportParams();
	}

	/**
	 * Reads the contents and extracts reportType, reportID, studyUID and
	 * patientsName from the XML representation of the DICOM-SR.
	 * 
	 * @throws Exception
	 */
	private void getReportParams() throws Exception {
		try {
			super.dsr_helper.getReportParams(super._contents);
		} catch (Exception e) {
		}

		_id_ontology = super.dsr_helper.getIDOntology();
		_id_report = super.dsr_helper.getIDReport();
		_id_trencadis_report = super.dsr_helper.getIDTRENCADISReport();
		_date_time_start = super.dsr_helper.get_DateTimeStart();
		_date_time_end = super.dsr_helper.get_DateTimeEnd();

		if (_id_ontology == null)
			throw new Exception("Can not read report parameters");
	}

	/**
	 * Getter method for the ontology ID
	 * 
	 * @return The ontology
	 */
	public String getIDOntology() {
		return _id_ontology;
	}

	/**
	 * Getter method for the report ID
	 * 
	 * @return The reportID
	 */
	public String getIDReport() {
		return _id_report;
	}

	/**
	 * Getter method for the TRENCADIS report ID
	 * 
	 * @return The TRENCADISreportID
	 */
	public String getIDTRENCADISReport() {
		return _id_trencadis_report;
	}

	/**
	 * Setter method for the TRENCADIS report ID
	 */
	public void setIDTRENCADISReport(String idtrencadisreport) {
		_id_trencadis_report = idtrencadisreport;
		_contents = _contents.replace("IDTRENCADISReport=\"\"",
				"IDTRENCADISReport=\"" + _id_trencadis_report + "\"");
	}
	
	/**
	 * Getter method for the date time start
	 * 
	 * @return The dateTimeStart
	 */
	public String getDateTimeStart() {
		return _date_time_start;
	}
	
	/**
	 * Getter method for the date time end
	 * 
	 * @return The dateTimeEnd
	 */
	public String getDateTimeEnd() {
		return _date_time_end;
	}

	/**
	 * Generates a DICOM-SR binary file from the contents in this class.
	 *
	 * @return A File object pointing to the generated DICOM-SR binary file.
	 * @throws Exception
	 */
	public File generateDICOMSR() throws Exception {
		return super.dsr_helper.generateDICOMSR(super._contents);
	}

	/**
	 * Generates a zipped DICOM-SR binary file from the contents in this class.
	 *
	 * @return A File object pointing to the generated DICOM-SR binary file.
	 * @throws Exception
	 */
	public File generateZippedDICOMSR() throws Exception {

		File dcm = super.dsr_helper.generateDICOMSR(super._contents);

		byte[] tmpBuf = new byte[1024];
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				dcm.getPath() + ".zip"));
		FileInputStream in = new FileInputStream(dcm.getPath());
		out.putNextEntry(new ZipEntry(dcm.getName()));

		int len;
		while ((len = in.read(tmpBuf)) > 0) {
			out.write(tmpBuf, 0, len);
		}
		out.closeEntry();
		in.close();
		out.close();

		return new File(dcm.getPath() + ".zip");

	}

	/**
	 * Generates a PDF report from the contents in this class.
	 *
	 * @return A File object pointing to the generated PDF report.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SAXException
	 */
	public File generatePDFReport(String strPath) throws IOException,
			InterruptedException, SAXException {

		// File dicom_sr__file = new File(strPath);
		// FileUtils.writeStringToFile(dicom_sr__file, this._contents);
		// System.out.println("Hola");

		String strPath2 = "/usr/local/soft/apache-tomcat-7.0.27/webapps/TRENCADIS_EIR/tmp/prueba"
				+ System.currentTimeMillis() + ".pdf";
		try {
			// PDFBreastReport pdfCreado = new
			// PDFBreastReport(strPath,strPath2,"");

		} catch (Exception ex) {

		}

		return new File(strPath2);
	}

}
