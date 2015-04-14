package trencadis.infrastructure.services.StorageBroker.impl;

import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.impl.servicegroup.client.ServiceGroupRegistrationClient;

public class StorageBrokerResourceHome extends ResourceHomeImpl {

	public ResourceKey create(String URI, String ontologyTypeID, String ontologyStructure, String startupCredential, ServiceGroupRegistrationClient regClient) throws Exception {

		// Create a resource and initialize it
		StorageBrokerResource storageBrokerResource = (StorageBrokerResource) createNewInstance();
		storageBrokerResource.setValues(URI, ontologyTypeID, ontologyStructure, startupCredential, regClient);
		storageBrokerResource.initialize();

		// Add the resource to the list of resources in this home
		ResourceKey key = new SimpleResourceKey(keyTypeName, storageBrokerResource.getID());
		add(key, storageBrokerResource);
		return key;

	}

}
