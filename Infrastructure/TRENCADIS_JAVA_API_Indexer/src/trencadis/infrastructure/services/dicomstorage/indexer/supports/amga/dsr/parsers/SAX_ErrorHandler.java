package trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.parsers;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SAX_ErrorHandler implements ErrorHandler {

	public void warning(SAXParseException arg0) throws SAXException {
		throw new UnsupportedOperationException(arg0.getMessage());
	}

	public void error(SAXParseException arg0) throws SAXException {
		throw new UnsupportedOperationException(arg0.getMessage());
	}

	public void fatalError(SAXParseException arg0) throws SAXException {
		throw new UnsupportedOperationException(arg0.getMessage());
	}

}
