package trencadis.middleware.operations;

import java.net.URI;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;
import org.globus.axis.message.addressing.Address;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.WSRFConstants;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.utils.AddressingUtils;
import org.oasis.wsrf.properties.QueryExpressionType;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;
import org.oasis.wsrf.properties.QueryResourceProperties_Element;
import org.oasis.wsrf.properties.QueryResourceProperties_PortType;
import org.oasis.wsrf.properties.WSResourcePropertiesServiceAddressingLocator;

import trencadis.infrastructure.services.stubs.StorageBroker.StorageBrokerPortType;
import trencadis.infrastructure.services.stubs.StorageBroker.service.StorageBrokerServiceAddressingLocator;
import trencadis.middleware.login.TRENCADIS_SESSION;

/**
 * This is an abstract class for all the operations in this package. All
 * operations have a session.
 */
public abstract class TRENCADIS_OPERATION_STORAGE_BROKER extends
		TRENCADIS_OPERATION_GENERIC {

	StorageBrokerPortType storageBroker = null;

	/**
	 * This constructor is used when we have the StorageBroker URI and we do not
	 * need to ask for it to the IIS.
	 *
	 * @param session
	 *            Session used by this operation
	 * @param storageBrokerURI
	 *            The URI of the OntologiesServer service
	 * @param reportType
	 *            The report type with which we work
	 * @throws Exception
	 */
	public TRENCADIS_OPERATION_STORAGE_BROKER(TRENCADIS_SESSION session,
			URI storageBrokerURI, String reportType) throws Exception {
		super(session);

		getserverPortType(storageBrokerURI, reportType);
	}

	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the StorageBroker URI.
	 * 
	 * @param session
	 *            Session used by this operation
	 * @param reportType
	 *            The report type with which we work
	 * @throws Exception
	 */
	public TRENCADIS_OPERATION_STORAGE_BROKER(TRENCADIS_SESSION session,
			String reportType) throws Exception {
		super(session);

		// Query the information service about the StorageBroker where we are
		// going to upload the file
		WSResourcePropertiesServiceAddressingLocator locator_query = new WSResourcePropertiesServiceAddressingLocator();
		EndpointReferenceType epr = new EndpointReferenceType(new Address(
				_session.getUrlIISCentral().get(0).toString()));
		QueryResourceProperties_PortType port = locator_query
				.getQueryResourcePropertiesPort(epr);

		// Get StorageBroker resources of all servers of this kind of report
		QueryExpressionType query_storageBroker = new QueryExpressionType();
		String expression_storageBroker = "//*[namespace-uri()='http://services.infrastructure.trencadis/StorageBroker' "
				+ "and local-name()='DSRType' and string()='"
				+ reportType
				+ "']/../*[namespace-uri()='http://services.infrastructure.trencadis/StorageBroker' "
				+ "and local-name()='Active']";
		query_storageBroker.setDialect(WSRFConstants.XPATH_1_DIALECT);
		query_storageBroker.setValue(expression_storageBroker);

		QueryResourceProperties_Element request_storageBroker = new QueryResourceProperties_Element();
		request_storageBroker.setQueryExpression(query_storageBroker);
		QueryResourcePropertiesResponse response_storageBroker = port
				.queryResourceProperties(request_storageBroker);

		MessageElement[] responseContents_storageBroker = response_storageBroker
				.get_any();

		if (responseContents_storageBroker == null
				|| responseContents_storageBroker.length < 1) {
			throw new Exception(
					"No URI of the StorageBroker is published in the central MDS for this kind of report");
		} else {
			getserverPortType(
					new URI(responseContents_storageBroker[0].getValue()),
					reportType);
		}

	}

	private void getserverPortType(URI _storageBrokerURI, String _reportType)
			throws Exception {

		QName storageBrokerKeyQName = new QName(
				"http://services.infrastructure.trencadis/StorageBroker",
				"StorageBrokerResourceKey");
		ResourceKey storageBrokerResourceKey = new SimpleResourceKey(storageBrokerKeyQName, _reportType);
		EndpointReferenceType storageBrokerResourceEPR = AddressingUtils
				.createEndpointReference(_storageBrokerURI.toString(),
						storageBrokerResourceKey);
		StorageBrokerServiceAddressingLocator locator_storageBroker = new StorageBrokerServiceAddressingLocator();
		storageBroker = locator_storageBroker
				.getStorageBrokerPortTypePort(storageBrokerResourceEPR);

	}

}
