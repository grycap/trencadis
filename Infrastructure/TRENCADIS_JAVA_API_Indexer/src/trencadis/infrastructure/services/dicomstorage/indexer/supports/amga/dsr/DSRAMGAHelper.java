package trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import arda.md.javaclient.AttributeSet;
import arda.md.javaclient.AttributeSetList;
import arda.md.javaclient.CollectionElement;
import arda.md.javaclient.CollectionElementList;
import arda.md.javaclient.CommandException;
import arda.md.javaclient.MDClient;
import arda.md.javaclient.MDServerConnection;
import arda.md.javaclient.MDServerConnectionContext;

import org.xml.sax.SAXException;

import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.parsers.DOM_TREE_AMGA_Ontology;


/*
 import org.globus.ftp.GridFTPClient;
 import org.ietf.jgss.GSSCredential;
 import org.globus.ftp.MlsxEntry;
 import org.globus.ftp.exception.ClientException;
 import org.globus.ftp.exception.ServerException;
 import java.util.Vector;
 import com.sun.security.auth.module.UnixSystem;
 */

public class DSRAMGAHelper extends Helper {

	// AMGA parameters
	private MDServerConnectionContext _serverConnContext;
	private MDServerConnection _serverConn;
	private MDClient _mdClient;
	private String _amga_home;
	// TODO: Use proxy_file
	private File _user_proxy_file = null;
	private boolean _dir_exists;

 

	/**
	 * This constructor receives the configuration through several parameters.
	 * The caller must know all the configuration details to stablish
	 * connections with the AMGA and Gridftp servers.
	 * 
	 * The constructor opens two connections, one with a running AMGA metadata
	 * server and other with a grid <it>gsiftp</it>. This means that the program
	 * will not continue execution if connections can not be stablished with
	 * both services.
	 * 
	 * @param certificate Certificate used for grid proxy authentication in the AMGA server
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public DSRAMGAHelper(String amga_host_name, String amga_host_port,
			String amga_home, String tmp_dir) throws IOException,
			InterruptedException {

		super(tmp_dir);

		// _user_proxy_file = TRENCADIS_UTILS.saveProxy(_tmp_dir, certificate);

		_serverConnContext = new MDServerConnectionContext();
		_serverConnContext.setHost(amga_host_name);
		_serverConnContext.setPort(Integer.parseInt(amga_host_port));
		_serverConnContext.setCurrentDir("/");
		_serverConnContext.setPermissionMask("rwx");
		_serverConnContext.setGroupMask("r-x");
		// _serverConnContext.setLogin("NULL");
		// TODO: Cambiar a autenticación con proxy
		_serverConnContext.setLogin("root");
		_serverConnContext.setPassword("root");
		_serverConnContext.setUseSSL(false);

		// _serverConnContext.setGridProxyFile(_user_proxy_file.getAbsolutePath());
		// _serverConnContext.setAuthMode(MDServerConnectionContext.AUTH_GRIDPROXY);
		_serverConnContext.setAuthMode(MDServerConnectionContext.AUTH_PASSWORD);

		_amga_home = amga_home;

	}

	public boolean checkAMGADirectoryExists(String IDOntology)
			throws IOException {

		_serverConn = new MDServerConnection(_serverConnContext);
		_mdClient = new MDClient(_serverConn);

		try {
			_serverConn.execute("ls /" + _amga_home + "/ONTO_" + IDOntology);
			_serverConn.disconnect();
			return true;
		} catch (CommandException e) {
			_serverConn.disconnect();
			return false;
		}

	}

	public boolean checkAMGAEntryExists(String IDOntology,
			String IDTRENCADISReport) throws IOException {

		_serverConn = new MDServerConnection(_serverConnContext);
		_mdClient = new MDClient(_serverConn);

		try {
			_serverConn.execute("ls /" + _amga_home + "/ONTO_" + IDOntology
					+ "/header/" + IDTRENCADISReport);
			_serverConn.disconnect();
			return true;
		} catch (CommandException e) {
			_serverConn.disconnect();
			return false;
		}

	}

	/**
	 * Creates a new structure in the AMGA catalogue that will be used to store
	 * a new kind of report. Also creates a directory in the gridftp server to
	 * store the DICOM SR files of this category.
	 * 
	 * @param xmlDsrStr
	 *            String with the report structure description
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SAXException
	 */
	public void addStructure(String xmlOntology) throws Exception {

		_serverConn = new MDServerConnection(_serverConnContext);
		_mdClient = new MDClient(_serverConn);
		// _serverConn.execute("transaction");
		DOM_TREE_AMGA_Ontology contentHandler = new DOM_TREE_AMGA_Ontology(
				_serverConn, _mdClient, _amga_home);
		int iErr = contentHandler.iParserAMGAAddStructureOntology(xmlOntology);
		if (iErr != 0) {
			_serverConn.execute("abort");
			throw new Exception(contentHandler.strGetError());
		}
		// _serverConn.execute("commit");

	}

	/**
	 * Deletes a report structure from the AMGA catalogue, this will remove the
	 * directory containing the structure and any report inside it. Also deletes
	 * the report type directory in the LFC.
	 * 
	 * @param reportType
	 *            Report type string identifier
	 * @throws CommandException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void deleteStructure(String IDOntology) throws Exception {

		_serverConn = new MDServerConnection(_serverConnContext);

		try {
			_serverConn.execute("transaction");
			_serverConn.execute("rm -rf " + _amga_home + "/ONTO_" + IDOntology);
			_serverConn.execute("commit");
		} catch (Exception ex) {
			_serverConn.execute("abort");
			throw new Exception(ex.toString());
		}
		_serverConn.disconnect();
	}

	/**
	 * Adds a new DICOM-SR report to the AMGA catalogue tree. The DICOM-SR
	 * report must be presented into an xml file. Also uploads a DICOM-SR binary
	 * file to the grid <it>gsiftp</it> server. Both actions must be atomically
	 * performed.
	 * 
	 * @param xmlDsr
	 *            String with the xml report.
	 * @throws Exception
	 */
	public void addReport(String xmlDICOMSR) throws Exception {
		_serverConn = new MDServerConnection(_serverConnContext);
		_mdClient = new MDClient(_serverConn);
		_serverConn.execute("transaction");
		DOM_TREE_AMGA_Ontology contentHandler = new DOM_TREE_AMGA_Ontology(
				_serverConn, _mdClient, _amga_home);
		int iErr = contentHandler.iParserAMGAInsertDICOMSR(xmlDICOMSR);
		if (iErr != 0) {
			_serverConn.execute("abort");
			throw new Exception(contentHandler.strGetError());
		}
		_serverConn.execute("commit");

	}

	/**
	 * Deletes a report of the specified structure file and ID from the AMGA
	 * catalogue tree and his entry in the GSI.
	 * 
	 * @param xmlDsrStr
	 *            String with the report structure of the report to be deleted
	 * @param reportID
	 *            Identifier of the report to be deleted
	 * @throws Exception
	 */
	public void deleteReport(String xmlOntology, String IDTRENCADISReport)
			throws Exception {
		_serverConn = new MDServerConnection(_serverConnContext);
		_mdClient = new MDClient(_serverConn);
		_serverConn.execute("transaction");
		DOM_TREE_AMGA_Ontology contentHandler = new DOM_TREE_AMGA_Ontology(
				_serverConn, _mdClient, _amga_home);
		int iErr = contentHandler.iParserAMGAremoveDICOMSR(xmlOntology,
				IDTRENCADISReport);
		if (iErr != 0) {
			_serverConn.execute("abort");
			throw new Exception(contentHandler.strGetError());
		}
		_serverConn.execute("commit");

	}

	/**
	 * Lists an AMGA report, showing its attributes and values for each entry in
	 * the catalogue structure tree.
	 * 
	 * @param xmlDsrStr
	 *            String with the Xml report structure file of the report to be
	 *            listed
	 * @param reportID
	 *            Identifier of the report to be listed
	 */
	public void listReport(String xmlOntology, String IDTRENCADISReport)
			throws Exception {
		_serverConn = new MDServerConnection(_serverConnContext);
		_mdClient = new MDClient(_serverConn);
		DOM_TREE_AMGA_Ontology contentHandler = new DOM_TREE_AMGA_Ontology(
				_serverConn, _mdClient, _amga_home);
		int iErr = contentHandler.iParserAMGAListDICOMSR(xmlOntology,
				IDTRENCADISReport);
		if (iErr != 0) {
			throw new Exception(contentHandler.strGetError());
		}
	}

	/**
	 * This function performs the received query in the AMGA server indicated in
	 * the constructor call of the DSRAMGAManager. It replaces the
	 * $h*o!s_p-i_t!a*l$ wildcard with the hospital identifier.
	 * 
	 * @param query
	 *            The search conditions in AMGA query language with wildcards to
	 *            be replaced by the amga_home directory.
	 * @return An attributeSetList object with the id of the DICOM-SR that
	 *         matches the query.
	 * @throws IOException
	 * @throws CommandException
	 */
	public AttributeSetList queryDICOMIds(String query, String reportType)
			throws IOException, CommandException {

		_serverConn = new MDServerConnection(_serverConnContext);
		_mdClient = new MDClient(_serverConn);

		String converted_query = query.replace("\n", "\\n").replace(
				"$h*o!s_p-i_t!a*l$", _amga_home);
		String[] attrId = new String[] { "/" + _amga_home + "/" + reportType
				+ "/header:FILE" };
		AttributeSetList reportIds = _mdClient.selectAttr(attrId,
				converted_query);

		_serverConn.disconnect();

		return reportIds;
	}

	public String downloadDICOMSR(String reportType, String reportID)
			throws Exception {

		_serverConn = new MDServerConnection(_serverConnContext);
		_mdClient = new MDClient(_serverConn);

		String query = "/" + _amga_home + "/" + reportType + "/header:FILE=\""
				+ reportID + "\"";
		String[] attrId = new String[] { "/" + _amga_home + "/" + reportType
				+ "/header:DICOM_SR" };
		AttributeSetList reportIds = _mdClient.selectAttr(attrId, query);

		_serverConn.disconnect();

		AttributeSet set = null;

		Iterator<AttributeSet> entries = reportIds.iterator();
		if (entries.hasNext()) {
			set = entries.next();
			return set.getValues()[0].replace("\\n", "\n");
		} else {
			throw new Exception(reportType + " -> " + reportID
					+ ": Report not found in the catalogue: " + _amga_home);
		}

	}
	
	public String getDICOMSRIDs() throws Exception {

		String retval = "";
		_serverConn = new MDServerConnection(_serverConnContext);
		_mdClient = new MDClient(_serverConn);
		
		Vector<String> v_onto = new Vector<String>();
		CollectionElementList dir = _mdClient.dir(_amga_home);
		for (CollectionElement element : dir) {
			if (element.isCollection())
				v_onto.add(element.getName());
		}
		String[] keys = { "IDTRENCADISREPORT" };
		for (String d : v_onto) {
			try {
				AttributeSetList attrs = null;
				attrs = _mdClient.getAttr(d + "/header", keys);
				if (attrs != null)
					for (AttributeSet attr : attrs) {
						retval += attr.getValues()[0];
						retval += ",";
					}
			} catch (CommandException e) {
				continue;
			}
		}
		retval = retval.substring(0, retval.length() - 1);
		return retval;
	}
	
	public String getDICOMSRIDsByOntology(String ontology) throws Exception {

		String retval = "";
		_serverConn = new MDServerConnection(_serverConnContext);
		_mdClient = new MDClient(_serverConn);
		
		String onto_directory = _amga_home + "/ONTO_" + ontology + "/header";
		String[] keys = { "IDTRENCADISREPORT" };
		AttributeSetList attrs = null;
		System.out.println(onto_directory);
		attrs = _mdClient.getAttr(onto_directory, keys);
		if (attrs != null)
			for (AttributeSet attr : attrs) {
				retval += attr.getValues()[0];
				retval += ",";
			}
		
		retval = retval.substring(0, retval.length() - 1);
		return retval;
	}

}
