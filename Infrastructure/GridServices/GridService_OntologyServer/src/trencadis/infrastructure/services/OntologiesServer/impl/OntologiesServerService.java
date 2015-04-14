package trencadis.infrastructure.services.OntologiesServer.impl;

import org.globus.wsrf.ResourceContext;

import java.rmi.RemoteException;

public class OntologiesServerService {

	public OntologiesServerService() throws Exception {

		// This calls the initialize method which creates the resource of this
		// service.
		OntologiesServerResource ontologiesServerResource = getResource();
		// If everythig goes fine, register with MDS
		ontologiesServerResource.IIS_Register();

	}

	private OntologiesServerResource getResource() throws RemoteException {

		Object resource = null;

		try {
			resource = ResourceContext.getResourceContext().getResource();
		} catch (Exception e) {
			throw new RemoteException("Unable to access resource.", e);
		}

		return (OntologiesServerResource) resource;
	}

	/* Service Implementation */
	public String xmlCreateOntology(String xmlInputCreateOntology)
			throws RemoteException {

		OntologiesServerResource ontologiesServerResource = getResource();
		return ontologiesServerResource.xmlCreateOntology(xmlInputCreateOntology);

	}

	public String xmlRemoveOntology(String xmlInputRemoveOntology)
			throws RemoteException {

		OntologiesServerResource ontologiesServerResource = getResource();
		return ontologiesServerResource.xmlRemoveOntology(xmlInputRemoveOntology);

	}

	public String xmlGetOntology(String xmlInputGetOntology)
			throws RemoteException {

		OntologiesServerResource ontologiesServerResource = getResource();
		return ontologiesServerResource.xmlGetOntology(xmlInputGetOntology);

	}

	public String xmlGetAllOntologies(String xmlInputGetAllOntologies)
			throws RemoteException {

		OntologiesServerResource ontologiesServerResource = getResource();
		return ontologiesServerResource.xmlGetAllOntologies(xmlInputGetAllOntologies);

	}

	public String xmlIsActive(String xmlInputIsActive) throws RemoteException {

		OntologiesServerResource ontologiesServerResource = getResource();
		return ontologiesServerResource.xmlIsActive(xmlInputIsActive);

	}

	public String xmlUpdateStorageServices(String xmlInputUpdateStorageServices)
			throws RemoteException {
		return "";
	}

	/*
	 * public String xmlUpdateStorageServices(String
	 * xmlInputUpdateStorageServices) throws RemoteException {
	 * 
	 * trencadis.infrastructure.services.OntologiesServer.impl.wrapper.
	 * xmlInputUpdateStorageServices.XmlWrapper inputWrapper = new
	 * trencadis.infrastructure
	 * .services.OntologiesServer.impl.wrapper.xmlInputUpdateStorageServices
	 * .XmlWrapper(xmlInputUpdateStorageServices, false); inputWrapper.wrap();
	 * 
	 * String xmlOutputUpdateStorageServices =
	 * "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	 * 
	 * try {
	 * 
	 * String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE(); String
	 * gkResult = gateKeeper.strAllowAccess(CERTIFICATE); if
	 * (!gkResult.contentEquals("OK")) throw new
	 * Exception("Connecting to gatekeeper: " + gkResult);
	 * 
	 * synchronized(this.updateStorageServices) {
	 * this.updateStorageServices.notify(); }
	 * 
	 * } catch (Exception e) { logger.log(Level.ALL,
	 * "Error removing ontology: ", e);
	 * 
	 * xmlOutputUpdateStorageServices += "<OUTPUT>\n";
	 * xmlOutputUpdateStorageServices += "\t<STATUS>-1</STATUS>\n";
	 * xmlOutputUpdateStorageServices += "\t<DESCRIPTION>\n\t" + e.getMessage()
	 * + "\t</DESCRIPTION>\n"; xmlOutputUpdateStorageServices += "</OUTPUT>\n";
	 * 
	 * return xmlOutputUpdateStorageServices; }
	 * 
	 * xmlOutputUpdateStorageServices += "<OUTPUT>\n";
	 * xmlOutputUpdateStorageServices += "\t<STATUS>0</STATUS>\n";
	 * xmlOutputUpdateStorageServices += "\t<DESCRIPTION>OK</DESCRIPTION>\n";
	 * xmlOutputUpdateStorageServices += "</OUTPUT>\n";
	 * 
	 * logger.log(Level.ALL, "-> OntologiesServer.xmlRemoveOntology()");
	 * 
	 * return xmlOutputUpdateStorageServices; }
	 * 
	 * 
	 * private class UpdateStorageServices extends Thread {
	 * 
	 * public boolean stop = false;
	 * 
	 * public void run() {
	 * 
	 * while(!stop) {
	 * 
	 * try {
	 * 
	 * synchronized(this) { try { this.wait(86400000); } catch
	 * (InterruptedException ie) {} update(); }
	 * 
	 * } catch (Exception e) { logger.log(Level.ALL,
	 * "Error updating storage services: ", e); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * private void update() throws Exception {
	 * 
	 * HostCredentialHandler hostCredentialHandler = new
	 * HostCredentialHandler(tmp_dir, "OntologiesServer");
	 * //hostCredentialHandler.createHostVOMSProxy(this.vo);
	 * hostCredentialHandler.createHostGridProxy(); String HOST_CREDENTIAL =
	 * hostCredentialHandler.getPlainTextCredential();
	 * 
	 * WSResourcePropertiesServiceAddressingLocator locator_query = new
	 * WSResourcePropertiesServiceAddressingLocator(); EndpointReferenceType epr
	 * = new EndpointReferenceType(new Address(url_iis_central));
	 * QueryResourceProperties_PortType port =
	 * locator_query.getQueryResourcePropertiesPort(epr);
	 * 
	 * //Get StorageDICOMFactory port type QueryExpressionType
	 * query_storageDICOM = new QueryExpressionType(); String
	 * expression_storageDICOM =
	 * "//*[namespace-uri()='http://services.infrastructure.trencadis/StorageDICOM' and local-name()='DSRType' and string()='StorageDICOMFactory URI']/../*[namespace-uri()='http://services.infrastructure.trencadis/StorageDICOM' and local-name()='Active']"
	 * ; query_storageDICOM.setDialect(WSRFConstants.XPATH_1_DIALECT);
	 * query_storageDICOM.setValue(expression_storageDICOM);
	 * 
	 * QueryResourceProperties_Element request_storageDICOM = new
	 * QueryResourceProperties_Element();
	 * request_storageDICOM.setQueryExpression(query_storageDICOM);
	 * QueryResourcePropertiesResponse response_storageDICOM =
	 * port.queryResourceProperties(request_storageDICOM);
	 * 
	 * MessageElement[] responseContents_storageDICOM =
	 * response_storageDICOM.get_any();
	 * 
	 * //Get StorageBrokerFactory port type QueryExpressionType
	 * query_storageBroker = new QueryExpressionType();
	 * 
	 * String expression_storageBroker =
	 * "//*[namespace-uri()='http://services.infrastructure.trencadis/StorageBroker' and local-name()='DSRType' and string()='StorageBrokerFactory URI']/../*[namespace-uri()='http://services.infrastructure.trencadis/StorageBroker' and local-name()='Active']"
	 * ; query_storageBroker.setDialect(WSRFConstants.XPATH_1_DIALECT);
	 * query_storageBroker.setValue(expression_storageBroker);
	 * 
	 * QueryResourceProperties_Element request_storageBroker = new
	 * QueryResourceProperties_Element();
	 * request_storageBroker.setQueryExpression(query_storageBroker);
	 * QueryResourcePropertiesResponse response_storageBroker =
	 * port.queryResourceProperties(request_storageBroker);
	 * 
	 * MessageElement[] responseContents_storageBroker =
	 * response_storageBroker.get_any();
	 * 
	 * //Create update parameters String xmlInputUpdateResources =
	 * "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	 * 
	 * if (responseContents_storageDICOM != null ||
	 * responseContents_storageBroker != null) {
	 * 
	 * String xmlInputGetAllOntologies =
	 * "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<INPUT>\n" +
	 * "<CERTIFICATE>" + HOST_CREDENTIAL + "</CERTIFICATE>\n" + "</INPUT>";
	 * 
	 * String xmlOutputGetAllOntologies =
	 * xmlGetAllOntologies(xmlInputGetAllOntologies);
	 * 
	 * trencadis.infrastructure.services.OntologiesServer.impl.wrapper.
	 * xmlOutputGetAllOntologies.XmlWrapper allOntologies_wrapper = new
	 * trencadis.infrastructure.services.OntologiesServer.impl.wrapper.
	 * xmlOutputGetAllOntologies.XmlWrapper(xmlOutputGetAllOntologies, false);
	 * allOntologies_wrapper.wrap();
	 * 
	 * if (allOntologies_wrapper.get_OUTPUT().get_STATUS().compareTo("-1") == 0)
	 * { throw new Exception("Can not retrieve ontologiesList: " +
	 * allOntologies_wrapper.get_OUTPUT().get_DESCRIPTION()); }
	 * 
	 * xmlInputUpdateResources += "<INPUT>\n" + "<CERTIFICATE>" +
	 * HOST_CREDENTIAL + "</CERTIFICATE>\n";
	 * 
	 * Iterator<?> filterIterator =
	 * allOntologies_wrapper.get_OUTPUT().getAll_FILTER();
	 * trencadis.infrastructure
	 * .services.OntologiesServer.impl.wrapper.xmlOutputGetAllOntologies.FILTER
	 * filter = null;
	 * 
	 * while(filterIterator.hasNext()) { filter =
	 * (trencadis.infrastructure.services
	 * .OntologiesServer.impl.wrapper.xmlOutputGetAllOntologies.FILTER)
	 * filterIterator.next();
	 * 
	 * String FILTER = filter.get_DICOM_SR().str_to_XML().replaceFirst(
	 * "..xml version=\"1.0\" encoding=\"UTF-8\"..\n", "" );
	 * 
	 * xmlInputUpdateResources += "<FILTER>" + FILTER + "</FILTER>\n"; }
	 * 
	 * xmlInputUpdateResources += "</INPUT>\n";
	 * 
	 * }
	 * 
	 * //Update StorageDICOM Services Vector<UpdateStorageDICOMThread>
	 * updateStorageDICOMThreads = new Vector<UpdateStorageDICOMThread>();
	 * Object updateStorageDICOM_monitor = new Object(); int
	 * updateStorageDICOM_count = 0;
	 * 
	 * if (responseContents_storageDICOM == null) { logger.log(Level.ALL,
	 * "No URI of the StorageDICOMFactory is published in the central MDS"); }
	 * else {
	 * 
	 * StorageDICOMFactoryServiceAddressingLocator locator_storageDICOMFactory =
	 * new StorageDICOMFactoryServiceAddressingLocator(); ArrayList<String>
	 * created = new ArrayList<String>(); boolean next;
	 * 
	 * for(int i=0; i<responseContents_storageDICOM.length; i++) { next = false;
	 * String strStorageDICOMFactoryURL =
	 * responseContents_storageDICOM[i].getValue();
	 * 
	 * for (int j=0; j < created.size(); j++) { if
	 * (strStorageDICOMFactoryURL.contentEquals(created.get(j))) { next = true;
	 * logger.log(Level.ALL, "Ignored replicated entry: " +
	 * strStorageDICOMFactoryURL); break; } }
	 * 
	 * if (next) continue;
	 * 
	 * UpdateStorageDICOMThread updateStorageDICOMThread = new
	 * UpdateStorageDICOMThread( locator_storageDICOMFactory,
	 * strStorageDICOMFactoryURL, xmlInputUpdateResources,
	 * updateStorageDICOM_monitor, HOST_CREDENTIAL );
	 * 
	 * updateStorageDICOMThreads.add(updateStorageDICOMThread);
	 * updateStorageDICOMThread.start(); updateStorageDICOM_count++;
	 * 
	 * created.add(strStorageDICOMFactoryURL); }
	 * 
	 * }
	 * 
	 * //Update StorageBroker services Vector<UpdateStorageBrokerThread>
	 * updateStorageBrokerThreads = new Vector<UpdateStorageBrokerThread>();
	 * Object updateStorageBroker_monitor = new Object(); int
	 * updateStorageBroker_count = 0;
	 * 
	 * if (responseContents_storageBroker == null) { logger.log(Level.ALL,
	 * "No URI of the StorageBrokerFactory is published in the central MDS"); }
	 * else {
	 * 
	 * StorageBrokerFactoryServiceAddressingLocator locator_storageBrokerFactory
	 * = new StorageBrokerFactoryServiceAddressingLocator(); ArrayList<String>
	 * created = new ArrayList<String>(); boolean next;
	 * 
	 * for(int i=0; i<responseContents_storageBroker.length; i++) { next =
	 * false; String strStorageBrokerFactoryURL =
	 * responseContents_storageBroker[i].getValue();
	 * 
	 * for (int j=0; j < created.size(); j++) { if
	 * (strStorageBrokerFactoryURL.contentEquals(created.get(j))) { next = true;
	 * logger.log(Level.ALL, "Ignored replicated entry: " +
	 * strStorageBrokerFactoryURL); break; } }
	 * 
	 * if (next) continue;
	 * 
	 * UpdateStorageBrokerThread updateStorageBrokerThread = new
	 * UpdateStorageBrokerThread( locator_storageBrokerFactory,
	 * strStorageBrokerFactoryURL, xmlInputUpdateResources,
	 * updateStorageBroker_monitor, HOST_CREDENTIAL );
	 * 
	 * updateStorageBrokerThreads.add(updateStorageBrokerThread);
	 * updateStorageBrokerThread.start(); updateStorageBroker_count++;
	 * 
	 * created.add(strStorageBrokerFactoryURL); }
	 * 
	 * }
	 * 
	 * // Wait threads DICOM synchronized(updateStorageDICOM_monitor) {
	 * 
	 * int finished;
	 * 
	 * { Iterator<UpdateStorageDICOMThread> updateStorageDICOMThreads_it =
	 * updateStorageDICOMThreads.iterator(); UpdateStorageDICOMThread
	 * updateStorageDICOMThread_aux = null; finished = 0; while
	 * (updateStorageDICOMThreads_it.hasNext()) { updateStorageDICOMThread_aux =
	 * updateStorageDICOMThreads_it.next(); if
	 * (updateStorageDICOMThread_aux.finished) finished++; } }
	 * 
	 * while (updateStorageDICOM_count - finished > 0) {
	 * 
	 * try {updateStorageDICOM_monitor.wait();} catch (InterruptedException ie)
	 * {};
	 * 
	 * Iterator<UpdateStorageDICOMThread> updateStorageDICOMThreads_it =
	 * updateStorageDICOMThreads.iterator(); UpdateStorageDICOMThread
	 * updateStorageDICOMThread_aux = null; finished = 0; while
	 * (updateStorageDICOMThreads_it.hasNext()) { updateStorageDICOMThread_aux =
	 * updateStorageDICOMThreads_it.next(); if
	 * (updateStorageDICOMThread_aux.finished) finished++; }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * // Wait threads Broker synchronized(updateStorageBroker_monitor) {
	 * 
	 * int finished;
	 * 
	 * { Iterator<UpdateStorageBrokerThread> updateStorageBrokerThreads_it =
	 * updateStorageBrokerThreads.iterator(); UpdateStorageBrokerThread
	 * updateStorageBrokerThread_aux = null; finished = 0; while
	 * (updateStorageBrokerThreads_it.hasNext()) { updateStorageBrokerThread_aux
	 * = updateStorageBrokerThreads_it.next(); if
	 * (updateStorageBrokerThread_aux.finished) finished++; } }
	 * 
	 * while (updateStorageBroker_count - finished > 0) {
	 * 
	 * try {updateStorageBroker_monitor.wait();} catch (InterruptedException ie)
	 * {};
	 * 
	 * Iterator<UpdateStorageBrokerThread> updateStorageBrokerThreads_it =
	 * updateStorageBrokerThreads.iterator(); UpdateStorageBrokerThread
	 * updateStorageBrokerThread_aux = null; finished = 0; while
	 * (updateStorageBrokerThreads_it.hasNext()) { updateStorageBrokerThread_aux
	 * = updateStorageBrokerThreads_it.next(); if
	 * (updateStorageBrokerThread_aux.finished) finished++; }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * logger.log(Level.ALL, "-> OntologiesServer.UpdateStorageServices");
	 * 
	 * }
	 * 
	 * private class UpdateStorageDICOMThread extends Thread {
	 * 
	 * private StorageDICOMFactoryServiceAddressingLocator
	 * locator_storageDICOMFactory; private String strStorageDICOMFactoryURL;
	 * private String xmlInputUpdateResources; private Object
	 * updateStorageDICOM_monitor; private String CERTIFICATE;
	 * 
	 * private Boolean finished;
	 * 
	 * public UpdateStorageDICOMThread (
	 * StorageDICOMFactoryServiceAddressingLocator locator_storageDICOMFactory,
	 * String strStorageDICOMFactoryURL, String xmlInputUpdateResources, Object
	 * updateStorageDICOM_monitor, String CERTIFICATE ) {
	 * this.locator_storageDICOMFactory = locator_storageDICOMFactory;
	 * this.strStorageDICOMFactoryURL = strStorageDICOMFactoryURL;
	 * this.xmlInputUpdateResources = xmlInputUpdateResources;
	 * this.updateStorageDICOM_monitor = updateStorageDICOM_monitor;
	 * this.CERTIFICATE = CERTIFICATE;
	 * 
	 * this.finished = false; }
	 * 
	 * public void run() {
	 * 
	 * try {
	 * 
	 * StorageDICOMFactoryPortType storageDICOMFactoryPortType =
	 * locator_storageDICOMFactory.getStorageDICOMFactoryPortTypePort(new
	 * URL(strStorageDICOMFactoryURL)); String
	 * xmlOutputUpdateResources_StorageDICOM =
	 * storageDICOMFactoryPortType.updateResources(xmlInputUpdateResources);
	 * 
	 * trencadis.infrastructure.services.OntologiesServer.impl.wrapper.
	 * xmlOutputUpdateResources_StorageDICOM.XmlWrapper
	 * xmlOutputUnregisterOntology_StorageDICOM_wrapper = new
	 * trencadis.infrastructure
	 * .services.OntologiesServer.impl.wrapper.xmlOutputUpdateResources_StorageDICOM
	 * .XmlWrapper(xmlOutputUpdateResources_StorageDICOM, false);
	 * xmlOutputUnregisterOntology_StorageDICOM_wrapper.wrap();
	 * 
	 * if
	 * (xmlOutputUnregisterOntology_StorageDICOM_wrapper.get_OUTPUT().get_STATUS
	 * ().compareTo("-1") == 0) { throw new
	 * Exception(xmlOutputUnregisterOntology_StorageDICOM_wrapper
	 * .get_OUTPUT().get_DESCRIPTION()); } logger.log(Level.ALL,
	 * "---> Contacted StorageDICOMFactory: " + strStorageDICOMFactoryURL);
	 * 
	 * } catch (Exception e) { logger.log(Level.ALL,
	 * "---> Can not contact StorageDICOMFactory: " + strStorageDICOMFactoryURL,
	 * e); }
	 * 
	 * synchronized(updateStorageDICOM_monitor) { finished = true;
	 * updateStorageDICOM_monitor.notify(); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * private class UpdateStorageBrokerThread extends Thread {
	 * 
	 * private StorageBrokerFactoryServiceAddressingLocator
	 * locator_storageBrokerFactory; private String strStorageBrokerFactoryURL;
	 * private String xmlInputUpdateResources; private Object
	 * updateStorageBroker_monitor; private String CERTIFICATE;
	 * 
	 * private Boolean finished;
	 * 
	 * public UpdateStorageBrokerThread (
	 * StorageBrokerFactoryServiceAddressingLocator
	 * locator_storageBrokerFactory, String strStorageBrokerFactoryURL, String
	 * xmlInputUpdateResources, Object updateStorageBroker_monitor, String
	 * CERTIFICATE ){
	 * 
	 * this.locator_storageBrokerFactory = locator_storageBrokerFactory;
	 * this.strStorageBrokerFactoryURL = strStorageBrokerFactoryURL;
	 * this.xmlInputUpdateResources = xmlInputUpdateResources;
	 * this.updateStorageBroker_monitor = updateStorageBroker_monitor;
	 * this.CERTIFICATE = CERTIFICATE;
	 * 
	 * this.finished = false; }
	 * 
	 * public void run() {
	 * 
	 * try { StorageBrokerFactoryPortType storageBrokerFactoryPortType =
	 * locator_storageBrokerFactory.getStorageBrokerFactoryPortTypePort(new
	 * URL(strStorageBrokerFactoryURL)); String
	 * xmlOutputUpdateResources_StorageBroker =
	 * storageBrokerFactoryPortType.updateResources(xmlInputUpdateResources);
	 * 
	 * trencadis.infrastructure.services.OntologiesServer.impl.wrapper.
	 * xmlOutputUpdateResources_StorageBroker.XmlWrapper
	 * xmlOutputUnregisterOntology_StorageBroker_wrapper = new
	 * trencadis.infrastructure.services.OntologiesServer.impl.wrapper.
	 * xmlOutputUpdateResources_StorageBroker
	 * .XmlWrapper(xmlOutputUpdateResources_StorageBroker, false);
	 * xmlOutputUnregisterOntology_StorageBroker_wrapper.wrap();
	 * 
	 * if
	 * (xmlOutputUnregisterOntology_StorageBroker_wrapper.get_OUTPUT().get_STATUS
	 * ().compareTo("-1") == 0) { throw new
	 * Exception(xmlOutputUnregisterOntology_StorageBroker_wrapper
	 * .get_OUTPUT().get_DESCRIPTION()); } logger.log(Level.ALL,
	 * "---> Contacted StorageBrokerFactory: " + strStorageBrokerFactoryURL); }
	 * catch (Exception e) { logger.log(Level.ALL,
	 * "---> Can not contact StorageBrokerFactory: " +
	 * strStorageBrokerFactoryURL, e); }
	 * 
	 * synchronized(updateStorageBroker_monitor) { finished = true;
	 * updateStorageBroker_monitor.notify(); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 */

}
