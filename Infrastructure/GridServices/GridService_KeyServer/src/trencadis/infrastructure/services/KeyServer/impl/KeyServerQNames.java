package trencadis.infrastructure.services.KeyServer.impl;

import javax.xml.namespace.QName;

public interface KeyServerQNames {
	public static final String NS = "http://services.infrastructure.trencadis/KeyServerService";

	public static final QName RP_KEYSERVERSERVICEINFO = new QName(NS, "KeyServerServiceInfo");
        
        public static final QName RP_KEYCERTCHAIN = new QName(NS, "KeyCertChain");
	
	public static final QName RESOURCE_PROPERTIES = new QName(NS, "KeyServerServiceResourceProperties");
}