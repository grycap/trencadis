package trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlInputUpdateResources;/*******************************************************************  *********** AUTOMATICALLY GENERATED CLASS BY XmlWrappers  **********  ********-----German Molto  (gmolto@dsic.upv.es) -----*******  ********************************************************************/ import org.w3c.dom.*;/** * Contains the methods that allow to obtain and change the information of * the elements <STRONG>FILTER </STRONG> from the XML document. */public class FILTER extends Wrapper{/** * Creates a Wrapper that works starting from the element FILTER * @param e The elemenent the Wrapper works with*/public FILTER(Element e){ super(e);}/** * Erases the element FILTER and its descendants * @throws java.util.NoSuchElementException If there specified element *           is not found * @throws IllegalStateException If the schema disallows erasing the element */public void delete(){  try{   elem.getParentNode().removeChild(elem);  }catch(Exception e){ throw new java.util.NoSuchElementException("Tried to erase a non-existent element");}  }/** * Obtains a Wrapper to work from the element DICOM_SR * @param value The Wrapper obtained */ public DICOM_SR get_DICOM_SR(){  Element el = getElementByTagName("DICOM_SR");   return new DICOM_SR(el); }  }