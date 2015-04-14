package trencadis.infrastructure.services.OntologiesServer.impl;

import javax.xml.namespace.QName;

public interface OntologiesServerQNames {
    public static final String NS = "http://services.infrastructure.trencadis/OntologiesServer";
    
    public static final QName RP_ONTOLOGIESSERVERSERVICEINFO = new QName(NS, "OntologiesServerServiceInfo");                                                                                

    public static final QName RESOURCE_PROPERTIES = new QName(NS, "OntologiesServerResourceProperties");
}
