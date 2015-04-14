package trencadis.infrastructure.services.StorageBroker.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.axis.message.MessageElement;
import org.globus.axis.message.addressing.Address;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceIdentifier;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.WSRFConstants;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.impl.ReflectionResourceProperty;
import org.globus.wsrf.impl.servicegroup.client.ServiceGroupRegistrationClient;
import org.oasis.wsrf.properties.QueryExpressionType;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;
import org.oasis.wsrf.properties.QueryResourceProperties_Element;
import org.oasis.wsrf.properties.QueryResourceProperties_PortType;
import org.oasis.wsrf.properties.WSResourcePropertiesServiceAddressingLocator;
import org.w3c.dom.NodeList;

import java.net.URI;

public class StorageBrokerResource implements Resource, ResourceIdentifier, ResourceProperties {

	private ResourcePropertySet propSet;
    private Object key;

    /* Resource properties */
	private String active;
	private String dsrtype;
	private String ontologyStructure;
	private String activeStartValue;
	private String dsrtypeStartValue;
	private String ontologyStructureStartValue;

	/*Configuration variables*/
	//Local parameters
	private String base_path;
	private String tmp_dir;
	private String path_cattrustcert;
	private long poll_interval;

	//Amga parameters
	private String amga_host_name;
	private String amga_host_port;

	//Index service
	private String url_iis_central;
	private String registration_path;

	private String startupCredential;
	private ServiceGroupRegistrationClient regClient = null;

	private long last_poll;
	private long current_poll;

	private Vector<String> storagedicom_uris = new Vector<String>();
	private Vector<String> dsr_types         = new Vector<String>();
	private Vector<String> backend_ids       = new Vector<String>();
	//private Vector<String> backend_hostnames = new Vector<String>();
	//private Vector<String> backend_ports     = new Vector<String>();

	private Logger logger = Logger.getLogger(StorageBrokerResource.class.getName());

	public void setValues(String URI, String ontologyTypeID, String ontologyStr, String startupCredential, ServiceGroupRegistrationClient regClient) {
		this.activeStartValue = URI;
		this.dsrtypeStartValue = ontologyTypeID;
		this.ontologyStructureStartValue = ontologyStr;
		this.startupCredential = startupCredential;
		this.regClient = regClient;
	}

	// This method is executed in the first access to the resource
	public Object initialize() throws Exception {

		//Normal behaviour
		this.propSet = new SimpleResourcePropertySet(StorageBrokerQNames.RESOURCE_PROPERTIES);

		try {

			ResourceProperty activeRP = new ReflectionResourceProperty(
					StorageBrokerQNames.RP_ACTIVE, "Active", this);
			this.propSet.add(activeRP);
			setActive(this.activeStartValue);

			ResourceProperty dsrtypeRP = new ReflectionResourceProperty(
					StorageBrokerQNames.RP_DSRTYPE, "DSRType", this);
			this.propSet.add(dsrtypeRP);
			setDSRType(this.dsrtypeStartValue);

			setOntologyStructure(this.ontologyStructureStartValue);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		if (!this.dsrtypeStartValue.equals("StorageBrokerFactory URI")) {

			//Configuration load
			File configFile = new File("/usr/local/globus4/TRENCADIS/StorageBroker/storageBrokerConfig.xml");
			trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.XmlWrapper configWrapper =
				new trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.XmlWrapper(configFile, false);
			configWrapper.wrap();

			trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.LOCAL_PARAMETERS localParameters = configWrapper.get_CONFIGURATION().get_LOCAL_PARAMETERS();
			this.base_path = localParameters.get_BASE_PATH();
			this.path_cattrustcert = localParameters.get_PATH_CATTRUSTCERT();
			this.tmp_dir = localParameters.get_TMP_DIR();
			this.poll_interval = Long.valueOf(localParameters.get_POLL_INTERVAL());
			last_poll=0;

			trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.AMGA_PARAMETERS amgaParameters = configWrapper.get_CONFIGURATION().get_AMGA_PARAMETERS();
			this.amga_host_name = amgaParameters.get_HOST_NAME();
			this.amga_host_port = amgaParameters.get_HOST_PORT();

			trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.INDEX_SERVICE indexServiceParameters = configWrapper.get_CONFIGURATION().get_INDEX_SERVICE();
			this.url_iis_central = indexServiceParameters.get_URL_IIS_CENTRAL();
			this.registration_path = indexServiceParameters.get_REGISTRATION_PATH();

			//Logger setup
			FileHandler logFile = new FileHandler(this.tmp_dir + "/StorageBrokerResource_log");
			this.logger.addHandler(logFile);
			this.logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			logFile.setFormatter(formatter);

			pollStorageDICOMResources();

		}

		this.key = dsrtypeStartValue;
		return key;
	}

	public synchronized void pollStorageDICOMResources() throws Exception {

		current_poll = System.currentTimeMillis();

		if ((current_poll - last_poll) > poll_interval) {

			WSResourcePropertiesServiceAddressingLocator locator_query = new WSResourcePropertiesServiceAddressingLocator();
			String dialect = WSRFConstants.XPATH_1_DIALECT;
			EndpointReferenceType epr = new EndpointReferenceType(new Address(url_iis_central));
			QueryResourceProperties_PortType port = locator_query.getQueryResourcePropertiesPort(epr);

			//Get StorageDICOM resources of all servers of this kind of report
			QueryExpressionType query_storageDICOM = new QueryExpressionType();
			String expression_storageDICOM = "//*[namespace-uri()='http://services.infrastructure.trencadis/StorageDICOM' and local-name()='DSRType' and string()='" + getDSRType() + "']/..";
			query_storageDICOM.setDialect(dialect);
			query_storageDICOM.setValue(expression_storageDICOM);

			QueryResourceProperties_Element request_storageDICOM = new QueryResourceProperties_Element();
			request_storageDICOM.setQueryExpression(query_storageDICOM);
			QueryResourcePropertiesResponse response_storageDICOM = port.queryResourceProperties(request_storageDICOM);

			MessageElement[] responseContents_storageDICOM = response_storageDICOM.get_any();

			if (responseContents_storageDICOM == null) {
				logger.log(Level.ALL, "No URI of the StorageDICOM is published in the central MDS for this kind of report.");
			} else {

				Vector<String> storagedicom_uris_aux = new Vector<String>();
				Vector<String> dsr_types_aux         = new Vector<String>();
				//Vector<String> backend_hostnames_aux = new Vector<String>();
				//Vector<String> backend_ports_aux     = new Vector<String>();
				Vector<String> backend_ids_aux       = new Vector<String>();

				ArrayList<String> erased = new ArrayList<String>();
				boolean next;

				for (int i=0; i < responseContents_storageDICOM.length; i++) {
					next = false;
					NodeList nodesList = responseContents_storageDICOM[i].getAsDOM().getChildNodes();
					String token = nodesList.item(0).getTextContent() + " - " + nodesList.item(1).getTextContent();
					for (int j=0; j < erased.size(); j++) {
						if (token.contentEquals(erased.get(j))) {
							next = true;
							logger.log(Level.ALL, "Ignored replicated entry: " + token);
							break;
						}
					}
					if (next) continue;

					//0 -> URI
					//1 -> DSRType
					//2 -> Hospital ID *
					//3 -> AMGA BACKEND IP *
					//4 -> AMGA BACKEND PORT *

					if (nodesList.item(0)==null || nodesList.item(1)==null || nodesList.item(2)==null/*|| nodesList.item(3)==null || nodesList.item(4)==null*/)
						throw new Exception("Some StorageDICOM resource properties are missing");

					storagedicom_uris_aux.add(nodesList.item(0).getTextContent());
					dsr_types_aux.add(nodesList.item(1).getTextContent());
					backend_ids_aux.add(nodesList.item(2).getTextContent());
					/*
					if (nodesList.item(3).getTextContent().equals("localhost") || nodesList.item(3).getTextContent().equals("127.0.0.1")) {
						URI StorageDICOMURI = new URI(nodesList.item(0).getTextContent());
						backend_hostnames_aux.add(StorageDICOMURI.getHost());
					} else {
						backend_hostnames_aux.add(nodesList.item(3).getTextContent());
					}

					backend_ports_aux.add(nodesList.item(4).getTextContent());
					*/
					erased.add(token);
				}

				this.storagedicom_uris.clear();
				this.dsr_types.clear();
				this.backend_ids.clear();
				//this.backend_hostnames.clear();
				//this.backend_ports.clear();
				
				this.storagedicom_uris.addAll(storagedicom_uris_aux);
				this.dsr_types.addAll(dsr_types_aux);
				this.backend_ids.addAll(backend_ids_aux);
				//this.backend_hostnames.addAll(backend_hostnames_aux);
				//this.backend_ports.addAll(backend_ports_aux);

			}

			last_poll = current_poll;
			logger.log(Level.ALL, this.getDSRType() + " -> Storage DICOM backends list updated");
		} else {
			logger.log(Level.ALL, this.getDSRType() + " -> No need to update Storage DICOM backends list");
		}

	}

	public synchronized Backends getBackends() {
		return new Backends(this.storagedicom_uris, this.dsr_types, this.backend_ids/*, this.backend_hostnames, this.backend_ports*/);
	}
	
	public class Backends {

		private Vector<String> storagedicom_uris = null;
		private Vector<String> dsr_types         = null;
		private Vector<String> backend_ids       = null;
		//private Vector<String> backend_hostnames = null;
		//private Vector<String> backend_ports     = null;

		public Backends(Vector<String> storagedicom_uris, Vector<String> dsr_types, Vector<String> backend_ids/*, Vector<String> backend_hostnames, Vector<String> backend_ports*/) {
			this.storagedicom_uris = storagedicom_uris;
			this.dsr_types         = dsr_types;
			this.backend_ids       = backend_ids;
			/*
			this.backend_hostnames = backend_hostnames;
			this.backend_ports     = backend_ports;
			*/
		}

		public synchronized Iterator<String> getStorageDicomURIs() {
			return this.storagedicom_uris.iterator();
		}
		
		public synchronized Iterator<String> getDsrTypes() {
			return this.dsr_types.iterator();
		}
		
		public synchronized Iterator<String> getBackendIds() {
			return this.backend_ids.iterator();
		}
/*
		public synchronized Iterator<String> getBackendHostnames() {
			return this.backend_hostnames.iterator();
		}

		public synchronized Iterator<String> getBackendPorts() {
			return this.backend_ports.iterator();
		}
*/
	}

	public void setOntologyStructure(String ontologyStructure) {
		this.ontologyStructure = ontologyStructure;
	}

	public ServiceGroupRegistrationClient getServiceGroupRegistrationClient() {
		return this.regClient;
	}

	/* Get/Setters for the RPs */
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getDSRType() {
		return this.dsrtype;
	}

	public void setDSRType(String dsrtype) {
		this.dsrtype = dsrtype;
	}

	public String getOntologyStructure() {
		return this.ontologyStructure;
	}

	/* Required by interface ResourceProperties */
	public ResourcePropertySet getResourcePropertySet() {
		return this.propSet;
	}

	public Object getID() {
		return this.key;
	}

}
