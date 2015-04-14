package trencadis.middleware.operations.DICOMStorage;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReportsID.DICOM_SR_ID;
import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReportsIDByOntology.XmlWrapper;
import trencadis.infrastructure.services.stubs.DICOMStorage.DICOMStoragePortType;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This class is used to get DICOM-SR identifiers from a DICOM Storage
 * of grid infrastructure.
 */
public class TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE {
	
	private TRENCADIS_SESSION session = null;
	private DICOMStoragePortType DICOMStorage = null;
	private String idOntology = null;
	
	private String xml = null;
	private String center_name = null;
	private TRENCADIS_DICOM_STORAGE_BACKEND backend = null;
	private Vector<DICOM_SR_ID> dicom_sr_ids = null;
	
	/**
	 * This constructor obtain a list of DICOM-SR identifiers from a given hospital.
	 * 
	 * @param DICOMStorage The DICOMStorage from the DICOM-SR identifiers will be download
	 * @throws Exception
	 */
	public TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE(TRENCADIS_SESSION session,
			DICOMStoragePortType DICOMStorage) throws Exception {
		this.session = session;
		this.DICOMStorage = DICOMStorage;
		this.idOntology = null;
		this.execute();
	}
	
	/**
	 * This constructor obtain a list of DICOM-SR identifiers from a given hospital
	 * with a given ontology.
	 * 
	 * @param DICOMStorage The DICOMStorage from the DICOM-SR identifiers will be download
	 * @throws Exception
	 */
	public TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE(TRENCADIS_SESSION session,
			DICOMStoragePortType DICOMStorage, String idOntology) throws Exception {
		this.session = session;
		this.DICOMStorage = DICOMStorage;
		this.idOntology = idOntology;
		this.execute();
	}	
	
	public TRENCADIS_DICOM_STORAGE_BACKEND getBackend() {
		return this.backend;
	}
	
	public Vector<DICOM_SR_ID> getDICOM_DSR_IDS() {
		return this.dicom_sr_ids;
	}

	public String toXML() {
		return xml;
	}
	
	public String getCenterName() {
		return this.center_name;
	}
	
	/**
	 * This method starts the download of DICOM-SR identifiers
	 * 
	 * @throws Exception 
	 */
	private void execute() throws Exception{

		if (idOntology == null) {
			this.getAllIDs();
		} else 
			this.getAllIDsByOntology();

	}
	
	/**
	 * Gets the DICOM-SR identifiers with a given ontology.
	 * 
	 * @throws Exception
	 */
	private void getAllIDsByOntology() throws Exception {
		String xmlInputDownloadReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "<IDONTOLOGY>"
				+ idOntology
				+ "</IDONTOLOGY>\n"
				+ "</INPUT>\n";	

		String xmlOutputDownloadReport = DICOMStorage.xmlDownloadAllReportsIDByOntology(xmlInputDownloadReport);		
		
		XmlWrapper downloadReport_wrapper = new XmlWrapper(xmlOutputDownloadReport, false);
		downloadReport_wrapper.wrap();
		
		if (!downloadReport_wrapper.get_OUTPUT().get_STATUS().equals("0")) {
			throw new Exception("Can not download report: "
					+ downloadReport_wrapper.get_OUTPUT().get_DESCRIPTION());
		} else {
			
			xml = downloadReport_wrapper.get_OUTPUT().str_to_XML();
			
			center_name = downloadReport_wrapper.get_OUTPUT().get_CENTERNAME();
			
			backend = new TRENCADIS_DICOM_STORAGE_BACKEND(downloadReport_wrapper.get_OUTPUT().get_BACKEND_PARAMETERS());
			
			Iterator it = downloadReport_wrapper.get_OUTPUT().get_DICOM_REPORTS_ID().getAll_DICOM_SR_ID();
			dicom_sr_ids = new Vector<DICOM_SR_ID>();
			while(it.hasNext()) {
				DICOM_SR_ID id = (DICOM_SR_ID)it.next();
				dicom_sr_ids.add(id);
			}
		}
	}
	
	/**
	 * Gets all DICOM-SR identifiers from a given center.
	 * 
	 * @throws Exception
	 */
	private void getAllIDs() throws Exception {
		
		String xmlInputDownloadReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "</INPUT>";	

		String xmlOutputDownloadReport = DICOMStorage.xmlDownloadAllReportsID(xmlInputDownloadReport);		
		
		XmlWrapper downloadReport_wrapper = new XmlWrapper(xmlOutputDownloadReport, false);
		downloadReport_wrapper.wrap();
		
		if (!downloadReport_wrapper.get_OUTPUT().get_STATUS().equals("0")) {
			throw new Exception("Can not download report: "
					+ downloadReport_wrapper.get_OUTPUT().get_DESCRIPTION());
		} else {
			
			xml = downloadReport_wrapper.get_OUTPUT().str_to_XML();
			
			center_name = downloadReport_wrapper.get_OUTPUT().get_CENTERNAME();
			
			backend = new TRENCADIS_DICOM_STORAGE_BACKEND(downloadReport_wrapper.get_OUTPUT().get_BACKEND_PARAMETERS());
			
			Iterator it = downloadReport_wrapper.get_OUTPUT().get_DICOM_REPORTS_ID().getAll_DICOM_SR_ID();
			dicom_sr_ids = new Vector<DICOM_SR_ID>();
			while(it.hasNext()) {
				DICOM_SR_ID id = (DICOM_SR_ID) it.next();
				dicom_sr_ids.add(id);
			}

		}
		
	}
	
}
