package trencadis.middleware.operations.DICOMStorage;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReportsByOntology.XmlWrapper;
import trencadis.infrastructure.services.stubs.DICOMStorage.DICOMStoragePortType;
import trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This class is used to download an XML representation of a DICOM-SR from the
 * grid infrastructure
 */
public class TRENCADIS_XMLDSR_DOWNLOAD extends
		TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE {
	
	private String idOntology = null;
	private String idTrencadisReport = null;
	
	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the Storage DICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_DOWNLOAD(TRENCADIS_SESSION session)throws Exception {
		super(session);
	}
	
	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the Storage DICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param idCenter Identifier of the hospital replica
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_DOWNLOAD(TRENCADIS_SESSION session, int idCenter)
			throws Exception {
		super(session, idCenter);
	}
	
	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the Storage DICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param idCenter Identifier of the hospital replica
	 * @param xmlDsr The DICOM-SR to download
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_DOWNLOAD(TRENCADIS_SESSION session, int idCenter,
			TRENCADIS_XML_DICOM_SR_FILE xmlDsr)
			throws Exception {
		super(session, idCenter, xmlDsr.getIDOntology());
		this.idOntology = xmlDsr.getIDOntology();
		this.idTrencadisReport = xmlDsr.getIDTRENCADISReport();
	}

	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the Storage DICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param idCenter Identifier of the hospital replica
	 * @param idOntology The report type of the DICOM-SR to download
	 * @param idTrencadisReport The report identifier of the DICOM-SR to download
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_DOWNLOAD(TRENCADIS_SESSION session, int idCenter,
			String idOntology, String idTrencadisReport)
			throws Exception {
		super(session, idCenter, idOntology);
		this.idOntology = idOntology;
		this.idTrencadisReport = idTrencadisReport;
	}
	
	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the Storage DICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param idOntology The report type of the DICOM-SR to download
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_DOWNLOAD(TRENCADIS_SESSION session, String idOntology)
			throws Exception {
		super(session, idOntology);
		this.idOntology = idOntology;
	}
	
	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the Storage DICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param idCenter Identifier of the hospital replica
	 * @param idOntology The report type of the DICOM-SR to download
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_DOWNLOAD(TRENCADIS_SESSION session, int idCenter, String idOntology)
			throws Exception {
		super(session, idCenter, idOntology);
		this.idOntology = idOntology;
	}

	/**
	 * This method starts the download, also returns the downloaded files in a Object
	 * 
	 * Each file is associated with the corresponding
	 * {@link trencadis.middleware.operations.TRENCADIS_XML_DICOM_SR_FILE
	 * TRENCADIS_XML_DICOM_SR_FILE}
	 * 
	 * @return Object The Object can be a {@link trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE
	 *         TRENCADIS_XML_DICOM_SR_FILE} or a
	 *         {@link trencadis.middleware.files.TRENCADIS_XML_DICOM_SR_FILE
	 *         TRENCADIS_XML_DICOM_SR_FILE} vector
	 * @throws Exception
	 */
	public Object execute() throws Exception {	
		
		if (idOntology != null && idTrencadisReport != null)
			return downloadReport();
		else {
			Vector<TRENCADIS_XML_DICOM_SR_FILE> retval = null;
			if (DICOMStorages == null && idOntology != null) {
				retval = downloadAllReportsByOntology(DICOMStorage);
			} else if (DICOMStorages == null && idOntology == null) {
				retval = downloadAllReports(DICOMStorage);
			} else if (DICOMStorages != null && idOntology != null) {
				retval = new Vector<TRENCADIS_XML_DICOM_SR_FILE>();
				for (DICOMStoragePortType dicomStorage : DICOMStorages)
					retval.addAll(downloadAllReportsByOntology(dicomStorage));
			} else if (DICOMStorages != null && idOntology == null) {
				retval = new Vector<TRENCADIS_XML_DICOM_SR_FILE>();
				for (DICOMStoragePortType dicomStorage : DICOMStorages)
					retval.addAll(downloadAllReports(dicomStorage));
			} else {
				throw new Exception("The parameters introduced are not invalid.");
			}
			return retval;	
		}		
		
	}
	
	private TRENCADIS_XML_DICOM_SR_FILE downloadReport() throws Exception{
		String xmlInputDownloadReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "<IDONTOLOGY>"
				+ idOntology
				+ "</IDONTOLOGY>"
				+ "<IDTRENCADISREPORT>"
				+ idTrencadisReport
				+ "</IDTRENCADISREPORT>\n" + "</INPUT>";

		String xmlOutputDownloadReport = DICOMStorage.xmlDownloadReport(xmlInputDownloadReport);

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadReport.XmlWrapper downloadReport_wrapper =
				new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadReport.XmlWrapper(xmlOutputDownloadReport, false);
		downloadReport_wrapper.wrap();
		FileUtils.writeStringToFile(new File("/home/locamo/example.txt"), xmlOutputDownloadReport);
		if (!downloadReport_wrapper.get_OUTPUT().get_STATUS().equals("0")) {
			throw new Exception("Can not download report: "
					+ downloadReport_wrapper.get_OUTPUT().get_DESCRIPTION());
		} else {
			String DICOM_SR = downloadReport_wrapper.get_OUTPUT().get_DICOM_SR().get_xml_DICOM_SR();
			TRENCADIS_XML_DICOM_SR_FILE dicom_sr = new TRENCADIS_XML_DICOM_SR_FILE(_session, DICOM_SR);
			return dicom_sr;
		}
	}
	
	private Vector<TRENCADIS_XML_DICOM_SR_FILE> downloadAllReports(DICOMStoragePortType DICOMStorage) throws Exception {
		Vector<TRENCADIS_XML_DICOM_SR_FILE> v_reports = null;
		String xmlInputDownloadReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "</INPUT>";	

		String xmlOutputDownloadReport = DICOMStorage.xmlDownloadAllReports(xmlInputDownloadReport);	
		
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReports.XmlWrapper downloadReport_wrapper = 
				new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReports.XmlWrapper(xmlOutputDownloadReport, false);
		downloadReport_wrapper.wrap();
		
		if (!downloadReport_wrapper.get_OUTPUT().get_STATUS().equals("0")) {
			throw new Exception("Can not download report: "
					+ downloadReport_wrapper.get_OUTPUT().get_DESCRIPTION());
		} else {
			
			Iterator it_reports = downloadReport_wrapper.get_OUTPUT().get_DICOM_REPORTS().getAll_DICOM_SR();
			v_reports = new Vector<TRENCADIS_XML_DICOM_SR_FILE>();
			while (it_reports.hasNext()){
				trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReports.DICOM_SR obj =
						(trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReports.DICOM_SR)it_reports.next();
				String dicom_sr = obj.getValue();
				TRENCADIS_XML_DICOM_SR_FILE xmldsr = new TRENCADIS_XML_DICOM_SR_FILE(_session, dicom_sr);
				v_reports.add(xmldsr);
			}			
			return v_reports;

		}
	}
	
	private Vector<TRENCADIS_XML_DICOM_SR_FILE> downloadAllReportsByOntology(DICOMStoragePortType DICOMStorage) throws Exception {
		Vector<TRENCADIS_XML_DICOM_SR_FILE> v_reports = null;
		String xmlInputDownloadReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "<IDONTOLOGY>"
				+ idOntology
				+ "</IDONTOLOGY>"
				+ "</INPUT>";	

		String xmlOutputDownloadReport = DICOMStorage.xmlDownloadAllReportsByOntology(xmlInputDownloadReport);	
		
		XmlWrapper downloadReport_wrapper = new XmlWrapper(xmlOutputDownloadReport, false);
		downloadReport_wrapper.wrap();
		
		if (!downloadReport_wrapper.get_OUTPUT().get_STATUS().equals("0")) {
			throw new Exception("Can not download report: "
					+ downloadReport_wrapper.get_OUTPUT().get_DESCRIPTION());
		} else {
			Iterator it_reports = downloadReport_wrapper.get_OUTPUT().get_DICOM_REPORTS().getAll_DICOM_SR();
			v_reports = new Vector<TRENCADIS_XML_DICOM_SR_FILE>();
			while (it_reports.hasNext()){
				trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReportsByOntology.DICOM_SR obj =
						(trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReportsByOntology.DICOM_SR)it_reports.next();
				String dicom_sr = obj.getValue();
				TRENCADIS_XML_DICOM_SR_FILE xmldsr = new TRENCADIS_XML_DICOM_SR_FILE(_session, dicom_sr);
				v_reports.add(xmldsr);
			}			
			return v_reports;
		}
	}

}
