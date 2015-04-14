package trencadis.infrastructure.services.StorageBroker.impl;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.File;

import javax.xml.namespace.QName;

import org.globus.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.utils.AddressingUtils;

import trencadis.infrastructure.services.StorageBroker.impl.StorageBrokerResource.Backends;
import trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlOutputMetadataQuery_StorageDICOM.ID;
import trencadis.infrastructure.services.stubs.StorageBroker.StorageBrokerPortType;
import trencadis.infrastructure.services.stubs.StorageBroker.service.StorageBrokerServiceAddressingLocator;
import trencadis.infrastructure.services.stubs.StorageDICOM.StorageDICOMPortType;
import trencadis.infrastructure.services.stubs.StorageDICOM.service.StorageDICOMServiceAddressingLocator;
import trencadis.infrastructure.gateKeeper.CGateKeeper;

import arda.md.javaclient.AttributeSet;
import arda.md.javaclient.AttributeSetList;

public class StorageBrokerService {

	// Configuration variables
	//Local parameters
	private String base_path;
	private String path_cattrustcert;
	private String tmp_dir;
	//Amga parameters
	//private String amga_host_name;
	//private String amga_host_port;
	//LFC parameters
	private String lcg_gfal_infosys;
	private String lcg_gfal_vo;
	private String lfc_home;
	private String lfc_host;
	private String lcg_catalog_type;
	private String lcg_rfio_type;
	private String vo;
	private String vo_default_se;
	//Gate keeper
	private String gk_host_name;
	private String gk_host_port;
	private String gk_db_name;
	private String gk_user;
	private String gk_password;

	private CGateKeeper gateKeeper;
	ResourceKey key;

	private Logger logger = Logger.getLogger(StorageBrokerService.class.getName());

	public StorageBrokerService() throws Exception {

		if (!getResource().getDSRType().equals("StorageBrokerFactory URI")) {

			//Configuration load
			File configFile = new File("/usr/local/globus4/TRENCADIS/StorageBroker/storageBrokerConfig.xml");
			trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.XmlWrapper configWrapper =
				new trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.XmlWrapper(configFile, false);
			configWrapper.wrap();

			trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.LOCAL_PARAMETERS localParameters = configWrapper.get_CONFIGURATION().get_LOCAL_PARAMETERS();
			this.base_path = localParameters.get_BASE_PATH();
			this.path_cattrustcert = localParameters.get_PATH_CATTRUSTCERT();
			this.tmp_dir = localParameters.get_TMP_DIR();

			//trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.AMGA_PARAMETERS amgaParameters = configWrapper.get_CONFIGURATION().get_AMGA_PARAMETERS();
			//this.amga_host_name = amgaParameters.get_HOST_NAME();
			//this.amga_host_port = amgaParameters.get_HOST_PORT();

			trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.LFC_PARAMETERS lfcParameters = configWrapper.get_CONFIGURATION().get_LFC_PARAMETERS();
			this.lcg_gfal_infosys = lfcParameters.get_LCG_GFAL_INFOSYS();
			this.lcg_gfal_vo = lfcParameters.get_LCG_GFAL_VO();
			this.lfc_home = lfcParameters.get_LFC_HOME();
			this.lfc_host = lfcParameters.get_LFC_HOST();
			this.lcg_catalog_type = lfcParameters.get_LCG_CATALOG_TYPE();
			this.lcg_rfio_type = lfcParameters.get_LCG_RFIO_TYPE();
			this.vo = lfcParameters.get_VO();
			this.vo_default_se = lfcParameters.get_VO_DEFAULT_SE();

			trencadis.infrastructure.services.StorageBroker.impl.wrapper.config.DATABASE_PARAMETERS gatekeeperParameters = configWrapper.get_CONFIGURATION().get_GATEKEEPER().get_DATABASE_PARAMETERS();
			this.gk_host_name = gatekeeperParameters.get_HOST_NAME();
			this.gk_host_port = gatekeeperParameters.get_HOST_PORT();
			this.gk_db_name = gatekeeperParameters.get_DB_NAME();
			this.gk_user = gatekeeperParameters.get_USER();
			this.gk_password = gatekeeperParameters.get_PASSWORD();

			//Initialize the gatekeeper
			gateKeeper = new CGateKeeper(
					"org.postgresql.Driver",
					"jdbc:storageBrokerGatekeeperPool:",
					"jdbc:postgresql://",
					this.gk_host_name,
					this.gk_host_port,
					this.gk_db_name,
					this.gk_user, this.gk_password,
					this.path_cattrustcert
			);

			//Logger setup
			FileHandler logFile = new FileHandler(this.tmp_dir + "/StorageBroker_log");
			logger.addHandler(logFile);
			logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			logFile.setFormatter(formatter);

		}

	}

	private StorageBrokerResource getResource() throws RemoteException {

		Object resource = null;
		try {
			resource = ResourceContext.getResourceContext().getResource();
		} catch (Exception e) {
			throw new RemoteException("Unable to access resource.", e);
		}

		StorageBrokerResource storageBrokerResource = (StorageBrokerResource) resource;
		return storageBrokerResource;
	}

	/* Service Implementation */
	public String xmlMultipleMetadataQuery(String xmlInputMultipleMetadataQuery) throws RemoteException {
		StorageBrokerResource storageBrokerResource = getResource();

		trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlInputMultipleMetadataQuery.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlInputMultipleMetadataQuery.XmlWrapper(xmlInputMultipleMetadataQuery, false);
		inputWrapper.wrap();

		String xmlOutputMultipleMetadataQuery = new String();
		String query = new String();

		try {

			if (storageBrokerResource.getDSRType().equals("StorageBrokerFactory URI"))
				throw new Exception("You called the StorageBrokerFactory URI publisher. This service does not realize any operation");

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			if (!gkResult.contentEquals("OK")) throw new Exception("Connecting to gatekeeper: " + gkResult);

			storageBrokerResource.pollStorageDICOMResources();

			query = inputWrapper.get_INPUT().get_QUERY();

			Backends backends = storageBrokerResource.getBackends();
			Iterator<String> storagedicom_uris = backends.getStorageDicomURIs();
			Iterator<String> dsr_types         = backends.getDsrTypes();
			Iterator<String> backend_ids       = backends.getBackendIds();

			Vector<SimultaneousQuery> queries = new Vector<SimultaneousQuery>();
			Object query_monitor = new Object();
			int query_count = 0;

			while(backend_ids.hasNext()) {

				String storagedicom_uri  = storagedicom_uris.next();
				String dsr_type          = dsr_types.next();
				String backend_id        = backend_ids.next();

				SimultaneousQuery simultaneousQuery = new SimultaneousQuery(
						storagedicom_uri,
						dsr_type,
						backend_id,
						query,
						query_monitor,
						CERTIFICATE
				);

				queries.add(simultaneousQuery);
				simultaneousQuery.start();
				query_count++;

			}

			synchronized(query_monitor) {

				int finished;

				{
					Iterator<SimultaneousQuery> queries_it = queries.iterator();
					SimultaneousQuery query_aux = null;
					finished = 0;
					while (queries_it.hasNext()) {
						query_aux = queries_it.next();
						if (query_aux.finished) finished++;
					}
				}

				while (query_count - finished > 0) {

					try {query_monitor.wait();} catch (InterruptedException ie) {};

					Iterator<SimultaneousQuery> queries_it = queries.iterator();
					SimultaneousQuery query_aux = null;
					finished = 0;
					while (queries_it.hasNext()) {
						query_aux = queries_it.next();
						if (query_aux.finished) finished++;
					}

				}

			}

			Iterator<SimultaneousQuery> queries_it = queries.iterator();
			SimultaneousQuery query_aux = null;
			while (queries_it.hasNext()) {
				query_aux = queries_it.next();	
				xmlOutputMultipleMetadataQuery += query_aux.getOutputBackend();
			}

		} catch (Exception e) {
			logger.log(Level.ALL, "Error querying backends: ", e);
			xmlOutputMultipleMetadataQuery += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
			xmlOutputMultipleMetadataQuery += "<OUTPUT>\n";
			xmlOutputMultipleMetadataQuery += "\t<STATUS>-1</STATUS>\n";
			xmlOutputMultipleMetadataQuery += "\t<DESCRIPTION>\n\t" + e.getMessage() + "\n\t</DESCRIPTION>\n";
			xmlOutputMultipleMetadataQuery += "</OUTPUT>\n";

			return xmlOutputMultipleMetadataQuery;
		}

		xmlOutputMultipleMetadataQuery += "\t<DESCRIPTION>OK</DESCRIPTION>\n";		
		xmlOutputMultipleMetadataQuery += "\t<STATUS>0</STATUS>\n";
		xmlOutputMultipleMetadataQuery += "<OUTPUT>\n";
		xmlOutputMultipleMetadataQuery += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

		xmlOutputMultipleMetadataQuery += "</OUTPUT>\n";

		logger.log(Level.ALL, "-> StorageBroker(" + storageBrokerResource.getID() + ").xmlMultipleMetadataQuery(): " + query.replace("$h*o!s_p-i_t!a*l$", "$(backend_home)"));

		return xmlOutputMultipleMetadataQuery;
	}

	public String xmlUnregisterOntology(String xmlInputUnregisterOntology) throws RemoteException {
		StorageBrokerResource storageBrokerResource = getResource();

		trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlInputUnregisterOntology.XmlWrapper inputWrapper =
			new trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlInputUnregisterOntology.XmlWrapper(xmlInputUnregisterOntology, false);
		inputWrapper.wrap();

		String xmlOutputUnregisterOntology = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			if (storageBrokerResource.getDSRType().equals("StorageBrokerFactory URI"))
				throw new Exception("You called the StorageBrokerFactory URI publisher. This service does not realize any operation");

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			if (!gkResult.contentEquals("OK")) throw new Exception("Connecting to gatekeeper: " + gkResult);

			//Stop ISS registration timer event
			storageBrokerResource.getServiceGroupRegistrationClient().terminate();

		} catch (Exception e) {
			logger.log(Level.ALL, "Error unregistering ontology: ", e);

			xmlOutputUnregisterOntology += "<OUTPUT>\n";
			xmlOutputUnregisterOntology += "\t<STATUS>-1</STATUS>\n";
			xmlOutputUnregisterOntology += "\t<DESCRIPTION>\n\t" + e.getMessage() + "\t</DESCRIPTION>\n";
			xmlOutputUnregisterOntology += "</OUTPUT>\n";

			return xmlOutputUnregisterOntology;
		}

		xmlOutputUnregisterOntology += "<OUTPUT>\n";
		xmlOutputUnregisterOntology += "\t<STATUS>0</STATUS>\n";
		xmlOutputUnregisterOntology += "\t<DESCRIPTION>OK</DESCRIPTION>\n";
		xmlOutputUnregisterOntology += "</OUTPUT>\n";

		logger.log(Level.ALL, "-> StorageBroker(" + storageBrokerResource.getID() + ").xmlAddReport()");

		return xmlOutputUnregisterOntology;
	}

	private class SimultaneousQuery extends Thread {

		private String storagedicom_uri;
		private String dsr_type;
		private String backend_id;

		private String query;
		private Object query_monitor;
		private String CERTIFICATE;

		private String xmlOutputBackend = new String();

		private Boolean finished;

		public SimultaneousQuery(
				String storagedicom_uri,
				String dsr_type,
				String backend_id,
				String query,
				Object query_monitor,
				String CERTIFICATE
		) {
			this.storagedicom_uri = storagedicom_uri;
			this.dsr_type         = dsr_type;
			this.backend_id       = backend_id;
			this.query            = query;
			this.query_monitor    = query_monitor;
			this.CERTIFICATE      = CERTIFICATE;

			this.finished         = false;
		}

		public void run() {

			AttributeSetList reportIds = null;
		    AttributeSet set = null;

			try {

				QName storageDICOMKeyQName = new QName("http://services.infrastructure.trencadis/StorageDICOM", "StorageDICOMResourceKey");
				ResourceKey storageDICOMResourceKey = new SimpleResourceKey(storageDICOMKeyQName, dsr_type);
				EndpointReferenceType storageDICOMResourceEPR = AddressingUtils.createEndpointReference(storagedicom_uri, storageDICOMResourceKey);
				StorageDICOMServiceAddressingLocator locator_storageDICOM = new StorageDICOMServiceAddressingLocator();
				StorageDICOMPortType storageDICOM = locator_storageDICOM.getStorageDICOMPortTypePort(storageDICOMResourceEPR);

				String xmlInputMetadataQuery =
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<INPUT>\n" +
					"<CERTIFICATE>" + CERTIFICATE + "</CERTIFICATE>\n" +
					"<QUERY>" + query + "</QUERY>\n" +
					"</INPUT>";

				String xmlOutputMetadataQuery = storageDICOM.xmlMetadataQuery(xmlInputMetadataQuery);

				trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlOutputMetadataQuery_StorageDICOM.XmlWrapper queryWrapper =
					new trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlOutputMetadataQuery_StorageDICOM.XmlWrapper(xmlOutputMetadataQuery, false);
				queryWrapper.wrap();
				
				if (queryWrapper.get_OUTPUT().get_STATUS().compareTo("-1") == 0)
					throw new Exception("Can not search in StorageDICOM(" + dsr_type + "):" + queryWrapper.get_OUTPUT().get_DESCRIPTION());

				xmlOutputBackend += "<BACKEND>\n";
				xmlOutputBackend += "\t<URI>"      + storagedicom_uri + "</URI>\n";
				xmlOutputBackend += "\t<DSR_TYPE>" + dsr_type         + "</DSR_TYPE>\n";
				xmlOutputBackend += "\t<HOSPITAL>" + backend_id       + "</HOSPITAL>\n";
				xmlOutputBackend += "\t<STATUS>0</STATUS>\n";
				xmlOutputBackend += "\t<DESCRIPTION>OK</DESCRIPTION>\n";

				Iterator<?> entries = queryWrapper.get_OUTPUT().getAll_ID();
				trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlOutputMetadataQuery_StorageDICOM.ID id = null;

				while(entries.hasNext()) {
					id = (trencadis.infrastructure.services.StorageBroker.impl.wrapper.xmlOutputMetadataQuery_StorageDICOM.ID) entries.next();
					xmlOutputBackend += "\t<ID>" + id.getValue() + "</ID>\n";
				}

				xmlOutputBackend += "</BACKEND>\n";

			} catch (Exception e) {
				logger.log(Level.ALL, "Error querying backend (" + backend_id + "):", e);

				xmlOutputBackend += "<BACKEND>\n";
				xmlOutputBackend += "\t<URI>"          + storagedicom_uri + "</URI>\n";
				xmlOutputBackend += "\t<DSR_TYPE>"     + dsr_type         + "</DSR_TYPE>\n";
				xmlOutputBackend += "\t<HOSPITAL>\n\t" + backend_id + "\t</HOSPITAL>\n";
				xmlOutputBackend += "\t<STATUS>-1</STATUS>\n";
				xmlOutputBackend += "\t<DESCRIPTION>\n\t" + e.getMessage() + "\t</DESCRIPTION>\n";
				xmlOutputBackend += "</BACKEND>\n";
			}

			synchronized(query_monitor) {
				finished = true;
				query_monitor.notify();
			}

		}

		public String getOutputBackend() {
			return xmlOutputBackend;
		}

	} /*End query thread*/

}
