/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.gateKeeper.credentials;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.globus.gsi.GlobusCredential;

public class HostCredentialHandler {

	String _tmpServiceDir;
	String _serviceName;
	GlobusCredential _credential;

	public HostCredentialHandler(String tmpServiceDir, String serviceName) {
		_tmpServiceDir = tmpServiceDir;
		_serviceName = serviceName;
	}

	public void createHostGridProxy() throws Exception {
		System.setProperty("X509_USER_KEY",  "/etc/grid-security/containerkey.pem");
		System.setProperty("X509_USER_CERT", "/etc/grid-security/containercert.pem");
		System.setProperty("X509_CERT_DIR",  "/etc/grid-security/certificates");
		System.setProperty("X509_VOMSDIR",   "/etc/grid-security/vomsdir"); //No fa falta
		System.setProperty("X509_USER_PROXY", _tmpServiceDir + "/" + _serviceName + "_container_proxy");

		_credential = new org.globus.tools.proxy.DefaultGridProxyModel().createProxy("");
		_credential.save(new FileOutputStream(_tmpServiceDir + "/" + _serviceName + "_container_proxy"));
	}

	public String getPlainTextCredential() throws IOException {
		StringBuilder contents = new StringBuilder();
		BufferedReader proxy =  new BufferedReader(new FileReader(_tmpServiceDir + "/" + _serviceName + "_container_proxy"));

		String line = null;
		while (( line = proxy.readLine()) != null){
			contents.append(line);
			contents.append(System.getProperty("line.separator"));
		}

		proxy.close();

		return contents.toString();
	}

}
