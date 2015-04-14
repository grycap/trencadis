package trencadis.infrastructure.services.EOUIDGenerator.impl.wrapper;

import java.rmi.RemoteException;


import org.globus.wsrf.ResourceContext;

public class EOUIDGeneratorService {
       	                         	

	public EOUIDGeneratorService() {

						                		
		try {                              
                    //This calls the initialize method which creates the "Active" resource of this service.
                    EOUIDGeneratorResource EOUIDGeneratorResource = getResource();              
                    //If everythig goes fine, register with MDS
                    EOUIDGeneratorResource.IIS_Register();
                                            
		} catch (Exception e) {
			e.printStackTrace();			
		}		
	}
        
	public String xmlGetNext(String  xmlInputGetNext) throws RemoteException {

                EOUIDGeneratorResource EOUIDGeneratorResource = getResource();
                return EOUIDGeneratorResource.xmlGetNext(xmlInputGetNext);
                                
	}
        
        
        public String xmlIsActive(String  xmlInputIsActive) throws RemoteException {

                EOUIDGeneratorResource EOUIDGeneratorResource = getResource();
                return EOUIDGeneratorResource.xmlIsActive(xmlInputIsActive);
                                
	}
        
        
        
        private EOUIDGeneratorResource getResource() throws RemoteException {

		Object resource = null;

		try {
			resource = ResourceContext.getResourceContext().getResource();
		} catch (Exception e) {
			throw new RemoteException("Unable to access resource.", e);
		}

		return (EOUIDGeneratorResource) resource;
	}
        
}
