package trencadis.infrastructure.services.StorageBroker.impl;

import javax.xml.namespace.QName;

public interface StorageBrokerQNames {
	public static final String NS = "http://services.infrastructure.trencadis/StorageBroker";

	public static final QName RP_ACTIVE            = new QName(NS, "Active");
	public static final QName RP_DSRTYPE           = new QName(NS, "DSRType");

	public static final QName RESOURCE_PROPERTIES = new QName(NS, "StorageDICOMResourceProperties");

}
