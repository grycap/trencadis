package trencadis.middleware.operations.OntologiesServer;

import java.net.URI;
import java.util.Vector;

import org.apache.axis.message.MessageElement;
import org.globus.axis.message.addressing.Address;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;

import trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS.IIS_Query_RPs;
import trencadis.infrastructure.services.stubs.OntologiesServer.OntologiesServerPortType;
import trencadis.infrastructure.services.stubs.OntologiesServer.service.OntologiesServerServiceAddressingLocator;
import trencadis.middleware.login.TRENCADIS_SESSION;
import trencadis.middleware.operations.TRENCADIS_OPERATION_GENERIC;

/**
 * This is an abstract class for all the operations in this package. All
 * operations have a session.
 */
public abstract class TRENCADIS_OPERATION_ONTOLOGIESSERVER_SERVICE extends
		TRENCADIS_OPERATION_GENERIC {

	protected OntologiesServerPortType ontologiesServer = null;

	/**
	 * This constructor is used when we have the OntologiesServer URI and we do
	 * not need to ask for it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @param ontologiesServerURI The URI of the OntologiesServer service
	 * @throws Exception
	 */
	public TRENCADIS_OPERATION_ONTOLOGIESSERVER_SERVICE(
			TRENCADIS_SESSION session, URI ontologiesServerURI)
			throws Exception {
		super(session);

		getserverPortType(ontologiesServerURI);

	}

	/**
	 * This constructor is used when we do not have the URI of the
	 * OntologiesServer and we have to ask for it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @throws Exception
	 */
	public TRENCADIS_OPERATION_ONTOLOGIESSERVER_SERVICE(
			TRENCADIS_SESSION session) throws Exception {
		super(session);

		boolean bConnect;

		IIS_Query_RPs query_iis = new IIS_Query_RPs();
		String strxPathQuery = "//*[namespace-uri()='http://services.infrastructure.trencadis/OntologiesServer'"
					+ " and local-name()='OntologiesServerServiceInfo']//*[local-name()='URI']";

		Vector it_URLs_IIS = _session.getUrlIISCentral();
		QueryResourcePropertiesResponse response = query_iis.QueryRPs(
				it_URLs_IIS, strxPathQuery);
		if (response == null) {
			throw new Exception(query_iis.strGetError());
		}

		MessageElement[] responseContents = response.get_any();

		bConnect = false;
		for (int i = 0; (i < responseContents.length) && (!bConnect); i++) {
			try {
				getserverPortType(new URI(responseContents[i].getValue()));
				bConnect = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!bConnect)
			throw new Exception(
					"All URIs of OntologiesServerService published in the central IIS are disconected");

	}

	protected void getserverPortType(URI _ontologiesServerURI) throws Exception {
		OntologiesServerServiceAddressingLocator locator = new OntologiesServerServiceAddressingLocator();
		// Create endpoint reference to service
		EndpointReferenceType endpoint = new EndpointReferenceType();
		endpoint.setAddress(new Address(_ontologiesServerURI.toString()));

		// Get PortType
		ontologiesServer = locator.getOntologiesServerPortTypePort(endpoint);
		String xmlInput = "<INPUT>"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>"
				+ "</INPUT>";
		String xmlOutput = ontologiesServer.xmlIsActive(xmlInput);

	}

}
