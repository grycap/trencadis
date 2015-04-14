package trencadis.infrastructure.services.DICOMStorage.impl;

import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.impl.servicegroup.client.ServiceGroupRegistrationClient;

public class DICOMStorageResourceHome extends ResourceHomeImpl {

	public ResourceKey create(String strCertificate, String baseURIResorce,
			String URIFactory, String IDOntology, String ontologyDescription,
			String xmlOntologyStructure) throws Exception {

		DICOMStorageResource DICOMStorageResource = null;

		// Create a resource
		DICOMStorageResource = (DICOMStorageResource) createNewInstance();

		// Creamos la clave del recurso
		ResourceKey key = new SimpleResourceKey(keyTypeName, IDOntology);

		// initialize resource
		DICOMStorageResource.initialize(strCertificate, key, baseURIResorce,
				URIFactory, IDOntology, ontologyDescription,
				xmlOntologyStructure);

		// Registramos en el MDS los RPs del Recurso
		DICOMStorageResource.IIS_Register();

		// add to list of resources
		add(key, DICOMStorageResource);

		return key;

	}

}
