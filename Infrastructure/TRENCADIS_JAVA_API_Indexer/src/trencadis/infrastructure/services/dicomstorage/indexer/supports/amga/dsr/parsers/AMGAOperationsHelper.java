package trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.parsers;

import arda.md.javaclient.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.objects.*;
import org.xml.sax.SAXException;

public class AMGAOperationsHelper {

	private MDServerConnection serverConn;
	private MDClient mdClient;

	public AMGAOperationsHelper(MDServerConnection serverConn, MDClient mdClient) {
		this.serverConn = serverConn;
		this.mdClient = mdClient;
	}
	

	public void beginTransaction() throws SAXException {

		try {
			serverConn.execute("transaction");
		} catch (Exception ex) {
			throw new SAXException("Stopping parser due to previous errors: Could not start the transaction: " + ex.getMessage());
		}

	}

	public void commitTransaction() throws SAXException {

		try {
			serverConn.execute("commit");
		} catch (Exception ex) {
			throw new SAXException("Stopping parser due to previous errors: Could not commit the transaction, catalogue may be inconsistent: " + ex.getMessage());
		}

	}

	public void abortTransaction(String error) throws SAXException {

		try {
			serverConn.execute("abort");
		} catch (Exception ex2) {
			throw new SAXException(error + "\nCould not abort properly the AMGA transaction, catalogue may be inconsistent:", ex2);
		}
		throw new SAXException("Stopping parser due to previous errors: " + error);

	}

	public void createDirectory(String strPath) throws SAXException {                
		try {                        
			mdClient.createDir(strPath);
		} catch (IOException ex) {
			abortTransaction("Can not create directory " + strPath + " due to a communication error, AMGA transaction will be aborted:" + ex.getMessage());
		} catch (CommandException ex) {
			throw new SAXException("Stopping parser due to previous errors: Can not create directory " + strPath + " due to a" +
					" server error, maybe an structure for that reportType identifier" +
					" already exists, AMGA transaction will be aborted:" + ex.getMessage());
		}

	}
        
        public void createDirectory(DSRNode current_node) throws SAXException {                
		try {                        
			mdClient.createDir(current_node.getPath());
		} catch (IOException ex) {
			abortTransaction("Can not create directory " + current_node.getPath() + " due to a communication error, AMGA transaction will be aborted:" + ex.getMessage());
		} catch (CommandException ex) {
			throw new SAXException("Stopping parser due to previous errors: Can not create directory " + current_node.getPath() + "_" + " due to a" +
					" server error, maybe an structure for that reportType identifier" +
					" already exists, AMGA transaction will be aborted:" + ex.getMessage());
		}

	}
        
        
	public void createHeaderDirectory(String strPath) throws SAXException {
                                
		String headerDir = strPath + "/header";

		try {

			mdClient.createDir(headerDir);
			mdClient.addAttr(headerDir, "IDTRENCADISREPORT", "TEXT");
			mdClient.addAttr(headerDir, "IDONTOLOGY", "TEXT");			

		} catch (IOException ex) {
			abortTransaction("Can not create directory " + headerDir + " due to a" +
					" communication error, AMGA transaction will be aborted:" + ex.getMessage());
		} catch (CommandException ex) {
			//If this command fails then it will abort automatically the transaction
			throw new SAXException("Stopping parser due to previous errors: Can not create directory " + headerDir + " due to a" +
					" server error, maybe an structure for that reportType identifier" +
					" already exists, AMGA transaction will be aborted:" + ex.getMessage());
		}

	}
        public void listEntryHeaderDirectory(String strPathbase, String IDTRENCADISReport) throws SAXException {

		String entry = strPathbase + "/header/" + IDTRENCADISReport;
                
                
		String[] attrKeys = new String[]{"IDTRENCADISREPORT", "IDONTOLOGY"};

		try {

			mdClient.dir(entry);

			AttributeSetList attributeSetList = mdClient.getAttr(entry, attrKeys);
			Iterator<AttributeSet> it = attributeSetList.iterator();
			AttributeSet set = it.next();

			System.out.println("Attributes of " + entry);
			int i, fin = attrKeys.length - 1;
			System.out.print("\t->");
			for (i = 0; i < fin; i++) {
				System.out.print(attrKeys[i] + "(" + set.getValue(attrKeys[i]) + "), ");
			}
			System.out.println(attrKeys[i] + "(" + set.getValue(attrKeys[i]) + ")");

		} catch (IOException ex) {
			throw new SAXException("Stopping parser due to previous errors: Can not list entry " + entry + " due to a communication error:", ex);
		} catch (CommandException ex) {
			//System.out.println("The entry " + entry + " does not exist for this report.");
		}

	}        
        
        public void removeEntryHeaderDirectory(String strPathbase, String IDTRENCADISReport) throws SAXException {

		String entry = strPathbase + "/header/" + IDTRENCADISReport;

		try {

			mdClient.dir(entry);

			try {
				mdClient.removeEntry(entry);
			} catch (IOException ex) {
				abortTransaction("Can not remove the entry " + entry + " due to a" +
						" communication error, AMGA transaction will be aborted:" + ex.getMessage());
			} catch (CommandException ex) {
				//If this command fails then it will abort automatically the transaction
				throw new SAXException("Stopping parser due to previous errors: Can not remove the entry " + entry + " due to a server error," +
						" AMGA transaction will be aborted:" + ex.getMessage());
			}

		} catch (IOException ex) {
			abortTransaction("Can not remove the entry " + entry + " due to a" +
					" communication error, AMGA transaction will be aborted:" + ex.getMessage());
		} catch (CommandException ex) {
			//System.out.println("The entry " + entry + " does not exist for this report.");
		}

	}
        
        
        
        
	public void createBaseDirectory(String strPathDir) throws SAXException {

		//String dir = getUnixPath(path);
		
		try {
			mdClient.dir(strPathDir);
		} catch (IOException ex) {
			abortTransaction("Can not create the directory " + strPathDir + " due to a" +
							 " communication error, AMGA transaction will be aborted:" + ex.getMessage());
		} catch (CommandException cx) {

			try {
				mdClient.createDir(strPathDir);
			} catch (IOException ex) {
				abortTransaction("Can not create directory " + strPathDir + " due to a" +
						" communication error, AMGA transaction will be aborted:" + ex.getMessage());
			} catch (CommandException ex) {
				throw new SAXException("Stopping parser due to previous errors: Can not create directory " + strPathDir + " due to a" +
						" server error, maybe an structure for that reportType identifier" +
						" already exists, AMGA transaction will be aborted:" + ex.getMessage());
			}

		}

	}

	public void addAtributes(DSRNode current_node) throws SAXException {
                
		
                Class<? extends DSRNode> nodeClass = current_node.getClass();

		String[] attrKeys = new String[0];
		String[] attrTypes = new String[0];

		if (nodeClass == CONTAINER.class) {
			attrKeys = ((CONTAINER) current_node).getAttrKeys();
			attrTypes = ((CONTAINER) current_node).getAttrTypes();		
		} else if (nodeClass == TEXT.class) {
			attrKeys = ((TEXT) current_node).getAttrKeys();
			attrTypes = ((TEXT) current_node).getAttrTypes();
		} else if (nodeClass == NUM.class) {
			attrKeys = ((NUM) current_node).getAttrKeys();
			attrTypes = ((NUM) current_node).getAttrTypes();
		} else if (nodeClass == CODE.class) {
			attrKeys = ((CODE) current_node).getAttrKeys();
			attrTypes = ((CODE) current_node).getAttrTypes();		
		} else if (nodeClass == DATE.class) {
			attrKeys = ((DATE) current_node).getAttrKeys();
			attrTypes = ((DATE) current_node).getAttrTypes();				
		} else {
			throw new SAXException("Stopping parser due to previous errors: Can not determine node's " + current_node.getPath() + " class");
		}
                                
		try {
			for (int i = 0; i < attrKeys.length; i++) {
				mdClient.addAttr(current_node.getPath(), attrKeys[i], attrTypes[i]);
			}
		} catch (IOException ex) {
			abortTransaction("Can not set attributes for directory " + current_node.getPath() + " due to a" +
						     " communication error, AMGA transaction will be aborted:" + ex.getMessage());
		} catch (CommandException ex) {
			//If this command fails then it will abort automatically the transaction
			throw new SAXException("Stopping parser due to previous errors: Can not set attributes for directory " + current_node.getPath() + " due to a" +
					" server error, AMGA transaction will be aborted:" + ex.getMessage());
		}

	}

    public void addEntryHeaderDirectory(String strPathBase, String IDTRENCADISReport, String IDOntology) throws SAXException {

		String headerEntry = strPathBase + "/header/" + IDTRENCADISReport;

		String[] attrKeys = new String[]{"IDTRENCADISREPORT", "IDONTOLOGY"};
		String[] attrValues = new String[]{IDTRENCADISReport, IDOntology};

		try {
			mdClient.addEntry(headerEntry, attrKeys, attrValues);
		} catch (IOException ex) {
			abortTransaction("Can not introduce the entry " + headerEntry + " due to a" +
					" communication error, AMGA transaction will be aborted:" + ex.getMessage());
		} catch (CommandException ex) {
			//If this command fails then it will abort automatically the transaction
			throw new SAXException("Stopping parser due to previous errors: Can not introduce the entry " + headerEntry + " due to a server error," +
					" maybe the xml does not match the report type specified in its header" +
					" or there is no structure created for that report type, AMGA" +
					" transaction will be aborted:" + ex.getMessage());
		}

	}
        
	public void addEntry(DSRNode current_node, String IDTRENCADISReport) throws SAXException {


		String entry = current_node.getPath() + "/" + IDTRENCADISReport;
		
		Class<? extends DSRNode> nodeClass = current_node.getClass();

		String[] attrKeys = new String[0];
		String[] attrKeys2 = new String[0];
		String[] attrValues = new String[0];
		String[] attrValues2 = new String[0];
		int j = 0;

		if (nodeClass == CONTAINER.class) {
			attrKeys = ((CONTAINER) current_node).getAttrKeys();
			attrValues = ((CONTAINER) current_node).getAttrValues();		
		} else if (nodeClass == TEXT.class) {
			attrKeys = ((TEXT) current_node).getAttrKeys();
			attrValues = ((TEXT) current_node).getAttrValues();
		} else if (nodeClass == NUM.class) {
			attrKeys = ((NUM) current_node).getAttrKeys();
			attrValues = ((NUM) current_node).getAttrValues();
		} else if (nodeClass == CODE.class) {
			attrKeys = ((CODE) current_node).getAttrKeys();
			attrValues = ((CODE) current_node).getAttrValues();		
		} else if (nodeClass == DATE.class) {
			attrKeys = ((DATE) current_node).getAttrKeys();
			attrValues = ((DATE) current_node).getAttrValues();		
		} else {
			throw new SAXException("Stopping parser due to previous errors: Can not determine node's " + entry + " class");
		}

		// ELIMINO LOS NULL
		int iCont = 0;
		for (int i = 0; i < attrValues.length; i++) {
			if (!attrValues[i].equals(""))
				iCont++;
		}

		attrValues2 = new String[iCont];
		attrKeys2 = new String[iCont];
		for(int i = 0; i < attrValues.length; i++) {
			attrValues[i] = attrValues[i].replace("\n", "\\n");   
                        if (!attrValues[i].equals("")){
                            attrValues2[j] = attrValues[i];
                            attrKeys2[j]   = attrKeys[i];
                            j++;
                        }
		}
                
                

		try {
			mdClient.addEntry(entry, attrKeys2, attrValues2);
		} catch (IOException ex) {
			abortTransaction("Can not introduce the entry " + entry + " due to a" +
					" communication error, AMGA transaction will be aborted: " + ex.getMessage());
		} catch (CommandException ex) {
			throw new SAXException("Stopping parser due to previous errors: Can not introduce the entry " + entry + " due to a server error," +
					" maybe the xml does not match the report type specified in its header" +
					" or there is no structure created for that report type, AMGA" +
					" transaction will be aborted:" + ex.getMessage());
		}

	}
	
	public void removeEntry(DSRNode current_node, String IDTRENCADISReport) throws SAXException {
		
                String entry = current_node.getPath() + "/" + IDTRENCADISReport;
		try {

			mdClient.dir(entry);

			try {
				mdClient.removeEntry(entry);
			} catch (IOException ex) {
				abortTransaction("Can not remove the entry " + entry + " due to a" +
						" communication error, AMGA transaction will be aborted:" + ex.getMessage());
			} catch (CommandException ex) {
				//If this command fails then it will abort automatically the transaction
				throw new SAXException("Stopping parser due to previous errors: Can not remove the entry " + entry + " due to a server error," +
						" AMGA transaction will be aborted:" + ex.getMessage());
			}

		} catch (IOException ex) {
			abortTransaction("Can not remove the entry " + entry + " due to a" +
					         " communication error, AMGA transaction will be aborted:" + ex.getMessage());
		} catch (CommandException ex) {
            //System.out.println("The entry " + entry + " does not exist for this report.");
		}

	}

	public void listEntry(DSRNode current_node, String IDTRENCADISReport) throws SAXException {

		Class<? extends DSRNode> nodeClass = current_node.getClass();
		
                String entry = current_node.getPath() + "/" + IDTRENCADISReport;
		String[] attrKeys = new String[0];

		if (nodeClass == CONTAINER.class) {
			System.out.print("Container: ");
			attrKeys = ((CONTAINER) current_node).getAttrKeys();		
		} else if (nodeClass == TEXT.class) {
			attrKeys = ((TEXT) current_node).getAttrKeys();
		} else if (nodeClass == NUM.class) {
			attrKeys = ((NUM) current_node).getAttrKeys();
		} else if (nodeClass == CODE.class) {
			attrKeys = ((CODE) current_node).getAttrKeys();		
		} else if (nodeClass == DATE.class) {
			attrKeys = ((DATE) current_node).getAttrKeys();		
		} else {
			throw new SAXException("Stopping parser due to previous errors: Can not determine node's " + entry + " class");
		}
		
		try {

			mdClient.dir(entry);

			AttributeSetList attributeSetList = mdClient.getAttr(entry, attrKeys);
			Iterator<AttributeSet> it = attributeSetList.iterator();
			AttributeSet set = it.next();

			System.out.println("Attributes of " + entry);
			int i, fin = attrKeys.length - 1;
			System.out.print("\t->");
			for (i = 0; i < fin; i++) {
				System.out.print(attrKeys[i] + "(" + set.getValue(attrKeys[i]) + "), ");
			}
			System.out.println(attrKeys[i] + "(" + set.getValue(attrKeys[i]) + ")");

		} catch (IOException ex) {
			throw new SAXException("Stopping parser due to previous errors: Can not list entry " + entry + " due to a communication error: " + ex.getMessage());
		} catch (CommandException ex) {
			//System.out.println("Stopping parser due to previous errors: The entry " + entry + " does not exist for this report: " + ex.getMessage());
		}

	}

	

	

	

	

}
