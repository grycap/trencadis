package trencadis.middleware.operations.DICOMStorage;

import java.net.URI;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.axis.message.MessageElement;
import org.globus.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.utils.AddressingUtils;
import org.oasis.wsrf.properties.QueryResourcePropertiesResponse;

import trencadis.infrastructure.services.TRENCADIS_JAVA_API_IIS.IIS_Query_RPs;
import trencadis.infrastructure.services.stubs.DICOMStorage.DICOMStoragePortType;
import trencadis.infrastructure.services.stubs.DICOMStorage.service.DICOMStorageServiceAddressingLocator;
import trencadis.middleware.login.TRENCADIS_SESSION;
import trencadis.middleware.operations.TRENCADIS_OPERATION_GENERIC;

/**
 * This is an abstract class for all the operations in this package. All
 * operations have a session.
 */
public abstract class TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE extends
		TRENCADIS_OPERATION_GENERIC {
	
	protected DICOMStoragePortType DICOMStorage = null;
	protected Vector<DICOMStoragePortType> DICOMStorages = null;

	/**
	 * This constructor is used when we have the StorageDICOM URI + Key and we
	 * do not need to ask for it to the IIS.
	 *
	 * @param session Session used by this operation
	 * @param storageDICOMURI The URI of the StorageDICOM service
	 * @param reportType The report type of the DICOM-SR to download
	 * @throws Exception
	 */
	public TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE(TRENCADIS_SESSION session,
			URI DICOMStorageURI, String Key) throws Exception {
		super(session);

		getserverPortType(DICOMStorageURI, Key);

	}
	
	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the StorageDICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @throws Exception
	 */
	public TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE(TRENCADIS_SESSION session) throws Exception {
		super(session);

		boolean bConnect;

		IIS_Query_RPs query_iis = new IIS_Query_RPs();

		// Check all URLResources
		String strxPathQuery = "//*[namespace-uri()='http://services.infrastructure.trencadis/DICOMStorage'"
		+ " and local-name()='DICOMStorageServiceInfo']/*[local-name()='URIResource']/*";
		
		DICOMStorages = new Vector<DICOMStoragePortType>();
		Vector it_URLs_IIS = _session.getUrlIISCentral();
		QueryResourcePropertiesResponse response = query_iis.QueryRPs(
				it_URLs_IIS, strxPathQuery);
		if (response == null) {
			throw new Exception(query_iis.strGetError());
		}

		MessageElement[] responseContents = response.get_any();

		bConnect = false;
		for (int i = 0; (i < responseContents.length) && (!bConnect); i = i + 2) {
			try {
				getserverPortType(new URI(responseContents[i].getValue()),
						responseContents[i + 1].getValue());
				bConnect = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!bConnect)
			throw new Exception(
					"All URIs of DICOMStrorage Resources published in the central IIS are disconected");
	}
	
	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the StorageDICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param reportType The report type of the DICOM-SR to download
	 * @throws Exception
	 */	
	public TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE(TRENCADIS_SESSION session,
			String IDOntology) throws Exception {
		super(session);

		boolean bConnect;

		IIS_Query_RPs query_iis = new IIS_Query_RPs();
		
		// Check all URLResources for a given ontology
		
		String strxPathQuery = "//*[local-name()='DICOMStorageServiceInfo'"
				+ "and boolean(./*[local-name()='Ontology' and IDOntology="
				+ IDOntology + "]/*)]/*[local-name()='URIResource']/*";
		
		DICOMStorages = new Vector<DICOMStoragePortType>();
		
		Vector it_URLs_IIS = _session.getUrlIISCentral();

		QueryResourcePropertiesResponse response = query_iis.QueryRPs(
				it_URLs_IIS, strxPathQuery);
		if (response == null) {
			throw new Exception(query_iis.strGetError());
		}

		MessageElement[] responseContents = response.get_any();

		bConnect = false;
		for (int i = 0; (i < responseContents.length) && (!bConnect); i = i + 2) {
			try {
				
				getserverPortType(new URI(responseContents[i].getValue()),
						responseContents[i + 1].getValue());
				bConnect = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!bConnect)
			throw new Exception(
					"All URIs of DICOMStrorage Resources for IDOntology = "
							+ IDOntology
							+ " published in the central IIS are disconected");
	}

	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the StorageDICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param id_hospital The name of the StorageDICOM service identifier
	 * @param reportType The report type of the DICOM-SR to download
	 * @throws Exception
	 */
	public TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE(TRENCADIS_SESSION session,
			int id_center, String IDOntology) throws Exception {

		super(session);

		boolean bConnect;

		IIS_Query_RPs query_iis = new IIS_Query_RPs();
		// Check all URLResources for a given ontology and hospital		
		String strxPathQuery = "//*[namespace-uri()='http://services.infrastructure.trencadis/DICOMStorage'"
				+ " and local-name()='DICOMStorageServiceInfo' and IDCenter='"
				+ Integer.toString(id_center)
				+ "' and boolean(./*[local-name()='Ontology' and IDOntology="
				+ IDOntology + "]/*)]/*[local-name()='URIResource']/*";
		
		Vector it_URLs_IIS = _session.getUrlIISCentral();

		QueryResourcePropertiesResponse response = query_iis.QueryRPs(
				it_URLs_IIS, strxPathQuery);
		if (response == null) {
			throw new Exception(query_iis.strGetError());
		}

		MessageElement[] responseContents = response.get_any();

		bConnect = false;
		for (int i = 0; (i < responseContents.length) && (!bConnect); i = i + 2) {
			try {
				getserverPortType(new URI(responseContents[i].getValue()),
						responseContents[i + 1].getValue());
				bConnect = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!bConnect)
			throw new Exception(
					"All URIs of DICOMStrorage Resources for IDOntology = "
							+ IDOntology
							+ " published in the central IIS are disconected");

	}
	
	/**
	 * This constructor is used when we have the hospital identifier and we need
	 * to ask the IIS for the StorageDICOM URI.
	 * 
	 * @param session Session used by this operation
	 * @param id_hospital The name of the StorageDICOM service identifier
	 * @throws Exception
	 */
	public TRENCADIS_OPERATION_DICOM_STORAGE_SERVICE(TRENCADIS_SESSION session,
			int id_center) throws Exception {

		super(session);

		boolean bConnect;

		IIS_Query_RPs query_iis = new IIS_Query_RPs();
		// Check all URLResources for a given hospital		
		String strxPathQuery = "//*[namespace-uri()='http://services.infrastructure.trencadis/DICOMStorage'"
				+ " and local-name()='DICOMStorageServiceInfo' and IDCenter='"
				+ Integer.toString(id_center) + "']/*[local-name()='URIResource']/*";
		
		Vector it_URLs_IIS = _session.getUrlIISCentral();

		QueryResourcePropertiesResponse response = query_iis.QueryRPs(
				it_URLs_IIS, strxPathQuery);
		if (response == null) {
			throw new Exception(query_iis.strGetError());
		}

		MessageElement[] responseContents = response.get_any();

		bConnect = false;
		for (int i = 0; (i < responseContents.length) && (!bConnect); i = i + 2) {
			try {
				getserverPortType(new URI(responseContents[i].getValue()),
						responseContents[i + 1].getValue());
				bConnect = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (!bConnect)
			throw new Exception(
					"All URIs of DICOMStrorage Resources for IDCenter = "
							+ id_center
							+ " published in the central IIS are disconected");

	}

	private void getserverPortType(URI _DICOMStorageURI, String Key)
			throws Exception {
		QName storageDICOMKeyQName = new QName(
				"http://services.infrastructure.trencadis/DICOMStorage",
				"DICOMStorageResourceKey");
		ResourceKey DICOMStorageResourceKey = new SimpleResourceKey(
				storageDICOMKeyQName, Key);
		EndpointReferenceType DICOMStorageResourceEPR = AddressingUtils
				.createEndpointReference(_DICOMStorageURI.toString(),
						DICOMStorageResourceKey);

		DICOMStorageServiceAddressingLocator locator = new DICOMStorageServiceAddressingLocator();
		
		// Get PortType
		DICOMStorage = locator.getDICOMStoragePortTypePort(DICOMStorageResourceEPR);
		if (DICOMStorages != null)
			this.DICOMStorages.add(DICOMStorage);

		String xmlInput = "<INPUT>"
				+ "<CERTIFICATE>"
				+ _session.getX509VOMSCredential()
				+ "</CERTIFICATE>"
				+ "</INPUT>";
		String xmlOutput = DICOMStorage.xmlIsActive(xmlInput);
	}

}
