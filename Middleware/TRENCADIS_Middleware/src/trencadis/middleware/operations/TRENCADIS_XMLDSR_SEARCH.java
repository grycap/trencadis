package trencadis.middleware.operations;

import java.net.URI;
import java.util.Iterator;

import trencadis.middleware.login.TRENCADIS_SESSION;

public class TRENCADIS_XMLDSR_SEARCH extends TRENCADIS_OPERATION_STORAGE_BROKER {

	private String _query;

	/**
	 * This constructor is used when we have the StorageBroker URI and we do not
	 * need to ask for it to the IIS.
	 *
	 * @param session
	 *            Session used by this operation
	 * @param storageBrokerURI
	 *            The URI of the StorageBroker service
	 * @param reportType
	 *            The kind of report upon which we perform operations
	 * @param query
	 *            The search to be performed
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_SEARCH(TRENCADIS_SESSION session,
			URI storageBrokerURI, String reportType, String query)
			throws Exception {
		super(session, storageBrokerURI, reportType);

		_query = query;
	}

	/**
	 * This constructor is used when we have the hospital identifier only and we
	 * need to ask the IIS for the StorageBroker URI.
	 * 
	 * @param session
	 *            Session used by this operation
	 * @param reportType
	 *            The kind of report upon which we perform operations
	 * @param query
	 *            The search to be performed
	 * @throws Exception
	 */
	public TRENCADIS_XMLDSR_SEARCH(TRENCADIS_SESSION session,
			String reportType, String query) throws Exception {
		super(session, reportType);

		_query = query;
	}

	/**
	 * This method starts a distributed metadata search in the TRENCADIS
	 * 
	 * @throws Exception
	 */
	public TRENCADIS_SEARCH_RESULTS execute() throws Exception {

		String xmlInputMultipleMetadataQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>\n"
				+ "<QUERY>" + _query + "</QUERY>\n" + "</INPUT>";

		String xmlOutputMultipleMetadataQuery = storageBroker
				.xmlMultipleMetadataQuery(xmlInputMultipleMetadataQuery);

		trencadis.middleware.wrapper.xmlOutputMultipleMetadataQuery_StorageBroker.XmlWrapper queryWrapper = new trencadis.middleware.wrapper.xmlOutputMultipleMetadataQuery_StorageBroker.XmlWrapper(
				xmlOutputMultipleMetadataQuery, false);
		queryWrapper.wrap();

		if (queryWrapper.get_OUTPUT().get_STATUS().compareTo("-1") == 0) {
			throw new Exception("Can not search in StorageBroker: "
					+ queryWrapper.get_OUTPUT().get_DESCRIPTION());
		} else {

			// Fill search results
			Iterator<?> backend_it = queryWrapper.get_OUTPUT().getAll_BACKEND();
			trencadis.middleware.wrapper.xmlOutputMultipleMetadataQuery_StorageBroker.BACKEND backend_aux = null;
			TRENCADIS_SEARCH_RESULTS search_results = new TRENCADIS_SEARCH_RESULTS();

			while (backend_it.hasNext()) {

				backend_aux = (trencadis.middleware.wrapper.xmlOutputMultipleMetadataQuery_StorageBroker.BACKEND) backend_it
						.next();
				TRENCADIS_SEARCH_BACKEND new_backend = new TRENCADIS_SEARCH_BACKEND(
						new URI(backend_aux.get_URI()),
						backend_aux.get_HOSPITAL(), backend_aux.get_DSR_TYPE());

				Iterator<?> element_it = backend_aux.getAll_ID();
				trencadis.middleware.wrapper.xmlOutputMultipleMetadataQuery_StorageBroker.ID element_aux = null;
				while (element_it.hasNext()) {

					element_aux = (trencadis.middleware.wrapper.xmlOutputMultipleMetadataQuery_StorageBroker.ID) element_it
							.next();
					TRENCADIS_SEARCH_ELEMENT new_element = new TRENCADIS_SEARCH_ELEMENT(
							element_aux.getValue());

					new_backend.addElement(new_element);

				}

				search_results.addBackend(new_backend);
			}

			return search_results;

		}

	}

}
