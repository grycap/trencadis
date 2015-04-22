package trencadis.infrastructure.services.DICOMStorage.impl;

import java.io.File;
import java.rmi.RemoteException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.io.FileUtils;
import org.globus.wsrf.*;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.impl.ReflectionResourceProperty;

import trencadis.common.util.CertUtils;
import trencadis.infrastructure.gateKeeper.CGateKeeper;
import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.BACKEND_PARAMETERS;
import trencadis.infrastructure.services.OntologiesServer.impl.OntologiesServerResource;
import trencadis.infrastructure.services.dicomstorage.indexer.Indexer;
import trencadis.infrastructure.services.dicomstorage.backend.BackEnd;
import trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS.IIS_Register;
import trencadis.infrastructure.services.stubs.DICOMStorage.DICOMStorageServiceInfo;
import trencadis.infrastructure.services.stubs.DICOMStorage.URIResourceInfo;
import trencadis.infrastructure.services.stubs.DICOMStorage.OntologyInfo;

public class DICOMStorageResource implements Resource, ResourceIdentifier,
		ResourceProperties {

	// It indicates if is in debug mode
	public static boolean bIsDebug = false;

	/*******************************************************************/
	/********** Local parameters ***************************************/
	private String center_name;
	private String base_path;
	private String path_cattrustcert;
	private String tmp_dir;
	private String xmlOntology;
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
	/****** TRENCADIS Java API SQL Ontologies Database Component *******/
	IIS_Register API_IIS_Register;
	/*******************************************************************/

	/*******************************************************************/
	/********************** Indexer Component **************************/
	private Indexer API_Indexer;

	/*******************************************************************/
	/********************** BackEnd Component *************************/
	private BackEnd API_BackEnd;

	private ResourcePropertySet propSet;
	/* Resource properties */
	private DICOMStorageServiceInfo RPs_DICOMStorageServiceInfo;
	private ResourceKey key;


	// This method is executed in the first access to the resource
	public void initialize(String strCertificate, ResourceKey key,
			String baseURIresource, String URIFactory, String IDOntology,
			String ontologyDescription, String xmlOntology) throws Exception {

		// key of the resource
		this.key = key;

		// Configuration load
		//File configFile = new File("/home/trencadis/DICOMStorage/DICOMStorageConfig.xml");
		File configFile = new File("/opt/trencadis/DICOMStorageConfig.xml");
		
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.XmlWrapper configWrapper = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.XmlWrapper(
				configFile, false);
		configWrapper.wrap();

		/***********************************************************************************/
		/****** Parametros Generales de Configuración del Servicio Ontologies Server *******/
		/***********************************************************************************/
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.SERVICE_PARAMETERS localParameters = configWrapper
				.get_CONFIGURATION().get_SERVICE_PARAMETERS();
		// Center identifier can not contain white spaces"
		this.center_name = localParameters.get_CENTERNAME();
		this.base_path = localParameters.get_BASE_PATH();
		this.path_cattrustcert = localParameters.get_PATH_CATRUSTCERT();
		this.tmp_dir = localParameters.get_TMP_DIR();
		this.xmlOntology = xmlOntology;
		/***********************************************************************************/

		/**************************************************************/
		/******************** COMPONENT GATEKEEPER *******************/
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.GATEKEEPER_PARAMETERS gatekeeperParameters = configWrapper
				.get_CONFIGURATION().get_GATEKEEPER_PARAMETERS();
		// Initialize the gatekeeper
		this.gateKeeper = new CGateKeeper("org.postgresql.Driver",
				"jdbc:storageDICOMFactoryGatekeeperPool:",
				"jdbc:postgresql://", gatekeeperParameters.get_HOST_NAME(),
				gatekeeperParameters.get_HOST_PORT(),
				gatekeeperParameters.get_DB_NAME(),
				gatekeeperParameters.get_USER(),
				gatekeeperParameters.get_PASSWORD(), this.path_cattrustcert);
		// Test the privilegies
		String strDN = gateKeeper.strExtractDN(strCertificate);
		System.out.println(gatekeeperParameters.get_HOST_NAME() + "\n" + gatekeeperParameters.get_DB_NAME() + "\n" + strDN);
		String gkResult = gateKeeper.strAllowAccess(strCertificate);
		if (!gkResult.contentEquals("OK"))
			throw new Exception("Inicizialize Resource. User:" + strDN
					+ ". Connecting to gatekeeper: " + gkResult);
		/**************************************************************/

		/**************************************************************/
		/******************** IIS Parameters ***********************/
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.INDEX_SERVICE_PARAMETERS indexServiceParameters = configWrapper
				.get_CONFIGURATION().get_INDEX_SERVICE_PARAMETERS();
		this.v_url_iis_central = new Vector();
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.URL_IIS URL_IIS;
		Iterator it_urls = configWrapper.get_CONFIGURATION()
				.get_INDEX_SERVICE_PARAMETERS().get_LIST_OF_URL_IIS()
				.getAll_URL_IIS();
		while (it_urls.hasNext()) {
			URL_IIS = (trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.URL_IIS) it_urls
					.next();
			v_url_iis_central.add(URL_IIS.getValue());
		}

		this.registration_path = indexServiceParameters.get_REGISTRATION_PATH();
		/****************************************************************/

		/**************************************************************/
		/******************** LOGGER COMPONENT ***********************/
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.LOGGER_PARAMETERS loggerParameters = configWrapper
				.get_CONFIGURATION().get_LOGGER_PARAMETERS();
		FileHandler logFile = new FileHandler(loggerParameters.get_LOG_PATH());
		logger.addHandler(logFile);
		logger.setLevel(Level.ALL);
		SimpleFormatter formatter = new SimpleFormatter();
		logFile.setFormatter(formatter);
		/**************************************************************/

		/**************************************************************/
		/******************** INDEXER COMPONENT ***********************/
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.INDEXER_PARAMETERS indexerParameters = configWrapper
				.get_CONFIGURATION().get_INDEXER_PARAMETERS();
		API_Indexer = new Indexer(indexerParameters.str_to_XML());
		int iErr = API_Indexer.iAddStructure(xmlOntology);
		if (iErr != 0)
			throw new Exception("Error creating structure IDOntology="
					+ IDOntology + ". " + API_Indexer.strGetError());
		/**************************************************************/

		/**************************************************************/
		/******************** BACKEND COMPONENT ***********************/
		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.config.BACKEND_PARAMETERS BackEndParameters = configWrapper
				.get_CONFIGURATION().get_BACKEND_PARAMETERS();
		API_BackEnd = new BackEnd(BackEndParameters.str_to_XML());

		/**************************************************************/
		/********************* Initialise RPs *************************/
		this.propSet = new SimpleResourcePropertySet(
				DICOMStorageQNames.RESOURCE_PROPERTIES);

		try {
			ResourceProperty DICOMStorageServiceInfo = new ReflectionResourceProperty(
					DICOMStorageQNames.RP_DICOMSTORAGESERVICEINFO,
					"DICOMStorageServiceInfo", this);
			this.propSet.add(DICOMStorageServiceInfo);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		String strSubjectDN = "";
		if (!this.bIsDebug) {
			X509Certificate uUIDCertChainX509 = CertUtils
					.getServCertificateChain();
			strSubjectDN = CertUtils.getPublicSubjectOU(uUIDCertChainX509);
		} else {
			strSubjectDN = "Test";
		}
		// Update Static Values of RPs
		RPs_DICOMStorageServiceInfo = new DICOMStorageServiceInfo();
		RPs_DICOMStorageServiceInfo.setDomain(strSubjectDN);
		RPs_DICOMStorageServiceInfo.setIDCenter(localParameters.get_IDCENTER());
		RPs_DICOMStorageServiceInfo.setCenterName(localParameters
				.get_CENTERNAME().replace(" ", "_"));
		RPs_DICOMStorageServiceInfo.setURIFactory(URIFactory);
		RPs_DICOMStorageServiceInfo.setTypeIndexer(indexerParameters
				.get_INDEXER_TYPE());

		URIResourceInfo URI_resource = new URIResourceInfo();
		URI_resource.setURI(baseURIresource);
		URI_resource.setKey(IDOntology);
		RPs_DICOMStorageServiceInfo.setURIResource(URI_resource);

		OntologyInfo Ontology = new OntologyInfo();
		Ontology.setIDOntology(Integer.parseInt(IDOntology));
		Ontology.setDescription(ontologyDescription);
		RPs_DICOMStorageServiceInfo.setOntology(Ontology);
	}

	/* Service Implementation */

	public String xmlAddReport(String xmlInputAddReport) throws RemoteException {

		String strDN = "";
		String strIDOntology = "";

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputAddReport.XmlWrapper input = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputAddReport.XmlWrapper(
				xmlInputAddReport, false);

		input.wrap();

		String xmlOutputAddReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			String CERTIFICATE = input.get_INPUT().get_CERTIFICATE();
			strDN = gateKeeper.strExtractDN(CERTIFICATE);
			System.out.println(strDN);
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			if (!gkResult.contentEquals("OK")) {
				xmlOutputAddReport += "<OUTPUT>\n";
				xmlOutputAddReport += "\t<STATUS>-1</STATUS>\n";
				xmlOutputAddReport += "\t<DESCRIPTION>\n\t" + gkResult
						+ "\t</DESCRIPTION>\n";
				xmlOutputAddReport += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, strDN
								+ " -> Error in Method: Resouce DICOMSrorage.xmlAddReport() IDOntology = "
								+ strIDOntology);
				logger.log(Level.ALL, gkResult);
				logger.log(Level.ALL, "*****************************************************");
				return xmlOutputAddReport;

			}

			strIDOntology = input.get_INPUT().get_IDONTOLOGY();
			if (!String.valueOf(
					this.RPs_DICOMStorageServiceInfo.getOntology()
							.getIDOntology()).equals(strIDOntology)) {
				xmlOutputAddReport += "<OUTPUT>\n";
				xmlOutputAddReport += "\t<STATUS>-1</STATUS>\n";
				xmlOutputAddReport += "\t<DESCRIPTION>\n\t"
						+ "The type of the report (IDOntology="
						+ strIDOntology
						+ ") does not correspond to the type managed by this StorageDICOM (IDOntology="
						+ this.RPs_DICOMStorageServiceInfo.getOntology()
								.getIDOntology() + "\t</DESCRIPTION>\n";
				xmlOutputAddReport += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, strDN
								+ " -> Error in Method: Resouce DICOMSrorage.xmlAddReport() IDOntology = "
								+ strIDOntology);
				logger.log(Level.ALL, xmlOutputAddReport);
				logger.log(Level.ALL, "*****************************************************");
				return xmlOutputAddReport;
			}

			// INSERT INTO BackEnd
			String xmlDSR_REPORT = input.get_INPUT().get_DICOM_SR()
					.get_xml_DICOM_SR();
			xmlDSR_REPORT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ xmlDSR_REPORT;

			String dicom_sr_name = this.tmp_dir + "/"
					+ input.get_INPUT().get_IDTRENCADISREPORT() + ".tmp";
			File dicom_sr__file = new File(dicom_sr_name);
			FileUtils.writeStringToFile(dicom_sr__file, xmlDSR_REPORT);

			int iErr = API_BackEnd.iAddFile(input.get_INPUT()
					.get_IDTRENCADISREPORT(), dicom_sr__file, CERTIFICATE);
			if (iErr == 0) {
				// IF INSERT INTO BACKEND, INSERT INTO INDEXER
				iErr = this.API_Indexer.iAddReport(xmlDSR_REPORT);
				if (iErr == 0) {
					xmlOutputAddReport += "<OUTPUT>\n";
					xmlOutputAddReport += "\t<STATUS>0</STATUS>\n";
					xmlOutputAddReport += "\t<DESCRIPTION>OK</DESCRIPTION>\n";
					xmlOutputAddReport += "</OUTPUT>\n";

					logger.log(Level.ALL, "*****************************************************");
					logger.log(Level.ALL, strDN
									+ " -> Method: Resouce DICOMSrorage.xmlAddReport() IDOntology = "
									+ strIDOntology);
					logger.log(Level.ALL, xmlOutputAddReport);
					logger.log(Level.ALL, "*****************************************************");
				} else {
					String strError = "";
					iErr = API_BackEnd.iDeleteFile(input.get_INPUT()
							.get_IDTRENCADISREPORT(), CERTIFICATE);
					if (iErr != 0)
						strError = "BackEnd Error: "
								+ API_BackEnd.strGetError() + ".\n";

					xmlOutputAddReport += "<OUTPUT>\n";
					xmlOutputAddReport += "\t<STATUS>-1</STATUS>\n";
					xmlOutputAddReport += "\t<DESCRIPTION>\n\t" + strError
							+ "Indexer Error: " + this.API_Indexer.strGetError()
							+ "\t</DESCRIPTION>\n";
					xmlOutputAddReport += "</OUTPUT>\n";

					logger.log(Level.ALL, "*****************************************************");
					logger.log(Level.ALL, strDN
									+ " -> Error in Method API_Indexer: Resouce DICOMSrorage.xmlAddReportID() IDOntology = "
									+ strIDOntology);
					logger.log(Level.ALL, xmlOutputAddReport);
					logger.log(Level.ALL, "*****************************************************");
				}
			} else {
				xmlOutputAddReport += "<OUTPUT>\n";
				xmlOutputAddReport += "\t<STATUS>-1</STATUS>\n";
				xmlOutputAddReport += "\t<DESCRIPTION>\n\t"
						+ this.API_BackEnd.strGetError() + "\t</DESCRIPTION>\n";
				xmlOutputAddReport += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL,strDN
								+ " -> Method: Resouce DICOMSorage.xmlDownloadReportID() IDOntology = "
								+ strIDOntology);
				logger.log(Level.ALL, xmlOutputAddReport);
				logger.log(Level.ALL, "*****************************************************");
			}

			return xmlOutputAddReport;

		} catch (Exception e) {
			xmlOutputAddReport += "<OUTPUT>\n";
			xmlOutputAddReport += "\t<STATUS>-1</STATUS>\n";
			xmlOutputAddReport += "\t<DESCRIPTION>\n\t" + e.getMessage()
					+ "\t</DESCRIPTION>\n";
			xmlOutputAddReport += "</OUTPUT>\n";

			logger.log(Level.ALL, "*****************************************************");
			logger.log(Level.ALL,strDN
							+ " -> Method: Resouce DICOMSrorage.xmlDownloadReport() IDOntology = "
							+ strIDOntology);
			logger.log(Level.ALL, xmlOutputAddReport);
			logger.log(Level.ALL, "*****************************************************");

			return xmlOutputAddReport;
		}

	}

	public String xmlRemoveReport(String xmlInputRemoveReport)
			throws RemoteException {

		String strDN = "";
		String strIDOntology = "";

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputRemoveReport.XmlWrapper inputWrapper = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputRemoveReport.XmlWrapper(
				xmlInputRemoveReport, false);
		inputWrapper.wrap();

		String xmlOutputRemoveReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

		try {

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			strDN = gateKeeper.strExtractDN(CERTIFICATE);
			System.out.println(strDN);
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			if (!gkResult.contentEquals("OK")) {
				xmlOutputRemoveReport += "<OUTPUT>\n";
				xmlOutputRemoveReport += "\t<STATUS>-1</STATUS>\n";
				xmlOutputRemoveReport += "\t<DESCRIPTION>\n\t" + gkResult
						+ "\t</DESCRIPTION>\n";
				xmlOutputRemoveReport += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL,strDN
								+ " -> Method: Resouce DICOMSrorage.xmlDownloadReport() IDOntology = "
								+ strIDOntology);
				logger.log(Level.ALL, xmlOutputRemoveReport);
				logger.log(Level.ALL, "*****************************************************");
				return xmlOutputRemoveReport;

			}
			strIDOntology = inputWrapper.get_INPUT().get_IDONTOLOGY();
			if (!String.valueOf(
					this.RPs_DICOMStorageServiceInfo.getOntology()
							.getIDOntology()).equals(strIDOntology)) {
				xmlOutputRemoveReport += "<OUTPUT>\n";
				xmlOutputRemoveReport += "\t<STATUS>-1</STATUS>\n";
				xmlOutputRemoveReport += "\t<DESCRIPTION>\n\t"
						+ "The type of the report (IDOntology="
						+ strIDOntology
						+ ") does not correspond to the type managed by this StorageDICOM (IDOntology="
						+ this.RPs_DICOMStorageServiceInfo.getOntology()
								.getIDOntology() + "\t</DESCRIPTION>\n";
				xmlOutputRemoveReport += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL,strDN
								+ " -> Method: Resouce DICOMSrorage.xmlDownloadReport() IDOntology = "
								+ strIDOntology);
				logger.log(Level.ALL, xmlOutputRemoveReport);
				logger.log(Level.ALL, "*****************************************************");
				return xmlOutputRemoveReport;
			}

			// REMOVE FROM BackEnd
			int iErr = this.API_Indexer.iDeleteReport(inputWrapper.get_INPUT()
					.get_IDTRENCADISREPORT(), xmlOntology);
			if (iErr == 0) {
				iErr = API_BackEnd.iDeleteFile(inputWrapper.get_INPUT()
						.get_IDTRENCADISREPORT(), CERTIFICATE);
				if (iErr == 0) {
					xmlOutputRemoveReport += "<OUTPUT>\n";
					xmlOutputRemoveReport += "\t<STATUS>0</STATUS>\n";
					xmlOutputRemoveReport += "\t<DESCRIPTION>\n\tOK\t</DESCRIPTION>\n";
					xmlOutputRemoveReport += "</OUTPUT>\n";

					logger.log(Level.ALL, "*****************************************************");
					logger.log(Level.ALL,strDN
									+ " -> Method: Resouce DICOMStorage.xmlDownloadReport() IDOntology = "
									+ strIDOntology);
					logger.log(Level.ALL, xmlOutputRemoveReport);
					logger.log(Level.ALL, "*****************************************************");
				} else {
					String strError = "";
					iErr = API_BackEnd.iDeleteFile(inputWrapper.get_INPUT()
							.get_IDTRENCADISREPORT(), CERTIFICATE);
					if (iErr != 0)
						strError = "BackEnd Error: "
								+ API_BackEnd.strGetError() + ".\n";

					xmlOutputRemoveReport += "<OUTPUT>\n";
					xmlOutputRemoveReport += "\t<STATUS>-1</STATUS>\n";
					xmlOutputRemoveReport += "\t<DESCRIPTION>\n\t" + strError
							+ "\n\tIndexer Error: " + this.API_BackEnd.strGetError()
							+ "\t</DESCRIPTION>\n";
					xmlOutputRemoveReport += "</OUTPUT>\n";

					logger.log(Level.ALL, "*****************************************************");
					logger.log(Level.ALL,strDN
									+ " -> Method: Resouce DICOMStorage.xmlDownloadReport() IDOntology = "
									+ strIDOntology);
					logger.log(Level.ALL, xmlOutputRemoveReport);
					logger.log(Level.ALL, "*****************************************************");
				}
			} else {
				xmlOutputRemoveReport += "<OUTPUT>\n";
				xmlOutputRemoveReport += "\t<STATUS>-1</STATUS>\n";
				xmlOutputRemoveReport += "\t<DESCRIPTION>\n\t"
						+ this.API_Indexer.strGetError() + "\t</DESCRIPTION>\n";
				xmlOutputRemoveReport += "</OUTPUT>\n";

				logger.log(Level.ALL, "******Indexer***********************************************");
				logger.log(Level.ALL,strDN
								+ " -> Method: Resouce DICOMStorage.xmlDownloadReport() IDOntology = "
								+ strIDOntology);
				logger.log(Level.ALL, xmlOutputRemoveReport);
				logger.log(Level.ALL, "*****************************************************");
			}

			return xmlOutputRemoveReport;

		} catch (Exception e) {
			xmlOutputRemoveReport += "<OUTPUT>\n";
			xmlOutputRemoveReport += "\t<STATUS>-1</STATUS>\n";
			xmlOutputRemoveReport += "\t<DESCRIPTION>\n\t" + e.getMessage()
					+ "\n\t</DESCRIPTION>\n";
			xmlOutputRemoveReport += "</OUTPUT>\n";

			logger.log(Level.ALL, "*****************************************************");
			logger.log(Level.ALL,strDN
							+ " -> Method: Resouce DICOMStorage.xmlDownloadReport() IDOntology = "
							+ strIDOntology);
			logger.log(Level.ALL, xmlOutputRemoveReport);
			logger.log(Level.ALL, "*****************************************************");

			return xmlOutputRemoveReport;
		}
	}

	public String xmlDownloadReport(String xmlInputDownloadReport)
			throws RemoteException {

		String xmlDICOM_SR = "";
		String strDN = "";
		String strIDOntology = "";

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputDownloadReport.XmlWrapper inputWrapper = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputDownloadReport.XmlWrapper(
				xmlInputDownloadReport, false);
		inputWrapper.wrap();

		String xmlOutputDownloadReport = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

		try {

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			System.out.println(gkResult);
			strDN = gateKeeper.strExtractDN(CERTIFICATE);
			if (!gkResult.contentEquals("OK")) {
				xmlOutputDownloadReport += "<OUTPUT>\n";
				xmlOutputDownloadReport += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadReport += "\t<DESCRIPTION>\n\t" + gkResult + "\n\t</DESCRIPTION>\n";
				xmlOutputDownloadReport += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL,strDN
								+ " -> Error in Method: Resouce DICOMStorage.xmlDownloadReport() IDOntology = "
								+ strIDOntology);
				logger.log(Level.ALL, gkResult);
				logger.log(Level.ALL, "*****************************************************");
				return xmlOutputDownloadReport;

			}
			strIDOntology = inputWrapper.get_INPUT().get_IDONTOLOGY();
			if (!String.valueOf(this.RPs_DICOMStorageServiceInfo.getOntology()
							.getIDOntology()).equals(strIDOntology)) {
				xmlOutputDownloadReport += "<OUTPUT>\n";
				xmlOutputDownloadReport += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadReport += "\t<DESCRIPTION>\n\t"
						+ "The type of the report (IDOntology="
						+ strIDOntology
						+ ") does not correspond to the type managed by this StorageDICOM (IDOntology="
						+ this.RPs_DICOMStorageServiceInfo.getOntology()
								.getIDOntology() + "\t</DESCRIPTION>\n";
				xmlOutputDownloadReport += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL,strDN
								+ " -> Error in Method: Resouce DICOMStorage.xmlDownloadReport() IDOntology = "
								+ strIDOntology);
				logger.log(Level.ALL, xmlOutputDownloadReport);
				logger.log(Level.ALL, "*****************************************************");
				return xmlOutputDownloadReport;
			}

			// Get DICOM_SR FROM BackEnd
			xmlDICOM_SR = this.API_BackEnd.xmlGetDICOMSRFile(inputWrapper
					.get_INPUT().get_IDTRENCADISREPORT(), CERTIFICATE);
			if (xmlDICOM_SR != null) {
				xmlOutputDownloadReport += "<OUTPUT>\n";
				xmlOutputDownloadReport += "\t<STATUS>0</STATUS>\n";
				xmlOutputDownloadReport += "\t<DESCRIPTION>\n\tOK\t</DESCRIPTION>\n";
				xmlOutputDownloadReport += "\t<DICOM_SR>\n";
				xmlOutputDownloadReport += "\t<xml_DICOM_SR>"
						+ xmlDICOM_SR.replace(
								"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
								"") + "</xml_DICOM_SR>\n";
				xmlOutputDownloadReport += "\t</DICOM_SR>\n";
				xmlOutputDownloadReport += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL,strDN
								+ " -> Method: Resouce DICOMStorage.xmlDownloadReport() IDOntology = "
								+ strIDOntology);
				logger.log(Level.ALL, xmlOutputDownloadReport);
				logger.log(Level.ALL, "*****************************************************");
			} else {
				xmlOutputDownloadReport += "<OUTPUT>\n";
				xmlOutputDownloadReport += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadReport += "\t<DESCRIPTION>\n"
						+ "\tAPI_BackEnd Error: "
						+ this.API_BackEnd.strGetError() + "\n\t</DESCRIPTION>\n";
				xmlOutputDownloadReport += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL,strDN
								+ " -> Method: Resouce DICOMStorage.xmlDownloadReport() IDOntology = "
								+ strIDOntology);
				logger.log(Level.ALL, xmlOutputDownloadReport);
				logger.log(Level.ALL, "*****************************************************");
			}
			return xmlOutputDownloadReport;

		} catch (Exception e) {
			xmlOutputDownloadReport += "<OUTPUT>\n";
			xmlOutputDownloadReport += "\t<STATUS>-1</STATUS>\n";
			xmlOutputDownloadReport += "\t<DESCRIPTION>\n\t" + e.getMessage() + "\n\t</DESCRIPTION>\n";
			xmlOutputDownloadReport += "</OUTPUT>\n";

			logger.log(Level.ALL, "*****************************************************");
			logger.log(Level.ALL,strDN
							+ " -> Method: Resouce DICOMSrorage.xmlDownloadReport() IDOntology = "
							+ strIDOntology);
			logger.log(Level.ALL, xmlOutputDownloadReport);
			logger.log(Level.ALL, "*****************************************************");

			return xmlOutputDownloadReport;
		}

	}
	
	public String xmlDownloadAllReports(String xmlInputDownloadReport)
			throws RemoteException {
				
		String xmlDICOM_SR = "";
		String xmlDICOM_SR_IDS = "";
		String strDN = "";

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputDownloadAllReports.XmlWrapper inputWrapper =
				new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputDownloadAllReports.XmlWrapper(
				xmlInputDownloadReport, false);
		inputWrapper.wrap();

		String xmlOutputDownloadAllReports = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			strDN = gateKeeper.strExtractDN(CERTIFICATE);
			if (!gkResult.contentEquals("OK")) {
				xmlOutputDownloadAllReports += "<OUTPUT>\n";
				xmlOutputDownloadAllReports += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadAllReports += "\t<DESCRIPTION>\n\t" + gkResult
										 + "\t</DESCRIPTION>\n";
				xmlOutputDownloadAllReports += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, gkResult);
				logger.log(Level.ALL, "*****************************************************");
				return xmlOutputDownloadAllReports;
			}
			
			// Get DICOM_SR FROM BackEnd
			xmlDICOM_SR_IDS = this.API_Indexer.iGetDICOMIDs();
			xmlDICOM_SR = this.API_BackEnd.xmlGetAllDICOMSRFiles(xmlDICOM_SR_IDS, CERTIFICATE);
			
			if (xmlDICOM_SR != null) {
				xmlOutputDownloadAllReports += "<OUTPUT>\n";
				xmlOutputDownloadAllReports += "\t<STATUS>0</STATUS>\n";
				xmlOutputDownloadAllReports += "\t<DESCRIPTION>\n\tOK\t</DESCRIPTION>\n";	
				xmlOutputDownloadAllReports += xmlDICOM_SR;
				xmlOutputDownloadAllReports += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, xmlOutputDownloadAllReports);
				logger.log(Level.ALL, "*****************************************************");
			} else {
				xmlOutputDownloadAllReports += "<OUTPUT>\n";
				xmlOutputDownloadAllReports += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadAllReports += "\t<DESCRIPTION>\n\t"
						+ this.API_BackEnd.strGetError() + "API_BackEnd Error:"
						+ this.API_BackEnd.strGetError() + "\t</DESCRIPTION>\n";
				xmlOutputDownloadAllReports += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, xmlOutputDownloadAllReports);
				logger.log(Level.ALL, "*****************************************************");
			}
			return xmlOutputDownloadAllReports;

		} catch (Exception e) {
			xmlOutputDownloadAllReports += "<OUTPUT>\n";
			xmlOutputDownloadAllReports += "\t<STATUS>-1</STATUS>\n";
			xmlOutputDownloadAllReports += "\t<DESCRIPTION>\n\t" + e.getMessage() + "\t</DESCRIPTION>\n";
			xmlOutputDownloadAllReports += "</OUTPUT>\n";

			logger.log(Level.ALL, "*****************************************************");
			logger.log(Level.ALL, xmlOutputDownloadAllReports);
			logger.log(Level.ALL, "*****************************************************");

			return xmlOutputDownloadAllReports;
		}
	}
	
	public String xmlDownloadAllReportsByOntology(String xmlInputDownloadReport)
			throws RemoteException {
				
		String xmlDICOM_SR = "";
		String xmlDICOM_SR_IDS = "";
		String strDN = "";

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputDownloadAllReportsByOntology.XmlWrapper inputWrapper =
				new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputDownloadAllReportsByOntology.XmlWrapper(
				xmlInputDownloadReport, false);
		inputWrapper.wrap();

		String xmlOutputDownloadAllReports = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			strDN = gateKeeper.strExtractDN(CERTIFICATE);
			if (!gkResult.contentEquals("OK")) {
				xmlOutputDownloadAllReports += "<OUTPUT>\n";
				xmlOutputDownloadAllReports += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadAllReports += "\t<DESCRIPTION>\n\t" + gkResult
										 + "\t</DESCRIPTION>\n";
				xmlOutputDownloadAllReports += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, gkResult);
				logger.log(Level.ALL, "*****************************************************");
				return xmlOutputDownloadAllReports;
			}
			
			// Get DICOM_SR FROM BackEnd
			xmlDICOM_SR_IDS = this.API_Indexer.iGetDICOMIDsByOntology(inputWrapper.get_INPUT().get_IDONTOLOGY());
			xmlDICOM_SR = this.API_BackEnd.xmlGetAllDICOMSRFiles(xmlDICOM_SR_IDS, CERTIFICATE);
			if (xmlDICOM_SR != null) {
				xmlOutputDownloadAllReports += "<OUTPUT>\n";
				xmlOutputDownloadAllReports += "\t<STATUS>0</STATUS>\n";
				xmlOutputDownloadAllReports += "\t<DESCRIPTION>\n\tOK\t</DESCRIPTION>\n";	
				xmlOutputDownloadAllReports += xmlDICOM_SR;
				xmlOutputDownloadAllReports += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, xmlOutputDownloadAllReports);
				logger.log(Level.ALL, "*****************************************************");
			} else {
				xmlOutputDownloadAllReports += "<OUTPUT>\n";
				xmlOutputDownloadAllReports += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadAllReports += "\t<DESCRIPTION>\n\t"
						+ this.API_BackEnd.strGetError() + "API_BackEnd Error:"
						+ this.API_BackEnd.strGetError() + "\t</DESCRIPTION>\n";
				xmlOutputDownloadAllReports += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, xmlOutputDownloadAllReports);
				logger.log(Level.ALL, "*****************************************************");
			}
			return xmlOutputDownloadAllReports;

		} catch (Exception e) {
			xmlOutputDownloadAllReports += "<OUTPUT>\n";
			xmlOutputDownloadAllReports += "\t<STATUS>-1</STATUS>\n";
			xmlOutputDownloadAllReports += "\t<DESCRIPTION>\n\t" + e.getMessage() + "\t</DESCRIPTION>\n";
			xmlOutputDownloadAllReports += "</OUTPUT>\n";

			logger.log(Level.ALL, "*****************************************************");
			logger.log(Level.ALL, xmlOutputDownloadAllReports);
			logger.log(Level.ALL, "*****************************************************");

			return xmlOutputDownloadAllReports;
		}
	}
	
	public String xmlDownloadAllReportsID(String xmlInputDownloadReport)
			throws RemoteException {
				
		String xmlDICOM_SR_ID = "";
		String strDN = "";

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputDownloadAllReportsID.XmlWrapper inputWrapper =
				new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputDownloadAllReportsID.XmlWrapper(
				xmlInputDownloadReport, false);
		inputWrapper.wrap();

		String xmlOutputDownloadAllReportsID = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			strDN = gateKeeper.strExtractDN(CERTIFICATE);
			if (!gkResult.contentEquals("OK")) {
				xmlOutputDownloadAllReportsID += "<OUTPUT>\n";
				xmlOutputDownloadAllReportsID += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadAllReportsID += "\t<DESCRIPTION>\n\t" + gkResult
										 	   + "\t</DESCRIPTION>\n";
				xmlOutputDownloadAllReportsID += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, gkResult);
				logger.log(Level.ALL, "*****************************************************");
				return xmlOutputDownloadAllReportsID;
			}

			// Get all DICOM_SR_ID FROM Indexer
			xmlDICOM_SR_ID = this.API_Indexer.iGetDICOMIDs();
			if (xmlDICOM_SR_ID != null) {
				xmlOutputDownloadAllReportsID += "<OUTPUT>\n";
				xmlOutputDownloadAllReportsID += "\t<STATUS>0</STATUS>\n";
				xmlOutputDownloadAllReportsID += "\t<DESCRIPTION>OK</DESCRIPTION>\n";
				xmlOutputDownloadAllReportsID += "\t<BACKEND_PARAMETERS>\n";
				xmlOutputDownloadAllReportsID += this.API_BackEnd.iGetBackEndParameters();
				xmlOutputDownloadAllReportsID += "\t</BACKEND_PARAMETERS>\n";
				xmlOutputDownloadAllReportsID += "\t<DICOM_REPORTS_ID>\n";
				xmlOutputDownloadAllReportsID += "\t\t<DICOM_SR_ID>";
				xmlOutputDownloadAllReportsID += xmlDICOM_SR_ID.replaceAll(",","</DICOM_SR_ID>\n\t\t<DICOM_SR_ID>");
				xmlOutputDownloadAllReportsID += "</DICOM_SR_ID>\n";
				xmlOutputDownloadAllReportsID += "\t</DICOM_REPORTS_ID>\n";
				xmlOutputDownloadAllReportsID += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, xmlOutputDownloadAllReportsID);
				logger.log(Level.ALL, "*****************************************************");
			} else {
				
				xmlOutputDownloadAllReportsID += "<OUTPUT>\n";
				xmlOutputDownloadAllReportsID += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadAllReportsID += "\t<DESCRIPTION>\n\t"
						+ this.API_Indexer.strGetError() + "API_Indexer Error:"
						+ this.API_Indexer.strGetError() + "\t</DESCRIPTION>\n";
				xmlOutputDownloadAllReportsID += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, xmlOutputDownloadAllReportsID);
				logger.log(Level.ALL, "*****************************************************");
			}
			return xmlOutputDownloadAllReportsID;

		} catch (Exception e) {
			xmlOutputDownloadAllReportsID += "<OUTPUT>\n";
			xmlOutputDownloadAllReportsID += "\t<STATUS>-1</STATUS>\n";
			xmlOutputDownloadAllReportsID += "\t<DESCRIPTION>\n\t" + e.getMessage() + "\t</DESCRIPTION>\n";
			xmlOutputDownloadAllReportsID += "</OUTPUT>\n";

			logger.log(Level.ALL, "*****************************************************");
			logger.log(Level.ALL, xmlOutputDownloadAllReportsID);
			logger.log(Level.ALL, "*****************************************************");

			return xmlOutputDownloadAllReportsID;
		}
	}
	
	public String xmlDownloadAllReportsIDByOntology(String xmlInputDownloadReport)
			throws RemoteException {
				
		String xmlDICOM_SR_ID = "";
		String strDN = "";

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputDownloadAllReportsIDByOntology.XmlWrapper inputWrapper =
				new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputDownloadAllReportsIDByOntology.XmlWrapper(
				xmlInputDownloadReport, false);
		inputWrapper.wrap();

		String xmlOutputDownloadAllReportsID = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			strDN = gateKeeper.strExtractDN(CERTIFICATE);
			if (!gkResult.contentEquals("OK")) {
				xmlOutputDownloadAllReportsID += "<OUTPUT>\n";
				xmlOutputDownloadAllReportsID += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadAllReportsID += "\t<DESCRIPTION>\n\t" + gkResult
										 	   + "\t</DESCRIPTION>\n";
				xmlOutputDownloadAllReportsID += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, gkResult);
				logger.log(Level.ALL, "*****************************************************");
				return xmlOutputDownloadAllReportsID;
			}
			
			// Get all DICOM_SR_ID of a given ontology FROM Indexer
			xmlDICOM_SR_ID = this.API_Indexer.iGetDICOMIDsByOntology(inputWrapper.get_INPUT().get_IDONTOLOGY());
			if (xmlDICOM_SR_ID != null) {
				xmlOutputDownloadAllReportsID += "<OUTPUT>\n";
				xmlOutputDownloadAllReportsID += "\t<STATUS>0</STATUS>\n";
				xmlOutputDownloadAllReportsID += "\t<DESCRIPTION>OK</DESCRIPTION>\n";
				xmlOutputDownloadAllReportsID += "\t<CENTERNAME>";
				xmlOutputDownloadAllReportsID += this.center_name;
				xmlOutputDownloadAllReportsID += "</CENTERNAME>\n";
				xmlOutputDownloadAllReportsID += "\t<BACKEND_PARAMETERS>\n";
				xmlOutputDownloadAllReportsID += this.API_BackEnd.iGetBackEndParameters();
				xmlOutputDownloadAllReportsID += "\t</BACKEND_PARAMETERS>\n";
				xmlOutputDownloadAllReportsID += "\t<DICOM_REPORTS_ID>\n";
				xmlOutputDownloadAllReportsID += "\t\t<DICOM_SR_ID>";
				xmlOutputDownloadAllReportsID += xmlDICOM_SR_ID.replaceAll(",","</DICOM_SR_ID>\n\t\t<DICOM_SR_ID>");
				xmlOutputDownloadAllReportsID += "</DICOM_SR_ID>\n";
				xmlOutputDownloadAllReportsID += "\t</DICOM_REPORTS_ID>\n";
				xmlOutputDownloadAllReportsID += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, xmlOutputDownloadAllReportsID);
				logger.log(Level.ALL, "*****************************************************");
			} else {
				
				xmlOutputDownloadAllReportsID += "<OUTPUT>\n";
				xmlOutputDownloadAllReportsID += "\t<STATUS>-1</STATUS>\n";
				xmlOutputDownloadAllReportsID += "\t<DESCRIPTION>\n\t"
						+ this.API_Indexer.strGetError() + " API_Indexer Error: "
						+ this.API_Indexer.strGetError() + "\t</DESCRIPTION>\n";
				xmlOutputDownloadAllReportsID += "</OUTPUT>\n";

				logger.log(Level.ALL, "*****************************************************");
				logger.log(Level.ALL, xmlOutputDownloadAllReportsID);
				logger.log(Level.ALL, "*****************************************************");
			}
			return xmlOutputDownloadAllReportsID;

		} catch (Exception e) {
			xmlOutputDownloadAllReportsID += "<OUTPUT>\n";
			xmlOutputDownloadAllReportsID += "\t<STATUS>-1</STATUS>\n";
			xmlOutputDownloadAllReportsID += "\t<DESCRIPTION>\n\t" + e.getMessage() + "\t</DESCRIPTION>\n";
			xmlOutputDownloadAllReportsID += "</OUTPUT>\n";

			logger.log(Level.ALL, "*****************************************************");
			logger.log(Level.ALL, xmlOutputDownloadAllReportsID);
			logger.log(Level.ALL, "*****************************************************");

			return xmlOutputDownloadAllReportsID;
		}
	}
	
	public String xmlMetadataQuery(String xmlInputMetadataQuery)
			throws RemoteException {

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputMetadataQuery.XmlWrapper inputWrapper = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputMetadataQuery.XmlWrapper(
				xmlInputMetadataQuery, false);
		inputWrapper.wrap();

		String xmlOutputMetadataQuery = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String query = new String();

		// AttributeSetList reportIds = null;
		// AttributeSet set = null;

		try {

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			if (!gkResult.contentEquals("OK"))
				throw new Exception("Connecting to gatekeeper: " + gkResult);

			query = inputWrapper.get_INPUT().get_QUERY();
			/*
			 * DSRAMGAHelper dsramga_manager = new DSRAMGAHelper(
			 * amga_host_name, amga_host_port,
			 * this.RPs_DICOMStorageInfo.getCenter(), tmp_dir );
			 * 
			 * reportIds = dsramga_manager.queryDICOMIds(query,
			 * String.valueOf(this
			 * .RPs_DICOMStorageInfo.getOntology().getIDOntology()));
			 */
			xmlOutputMetadataQuery += "<OUTPUT>\n";
			xmlOutputMetadataQuery += "\t<URI>"
					+ this.RPs_DICOMStorageServiceInfo.getURIResource()
							.getURI() + "</URI>\n";
			xmlOutputMetadataQuery += "\t<DSR_TYPE>"
					+ this.RPs_DICOMStorageServiceInfo.getURIResource()
							.getKey() + "</DSR_TYPE>\n";
			xmlOutputMetadataQuery += "\t<HOSPITAL>"
					+ this.RPs_DICOMStorageServiceInfo.getCenterName()
					+ "</HOSPITAL>\n";
			xmlOutputMetadataQuery += "\t<STATUS>0</STATUS>\n";
			xmlOutputMetadataQuery += "\t<DESCRIPTION>OK</DESCRIPTION>\n";

			// Iterator<AttributeSet> entries = reportIds.iterator();
			// while (entries.hasNext()) {
			// set = entries.next();
			// String attr = set.getKeys()[0];
			// xmlOutputMetadataQuery += "\t<ID>" + set.getValue(attr) +
			// "</ID>\n";
			// }

			xmlOutputMetadataQuery += "</OUTPUT>\n";

		} catch (Exception e) {
			logger.log(Level.ALL, "Error querying backend ("
					+ this.RPs_DICOMStorageServiceInfo.getCenterName() + "):",
					e);

			xmlOutputMetadataQuery += "<OUTPUT>\n";
			xmlOutputMetadataQuery += "\t<URI>"
					+ this.RPs_DICOMStorageServiceInfo.getURIResource()
							.getURI() + "</URI>\n";
			xmlOutputMetadataQuery += "\t<DSR_TYPE>"
					+ this.RPs_DICOMStorageServiceInfo.getURIResource()
							.getKey() + "</DSR_TYPE>\n";
			xmlOutputMetadataQuery += "\t<HOSPITAL>\n\t"
					+ this.RPs_DICOMStorageServiceInfo.getCenterName()
					+ "\t</HOSPITAL>\n";
			xmlOutputMetadataQuery += "\t<STATUS>-1</STATUS>\n";
			xmlOutputMetadataQuery += "\t<DESCRIPTION>\n\t" + e.getMessage()
					+ "\t</DESCRIPTION>\n";
			xmlOutputMetadataQuery += "</OUTPUT>\n";
		}

		logger.log(Level.ALL, "-> StorageDICOM(" + getID()
				+ ").xmlMetadataQuery()");

		return xmlOutputMetadataQuery;

	}

	public String xmlUnregisterOntology(String xmlInputUnregisterOntology)
			throws RemoteException {

		trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputUnregisterOntology.XmlWrapper inputWrapper = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputUnregisterOntology.XmlWrapper(
				xmlInputUnregisterOntology, false);
		inputWrapper.wrap();

		String xmlOutputUnregisterOntology = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

		try {

			String CERTIFICATE = inputWrapper.get_INPUT().get_CERTIFICATE();
			String gkResult = gateKeeper.strAllowAccess(CERTIFICATE);
			if (!gkResult.contentEquals("OK"))
				throw new Exception("Connecting to gatekeeper: " + gkResult);

			// Stop ISS registration timer event
			// getServiceGroupRegistrationClient().terminate();

		} catch (Exception e) {
			logger.log(Level.ALL, "Error unregistering ontology: ", e);

			xmlOutputUnregisterOntology += "<OUTPUT>\n";
			xmlOutputUnregisterOntology += "\t<STATUS>-1</STATUS>\n";
			xmlOutputUnregisterOntology += "\t<DESCRIPTION>\n\t"
					+ e.getMessage() + "\t</DESCRIPTION>\n";
			xmlOutputUnregisterOntology += "</OUTPUT>\n";
			return xmlOutputUnregisterOntology;
		}

		xmlOutputUnregisterOntology += "<OUTPUT>\n";
		xmlOutputUnregisterOntology += "\t<STATUS>0</STATUS>\n";
		xmlOutputUnregisterOntology += "\t<DESCRIPTION>OK</DESCRIPTION>\n";
		xmlOutputUnregisterOntology += "</OUTPUT>\n";

		logger.log(Level.ALL, "-> StorageDICOM(" + getID()
				+ ").xmlUnregisterOntology()");

		return xmlOutputUnregisterOntology;
	}

	public String xmlIsActive(String xmlInputIsActive) throws RemoteException {
		String xmlOutputIsActive = "";
		try {

			trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputIsActive.XmlWrapper inputWrapper = new trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputIsActive.XmlWrapper(
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

			logger.log(Level.ALL, "*****************************************************");
			logger.log(Level.ALL, "strDN -> Error in Method: xmlIsActive.xmlGetNext()");
			logger.log(Level.ALL, xmlOutputIsActive);
			logger.log(Level.ALL, "*****************************************************");

			return xmlOutputIsActive;
		}
	}

	/* Get/Setters for the RPs */
	public DICOMStorageServiceInfo getDICOMStorageServiceInfo() {
		return RPs_DICOMStorageServiceInfo;
	}

	public void setDICOMStorageServiceInfo(
			DICOMStorageServiceInfo RPs_DICOMStorageInfo) {
		this.RPs_DICOMStorageServiceInfo = RPs_DICOMStorageServiceInfo;
	}

	/* Required by interface ResourceProperties */
	public ResourcePropertySet getResourcePropertySet() {
		return this.propSet;
	}

	public Object getID() {
		return this.key;
	}

	public void IIS_Register() {
		try {
			// Inizialize IIS Register component ald register the RP
			API_IIS_Register = new IIS_Register();
			API_IIS_Register.register(this.registration_path, key);
		} catch (Exception e) {
			logger.log(Level.ALL,
					"Exception when trying to register in the IIS " + e);
		}
	}

}
