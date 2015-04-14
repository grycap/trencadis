package trencadis.infrastructure.services.EOUIDGenerator.impl;

import org.globus.wsrf.Resource;
import org.globus.wsrf.impl.SingletonResourceHome;

public class EOUIDGeneratorResourceHome extends SingletonResourceHome {

	public Resource findSingleton() {
		try {
			// Create a resource and initialize it.
			EOUIDGeneratorResource uUIDResource = new EOUIDGeneratorResource();
			uUIDResource.initialize();
                                                
			return uUIDResource;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
}