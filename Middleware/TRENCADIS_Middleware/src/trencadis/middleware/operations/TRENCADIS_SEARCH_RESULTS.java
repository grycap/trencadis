package trencadis.middleware.operations;

import java.util.Iterator;
import java.util.Vector;

/**
 * This class is used to store the results of a search in a structured way.
 */
public class TRENCADIS_SEARCH_RESULTS {

	private Vector<TRENCADIS_SEARCH_BACKEND> _backends = new Vector<TRENCADIS_SEARCH_BACKEND>();

	/**
	 * This method is only called by the operations in this package in order to add a new
	 * {@link trencadis.middleware.operations.TRENCADIS_SEARCH_BACKEND TRENCADIS_SEARCH_BACKEND} to the results.
	 * @param backend The new {@link trencadis.middleware.operations.TRENCADIS_SEARCH_BACKEND TRENCADIS_SEARCH_BACKEND} to be added.
	 */
	void addBackend(TRENCADIS_SEARCH_BACKEND backend) {
		_backends.add(backend);
	}

	/**
	 * This method returns an {@link java.util.Iterator Iterator} of all the backends in the results.
	 * @return An {@link java.util.Iterator Iterator} with all the backends
	 */
	public Iterator<TRENCADIS_SEARCH_BACKEND> getBackends() {
		return _backends.iterator();
	}

	/**
	 * This function returns a {@link trencadis.middleware.operations.TRENCADIS_SEARCH_BACKEND TRENCADIS_SEARCH_BACKEND}
	 * from the internal list of backends of this class.
	 * 
	 * @param index The index of the backend to be returned
	 * @return A {@link trencadis.middleware.operations.TRENCADIS_SEARCH_BACKEND TRENCADIS_SEARCH_BACKEND} according to the index
	 */
	public TRENCADIS_SEARCH_BACKEND getBackend(int index) {
		return _backends.get(index);
	}

}
