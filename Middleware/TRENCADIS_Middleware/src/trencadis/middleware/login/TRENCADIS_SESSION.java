package trencadis.middleware.login;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Vector;

import org.apache.axis.components.uuid.SimpleUUIDGen;

import trencadis.infrastructure.voms.contact.VOMSProxyInit;
import trencadis.infrastructure.voms.contact.VOMSRequestOptions;
import trencadis.infrastructure.voms.contact.VOMSServerInfo;
import trencadis.middleware.wrapper.config.URL_IIS;
import trencadis.middleware.wrapper.config.XmlWrapper;

/**
 * This class creates a VOMS credential and manages the session parameters and
 * the created proxy
 */
public class TRENCADIS_SESSION {

	private SimpleUUIDGen _entropyProvider = new SimpleUUIDGen();

	private String _base_path;
	private String _usercert;
	private String _userkey;
	private String _pathtrustcert;
	private String _tmpdir;
	private String _host_voms;
	private String _port_voms;
	private String _host_dn_voms;
	private String _vo;
	private String _vomses;
	private String _vomsdir;
	private Vector _v_urls_iis;
	private String _password;
	private String _proxyPath;
	private X509Certificate _x509UserCertificate;

	/**
	 * Creates a session object by reading a configuration file the session
	 * object contains a VOMS Proxy initialized in the constructor.
	 * 
	 * @param configFile
	 *            File object pointing to the configuration file to be read
	 * @param password
	 *            Password of the user certificate
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public TRENCADIS_SESSION(File configFile, String password)
			throws IOException, InterruptedException {

		URL_IIS URL_IIS;

		XmlWrapper inputWrapper = new XmlWrapper(configFile, false);
		inputWrapper.wrap();

		_base_path = inputWrapper.get_CONFIGURATION().get_MIDDLEWARE_PARAMETERS().get_BASE_PATH();
		_usercert = inputWrapper.get_CONFIGURATION().get_MIDDLEWARE_PARAMETERS().get_USERCERT();
		_userkey = inputWrapper.get_CONFIGURATION().get_MIDDLEWARE_PARAMETERS().get_USERKEY();
		_pathtrustcert = inputWrapper.get_CONFIGURATION().get_MIDDLEWARE_PARAMETERS().get_PATH_CATRUSTCERT();
		_tmpdir = inputWrapper.get_CONFIGURATION().get_MIDDLEWARE_PARAMETERS().get_TMP_DIR();

		_host_voms = inputWrapper.get_CONFIGURATION().get_VOMS_PARAMETERS().get_HOST();
		_port_voms = inputWrapper.get_CONFIGURATION().get_VOMS_PARAMETERS().get_PORT();
		_host_dn_voms = inputWrapper.get_CONFIGURATION().get_VOMS_PARAMETERS().get_HOSTDN();
		_vo = inputWrapper.get_CONFIGURATION().get_VOMS_PARAMETERS().get_VO();
		_vomses = inputWrapper.get_CONFIGURATION().get_VOMS_PARAMETERS().get_VOMSES();
		_vomsdir = inputWrapper.get_CONFIGURATION().get_VOMS_PARAMETERS().get_VOMSDIR();

		_v_urls_iis = new Vector();
		Iterator it_urls = inputWrapper.get_CONFIGURATION()
				.get_INDEX_SERVICE_PARAMETERS().getAll_URL_IIS();
		while (it_urls.hasNext()) {
			URL_IIS = (trencadis.middleware.wrapper.config.URL_IIS) it_urls
					.next();
			_v_urls_iis.add(URL_IIS.getValue());
		}

		_password = password;

		_proxyPath = _tmpdir + "/x509up_uTRENCADIS_MIDDLEWARE_"
				+ System.currentTimeMillis();

		// TODO code application logic here
		System.setProperty("X509_USER_CERT", _usercert);
		System.setProperty("X509_USER_KEY", _userkey);
		System.setProperty("VOMSES_LOCATION", _vomses);
		System.setProperty("VOMSDIR", _vomsdir);
		System.setProperty("CADIR", _pathtrustcert);
		System.setProperty("X509_CERT_DIR", _pathtrustcert);

		VOMSServerInfo vsi = new VOMSServerInfo();
		vsi.setVoName(_vo);
		vsi.setHostName(_host_voms);
		vsi.setPort(Integer.parseInt(_port_voms));
		vsi.setHostDn(_host_dn_voms);
		vsi.setAlias(_vo);

		VOMSProxyInit vpi = null;
		vpi = VOMSProxyInit.instance(_password);

		vpi.setProxyOutputFile(_proxyPath);
		vpi.setProxyType(trencadis.infrastructure.voms.contact.VOMSProxyBuilder.GT4_PROXY);
		vpi.setDelegationType(trencadis.infrastructure.voms.contact.VOMSProxyConstants.DELEGATION_FULL);

		VOMSRequestOptions vrp = new VOMSRequestOptions();
		vrp.setVoName(_vo);
		vrp.setLifetime(10000);
		java.util.ArrayList<VOMSRequestOptions> alvrp = new java.util.ArrayList<VOMSRequestOptions>();
		alvrp.add(vrp);

		vpi.addVomsServer(vsi);
		vpi.getVomsProxy(alvrp);

		System.setProperty("X509_USER_PROXY", _proxyPath);

	}

	/**
	 * This method is used to return a String with the X509 credential generated
	 * for this session object.
	 * 
	 * @return String with an X509 credential
	 * @throws IOException
	 */
	public String getX509VOMSCredential() throws IOException {

		FileReader fr = new FileReader(_proxyPath);
		BufferedReader br = new BufferedReader(fr);

		String text = new String();
		String aux = new String();

		while ((aux = br.readLine()) != null) {
			text += aux + "\n";
		}

		br.close();
		fr.close();

		return text.toString();
	}

	/**
	 * Getter method for the URI of the central information service
	 * 
	 * @return The URI of the central information service
	 */
	public Vector getUrlIISCentral() {
		return _v_urls_iis;
	}

	/**
	 * Getter method for the user certificate
	 * 
	 * @return Path to the user certificate
	 */
	public String getUsercert() {
		return _usercert;
	}

	/**
	 * Getter method for the user certificate
	 * 
	 * @return Path to the user certificate
	 */
	public String getUserName() {
		try {
			X509Certificate userCert = org.globus.gsi.CertUtil
					.loadCertificate(_usercert);
			String userDN = userCert.getSubjectDN().toString();

			String login = userDN.split(",")[4].substring(3);

			return login;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**************************************************************************/
	/* Extrae el DN del certificado */
	/**************************************************************************/
	public String getDN() {
		try {

			X509Certificate userCert = org.globus.gsi.CertUtil
					.loadCertificate(_usercert);

			// Retorna el Distinguised Name
			String userDN = userCert.getSubjectDN().toString();
			return userDN.replace(" ", "").replace("/", "_").replace(",", "_")
					.replace("=", "_");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public String getLiteralDN() {
		try {
			X509Certificate userCert = org.globus.gsi.CertUtil
					.loadCertificate(_usercert);
			// Distinguised Name
			return userCert.getSubjectDN().toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Getter method for the user key
	 * 
	 * @return Path to the user key
	 */
	public String getUserkey() {
		return _userkey;
	}

	/**
	 * Getter method for virtual organization
	 * 
	 * @return Virtual organization
	 */
	public String getVO() {
		return _vo;
	}

	/**
	 * Getter method for the vomses configuration
	 * 
	 * @return The vomses configuration path
	 */
	public String getVomses() {
		return _vomses;
	}

	/**
	 * Getter method for the temporal directory
	 * 
	 * @return The temporal directory path
	 */
	public String getTmpDir() {
		return _tmpdir;
	}

	/**
	 * This method returns a file pointing to the proxy created for this session
	 * 
	 * @return A File object pointing to the proxy location.
	 * @throws IOException
	 */
	public File getX509VOMSProxyFile() throws IOException {
		return new File(_proxyPath);
	}

	/**
	 * Returns the error output of an executed command. Internal use.
	 * 
	 * @param p
	 *            The executed process from which to get the errors
	 * @return The string with the error output
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException
	 */
	private String getErrorString(Process p) throws FileNotFoundException,
			IOException {

		String error = new String();

		BufferedInputStream bin = new BufferedInputStream(p.getErrorStream());
		InputStreamReader chin = new InputStreamReader(bin);

		while (true) {
			int ch = chin.read();
			if (ch == -1) {
				break;
			}
			error = error + (char) ch;
		}

		return error;
	}

}
