package trencadis.infrastructure.services.dicomstorage.backend.grid_ftp;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.io.FileUtils;
import org.globus.ftp.DataSink;
import org.globus.ftp.FileRandomIO;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.GridFTPSession;
import org.globus.ftp.exception.ClientException;
import org.globus.ftp.exception.ServerException;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

public class Grid_FTP {


	// GridFTP parameters

	private GridFTPClient _gftp_client;
	private String _gftp_host;
	private int _gftp_port;
	private String _gftp_home_dir;
	private GSSCredential _credential;
	
	/**
	 * Creates a new GridFTP Client
	 * 
	 * @param host_name Direction of server
	 * @param host_port Port of server
	 * @param home_dir Directory of the file system server
	 */
	public Grid_FTP(String host_name, String host_port,
			String home_dir) {
		
		_gftp_host = host_name;
		_gftp_port = Integer.parseInt(host_port);
		_gftp_home_dir = home_dir;
		_credential = null;
	}
	
	/**
	 * Creates a new GSSCredential for do operations over GridFTP server
	 * 
	 * @param credentialFile
	 */
	private void setCredential(String credentialFile) {		
		try {
			File proxy = new File("/tmp/gcredentials");
			FileUtils.writeStringToFile(proxy, credentialFile);
			GlobusCredential gcred = new GlobusCredential("/tmp/gcredentials");
			_credential = new GlobusGSSCredentialImpl(gcred, GSSCredential.DEFAULT_LIFETIME);			
		} catch (IOException | GlobusCredentialException | GSSException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the configuration of BackEnd
	 * 	
	 * @return configuration (XML format)
	 */
    public String getBackEndParameters() {
    	String config = "";
    	try {
	    	config += "\t\t<BACKEND_TYPE>GRID_FTP</BACKEND_TYPE>\n";
	    	config += "\t\t<HOST_NAME>";
	    	if (_gftp_host.equals("localhost"))    		
					config += InetAddress.getLocalHost().getCanonicalHostName();
			else
	    		config += _gftp_host;
	    	config += "</HOST_NAME>\n";
	    	config += "\t\t<HOST_PORT>";
	    	config += _gftp_port;
	    	config += "</HOST_PORT>\n";
	    	config += "\t\t<HOME_DIR>";
	    	config += _gftp_home_dir;
	    	config += "</HOME_DIR>\n"; 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	return config;
    }
	
	/**
	 * Adds a DICOM-SR report to the GridFTP server.
	 * 
	 * @param strIDFile Identifier of the report
	 * @param f File descriptor of the DICOM-SR file to add
	 * @param credentialFile X509 VOMS Credential
	 */
	public void addFile(String strIDFile, File f, String credentialFile) {
		
		try {
			
			if (_credential == null)
				setCredential(credentialFile);
			
			_gftp_client = new GridFTPClient(_gftp_host, _gftp_port);
			_gftp_client.authenticate(_credential);
			_gftp_client.setType(GridFTPSession.TYPE_ASCII);
			_gftp_client.setPassive();
			_gftp_client.setLocalActive();

			String fileName = _gftp_home_dir + "/" + strIDFile;

			if (_gftp_client.exists(fileName)) {
				throw new IOException("The file <" + fileName
						+ "> already exists in the remote server:");
			}

			_gftp_client.put(f, fileName, true);
			
			_gftp_client.close();
			
		} catch (ServerException | IOException | ClientException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	/**
	 * Deletes a DICOM-SR report from the GridFTP server.
	 * 
	 * @param strIDFile Identifier of the report
	 * @param credentialFile X509 VOMS Credential
	 */
	public void deleteFile(String strIDFile, String credentialFile) {

		try {
			
			if (_credential == null)
				setCredential(credentialFile);
			
			_gftp_client = new GridFTPClient(_gftp_host, _gftp_port);
			_gftp_client.authenticate(_credential);
			_gftp_client.setType(GridFTPSession.TYPE_ASCII);
			_gftp_client.setPassive();
			_gftp_client.setLocalActive();
			
			if (!_gftp_client.exists(_gftp_home_dir + "/" + strIDFile)) {
				throw new IOException("The file <" + strIDFile
						+ "> doesn't exist in the remote server: ");
			}
			
			_gftp_client.deleteFile(_gftp_home_dir + "/" + strIDFile);

			_gftp_client.close();
			
		} catch (ServerException | IOException | ClientException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	/**
	 * Downloads a DICOM-SR report from the GridFTP server.
	 * 
	 * @param strIDFile Identifier of the report
	 * @param credentialFile X509 VOMS Credential
	 * @return DICOM-SR report
	 */
	public String xmlGetDICOMSRFile(String strIDFile, String credentialFile) {
	 
		try {

			File dsr = new File("/tmp/" + strIDFile);
			if (_credential == null)
				setCredential(credentialFile);
			
			_gftp_client = new GridFTPClient(_gftp_host, _gftp_port);
			_gftp_client.authenticate(_credential);
			_gftp_client.setType(GridFTPSession.TYPE_ASCII);
			_gftp_client.setPassive();
			_gftp_client.setLocalActive();
			
			if (!_gftp_client.exists(_gftp_home_dir + "/" + strIDFile)) {
				throw new IOException("The file <" + strIDFile
						+ "> doesn't exist in the remote server:");
			}
			
			_gftp_client.get(_gftp_home_dir + "/" + strIDFile, dsr);

			_gftp_client.close();
			
			String retval = FileUtils.readFileToString(dsr);
			dsr.delete();
			return retval;
			
		} catch (ServerException | IOException | ClientException e) {
			throw new RuntimeException(e.getMessage());
		}

	}
	
	/**
	 * Downloads a DICOM-SR report from the GridFTP server.
	 * 
	 * @param strIDFile Identifier of the report
	 * @param credentialFile X509 VOMS Credential
	 * @param localPath Local path to store the report
	 * @return true if the download is done successfully
	 */
	public boolean xmlGetDICOMSRFile(String strIDFile, String credentialFile, String localPath) {
		boolean isDownloaded = false;
		try {

			File dsr = new File(localPath + "/" + strIDFile);
			if (_credential == null)
				setCredential(credentialFile);
			
			_gftp_client = new GridFTPClient(_gftp_host, _gftp_port);
			_gftp_client.authenticate(_credential);
			_gftp_client.setType(GridFTPSession.TYPE_ASCII);
			_gftp_client.setPassive();
			_gftp_client.setLocalActive();
			
			if (!_gftp_client.exists(_gftp_home_dir + "/" + strIDFile)) {
				throw new IOException("The file <" + strIDFile
						+ "> doesn't exist in the remote server:");
			}
			
			_gftp_client.get(_gftp_home_dir + "/" + strIDFile, dsr);

			_gftp_client.close();
			isDownloaded = true;
			
		} catch (ServerException | IOException | ClientException e) {
			throw new RuntimeException(e.getMessage());
		}
		return isDownloaded;
	}
	
	/**
	 * Downloads all DICOM-SR reports from the GridFTP server.
	 * 
	 * @param strIDFiles All identifiers of the reports
	 * @param credentialFile X509 VOMS Credential
	 * @return DICOM-SR reports
	 */
	public String xmlGetAllDICOMSRFiles(String strIDFiles, String credentialFile){
    	try{
    		
			if (_credential == null)
				setCredential(credentialFile);
    		
    		_gftp_client = new GridFTPClient(_gftp_host, _gftp_port);
			_gftp_client.authenticate(_credential);
			
			String retval = "";
						    
        	String[] idFiles = strIDFiles.split(",");
        	retval += "\t<DICOM_REPORTS>\n";
        	for(String strIDFile : idFiles) {
        		File dsr = new File("/tmp/" + strIDFile);
        		DataSink sink = new FileRandomIO(new RandomAccessFile(dsr, "rw"));		
        		_gftp_client.setPassive();
    			_gftp_client.setLocalActive();
        		_gftp_client.extendedGet(_gftp_home_dir + "/" + strIDFile,
        								 _gftp_client.getSize(_gftp_home_dir + "/" + strIDFile),
        								 sink,
        								 null);
        		//retval += "\t<DICOM_SR>";
        		String aux = FileUtils.readFileToString(dsr);
        		retval += aux.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        		//retval += "</DICOM_SR>\n";
        		dsr.delete();
        	}
        	retval += "\t</DICOM_REPORTS>\n";
        	_gftp_client.close();
        	
        	return retval;

        }catch(IOException | ServerException | ClientException ex){
        	ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

}
