package trencadis.infrastructure.services.StorageBroker.impl;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.File;
import java.net.URL;

import javax.xml.namespace.QName;

import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.WSRFConstants;
import org.globus.axis.message.addressing.Address;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.MessageContext;
import org.apache.axis.message.MessageElement;
import org.globus.wsrf.utils.AddressingUtils;
import org.globus.wsrf.container.ServiceHost;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.impl.servicegroup.client.ServiceGroupRegistrationClient;
import org.globus.mds.servicegroup.client.ServiceGroupRegistrationParameters;
import org.oasis.wsrf.lifetime.Destroy;
import org.oasis.wsrf.properties.QueryExpressionType;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;
import org.oasis.wsrf.properties.QueryResourceProperties_Element;
import org.oasis.wsrf.properties.QueryResourceProperties_PortType;
import org.oasis.wsrf.properties.WSResourcePropertiesServiceAddressingLocator;

import trencadis.infrastructure.gateKeeper.credentials.HostCredentialHandler;
import trencadis.infrastructure.gateKeeper.CGateKeeper;
import trencadis.infrastructure.services.stubs.OntologiesServer.OntologiesServerPortType;
import trencadis.infrastructure.services.stubs.OntologiesServer.service.OntologiesServerServiceAddressingLocator;
import trencadis.infrastructure.services.stubs.StorageBroker.StorageBrokerPortType;
import trencadis.infrastructure.services.stubs.StorageBroker.service.StorageBrokerServiceAddressingLocator;
import trencadis.infrastructure.services.stubs.StorageBrokerFactory.CreateResourceResponse;

public class StorageBrokerFactoryService {

	//Configuration variables
	//Local parameters
	private String base_path;
	private String tmp_dir;
	private String path_cattrustcert;

	//VO parameters
	private String vo;

	//IIS parameters
	private String url_iis_central;
	private String registration_path;

	//Gate keeper
	private String gk_host_name;
	private String gk_host_port;
	private String gk_db_name;
	private String gk_user;
	private String gk_password;
	private CGateKeeper gateKeeper;

	private String storageBrokerFactoryURI;
	private String storageBrokerURI;
	
	//Resource list management
	private Vector<String> _resources = new Vector<String>();
	private Vector<String> _last_resources;
	
	private Logger logger = Logger.getLogger(StorageBrokerFactoryService.class.getName());

	public StorageBrokerFactoryService () throws Exception {

		File configFile = new File("/usr/local/globus4/TRENCADIS/StorageBroker/storageBrokerConfig.xml");
		trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.XmlWrapper configWrapper =
			new trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.XmlWrapper(configFile, false);
		configWrapper.wrap();

		trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.LOCAL_PARAMETERS localParameters = configWrapper.get_CONFIGURATION().get_LOCAL_PARAMETERS();
		this.base_path = localParameters.get_BASE_PATH();
		this.path_cattrustcert = localParameters.get_PATH_CATTRUSTCERT();
		this.tmp_dir = localParameters.get_TMP_DIR();

		trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.LFC_PARAMETERS lfcParameters = configWrapper.get_CONFIGURATION().get_LFC_PARAMETERS();
		this.vo = lfcParameters.get_VO();

		trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.DATABASE_PARAMETERS gatekeeperParameters = configWrapper.get_CONFIGURATION().get_GATEKEEPER().get_DATABASE_PARAMETERS();
		this.gk_host_name = gatekeeperParameters.get_HOST_NAME();
		this.gk_host_port = gatekeeperParameters.get_HOST_PORT();
		this.gk_db_name = gatekeeperParameters.get_DB_NAME();
		this.gk_user = gatekeeperParameters.get_USER();
		this.gk_password = gatekeeperParameters.get_PASSWORD();

		trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.INDEX_SERVICE indexServiceParameters = configWrapper.get_CONFIGURATION().get_INDEX_SERVICE();
		this.url_iis_central = indexServiceParameters.get_URL_IIS_CENTRAL();
		this.registration_path = indexServiceParameters.get_REGISTRATION_PATH();

		//Initialize the gatekeeper
		this.gateKeeper = new CGateKeeper(
				"org.postgresql.Driver",
				"jdbc:storageBrokerFactoryGatekeeperPool:",
				"jdbc:postgresql://",
				this.gk_host_name,
				this.gk_host_port,
				this.gk_db_name,
				this.gk_user, this.gk_password,
				this.path_cattrustcert
		);

		//Logger setup
		FileHandler logFile = new FileHandler(this.tmp_dir + "/StorageBrokerFactory_log");
		logger.addHandler(logFile);
		logger.setLevel(Level.ALL);
		SimpleFormatter formatter = new SimpleFormatter();
		logFile.setFormatter(formatter);
		
		HostCredentialHandler hostCredentialHandler = new HostCredentialHandler(this.tmp_dir, "StorageBrokerFactory");
		//hostCredentialHandler.createHostVOMSProxy(this.vo);
		hostCredentialHandler.createHostGridProxy();
		String HOST_CREDENTIAL = hostCredentialHandler.getPlainTextCredential();

		OntologiesServerPortType ontologiesServer = getOntologiesServerPortType();
		
		String xmlInputGetAllOntologies_OntologiesServer =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<INPUT>\n" +
			"<CERTIFICATE>" +
			HOST_CREDENTIAL +
			"</CERTIFICATE>\n" +
			"</INPUT>";

		String xmlOutputGetAllOntologies_OntologiesServer = ontologiesServer.xmlGetAllOntologies(xmlInputGetAllOntologies_OntologiesServer);

		trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlOutputGetAllOntologies_OntologiesServer.XmlWrapper allOntologies_wrapper =
			new trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlOutputGetAllOntologies_OntologiesServer.XmlWrapper(xmlOutputGetAllOntologies_OntologiesServer, false);
		allOntologies_wrapper.wrap();

		if (allOntologies_wrapper.get_OUTPUT().get_STATUS().compareTo("-1") == 0) {
			throw new Exception("Can not retrieve ontologiesList from OntologiesServer: " + allOntologies_wrapper.get_OUTPUT().get_DESCRIPTION());
		}

		//URIS needed at resource creation time
		EndpointReferenceType storageBrokerFactoryEPR = AddressingUtils.createEndpointReference(ResourceContext.getResourceContext(), null);
		this.storageBrokerFactoryURI = storageBrokerFactoryEPR.getAddress().toString();
		this.storageBrokerURI = this.storageBrokerFactoryURI.replace("Factory", "");

		//Create a resource for each type of report in the OntologiesServer
		Iterator<?> filterIterator = allOntologies_wrapper.get_OUTPUT().getAll_FILTER();
		trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlOutputGetAllOntologies_OntologiesServer.FILTER filter = null;
		
		while(filterIterator.hasNext()) {
			filter = (trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlOutputGetAllOntologies_OntologiesServer.FILTER) filterIterator.next();

			String FILTER = filter.get_DICOM_SR().str_to_XML().replaceFirst(
                    "..xml version=\"1.0\" encoding=\"UTF-8\"..\n",
                    ""
            );

			String xmlInputCreateResource =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<INPUT>\n" +
				"<CERTIFICATE>" + HOST_CREDENTIAL +	"</CERTIFICATE>\n" +
				"<FILTER>" + FILTER + "</FILTER>\n" +
				"<FACTORY_RESOURCE>false</FACTORY_RESOURCE>\n" +
				"</INPUT>";

			this.createResource(xmlInputCreateResource);
			_resources.add(filter.get_DICOM_SR().getAttr_reportType());
		}

		//Publish in the local IIS the factory service
		String xmlInputCreateResource =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<INPUT>\n" +
			"<CERTIFICATE>" + HOST_CREDENTIAL +	"</CERTIFICATE>\n" +
			"<FILTER>None</FILTER>\n" +
			"<FACTORY_RESOURCE>true</FACTORY_RESOURCE>\n" +
			"</INPUT>";
		this.createResource(xmlInputCreateResource);

	}

	private OntologiesServerPortType getOntologiesServerPortType() throws Exception {

		WSResourcePropertiesServiceAddressingLocator locator_query = new WSResourcePropertiesServiceAddressingLocator();
		QueryExpressionType query = new QueryExpressionType();
		String dialect = WSRFConstants.XPATH_1_DIALECT;

		String expression = "//*[namespace-uri()='http://services.infrastructure.trencadis/OntologiesServer' and local-name()='Active']";

		query.setDialect(dialect);
		query.setValue(expression);

		EndpointReferenceType epr = new EndpointReferenceType(new Address(url_iis_central));
		QueryResourceProperties_PortType port = locator_query.getQueryResourcePropertiesPort(epr);
		QueryResourceProperties_Element request = new QueryResourceProperties_Element();
		request.setQueryExpression(query);
		QueryResourcePropertiesResponse response = port.queryResourceProperties(request);

		MessageElement[] responseContents = response.get_any();

		if (responseContents == null)
			throw new Exception("The URI of the OntologiesServer is not published in the central MDS: ");

		String strOntologyServerURL = responseContents[0].getValue();

		OntologiesServerServiceAddressingLocator locator_ontology = new  OntologiesServerServiceAddressingLocator();

		return locator_ontology.getOntologiesServerPortTypePort(new URL(strOntologyServerURL));

	}

	/* Implementation of createResource Operation */
	public CreateResourceResponse createResource(String xmlInputCreateResource) throws Exception {		
		ResourceContext ctx = null;
		StorageBrokerResourceHome home = null;
		ResourceKey key = null;

		trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlInputCreateResource.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlInputCreateResource.XmlWrapper(xmlInputCreateResource, false);
		inputWrapper.wrap();

		String gkResult = gateKeeper.strAllowAccess(inputWrapper.get_INPUT().get_CERTIFICATE());
		if (!gkResult.contentEquals("OK")) throw new Exception("Connecting to gatekeeper: " + gkResult);

		String startupCredential = null;
		String ontologyTypeID    = null;
		String ontologyStructure = null;
		boolean factoryResource  = inputWrapper.get_INPUT().get_FACTORY_RESOURCE();

		if(!factoryResource) {
			String FILTER = inputWrapper.get_INPUT().get_FILTER().get_DICOM_SR().str_to_XML().replaceFirst(
                    "..xml version=\"1.0\" encoding=\"UTF-8\"..\n",
                    ""
            );
			ontologyTypeID    = inputWrapper.get_INPUT().get_FILTER().get_DICOM_SR().getAttr_reportType();
			ontologyStructure = FILTER;
			startupCredential = inputWrapper.get_INPUT().get_CERTIFICATE();
		}

		ServiceGroupRegistrationClient regClient = null;
		/* First, we create a new MathResource through the MathResourceHome */

		ctx = ResourceContext.getResourceContext();
		home = (StorageBrokerResourceHome) ctx.getResourceHome();
		if(factoryResource) {
			regClient = ServiceGroupRegistrationClient.getContainerClient();
			key = home.create(this.storageBrokerFactoryURI, "StorageBrokerFactory URI", "none", null, null);
		} else {
			regClient = new ServiceGroupRegistrationClient();
			key = home.create(this.storageBrokerURI, ontologyTypeID, ontologyStructure, startupCredential, regClient);
		}

		org.globus.axis.message.addressing.EndpointReferenceType epr = null;

		/* We construct the instance's endpoint reference. The instance's service
		 * path can be found in the WSDD file as a parameter. */
		try {
			URL baseURL = ServiceHost.getBaseURL();
			String instanceService = (String) MessageContext.getCurrentContext().getService().getOption("instance");
			String instanceURI = baseURL.toString() + instanceService;
			// The endpoint reference includes the instance's URI and the resource key
			epr = AddressingUtils.createEndpointReference(instanceURI, key);
		} catch (Exception e) {
			throw new RemoteException("", e);
		}

		/*Register new resources in the IIS*/
		ServiceGroupRegistrationParameters params = ServiceGroupRegistrationClient.readParams(registration_path);
		params.setRegistrantEPR(epr);
		regClient.register(params);

		/*Finally, return the endpoint reference in a CreateResourceResponse*/
		CreateResourceResponse response = new CreateResourceResponse();
		response.setEndpointReference(epr);
		
		return response;
	}

	public String updateResources(String xmlInputUpdateResources) throws Exception {

		trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlInputUpdateResources.XmlWrapper updateResourcesWrapper =
			new trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlInputUpdateResources.XmlWrapper(xmlInputUpdateResources, false);
		updateResourcesWrapper.wrap();

		String xmlOutputUpdateResources = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			String CERTIFICATE = updateResourcesWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			if (!gkResult.contentEquals("OK")) throw new Exception("Connecting to gatekeeper: " + gkResult);

			_last_resources = _resources;
			_resources = new Vector<String>();

			//Update resources
			//-> Add new ones if they do not exist
			Iterator<?> filterIterator = updateResourcesWrapper.get_INPUT().getAll_FILTER();
			trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlInputUpdateResources.FILTER filter = null;

			while(filterIterator.hasNext()) {

				filter = (trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlInputUpdateResources.FILTER) filterIterator.next();

				String FILTER = filter.get_DICOM_SR().str_to_XML().replaceFirst(
						"..xml version=\"1.0\" encoding=\"UTF-8\"..\n",
						""
				);

				Iterator<String> lastResourcesIterator = _last_resources.iterator();
				String ontologyTypeID = filter.get_DICOM_SR().getAttr_reportType();
				String ontologyTypeIDAux;
				int exists = 0;

				while (lastResourcesIterator.hasNext()) {
					ontologyTypeIDAux = (String) lastResourcesIterator.next();

					if (ontologyTypeIDAux.equals(ontologyTypeID)) {
						exists = 1;
						_resources.add(ontologyTypeID);
						break;
					}
				}

				if (exists == 0) {

					try {
						String xmlInputCreateResource =
							"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
							"<INPUT>\n" +
							"<CERTIFICATE>" + CERTIFICATE +	"</CERTIFICATE>\n" +
							"<FILTER>" + FILTER + "</FILTER>\n" +
							"<FACTORY_RESOURCE>false</FACTORY_RESOURCE>\n" +
							"</INPUT>";
						createResource(xmlInputCreateResource);
						_resources.add(ontologyTypeID);
						logger.log(Level.ALL, "---> Created resource: " + ontologyTypeID);
					} catch (Exception e) {
						logger.log(Level.ALL, "---> Can not create resource (" + ontologyTypeID + "): ", e);
					}

				}

			}
			
			//-> Delete resources that does not exist now
			Iterator<String> lastResourcesIterator = _last_resources.iterator();
			String ontologyTypeID;

			while (lastResourcesIterator.hasNext()) {
				ontologyTypeID = (String) lastResourcesIterator.next();

				Iterator<String> resourcesIterator = _resources.iterator();
				String ontologyTypeIDAux;
				int exists = 0;

				while (resourcesIterator.hasNext()) {
					ontologyTypeIDAux = (String) resourcesIterator.next();

					if (ontologyTypeID.equals(ontologyTypeIDAux)) {
						exists = 1;
						break;
					}
				}

				if (exists == 0) {

					try {

						QName storageBrokerKeyQName = new QName("http://services.infrastructure.trencadis/StorageBroker", "StorageBrokerResourceKey");

						String xmlInputUnregisterOntology_StorageBroker =
							"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
							"<INPUT>\n" +
							"<CERTIFICATE>" +
							CERTIFICATE +
							"</CERTIFICATE>\n" +
							"</INPUT>";

						ResourceKey storageBrokerResourceKey = new SimpleResourceKey(storageBrokerKeyQName, ontologyTypeID);
						EndpointReferenceType storageBrokerResourceEPR = AddressingUtils.createEndpointReference(storageBrokerURI, storageBrokerResourceKey);
						StorageBrokerServiceAddressingLocator locator_storageBroker = new StorageBrokerServiceAddressingLocator();
						StorageBrokerPortType storageBrokerResourcePortType = locator_storageBroker.getStorageBrokerPortTypePort(storageBrokerResourceEPR);

						String xmlOutputUnregisterOntology_StorageBroker = storageBrokerResourcePortType.xmlUnregisterOntology(xmlInputUnregisterOntology_StorageBroker);

						trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputUnregisterOntology_StorageBroker.XmlWrapper xmlOutputUnregisterOntology_StorageBroker_wrapper =
							new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlOutputUnregisterOntology_StorageBroker.XmlWrapper(xmlOutputUnregisterOntology_StorageBroker, false);
						xmlOutputUnregisterOntology_StorageBroker_wrapper.wrap();

						if (xmlOutputUnregisterOntology_StorageBroker_wrapper.get_OUTPUT().get_STATUS().compareTo("-1") == 0) {
							throw new Exception(xmlOutputUnregisterOntology_StorageBroker_wrapper.get_OUTPUT().get_DESCRIPTION());
						}

						storageBrokerResourcePortType.destroy(new Destroy());
						logger.log(Level.ALL, "---> Unregistered resource: " + ontologyTypeID);
					} catch (Exception e) {
						logger.log(Level.ALL, "---> Can not unregister resource (" + ontologyTypeID + "): ", e);
					}

				}

			}

		} catch (Exception e) {
			logger.log(Level.ALL, "Error updating StorageBroker resources: ", e);

			xmlOutputUpdateResources += "<OUTPUT>\n";
			xmlOutputUpdateResources += "\t<STATUS>-1</STATUS>\n";
			xmlOutputUpdateResources += "\t<DESCRIPTION>\n\t" + e.getMessage() + "\t</DESCRIPTION>\n";
			xmlOutputUpdateResources += "</OUTPUT>\n";

			return xmlOutputUpdateResources;
		}

		xmlOutputUpdateResources += "<OUTPUT>\n";
		xmlOutputUpdateResources += "\t<STATUS>0</STATUS>\n";
		xmlOutputUpdateResources += "\t<DESCRIPTION>OK</DESCRIPTION>\n";
		xmlOutputUpdateResources += "</OUTPUT>\n";

		logger.log(Level.ALL, "-> StorageBrokerFactory.updateResources()");

		return xmlOutputUpdateResources;
	}

}
