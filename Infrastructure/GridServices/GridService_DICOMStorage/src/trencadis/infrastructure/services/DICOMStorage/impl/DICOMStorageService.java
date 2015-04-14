package trencadis.infrastructure.services.DICOMStorage.impl;

import java.rmi.RemoteException;

import org.globus.wsrf.ResourceContext;

public class DICOMStorageService {

	public DICOMStorageService() throws Exception {
		
	}

	private DICOMStorageResource getResource() throws RemoteException {

		Object resource = null;
		try {
			resource = ResourceContext.getResourceContext().getResource();
		} catch (Exception e) {
			throw new RemoteException("Unable to access resource.", e);
		}

		DICOMStorageResource storageDICOMResource = (DICOMStorageResource) resource;
		return storageDICOMResource;
	}
	
	public String xmlAddReport(String xmlInputAddReport) throws RemoteException {
                DICOMStorageResource storageDICOMResource = getResource();
                return storageDICOMResource.xmlAddReport(xmlInputAddReport);
	}

	public String xmlRemoveReport(String xmlInputRemoveReport) throws RemoteException {
		DICOMStorageResource storageDICOMResource = getResource();
		return storageDICOMResource.xmlRemoveReport(xmlInputRemoveReport);
	}

	public String xmlDownloadReport(String xmlInputDownloadReport) throws RemoteException {
		DICOMStorageResource storageDICOMResource = getResource();
		return storageDICOMResource.xmlDownloadReport(xmlInputDownloadReport);
	}
	
	public String xmlDownloadAllReports(String xmlInputDownloadReport) throws RemoteException {
		DICOMStorageResource storageDICOMResource = getResource();
		return storageDICOMResource.xmlDownloadAllReports(xmlInputDownloadReport);
	}
	
	public String xmlDownloadAllReportsByOntology(String xmlInputDownloadReport) throws RemoteException {
		DICOMStorageResource storageDICOMResource = getResource();
		return storageDICOMResource.xmlDownloadAllReportsByOntology(xmlInputDownloadReport);
	}
	
	public String xmlDownloadAllReportsID(String xmlInputDownloadReport) throws RemoteException {
		DICOMStorageResource storageDICOMResource = getResource();
		return storageDICOMResource.xmlDownloadAllReportsID(xmlInputDownloadReport);
	}
	
	public String xmlDownloadAllReportsIDByOntology(String xmlInputDownloadReport) throws RemoteException {
		DICOMStorageResource storageDICOMResource = getResource();
		return storageDICOMResource.xmlDownloadAllReportsIDByOntology(xmlInputDownloadReport);
	}
	
	public String xmlMetadataQuery(String xmlInputMetadataQuery) throws RemoteException {

		DICOMStorageResource storageDICOMResource = getResource();
		return storageDICOMResource.xmlMetadataQuery(xmlInputMetadataQuery);

	}

	public String xmlUnregisterOntology(String xmlInputUnregisterOntology) throws RemoteException {
		DICOMStorageResource storageDICOMResource = getResource();		
		return storageDICOMResource.xmlUnregisterOntology(xmlInputUnregisterOntology);
	}
         public String xmlIsActive(String  xmlInputIsActive) throws RemoteException {                
                DICOMStorageResource storageDICOMResource = getResource();                
                return storageDICOMResource.xmlIsActive(xmlInputIsActive);                
                                
	}

}