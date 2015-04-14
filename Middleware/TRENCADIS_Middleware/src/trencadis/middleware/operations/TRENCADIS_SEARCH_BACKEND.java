package trencadis.middleware.operations;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class is used to store the information of the search results of a particular backend.
 */
public class TRENCADIS_SEARCH_BACKEND {

	private URI _storageDICOMURI;
	private String _hospitalId;
	private String _reportType;

	private Vector<TRENCADIS_SEARCH_ELEMENT> _elements = new Vector<TRENCADIS_SEARCH_ELEMENT>();

	/**
	 * The constructor stores the backend information parameters in the class.
	 * 
	 * @param storageDICOMURI URI of the StorageDICOM service of this backend
	 * @param hospital_id Hospital identifier of this backend
	 * @param reportType The type of the DICOM-SR of the search
	 * @throws IOException
	 */
	TRENCADIS_SEARCH_BACKEND(URI storageDICOMURI, String hospital_id, String reportType) throws IOException {
		_storageDICOMURI = storageDICOMURI;
		_hospitalId = hospital_id;
		_reportType = reportType;
	}

	/**
	 * This method is only called by the operations in this package in order to add a new
	 * {@link trencadis.middleware.operations.TRENCADIS_SEARCH_ELEMENT TRENCADIS_SEARCH_ELEMENT} to the results.
	 * @param element The new {@link trencadis.middleware.operations.TRENCADIS_SEARCH_ELEMENT TRENCADIS_SEARCH_ELEMENT} to be added.
	 */
	void addElement(TRENCADIS_SEARCH_ELEMENT element) {
		_elements.add(element);
	}

	/**
	 * This method returns an {@link java.util.Iterator Iterator} of all the elements in the backend results.
	 * @return An {@link java.util.Iterator Iterator} with all the elements in the backend results
	 */
	public Iterator<TRENCADIS_SEARCH_ELEMENT> getElements() {
		return _elements.iterator();
	}

	/**
	 * This function returns a {@link trencadis.middleware.operations.TRENCADIS_SEARCH_ELEMENT TRENCADIS_SEARCH_ELEMENT}
	 * from the internal list of elements of this backend.
	 * 
	 * @param index The index of the element to be returned
	 * @return A {@link trencadis.middleware.operations.TRENCADIS_SEARCH_ELEMENT TRENCADIS_SEARCH_ELEMENT} according to the index
	 */
	public TRENCADIS_SEARCH_ELEMENT getElement(int index) {
		return _elements.get(index);
	}

	/**
	 * Getter method for the Storage DICOM URI
	 * @return The Storage DICOM URI
	 */
	public URI getStorageDICOMURI() {
		return _storageDICOMURI;
	}

	/**
	 * Getter method for the hospital id
	 * @return The hospital id
	 */
	public String getHospitalId() {
		return _hospitalId;
	}

	/**
	 * Getter method for the report type 
	 * @return The report type
	 */
	public String getReportType() {
		return _reportType;
	}

}
