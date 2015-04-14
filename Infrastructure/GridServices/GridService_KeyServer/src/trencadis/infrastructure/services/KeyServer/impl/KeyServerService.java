package trencadis.infrastructure.services.KeyServer.impl;

import org.globus.wsrf.ResourceContext;

import java.rmi.RemoteException;


public class KeyServerService {

	public KeyServerService() throws RemoteException {		
            //This calls the initialize method which creates the resource of this service.
            KeyServerResource KeyServerResource = getResource();              
            //If everythig goes fine, register with MDS
            KeyServerResource.IIS_Register();       
	}
	        
        private KeyServerResource getResource() throws RemoteException {

		Object resource = null;

		try {
			resource = ResourceContext.getResourceContext().getResource();
		} catch (Exception e) {
			throw new RemoteException("Unable to access resource.", e);
		}

		return (KeyServerResource) resource;
	}

	/* Implementation of store, retrieve, and update operations */
	public String xmlStore(String xmlInputStore) throws RemoteException {

		KeyServerResource keyServerResource = getResource();	
		return keyServerResource.xmlStore(xmlInputStore);
                
	}
        
	public String xmlRetrieve(String xmlInputRetrieve) throws RemoteException {

		KeyServerResource keyServerResource = getResource();	
		return keyServerResource.xmlRetrieve(xmlInputRetrieve);
                
	}
        
	public String xmlUpdate(String xmlInputUpdate) throws RemoteException {

		KeyServerResource keyServerResource = getResource();	
		return keyServerResource.xmlUpdate(xmlInputUpdate);
                
	}
                
	public String xmlSign(String xmlInputSign) throws RemoteException {

		KeyServerResource keyServerResource = getResource();	
		return keyServerResource.xmlSign(xmlInputSign);
	
	}	


}