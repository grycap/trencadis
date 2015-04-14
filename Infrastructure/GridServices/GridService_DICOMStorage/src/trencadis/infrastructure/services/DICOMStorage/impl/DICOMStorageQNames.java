package trencadis.infrastructure.services.DICOMStorage.impl;

import javax.xml.namespace.QName;

public interface DICOMStorageQNames {
	public static final String NS = "http://services.infrastructure.trencadis/DICOMStorage";

	public static final QName RP_DICOMSTORAGESERVICEINFO = new QName(NS, "DICOMStorageServiceInfo");                                                                                

        public static final QName RESOURCE_PROPERTIES = new QName(NS, "DICOMStorageResourceProperties");
                        

}
