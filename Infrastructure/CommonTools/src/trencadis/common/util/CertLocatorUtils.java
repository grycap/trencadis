/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.common.util;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CertLocatorUtils {

	static Log logger = LogFactory.getLog(CertLocatorUtils.class);

	/**
	 * Posibles localizaciones de los certificados de CAs.
	 */
	private static final String[] TRUSTEDCA_LOCATION = {
		System.getenv("X509_CERT_DIR"),
		System.getProperty("user.home") + "/.globus/certificates",
		"/etc/grid-security/certificates",
		System.getenv("GLOBUS_LOCATION") + "/share/certificates"
	};

	/**
	 * Retorna el primer objeto valido de un arreglo.
	 * @param possiblePath
	 * @param isDirectory
	 * @return credentialFilePath. Puede ser null.
	 */
	private static String getCredentialFilePath(String[] possiblePath,
			boolean isDirectory) {
		String credentialFilePath = null;
		for (int i = 0; i < possiblePath.length; i++) {
			if (possiblePath[i] != null) {
				boolean isValid =
					(isDirectory) ? ((new File(possiblePath[i]).isDirectory())) : ((new File(possiblePath[i]).isFile()));
				if (isValid) {
					credentialFilePath = possiblePath[i];
					break;
				}
			}
		}
		return credentialFilePath;
	}

	/**
	 * Devuelve la ruta al directorio Trusted CA.
	 */
	protected static String getPathToTrustedCA() {
		return getCredentialFilePath(CertLocatorUtils.TRUSTEDCA_LOCATION, true);
	}
}

