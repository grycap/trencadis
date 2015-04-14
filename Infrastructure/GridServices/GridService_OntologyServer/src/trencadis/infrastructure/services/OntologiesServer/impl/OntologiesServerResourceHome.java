package trencadis.infrastructure.services.OntologiesServer.impl;

import org.globus.wsrf.Resource;
import org.globus.wsrf.impl.SingletonResourceHome;

public class OntologiesServerResourceHome extends SingletonResourceHome {

	public Resource findSingleton() {

		try {
			//Create a resource and initialize it.
			OntologiesServerResource ontologiesServerResource = new OntologiesServerResource();
			ontologiesServerResource.initialize();                        
                        
			return ontologiesServerResource;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
