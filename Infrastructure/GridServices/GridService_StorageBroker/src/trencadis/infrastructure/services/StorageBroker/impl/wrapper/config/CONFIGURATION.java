package trencadis.infrastructure.services.StorageBroker.impl.wrapper.config;/*******************************************************************  *********** AUTOMATICALLY GENERATED CLASS BY XmlWrappers  **********  ********-----German Molto  (gmolto@dsic.upv.es) -----*******  ********************************************************************/ import org.w3c.dom.*;/** * Contains the methods that allow to obtain and change the information of * the elements <STRONG>CONFIGURATION </STRONG> from the XML document. */public class CONFIGURATION extends Wrapper{/** * Creates a Wrapper that works starting from the element CONFIGURATION * @param e The elemenent the Wrapper works with*/public CONFIGURATION(Element e){ super(e);}/** * Obtains a Wrapper to work from the element LOCAL_PARAMETERS * @param value The Wrapper obtained */ public LOCAL_PARAMETERS get_LOCAL_PARAMETERS(){  Element el = getElementByTagName("LOCAL_PARAMETERS");   return new LOCAL_PARAMETERS(el); }  /** * Obtains a Wrapper to work from the element AMGA_PARAMETERS * @param value The Wrapper obtained */ public AMGA_PARAMETERS get_AMGA_PARAMETERS(){  Element el = getElementByTagName("AMGA_PARAMETERS");   return new AMGA_PARAMETERS(el); }  /** * Obtains a Wrapper to work from the element LFC_PARAMETERS * @param value The Wrapper obtained */ public LFC_PARAMETERS get_LFC_PARAMETERS(){  Element el = getElementByTagName("LFC_PARAMETERS");   return new LFC_PARAMETERS(el); }  /** * Obtains a Wrapper to work from the element GATEKEEPER * @param value The Wrapper obtained */ public GATEKEEPER get_GATEKEEPER(){  Element el = getElementByTagName("GATEKEEPER");   return new GATEKEEPER(el); }  /** * Obtains a Wrapper to work from the element INDEX_SERVICE * @param value The Wrapper obtained */ public INDEX_SERVICE get_INDEX_SERVICE(){  Element el = getElementByTagName("INDEX_SERVICE");   return new INDEX_SERVICE(el); }  }