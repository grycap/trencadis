package trencadis.infrastructure.services.EOUIDGenerator.impl;

import javax.xml.namespace.QName;

public interface EOUIDGeneratorQNames {
	public static final String NS = "http://services.infrastructure.trencadis/EOUIDGenerator";

	public static final QName RP_EOUIDGENERATORSERVICEINFO = new QName(NS, "EOUIDGeneratorServiceInfo");
	
	public static final QName RESOURCE_PROPERTIES = new QName(NS, "EOUIDGeneratorResourceProperties");
}