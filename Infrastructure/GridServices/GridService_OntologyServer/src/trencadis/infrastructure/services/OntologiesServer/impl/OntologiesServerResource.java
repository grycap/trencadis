package trencadis.infrastructure.services.OntologiesServer.impl;

import java.io.File;
import java.util.Vector;
import java.rmi.RemoteException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.globus.axis.message.addressing.AttributedURIType;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.*;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.impl.ReflectionResourceProperty;
import org.globus.wsrf.utils.AddressingUtils;
import trencadis.common.util.CertUtils;
import trencadis.infrastructure.TRENCADIS_JAVA_API_SQL_Ontologies_Database.TRENCADIS_JAVA_API_SQL_Ontologies_Database;
import trencadis.infrastructure.gateKeeper.CGateKeeper;
import trencadis.infrastructure.services.stubs.OntologiesServer.OntologiesServerServiceInfo;
import trencadis.infrastructure.services.stubs.OntologiesServer.OntologyInfo;
import trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS.IIS_Register;

public class OntologiesServerResource implements Resource, ResourceProperties {
	// It indicates if is in debug mode
	public static boolean bIsDebug = false;

	/*******************************************************************/
	/********** Local parameters ***************************************/
	private String base_path;
	private String path_cattrustcert;
	private String tmp_dir;
	/*******************************************************************/

	/*******************************************************************/
	// Index service Parameter
	private Vector v_url_iis_central;
	/*******************************************************************/

	/*******************************************************************/
	/****************** IIS Parameters *********************************/
	private String registration_path;
	/*******************************************************************/

	/*******************************************************************/
	/****************** GateKeeper Component **************************/
	private CGateKeeper gateKeeper;
	/*******************************************************************/

	/*******************************************************************/
	/****************** Logger Component *******************************/
	private Logger logger = Logger.getLogger(OntologiesServerResource.class
			.getName());
	/*******************************************************************/

	/*******************************************************************/
	/****** TRENCADIS Java API IIS Register ****************************/
	IIS_Register API_IIS_Register;
	/*******************************************************************/

	/*******************************************************************/
	/****** TRENCADIS Java API SQL Ontologies Database Component *******/
	TRENCADIS_JAVA_API_SQL_Ontologies_Database API_Ontologies_db;
	/*******************************************************************/

	/* Resource Property Set */
	private ResourcePropertySet propSet;

	/* Resource properties */
	private OntologiesServerServiceInfo RPs_OntologiesServerServiceInfo;

	// In Singleton services this method is executed in the first access to the
	// resource
	public void initialize() throws Exception {

		File configFile = new File(
				"/home/trencadis/OntologiesServer/OntologiesServerConfig.xml");
		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.config.XmlWrapper configWrapper = new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.config.XmlWrapper(
				configFile, false);
		configWrapper.wrap();

		/***********************************************************************************/
		/****** Parametros Generales de Configuración del Servicio Ontologies Server *******/
		/***********************************************************************************/
		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.config.SERVICE_PARAMETERS localParameters = configWrapper
				.get_CONFIGURATION().get_SERVICE_PARAMETERS();
		this.base_path = localParameters.get_BASE_PATH();
		this.path_cattrustcert = localParameters.get_PATH_CATRUSTCERT();
		this.tmp_dir = localParameters.get_TMP_DIR();
		/************************************************************************************/

		/**************************************************************/
		/******************** IIS Parameters ***********************/
		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.config.INDEX_SERVICE_PARAMETERS indexServiceParameters = configWrapper
				.get_CONFIGURATION().get_INDEX_SERVICE_PARAMETERS();

		this.v_url_iis_central = new Vector();
		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.config.URL_IIS URL_IIS;
		Iterator it_urls = configWrapper.get_CONFIGURATION()
				.get_INDEX_SERVICE_PARAMETERS().get_LIST_OF_URL_IIS()
				.getAll_URL_IIS();
		while (it_urls.hasNext()) {
			URL_IIS = (trencadis.infrastructure.services.OntologiesServer.impl.wrapper.config.URL_IIS) it_urls
					.next();
			v_url_iis_central.add(URL_IIS.getValue());
		}

		this.registration_path = indexServiceParameters.get_REGISTRATION_PATH();
		/****************************************************************/

		/**************************************************************/
		/******************** COMPONENT GATEKEEPER *******************/
		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.config.GATEKEEPER_PARAMETERS gatekeeperParameters = configWrapper
				.get_CONFIGURATION().get_GATEKEEPER_PARAMETERS();
		// Initialize the gatekeeper
		this.gateKeeper = new CGateKeeper("org.postgresql.Driver",
				"jdbc:ontologiesServerGatekeeperPool:", "jdbc:postgresql://",
				gatekeeperParameters.get_HOST_NAME(),
				gatekeeperParameters.get_HOST_PORT(),
				gatekeeperParameters.get_DB_NAME(),
				gatekeeperParameters.get_USER(),
				gatekeeperParameters.get_PASSWORD(), this.path_cattrustcert);
		/**************************************************************/

		/**************************************************************/
		/******************** LOGGER COMPONENT ***********************/
		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.config.LOGGER_PARAMETERS loggerParameters = configWrapper
				.get_CONFIGURATION().get_LOGGER_PARAMETERS();
		FileHandler logFile = new FileHandler(loggerParameters.get_LOG_PATH());
		logger.addHandler(logFile);
		logger.setLevel(Level.ALL);
		SimpleFormatter formatter = new SimpleFormatter();
		logFile.setFormatter(formatter);
		/**************************************************************/

		/**************************************************************/
		/***** TRENCADIS_JAVA_API_SQL_Ontologies Component ************/
		// Initialize connection pool wrapping driver for ontologies
		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.config.BACKEND_PARAMETERS backendParameters = configWrapper
				.get_CONFIGURATION().get_BACKEND_PARAMETERS();
		API_Ontologies_db = new TRENCADIS_JAVA_API_SQL_Ontologies_Database(
				backendParameters.get_HOST_NAME(),
				backendParameters.get_HOST_PORT(),
				backendParameters.get_DB_NAME(), backendParameters.get_USER(),
				backendParameters.get_PASSWORD());
		/**************************************************************/

		/********* ESTA PARTE ES PARA SINCRONIZAR CON LOS STORAGEDICOM *******/
		/********* De momento lo desativamos *********************************/
		// updateStorageServices = new UpdateStorageServices();
		// updateStorageServices.start();
		/*********************************************************************/

		if (!bIsDebug) {

			// Normal behaviour -> Register resource properties
			this.propSet = new SimpleResourcePropertySet(
					OntologiesServerQNames.RESOURCE_PROPERTIES);

			try {
				ResourceProperty ontologiesServerServiceInfo = new ReflectionResourceProperty(
						OntologiesServerQNames.RP_ONTOLOGIESSERVERSERVICEINFO,
						"OntologiesServerServiceInfo", this);
				this.propSet.add(ontologiesServerServiceInfo);

			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}

			try {

				ResourceContext ctx;
				try {
					ctx = ResourceContext.getResourceContext();
				} catch (ResourceContextException e) {
					logger.log(Level.ALL, "Could not get ResourceContext: " + e);
					return;
				}

				EndpointReferenceType endpoint;
				try {
					endpoint = AddressingUtils.createEndpointReference(ctx,
							null);
				} catch (Exception e) {
					logger.log(Level.ALL, "Could not form EPR: " + e);
					return;
				}

				X509Certificate uUIDCertChainX509 = CertUtils
						.getServCertificateChain();

				// Update Static Values of RPs
				RPs_OntologiesServerServiceInfo = new OntologiesServerServiceInfo();
				RPs_OntologiesServerServiceInfo.setDomain(CertUtils
						.getPublicSubjectOU(uUIDCertChainX509));
				AttributedURIType address = endpoint.getAddress();
				RPs_OntologiesServerServiceInfo.setURI(address.toString());

				// Update Dinamic Values of the RPs
				this.UpdateOntologiesServerServiceInfoRP();

			} catch (Exception e) {
				logger.log(Level.ALL,
						"Exception when trying to initializing this UUIDkeyGeneratorService: "
								+ e);
			}
		}
	}

	/* Service Implementation */
	public String xmlCreateOntology(String xmlInputCreateOntology)
			throws RemoteException {
		String strDN = "";

		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlInputCreateOntology.XmlWrapper inputWrapper = new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlInputCreateOntology.XmlWrapper(
				xmlInputCreateOntology, false);
		inputWrapper.wrap();

		String xmlOutputCreateOntology = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			strDN = gateKeeper.strExtractDN(CERTIFICATE);
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			if (!gkResult.contentEquals("OK"))
				throw new Exception("Connecting to gatekeeper: " + gkResult);

			String strXMLOntology = inputWrapper.get_INPUT().get_ONTOLOGY()
					.get_xml_DICOM_SR_Template();

			int iErr = API_Ontologies_db.iInsertOntology(inputWrapper
					.get_INPUT().get_IDONTOLOGY(), inputWrapper.get_INPUT()
					.get_DESCRIPTION(), strXMLOntology);
			if (iErr == 0) {
				xmlOutputCreateOntology += "<OUTPUT>\n";
				xmlOutputCreateOntology += "\t<STATUS>0</STATUS>\n";
				xmlOutputCreateOntology += "\t<DESCRIPTION>\n\tOK\t</DESCRIPTION>\n";
				xmlOutputCreateOntology += "</OUTPUT>\n";

				// Update Dinamic Values of the RPs
				UpdateOntologiesServerServiceInfoRP();

				logger.log(Level.ALL,
						"*****************************************************");
				logger.log(Level.ALL, strDN
						+ " -> Method: OntologiesServer.xmlCreateOntology()");
				logger.log(Level.ALL, xmlInputCreateOntology);
				logger.log(Level.ALL,
						"*****************************************************");
			} else {
				xmlOutputCreateOntology += "<OUTPUT>\n";
				xmlOutputCreateOntology += "\t<STATUS>-1</STATUS>\n";
				xmlOutputCreateOntology += "\t<DESCRIPTION>\n\t"
						+ API_Ontologies_db.strGetError()
						+ "\t</DESCRIPTION>\n";
				xmlOutputCreateOntology += "</OUTPUT>\n";

				logger.log(Level.ALL,
						"*****************************************************");
				logger.log(
						Level.ALL,
						strDN
								+ "-> Error in Method: OntologiesServer.xmlCreateOntology()");
				logger.log(Level.ALL, xmlOutputCreateOntology);
				logger.log(Level.ALL,
						"*****************************************************");
			}

			return xmlOutputCreateOntology;

		} catch (Exception e) {

			xmlOutputCreateOntology += "<OUTPUT>\n";
			xmlOutputCreateOntology += "\t<STATUS>-1</STATUS>\n";
			xmlOutputCreateOntology += "\t<DESCRIPTION>\n\t" + e.getMessage()
					+ "\t</DESCRIPTION>\n";
			xmlOutputCreateOntology += "</OUTPUT>\n";

			logger.log(Level.ALL,
					"*****************************************************");
			logger.log(
					Level.ALL,
					strDN
							+ " -> Error in Method: OntologiesServer.xmlCreateOntology()");
			logger.log(Level.ALL, xmlOutputCreateOntology);
			logger.log(Level.ALL,
					"*****************************************************");

			return xmlOutputCreateOntology;
		}
	}

	public String xmlRemoveOntology(String xmlInputRemoveOntology)
			throws RemoteException {

		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlInputRemoveOntology.XmlWrapper inputWrapper = new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlInputRemoveOntology.XmlWrapper(
				xmlInputRemoveOntology, false);
		inputWrapper.wrap();

		String xmlOutputRemoveOntology = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			if (!gkResult.contentEquals("OK"))
				throw new Exception("Connecting to gatekeeper: " + gkResult);

			int iErr = API_Ontologies_db.iRemoveOntology(inputWrapper
					.get_INPUT().get_IDONTOLOGY());
			if (iErr == 0) {
				xmlOutputRemoveOntology += "<OUTPUT>\n";
				xmlOutputRemoveOntology += "\t<STATUS>0</STATUS>\n";
				xmlOutputRemoveOntology += "\t<DESCRIPTION>OK</DESCRIPTION>\n";
				xmlOutputRemoveOntology += "</OUTPUT>\n";

				// Update Dinamic Values of the RPs
				UpdateOntologiesServerServiceInfoRP();

				logger.log(Level.ALL,
						"*****************************************************");
				logger.log(Level.ALL,
						"strDN -> Method: OntologiesServer.xmlRemoveOntology()");
				logger.log(Level.ALL, xmlInputRemoveOntology);
				logger.log(Level.ALL,
						"*****************************************************");

			} else {
				xmlOutputRemoveOntology += "<OUTPUT>\n";
				xmlOutputRemoveOntology += "\t<STATUS>-1</STATUS>\n";
				xmlOutputRemoveOntology += "\t<DESCRIPTION>\n\t"
						+ API_Ontologies_db.strGetError()
						+ "\t</DESCRIPTION>\n";
				xmlOutputRemoveOntology += "</OUTPUT>\n";

				logger.log(Level.ALL,
						"*****************************************************");
				logger.log(Level.ALL,
						"strDN -> Error in Method: OntologiesServer.xmlRemoveOntology()");
				logger.log(Level.ALL, xmlOutputRemoveOntology);
				logger.log(Level.ALL,
						"*****************************************************");
			}

			return xmlOutputRemoveOntology;

		} catch (Exception e) {

			xmlOutputRemoveOntology += "<OUTPUT>\n";
			xmlOutputRemoveOntology += "\t<STATUS>-1</STATUS>\n";
			xmlOutputRemoveOntology += "\t<DESCRIPTION>\n\t" + e.getMessage()
					+ "\t</DESCRIPTION>\n";
			xmlOutputRemoveOntology += "</OUTPUT>\n";

			logger.log(Level.ALL,
					"*****************************************************");
			logger.log(Level.ALL,
					"strDN -> Error in Method: OntologiesServer.xmlRemoveOntology()");
			logger.log(Level.ALL, xmlOutputRemoveOntology);
			logger.log(Level.ALL,
					"*****************************************************");

			return xmlOutputRemoveOntology;
		}

	}

	public String xmlGetOntology(String xmlInputGetOntology)
			throws RemoteException {

		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlInputGetOntology.XmlWrapper inputWrapper = new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlInputGetOntology.XmlWrapper(
				xmlInputGetOntology, false);
		inputWrapper.wrap();

		String xmlOutputGetOntology = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			String gkResult = gateKeeper.strAllowAccess(inputWrapper
					.get_INPUT().get_CERTIFICATE());
			if (!gkResult.contentEquals("OK"))
				throw new Exception("Connecting to gatekeeper: " + gkResult);

			String xmlOntology = API_Ontologies_db.xmlGetOntology(inputWrapper
					.get_INPUT().get_IDONTOLOGY());
			if (!xmlOntology.equals("-1")) {
				xmlOutputGetOntology += "<OUTPUT>\n";
				xmlOutputGetOntology += "\t<STATUS>0</STATUS>\n";
				xmlOutputGetOntology += "\t<DESCRIPTION>OK</DESCRIPTION>\n";
				xmlOutputGetOntology += xmlOntology;
				xmlOutputGetOntology += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, "strDN -> Method: OntologiesServer.xmlGetOntology()");
				logger.log(Level.ALL, xmlInputGetOntology);
				logger.log(Level.ALL, "*****************************************************");

			} else {
				xmlOutputGetOntology += "<OUTPUT>\n";
				xmlOutputGetOntology += "\t<STATUS>-1</STATUS>\n";
				xmlOutputGetOntology += "\t<DESCRIPTION>Ontology "
						+ inputWrapper.get_INPUT().get_IDONTOLOGY()
						+ " not found in database.</DESCRIPTION>\n";
				xmlOutputGetOntology += "\t<ONTOLOGY>\n\t</ONTOLOGY>\n";
				xmlOutputGetOntology += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, "strDN -> Error in Method: OntologiesServer.xmlGetOntology()");
				logger.log(Level.ALL, xmlOutputGetOntology);
				logger.log(Level.ALL, "*****************************************************");
			}

			return xmlOutputGetOntology;

		} catch (Exception e) {

			xmlOutputGetOntology += "<OUTPUT>\n";
			xmlOutputGetOntology += "\t<STATUS>-1</STATUS>\n";
			xmlOutputGetOntology += "\t<DESCRIPTION>" + e.getMessage()
					+ "</DESCRIPTION>\n";
			xmlOutputGetOntology += "\t<ONTOLOGY>\n\t</ONTOLOGY>\n";
			xmlOutputGetOntology += "</OUTPUT>\n";

			logger.log(Level.ALL, "*****************************************************");
			logger.log(Level.ALL, "strDN -> Error in Method: OntologiesServer.xmlGetOntology()");
			logger.log(Level.ALL, xmlOutputGetOntology);
			logger.log(Level.ALL, "*****************************************************");

			return xmlOutputGetOntology;
		}

	}

	public String xmlGetAllOntologies(String xmlInputGetAllOntologies)
			throws RemoteException {

		trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlInputGetAllOntologies.XmlWrapper inputWrapper = new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlInputGetAllOntologies.XmlWrapper(
				xmlInputGetAllOntologies, false);
		inputWrapper.wrap();

		String xmlOutputAllOntologies = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {
			String gkResult = gateKeeper.strAllowAccess(inputWrapper.get_INPUT().get_CERTIFICATE());
			if (!gkResult.contentEquals("OK"))
				throw new Exception("Connecting to gatekeeper: " + gkResult);

			String xmlALLOntologies = API_Ontologies_db.xmlGetAllOntologies();
			if (!xmlALLOntologies.equals("-1")) {
				xmlOutputAllOntologies += "<OUTPUT>\n";
				xmlOutputAllOntologies += "\t<STATUS>0</STATUS>\n";
				xmlOutputAllOntologies += "\t<DESCRIPTION>OK</DESCRIPTION>\n";
				xmlOutputAllOntologies += xmlALLOntologies;
				xmlOutputAllOntologies += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, "strDN -> Method: OntologiesServer.xmlGetOntology()");
				logger.log(Level.ALL, xmlInputGetAllOntologies);
				logger.log(Level.ALL, "*****************************************************");

			} else {
				xmlOutputAllOntologies += "<OUTPUT>\n";
				xmlOutputAllOntologies += "\t<STATUS>0</STATUS>\n";
				xmlOutputAllOntologies += "\t<DESCRIPTION>" + xmlALLOntologies + "</DESCRIPTION>\n";
				xmlOutputAllOntologies += "\t<ONTOLOGIES></ONTOLOGIES>\n";
				xmlOutputAllOntologies += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, "strDN -> Error in Method: OntologiesServer.xmlGetAllOntologies()");
				logger.log(Level.ALL, xmlOutputAllOntologies);
				logger.log(Level.ALL, "*****************************************************");

			}

			return xmlOutputAllOntologies;

		} catch (Exception e) {

			xmlOutputAllOntologies += "<OUTPUT>\n";
			xmlOutputAllOntologies += "\t<STATUS>-1</STATUS>\n";
			xmlOutputAllOntologies += "\t<DESCRIPTION>\n\t" + e.getMessage() + "</DESCRIPTION>\n";
			xmlOutputAllOntologies += "\t<ONTOLOGIES></ONTOLOGIES>\n";
			xmlOutputAllOntologies += "</OUTPUT>\n";

			logger.log(Level.ALL,
					"*****************************************************");
			logger.log(Level.ALL, "strDN -> Error in Method: OntologiesServer.xmlGetAllOntologies()");
			logger.log(Level.ALL, xmlOutputAllOntologies);
			logger.log(Level.ALL,
					"*****************************************************");

			return xmlOutputAllOntologies;
		}

	}

	public String xmlIsActive(String xmlInputIsActive) throws RemoteException {
		String xmlOutputIsActive = "";
		try {

			trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlInputIsActive.XmlWrapper inputWrapper = new trencadis.infrastructure.services.OntologiesServer.impl.wrapper.xmlInputIsActive.XmlWrapper(
					xmlInputIsActive, false);
			inputWrapper.wrap();

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			if (!gkResult.contentEquals("OK"))
				throw new Exception("Connecting to gatekeeper: " + gkResult);

			xmlOutputIsActive += "<OUTPUT>\n";
			xmlOutputIsActive += "\t<STATUS>0</STATUS>\n";
			xmlOutputIsActive += "\t<DESCRIPTION></DESCRIPTION>\n";
			xmlOutputIsActive += "</OUTPUT>\n";

			return xmlOutputIsActive;
		} catch (Exception ex) {
			xmlOutputIsActive += "<OUTPUT>\n";
			xmlOutputIsActive += "\t<STATUS>-1</STATUS>\n";
			xmlOutputIsActive += "\t<DESCRIPTION>\n\t" + ex.toString()
					+ "</DESCRIPTION>\n";
			xmlOutputIsActive += "</OUTPUT>\n";

			logger.log(Level.ALL,
					"*****************************************************");
			logger.log(Level.ALL,
					"strDN -> Error in Method: OntologiesServer.xmlIsActive()");
			logger.log(Level.ALL, xmlOutputIsActive);
			logger.log(Level.ALL,
					"*****************************************************");

			return xmlOutputIsActive;
		}
	}

	public String xmlUpdateStorageServices(String xmlInputUpdateStorageServices)
			throws RemoteException {
		return "";
	}

	/* Get/Setters for the RPs */
	public OntologiesServerServiceInfo getOntologiesServerServiceInfo() {
		return RPs_OntologiesServerServiceInfo;
	}

	public void setOntologiesServerServiceInfo(
			OntologiesServerServiceInfo RPs_OntologiesServerServiceInfo) {
		this.RPs_OntologiesServerServiceInfo = RPs_OntologiesServerServiceInfo;
	}

	public void UpdateOntologiesServerServiceInfoRP() {
		try {
			OntologyInfo[] Ontologies = null;

			int iNOntologies = API_Ontologies_db.iNumberOfOntologies();
			if (iNOntologies == -1) {
				RPs_OntologiesServerServiceInfo.setNumberOfOntolofies(0);
				Ontologies = new OntologyInfo[0];
				RPs_OntologiesServerServiceInfo.setOntology(Ontologies);

				logger.log(Level.ALL,
						"*****************************************************");
				logger.log(Level.ALL,
						"strDN -> Method: OntologiesServer.UpdateOntologiesServerServiceInfoRP()");
				logger.log(Level.ALL, API_Ontologies_db.strGetError());
				logger.log(Level.ALL,
						"*****************************************************");

			} else {
				RPs_OntologiesServerServiceInfo
						.setNumberOfOntolofies(iNOntologies);

				int[] iId = new int[iNOntologies];
				String[] strDescripcion = new String[iNOntologies];

				iNOntologies = API_Ontologies_db
						.iUpdateOntologiesServerServiceInfo(iId, strDescripcion);
				if (iNOntologies == -1) {
					RPs_OntologiesServerServiceInfo.setNumberOfOntolofies(0);
					Ontologies = new OntologyInfo[0];
					RPs_OntologiesServerServiceInfo.setOntology(Ontologies);

					logger.log(Level.ALL,
							"*****************************************************");
					logger.log(Level.ALL,
							"strDN -> Method: OntologiesServer.UpdateOntologiesServerServiceInfoRP()");
					logger.log(Level.ALL, API_Ontologies_db.strGetError());
					logger.log(Level.ALL,
							"*****************************************************");
				} else {
					Ontologies = new OntologyInfo[iNOntologies];
					for (int i = 0; i < iNOntologies; i++) {
						Ontologies[i] = new OntologyInfo(iId[i],
								strDescripcion[i]);
					}
				}
			}

			// Update RP Properties
			RPs_OntologiesServerServiceInfo.setOntology(Ontologies);

		} catch (Exception e) {
			logger.log(Level.ALL,
					"*****************************************************");
			logger.log(Level.ALL,
					"strDN -> Method: OntologiesServer.UpdateOntologiesServerServiceInfoRP()");
			logger.log(Level.ALL, e.toString());
			logger.log(Level.ALL,
					"*****************************************************");
		}
	}

	/* Required by interface ResourceProperties */
	public ResourcePropertySet getResourcePropertySet() {
		return this.propSet;
	}

	public void IIS_Register() {
		try {
			// Inizialize IIS Register component ald register the RP
			API_IIS_Register = new IIS_Register();
			API_IIS_Register.register(this.registration_path);
		} catch (Exception e) {
			logger.log(Level.ALL,
					"Exception when trying to register in the IIS " + e);
		}
	}

}
