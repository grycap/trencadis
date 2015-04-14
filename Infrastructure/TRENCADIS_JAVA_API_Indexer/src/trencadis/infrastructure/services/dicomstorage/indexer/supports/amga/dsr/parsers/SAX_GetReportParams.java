package trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.parsers;

import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.Helper;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class SAX_GetReportParams implements ContentHandler {

	private Helper manager;
	private String IDreport          = new String();
	private String IDOntology        = new String();
	private String Description       = new String();
	private String IDTRENCADISReport = new String();
	private String DateTimeStart 	 = new String();
	private String DateTimeEnd		 = new String();

	public SAX_GetReportParams(Helper manager) {
		this.manager = manager;
	}

	public String getIDOntology() {
		return IDOntology;
	}

	public void setDocumentLocator(Locator arg0) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void startPrefixMapping(String arg0, String arg1) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void endPrefixMapping(String arg0) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {

		if (qName.equals("DICOM_SR")) {
			IDreport          = atts.getValue("IDReport");                        
			IDOntology        = atts.getValue("IDOntology");			                                 
			Description       = atts.getValue("Description");
			IDTRENCADISReport = atts.getValue("IDTRENCADISReport");
			DateTimeStart	  = atts.getValue("DateTimeStart");
			DateTimeEnd 	  = atts.getValue("DateTimeEnd");
			manager.setReportParams(IDOntology, IDreport,Description,IDTRENCADISReport, DateTimeStart, DateTimeEnd);
			throw new SAXException();
		}
	}

	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void processingInstruction(String arg0, String arg1) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void skippedEntity(String arg0) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void startDocument() throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void endDocument() throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void endElement(String arg0, String arg1, String arg2) throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

}
