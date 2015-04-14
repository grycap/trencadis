package trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS;

import java.net.URL;

import org.apache.axis.MessageContext;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.globus.mds.servicegroup.client.ServiceGroupRegistrationParameters;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.container.ServiceHost;
import org.globus.wsrf.impl.servicegroup.client.ServiceGroupRegistrationClient;
import org.globus.wsrf.utils.AddressingUtils;


/**
 *
 * @author root
 */
public class IIS_Register {

	private String strError = "";

	public String strGetError() {
		return strError;
	}

	public void register(String strPathxmlRegistrationFile) throws Exception {
		ResourceContext ctx = null;
		try {
			ctx = ResourceContext.getResourceContext();
		} catch (ResourceContextException e) {
			strError = e.toString();
			e.printStackTrace();
		}

		EndpointReferenceType endpoint = null;
		try {
			endpoint = AddressingUtils.createEndpointReference(ctx, null);
		} catch (Exception e) {
			strError = e.toString();
			e.printStackTrace();
		}

		try {
			ServiceGroupRegistrationParameters params = ServiceGroupRegistrationClient
					.readParams(strPathxmlRegistrationFile);

			params.setRegistrantEPR(endpoint);

			ServiceGroupRegistrationClient client = ServiceGroupRegistrationClient
					.getContainerClient();

			client.register(params);

		} catch (Exception e) {
			strError = e.toString();
			e.printStackTrace();
		}
	}

	public void register(String strPathxmlRegistrationFile, ResourceKey key)
			throws Exception {

		EndpointReferenceType endpoint = null;
		try {

			URL baseURL = ServiceHost.getBaseURL();
			String instanceService = (String) MessageContext
					.getCurrentContext().getService().getOption("instance");
			String instanceURI = baseURL.toString() + instanceService;
			// The endpoint reference includes the instance's URI and the
			// resource key
			System.out.println("URI IIS " + instanceURI);
			endpoint = AddressingUtils
					.createEndpointReference(instanceURI, key);

		} catch (Exception e) {
			strError = e.toString();
			e.printStackTrace();
		}

		try {
			ServiceGroupRegistrationParameters params = ServiceGroupRegistrationClient
					.readParams(strPathxmlRegistrationFile);

			params.setRegistrantEPR(endpoint);

			ServiceGroupRegistrationClient client = new ServiceGroupRegistrationClient();

			client.register(params);

		} catch (Exception e) {
			strError = e.toString();
			e.printStackTrace();
		}
	}

}
