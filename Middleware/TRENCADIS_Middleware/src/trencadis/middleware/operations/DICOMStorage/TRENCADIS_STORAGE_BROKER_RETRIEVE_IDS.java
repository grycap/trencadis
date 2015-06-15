package trencadis.middleware.operations.DICOMStorage;

import java.util.Vector;

import trencadis.infrastructure.services.stubs.DICOMStorage.DICOMStoragePortType;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This class is used to download an XML representation of a DICOM-SR from the
 * grid infrastructure
 */
public class TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS extends TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE {
	
	private Vector<TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE> v_dicomStorage = 
			new Vector<TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE>();
	
	
	/**
	 * This constructor retrieves all DICOM-SR identifiers from all centers
	 * and all ontologies.
	 * 
	 * @param session Session used by this operation
	 * @throws Exception
	 */
	public TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS(TRENCADIS_SESSION session)
			throws Exception {
		super(session);
		
		for (DICOMStoragePortType DICOMStorage : super.DICOMStorages) {
			this.v_dicomStorage.add(new TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE(session, DICOMStorage)); 
		} 
		
	}
	
	/**
	 * This constructor retrieves all DICOM-SR identifiers from all centers
	 * with a given ontology.
	 * 
	 * @param session Session used by this operation
	 * @param idOntology The report type of the DICOM-SR to download
	 * @throws Exception
	 */
	public TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS(TRENCADIS_SESSION session, String idOntology)
			throws Exception {
		super(session, idOntology);

		for (DICOMStoragePortType DICOMStorage : super.DICOMStorages) {
			this.v_dicomStorage.add(new TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE(session, DICOMStorage, idOntology)); 
		} 
	}
	
	/**
	 * This constructor retrieves all DICOM-SR identifiers of all ontologies
	 * from a given center.
	 * 
	 * @param session Session used by this operation
	 * @param id_center Identifier of the hospital replica
	 * @throws Exception
	 */
	public TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS(TRENCADIS_SESSION session, int idCenter)
			throws Exception {
		super(session, idCenter);
		this.v_dicomStorage.add(new TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE(session, super.DICOMStorage)); 
	}
	
	/**
	 * This constructor retrieves all DICOM-SR identifiers of a given center
	 * and a given ontology.
	 * 
	 * @param session Session used by this operation
	 * @param idCenter Identifier of the hospital replica
	 * @param idOntology The report type of the DICOM-SR to download
	 * @throws Exception
	 */
	public TRENCADIS_STORAGE_BROKER_RETRIEVE_IDS(TRENCADIS_SESSION session, int idCenter, String idOntology)
			throws Exception {
		super(session, idCenter, idOntology);
		this.v_dicomStorage.add(new TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE(session, super.DICOMStorage, idOntology)); 
	}	
	
	public Vector<TRENCADIS_RETRIEVE_IDS_FROM_DICOM_STORAGE> getDICOMStorageIDS() {
		return this.v_dicomStorage;
	}
	
}
