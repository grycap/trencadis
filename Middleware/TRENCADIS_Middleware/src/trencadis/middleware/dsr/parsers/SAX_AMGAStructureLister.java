package trencadis.middleware.dsr.parsers;

import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.objects.*;

import java.util.LinkedList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class SAX_AMGAStructureLister implements ContentHandler {

	private LinkedList<DSRNode> currentNode = new LinkedList<DSRNode>();
	private String buffer = new String();
	private String codeValue = new String();
	private String codeSchema = new String();
	int indent = 0;

	public SAX_AMGAStructureLister() {
	}

	public void setDocumentLocator(Locator arg0) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void startDocument() throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void endDocument() throws SAXException {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void startPrefixMapping(String arg0, String arg1) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void endPrefixMapping(String arg0) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void startElement(String uri, String name, String qName, Attributes atts) {

		if (qName.equals("CODE_VALUE")) {
			buffer = "";
		} else if (qName.equals("CODE_SCHEMA")) {
			buffer = "";
		} else if (qName.equals("CODE_MEANING")) {
			buffer = "";		
		} else if (qName.equals("TEXT")) {
			currentNode.add(new TEXT());		
		} else if (qName.equals("DATE")) {
			currentNode.add(new DATE());				
		} else if (qName.equals("CODE")) {
			currentNode.add(new CODE());
		} else if (qName.equals("NUM")) {
			currentNode.add(new NUM());		
		} else if (qName.equals("CONTAINER")) {
			currentNode.add(new CONTAINER());
		} else if (qName.equals("CHILDREN")) {
			indent++;
		}

	}

	public void endElement(String uri, String name, String qName) throws SAXException {

		if (qName.equals("CODE_VALUE")) {
			codeValue = buffer;
		} else if (qName.equals("CODE_SCHEMA")) {
			codeSchema = buffer;
		} else if (qName.equals("CODE_MEANING")) {

			for (int i = 0; i < indent; i++) {
				System.out.print("\t");
			}

			System.out.print(
					"-> " + buffer + " (" + codeValue + "_" + codeSchema + ") " +
					currentNode.getLast().getTypeIdentifier()+ " [");

			String[] attrKeys = currentNode.getLast().getAttrKeys();
			int i, fin = attrKeys.length - 1;
			for (i = 3; i < fin; i++) {
				System.out.print(attrKeys[i] + ", ");
			}
			if (i == fin) {
				System.out.println(attrKeys[i] + "]");
			} else {
				System.out.println("]");
			}

		} else if (qName.equals("PNAME")) {
			currentNode.removeLast();
		} else if (qName.equals("TEXT")) {
			currentNode.removeLast();
		} else if (qName.equals("DATETIME")) {
			currentNode.removeLast();
		} else if (qName.equals("DATE")) {
			currentNode.removeLast();
		} else if (qName.equals("TIME")) {
			currentNode.removeLast();
		} else if (qName.equals("SCOORD")) {
			currentNode.removeLast();
		} else if (qName.equals("CODE")) {
			currentNode.removeLast();
		} else if (qName.equals("NUM")) {
			currentNode.removeLast();
		} else if (qName.equals("UIDREF")) {
			currentNode.removeLast();
		} else if (qName.equals("CONTAINER")) {
			currentNode.removeLast();
		} else if (qName.equals("CHILDREN")) {
			indent--;
		}

	}

	public void characters(char ch[], int start, int length) {
		for (int i = start; i < start + length; i++) {
			buffer += ch[i];
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

}
