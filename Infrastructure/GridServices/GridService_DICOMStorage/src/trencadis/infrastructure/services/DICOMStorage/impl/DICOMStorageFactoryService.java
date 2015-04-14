package trencadis.infrastructure.services.DICOMStorage.impl;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.axis.MessageContext;
import org.apache.axis.message.MessageElement;
import org.globus.axis.message.addressing.Address;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.container.ServiceHost;
import org.globus.wsrf.utils.AddressingUtils;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;

import trencadis.infrastructure.gateKeeper.credentials.HostCredentialHandler;
import trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS.IIS_Query_RPs;
import trencadis.infrastructure.services.stubs.OntologiesServer.OntologiesServerPortType;
import trencadis.infrastructure.services.stubs.OntologiesServer.service.OntologiesServerServiceAddressingLocator;
import trencadis.infrastructure.services.stubs.DICOMStorageFactory.CreateResourceResponse;

public class DICOMStorageFactoryService {

	// It indicates if is in debug mode
	public static boolean bIsDebug = false;

	public DICOMStorageFactoryService() throws Exception {

		File configFile = new File(
				"/home/trencadis/DICOMStorage/DICOMStorageConfig.xml");
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.XmlWrapper configWrapper = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.XmlWrapper(
				configFile, false);
		configWrapper.wrap();

		Vector v_url_iis_central = new Vector();
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.URL_IIS URL_IIS;
		Iterator it_urls = configWrapper.get_CONFIGURATION()
				.get_INDEX_SERVICE_PARAMETERS().get_LIST_OF_URL_IIS()
				.getAll_URL_IIS();
		while (it_urls.hasNext()) {
			URL_IIS = (trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.URL_IIS) it_urls
					.next();
			v_url_iis_central.add(URL_IIS.getValue());
		}

		// credenciales de Host
		HostCredentialHandler hostCredentialHandler = new HostCredentialHandler(
				configWrapper.get_CONFIGURATION().get_SERVICE_PARAMETERS()
						.get_TMP_DIR(), "StorageDICOMFactory");

		// ESTAS CREDENCIALES DEBERIAN SER DEL SERVIDOR VOMS Y NO DE HOST
		// hostCredentialHandler.createHostVOMSProxy(this.vo);
		hostCredentialHandler.createHostGridProxy();
		String HOST_CREDENTIAL = hostCredentialHandler.getPlainTextCredential();

		OntologiesServerPortType ontologiesServer = getOntologiesServerPortType(
				v_url_iis_central, HOST_CREDENTIAL);

		String xmlInputGetAllOntologies_OntologiesServer = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<INPUT>\n"
				+ "\t<CERTIFICATE>"
				+ HOST_CREDENTIAL
				+ "</CERTIFICATE>\n" + "</INPUT>";
		String xmlOutputGetAllOntologies_OntologiesServer = ontologiesServer
				.xmlGetAllOntologies(xmlInputGetAllOntologies_OntologiesServer);

		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputGetAllOntologies.XmlWrapper allOntologies_wrapper = new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputGetAllOntologies.XmlWrapper(
				xmlOutputGetAllOntologies_OntologiesServer, false);

		allOntologies_wrapper.wrap();

		if (allOntologies_wrapper.get_OUTPUT().get_STATUS().compareTo("-1") == 0) {
			throw new Exception(
					"Can not retrieve ontologiesList from OntologiesServer: "
							+ allOntologies_wrapper.get_OUTPUT()
									.get_DESCRIPTION());
		}

		/************** RECUPERA TODAS LAS ONTOLOGIAS Y CREO UN RECURSO POR ONTOLOGIA */
		// Create a resource for each type of report in the OntologiesServer
		Iterator<?> filterIterator = allOntologies_wrapper.get_OUTPUT()
				.get_ONTOLOGIES().getAll_ONTOLOGY();

		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputGetAllOntologies.ONTOLOGY filter = null;

		while (filterIterator.hasNext()) {

			filter = (trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputGetAllOntologies.ONTOLOGY) filterIterator
					.next();

			String xmlInputCreateResource = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<INPUT>\n"
					+ "<CERTIFICATE>"
					+ HOST_CREDENTIAL
					+ "</CERTIFICATE>\n"
					+ "<FILTER>"
					+ filter.get_xml_DICOM_SR_Template()
					+ "</FILTER>\n"
					+ "<FACTORY_RESOURCE>false</FACTORY_RESOURCE>\n"
					+ "</INPUT>";

			this.createResource(xmlInputCreateResource);
		}
		/******************************************************************************/
	}

	private OntologiesServerPortType getOntologiesServerPortType(
			Vector v_url_iis_central, String strCertificate) throws Exception {

		OntologiesServerPortType ontologiesserver = null;

		IIS_Query_RPs query_iis = new IIS_Query_RPs();
		String strxPathQuery = "//*[namespace-uri()='http://services.infrastructure.trencadis/OntologiesServer' and local-name()='OntologiesServerServiceInfo']//*[local-name()='URI']";

		QueryResourcePropertiesResponse response = query_iis.QueryRPs(
				v_url_iis_central, strxPathQuery);
		if (response == null) {
			throw new Exception(query_iis.strGetError());
		}

		MessageElement[] responseContents = response.get_any();

		boolean bConnect = false;
		for (int i = 0; (i < responseContents.length) && (!bConnect); i++) {
			try {
				ontologiesserver = getserverPortType(new URI(
						responseContents[i].getValue()), strCertificate);
				bConnect = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (!bConnect)
			throw new Exception(
					"All URIs of OntologiesServerService published in the central IIS are disconected");

		return ontologiesserver;

	}

	protected OntologiesServerPortType getserverPortType(
			URI _ontologiesServerURI, String strCertificate) throws Exception {
		OntologiesServerServiceAddressingLocator locator = new OntologiesServerServiceAddressingLocator();
		// Create endpoint reference to service
		EndpointReferenceType endpoint = new EndpointReferenceType();
		endpoint.setAddress(new Address(_ontologiesServerURI.toString()));

		// Get PortType
		OntologiesServerPortType ontologiesServer = locator.getOntologiesServerPortTypePort(endpoint);
		String xmlInput = "<INPUT>" + "<CERTIFICATE>" + strCertificate
				+ "</CERTIFICATE>" + "</INPUT>";
		ontologiesServer.xmlIsActive(xmlInput);

		return ontologiesServer;
	}

	/* Implementation of createResource Operation */
	public CreateResourceResponse createResource(String xmlInputCreateResource)
			throws Exception {

		ResourceContext ctx = null;
		DICOMStorageResourceHome home = null;
		ResourceKey key = null;
		String URIFactory = "";
		String baseURIresource = "";

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputCreateResource.XmlWrapper inputWrapper = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputCreateResource.XmlWrapper(
				xmlInputCreateResource, false);
		inputWrapper.wrap();

		String IDOntology = null;
		String ontologyDescription = null;
		String xmlOntology = null;

		String FILTER = inputWrapper
				.get_INPUT()
				.get_FILTER()
				.get_DICOM_SR()
				.str_to_XML()
				.replaceFirst("..xml version=\"1.0\" encoding=\"UTF-8\"..\n",
						"");

		IDOntology = inputWrapper.get_INPUT().get_FILTER().get_DICOM_SR()
				.getAttr_IDOntology();
		ontologyDescription = inputWrapper.get_INPUT().get_FILTER()
				.get_DICOM_SR().getAttr_Description();
		xmlOntology = FILTER;

		ctx = ResourceContext.getResourceContext();

		home = (DICOMStorageResourceHome) ctx.getResourceHome();

		if (!bIsDebug) {
			// URIS needed at resource creation time
			EndpointReferenceType storageDIMCOMFactoryEPR = AddressingUtils
					.createEndpointReference(
							ResourceContext.getResourceContext(), null);
			URIFactory = storageDIMCOMFactoryEPR.getAddress().toString();
			baseURIresource = URIFactory.replace("Factory", "");

		}

		key = home.create(inputWrapper.get_INPUT().get_CERTIFICATE(),
				baseURIresource, URIFactory, IDOntology, ontologyDescription,
				xmlOntology);

		// Creo el EndPoint Reference a devolver
		EndpointReferenceType epr = null;
		try {
			URL baseURL = ServiceHost.getBaseURL();
			String instanceService = (String) MessageContext
					.getCurrentContext().getService().getOption("instance");
			String instanceURI = baseURL.toString() + instanceService;
			// The endpoint reference includes the instance's URI and the
			// resource key
			epr = AddressingUtils.createEndpointReference(instanceURI, key);
		} catch (Exception e) {
			throw new RemoteException("", e);
		}

		/* Finally, return the endpoint reference in a CreateResourceResponse */
		CreateResourceResponse response = new CreateResourceResponse();
		response.setEndpointReference(epr);

		return response;

	}

	public String updateResources(String xmlInputUpdateResources)
			throws Exception {

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputUpdateResources.XmlWrapper updateResourcesWrapper = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputUpdateResources.XmlWrapper(
				xmlInputUpdateResources, false);
		updateResourcesWrapper.wrap();

		String xmlOutputUpdateResources = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		// TO DO

		return xmlOutputUpdateResources;
	}

}
