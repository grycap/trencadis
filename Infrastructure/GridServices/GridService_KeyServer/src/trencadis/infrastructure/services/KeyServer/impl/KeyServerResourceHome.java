package trencadis.infrastructure.services.KeyServer.impl;

import org.globus.wsrf.Resource;
import org.globus.wsrf.impl.SingletonResourceHome;

public class KeyServerResourceHome extends SingletonResourceHome {

	public Resource findSingleton() {
		try {
			// Create a resource and initialize it.
			KeyServerResource keyServerResource = new KeyServerResource();
			keyServerResource.initialize();
                        
			return keyServerResource;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
}