/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.middleware.operations.EOUIDGeneratorService;

import java.net.URI;
import java.util.Vector;

import org.apache.axis.message.MessageElement;
import org.globus.axis.message.addressing.Address;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;

import trencadis.infrastructure.services.stubs.EOUIDGenerator.EOUIDGeneratorPortType;
import trencadis.infrastructure.services.stubs.EOUIDGenerator.service.EOUIDGeneratorServiceAddressingLocator;
import trencadis.middleware.login.TRENCADIS_SESSION;
import trencadis.middleware.operations.TRENCADIS_OPERATION_GENERIC;
import trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS.IIS_Query_RPs;

/**
 *
 * @author root
 */
public class TRENCADIS_OPERATION_EOUIDGENERATOR_SERVICE extends
		TRENCADIS_OPERATION_GENERIC {

	protected EOUIDGeneratorPortType EOUIDGeneratorService = null;

	public TRENCADIS_OPERATION_EOUIDGENERATOR_SERVICE(
			TRENCADIS_SESSION session, URI EOUIDGeneratorServiceURI)
			throws Exception {

		super(session);

		getserverPortType(EOUIDGeneratorServiceURI);

	}

	/**
	 * This constructor is used when we do not have the URI of the
	 * OntologiesServer and we have to ask for it to the IIS.
	 *
	 * @param session
	 *            Session used by this operation
	 * @throws Exception
	 */
	public TRENCADIS_OPERATION_EOUIDGENERATOR_SERVICE(TRENCADIS_SESSION session)
			throws Exception {

		super(session);

		boolean bConnect = false;

		IIS_Query_RPs query_iis = new IIS_Query_RPs();
		String strxPathQuery = "//*[namespace-uri()='http://services.infrastructure.trencadis/EOUIDGenerator'"
					+ " and local-name()='EOUIDGeneratorServiceInfo']//*[local-name()='URI']";
		
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
					"All URIs of EOUIDGeneratorService published in the central IIS are disconected");

	}

	protected void getserverPortType(URI _EOUIDGeneratorService)
			throws Exception {

		EOUIDGeneratorServiceAddressingLocator locator = new EOUIDGeneratorServiceAddressingLocator();
		// Create endpoint reference to service
		EndpointReferenceType endpoint = new EndpointReferenceType();
		endpoint.setAddress(new Address(_EOUIDGeneratorService.toString()));

		// Get PortType
		EOUIDGeneratorService = locator.getEOUIDGeneratorPortTypePort(endpoint);
		String xmlInput = "<INPUT>"
						+ "<CERTIFICATE>"
						+ _session.getX509VOMSCredential()
						+ "</CERTIFICATE>"
						+ "</INPUT>";
		String xmlOutput = EOUIDGeneratorService.xmlIsActive(xmlInput);
	}

}
