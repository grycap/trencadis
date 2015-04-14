/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.common.util;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;

import org.apache.axis.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.TrustedCertificates;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.gsi.proxy.ProxyPathValidator;
import org.globus.wsrf.config.ConfigException;
//import org.globus.wsrf.impl.security.descriptor.ServiceSecurityConfig;

import org.globus.wsrf.impl.security.descriptor.ServiceSecurityDescriptor;
import org.globus.wsrf.security.SecurityException;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.utils.ContextUtils;
import org.gridforum.jgss.ExtendedGSSCredential;
import org.gridforum.jgss.ExtendedGSSManager;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

/**
 * Esta clase contiene varios metodos utilizados por los Services y
 * Resources para acceder y modificar valores del entorno de seguridad.
 * @author etorres
 *
 */
public class CertUtils extends CertLocatorUtils {

	static Log logger = LogFactory.getLog(CertUtils.class);

	public static final int SERVICE_SUBJECT = 0;
	public static final int CONTAINER_SUBJECT = 1;

	/*
	 * Para manipular certificados X509.
	 */

	/**
	 * Carga un certificado como cadena X509 a partir de un String
	 * que contiene un certificado codificado en PEM.
	 * Este es el formato utilizado en los certificados Proxy.
	 */
	public static X509Certificate getProxyCertChainX509(String certChainStr) {
		X509Certificate certChainX509 = null;
		try {
			// skipping possible garbage
			certChainStr = certChainStr.trim();

			// create certificate factory
			CertificateFactory cf = CertificateFactory.getInstance("X.509");

			InputStream certStream = null;
			certStream = new java.io.ByteArrayInputStream(certChainStr.getBytes("UTF-8"));

			certChainX509 = (X509Certificate)cf.generateCertificate(certStream);
		} catch (Exception e) {
			logger.error("Could not load Certificate Chain: " + e);
		}
		return certChainX509;		
	}

	/**
	 * Carga un certificado como cadena X509 a partir de un String
	 * que contiene un certificado codificado en DER + PEM.
	 * Este es el formato utilizado en los certificados de host.
	 */
	public static X509Certificate getHostCertChainX509(String certChainStr) {
		X509Certificate certChainX509 = null;
		try {
			// skipping possible garbage
			certChainStr = certChainStr.trim();

			InputStream certStream = null;
			certStream = new java.io.ByteArrayInputStream(certChainStr.getBytes("UTF-8"));

			certChainX509 = CertUtil.loadCertificate(certStream);
		} catch (Exception e) {
			logger.error("Could not load Certificate Chain: " + e);
		}
		return certChainX509;		
	}

	/**
	 * Obtiene el Distinguished Name del issuer de un certificado X509.
	 */
	public static String getHostCertIssuerDN(X509Certificate hostCertChainX509) {
		String issuerDN = "";
		try {
			X500Principal principal = hostCertChainX509.getIssuerX500Principal();
			issuerDN = principal.getName(X500Principal.RFC2253);
		} catch (Exception e) {
			logger.error("Can not load issuer Distinguished Name: " + e);
		}
		return issuerDN;
	}	

	/**
	 * Validates X509 certificate agains trusted CAs.
	 * @param proxy
	 * @throws Exception 
	 * @throws Exception
	 */
	public static void validate(X509Certificate certificate)
	throws Exception {
		X509Certificate[] certificateChain =
			new X509Certificate[] {certificate};

		String caTrustCertPath = CertLocatorUtils.getPathToTrustedCA();

		TrustedCertificates trustedCerts =
			TrustedCertificates.load(caTrustCertPath);

		if (trustedCerts == null ||
				trustedCerts.getCertificates() == null ||
				trustedCerts.getCertificates().length == 0) {
			throw new Exception("Unable to load CA ceritificates");
		}

		ProxyPathValidator validator = new ProxyPathValidator();

		validator.validate(certificateChain,
				trustedCerts.getCertificates());
	}

	/*
	 * Para Servicios y Recursos. Necesitan ser llamados desde el
	 * ambito de un Servicio o Recurso establecidos.
	 */

	/**
	 * Devuelve el nombre del archivo que contiene las credenciales
	 * publicas con que ha sido iniciado el servicio, cuando las
	 * credenciales han sido configuradas en el Security Descriptor.
	 * @return String servCertFilename. Puede ser null.
	 * @throws Exception
	 */
/*	public static String getServCertFilename()
	throws Exception {
		String servCertFilename = null;
		try {
			ServiceSecurityDescriptor servSecDesc =
				getServSecurityDesc();
			servCertFilename = servSecDesc.getCertFilename();
		} catch (ConfigException e) {
			throw new Exception("ServiceSecurityDescriptor load failed", e);
		} catch (SecurityException e) {
			throw new Exception("ServiceSecurityConfig initialization failed", e);
		}
		return servCertFilename;
	}*/

	/**
	 * Devuelve el nombre del archivo que contiene la clave
	 * privada con que ha sido iniciado el servicio, cuando
	 * las credenciales han sido configuradas en el Security
	 * Descriptor.
	 * @return String servPrivFilename. Puede ser null.
	 * @throws Exception
	 */
        /*
	public static String getServPrivFilename()
	throws Exception {
		String servPrivFilename = null;
		try {
			ServiceSecurityDescriptor servSecDesc =
				getServSecurityDesc();
			servPrivFilename = servSecDesc.getKeyFilename();
		} catch (ConfigException e) {
			throw new Exception("ServiceSecurityDescriptor load failed", e);
		} catch (SecurityException e) {
			throw new Exception("ServiceSecurityConfig initialization failed", e);
		}
		return servPrivFilename;
	}	
*/
	/**
	 * Carga el ServiceSecurityDescriptor para los metodos
	 * getServPrivFilename y getServCertFilename.
	 * @return ServiceSecurityDescriptor
	 * @throws SecurityException
	 * @throws ConfigException
	 */
/*
	private static ServiceSecurityDescriptor getServSecurityDesc() 
	throws SecurityException, ConfigException {

		MessageContext msgCtx = MessageContext.getCurrentContext();
		String jndiPath = ContextUtils.getTargetServicePath(msgCtx);

		// forces the service security to be initialized
		ServiceSecurityConfig.initialize(msgCtx);

		ServiceSecurityDescriptor servSecurityDesc =
			ServiceSecurityConfig.getSecurityDescriptor(jndiPath);

		return servSecurityDesc;
	}
*/
	/**
	 * Extrae el distinguished name (DN) de una cadena de credenciales.
	 * @return
	 */
	public static String getPublicSubjectDN(X509Certificate certChainX509)
	throws Exception {
		String publicSubjectDN = "";
		try {
			publicSubjectDN = certChainX509.getSubjectX500Principal().getName();
		} catch (Exception e) {
			throw new Exception("Unable to retrieve Subject DN: ", e);
		}
		return publicSubjectDN;
	}

	/**
	 * Extrae el Common Name del Subject DN una cadena de credenciales.
	 * @return
	 */
	public static String getPublicSubjectCN(X509Certificate certChainX509)
	throws Exception {
		String publicSubjectCN = "";
		try {
			// Distinguished Name
			publicSubjectCN = certChainX509.getSubjectX500Principal().getName();

			// Common Name
			String[] attArr = publicSubjectCN.split(",");
			for (int i = 0; i < attArr.length; i++) {
				boolean found = false;
				String[] possibleCNArr = { "CN=", "cn=" };
				for (int j = 0; j < possibleCNArr.length; j++) {
					if (attArr[i].startsWith(possibleCNArr[j])) {
						publicSubjectCN =
							attArr[i].replaceFirst(possibleCNArr[j], "");
						found = true;
						break;
					}
				}
				if (found) break;
			}
		} catch (Exception e) {
			throw new Exception("Unable to retrieve Subject CN: ", e);
		}
		return publicSubjectCN;
	}

	/**
	 * Extrae toda la informacion a partir del Organizational Unit del Subject
	 * DN de una cadena de credenciales.
	 * @return
	 */
	public static String getPublicSubjectOU(X509Certificate certChainX509)
	throws Exception {
		final String separator = ",";
		String publicSubjectOU = null;
		try {
			// Distinguished Name
			String tmpPublicSubjectOU =
				certChainX509.getSubjectX500Principal().getName(
						X500Principal.RFC2253);

			// Organizational Unit
			String ouAttr = "OU=";
			String[] attArr = tmpPublicSubjectOU.split(separator);
			boolean found = false;
			for (int i = 0; i < attArr.length; i++) {
				if ((!found) && (attArr[i].startsWith(ouAttr))) {
					publicSubjectOU = attArr[i];
					found = true;
				} else if (found) {
					publicSubjectOU = publicSubjectOU + separator + attArr[i];
				}
			}
		} catch (Exception e) {
			throw new Exception("Unable to retrieve Subject OU: ", e);
		}
		return publicSubjectOU;
	}

	/**
	 * Extrae las credenciales publicas del Service.
	 * @return X509Certificate - Service Certificate Chain.
	 * @throws Exception
	 */
	public static X509Certificate getServCertificateChain()
	throws Exception {
		return getCertificateChain(CertUtils.SERVICE_SUBJECT);
	}

	/**
	 * Extrae las credenciales publicas del Container.
	 * @return X509Certificate - Container Certificate Chain.
	 * @throws Exception
	 */
	public static X509Certificate getContCertificateChain()
	throws Exception {
		return getCertificateChain(CertUtils.CONTAINER_SUBJECT);
	}

	/**
	 * Extrae la Private Key del Service.
	 * @return PrivateKey - Service Certificate Chain.
	 * @throws Exception
	 */
	public static PrivateKey getServPrivateKey()
	throws Exception {
		return getPrivateKey(CertUtils.SERVICE_SUBJECT);
	}

	/**
	 * Extrae la Private Key del Container.
	 * @return PrivateKey - Container Certificate Chain.
	 * @throws Exception
	 */
	public static PrivateKey getContPrivateKey()
	throws Exception {
		return getPrivateKey(CertUtils.CONTAINER_SUBJECT);
	}

	/**
	 * Extrae las credenciales publicas del Container o el Service.
	 * @return X509Certificate - Certificate Chain.
	 * @throws Exception
	 */
	public static X509Certificate getCertificateChain(int subjectID)
	throws Exception {
		X509Certificate[] certChainArr =
			CertUtils.getCertificateChainArr(subjectID);
		return certChainArr[0];
	}	

	/**
	 * Extrae de las credenciales del Container o el Service los
	 * certificados que contienen la clave publica.
	 * @return X509Certificate[] certs
	 *                           Certificados que contienen la clave
	 *                           publica.
	 */
	public static X509Certificate[] getCertificateChainArr(int subjectID)
	throws Exception {
		X509Certificate[] servChainArr = null;
		try {
			GlobusGSSCredentialImpl gssCred =
				CertUtils.getGlobusGSSCredential(subjectID);
			servChainArr = gssCred.getCertificateChain();
			if ((servChainArr == null) || (servChainArr.length < 0)) {
				throw new Exception("No certificate chains found");
			}
		} catch (Exception e) {
			throw new Exception("Unable to load certificate chain: ", e);
		}
		return servChainArr;
	}	

	/**
	 * Extrae la Private Key del Container o el Service.
	 * @return PrivateKey - Certificate Chain.
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(int subjectID)
	throws Exception {
		PrivateKey privKey = null;
		try {
			GlobusGSSCredentialImpl gssCred =
				CertUtils.getGlobusGSSCredential(subjectID);
			privKey = gssCred.getPrivateKey();
			if (privKey == null) {
				throw new Exception("No Private Key found");
			}
		} catch (Exception e) {
			throw new Exception("Unable to load Private Key: ", e);
		}			
		return privKey;
	}	

	/**
	 * Exporta las credenciales del Service.
	 * @return String - Credenciales exportadas.
	 * @throws Exception
	 */
	public static String getServExportedCredentials()
	throws Exception{
		return CertUtils.getExportedCredentials(CertUtils.SERVICE_SUBJECT);
	}

	/**
	 * Exporta las credenciales de Container o de Service de
	 * forma que puedan ser transmitidas por un canal seguro
	 * y recuperadas por el cliente.
	 * @param subjectID
	 * @return String - Credenciales exportadas.
	 * @throws Exception
	 */
	public static String getExportedCredentials(int subjectID)
	throws Exception {
		String exportedCredentials = null;
		try {
			GlobusGSSCredentialImpl gssCred =
				CertUtils.getGlobusGSSCredential(subjectID);
			byte[] data = gssCred.export(ExtendedGSSCredential.IMPEXP_OPAQUE);
			exportedCredentials = new String(data);

			// extraemos las credenciales publicas
			final String LINE_TERMINATORS = "\n|\r\n|\r|\u0085|\u2028|\u2029";
			final String CERT_BEGIN_LINE = "-----BEGIN CERTIFICATE-----";
			final String CERT_END_LINE =   "-----END CERTIFICATE-----";

			String[] lines = exportedCredentials.split(LINE_TERMINATORS);
			exportedCredentials = "";

			int status = 0;

			for (int i = 0; i < lines.length; i++) {
				switch (status) {
				case 0:
					// buscando la cabecera de certificado
					if (lines[i].trim().equals(new String(CERT_BEGIN_LINE))) {
						status = 1;
						exportedCredentials = exportedCredentials + lines[i] +
						System.getProperty("line.separator");
					}
					break;
				case 1:
					// dentro del certificado
					exportedCredentials = exportedCredentials + lines[i];
					if (lines[i].trim().equals(new String(CERT_END_LINE))) {
						status = 2;
					} else {
						exportedCredentials = exportedCredentials +
						System.getProperty("line.separator");
					}
					break;
				default:
				case 2:
					// no esperamos encontrar mas de una cadena
					if (lines[i].trim().equals(new String(CERT_END_LINE)))
						throw new Exception("Malformed exported credentials");
					break;
				}
			}
		} catch (Exception e) {
			throw new Exception("Can not export credentials", e);
		}
		return exportedCredentials;
	}

	/**
	 * Extrae las credenciales GSS del subject del Container o el
	 * Service. 
	 * @param int subjectID - Identificador de sujeto. Por defecto el
	 *                        sujeto del Service.  
	 * @return GlobusGSSCredentialImpl - Credencial.
	 */
	@SuppressWarnings("unchecked")
	public static GlobusGSSCredentialImpl getGlobusGSSCredential(int subjectID)
	throws Exception {
		GlobusGSSCredentialImpl gssCred = null;
		Subject subject = null;
		try {
			switch (subjectID) {
			case CertUtils.CONTAINER_SUBJECT:
				subject = SecurityManager.getManager().getSystemSubject();
				break;
			case CertUtils.SERVICE_SUBJECT:				
			default:
				subject = SecurityManager.getManager().getServiceSubject();
			break;
			}
			Iterator it = subject.getPrivateCredentials().iterator();
			gssCred = (GlobusGSSCredentialImpl)it.next();
		} catch (NullPointerException e) {
			throw new Exception("Unable to obtain subject: ", e);
		} catch (Exception e) {
			throw new Exception("Unable to load credentials from subject: ", e);
		}
		return gssCred;
	}

	/*
	 * Para clientes. Necesitan ser llamados desde el ambito de una
	 * aplicacion cliente. 
	 */

	/**
	 * This method gets the default user proxy certificate in a String
	 * variable. The exported certificate in PEM format should be transported
	 * to the WS in a secure way.
	 * @return strProxy
	 *         Certificate in PEM format.
	 */
	public static String getDefaultUserProxyIntoStr() {
		ExtendedGSSCredential gssCred = null;
		String strProxy = null;
		try {
			ExtendedGSSManager manager = (ExtendedGSSManager)ExtendedGSSManager.getInstance();
			gssCred = (ExtendedGSSCredential)manager.createCredential(GSSCredential.ACCEPT_ONLY);

			byte [] data = gssCred.export(ExtendedGSSCredential.IMPEXP_OPAQUE);
			strProxy = new String(data);

			gssCred.dispose();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			gssCred = null;
		}
		return strProxy;
	}

	/**
	 * Carga las credenciales de Container en un objeto GSSCredential
	 * que puede ser usado para iniciar una conexion pasandola como
	 * propiedad al Stub en GSIConstants.GSI_CREDENTIALS
	 * @return GSI Credentials
	 */
	public static GSSCredential getContainetCredIntoGSICred() {
		GSSCredential gssCred = null;
		try {
			// credentials
			PrivateKey contPrivateKey = CertUtils.getContPrivateKey();
			X509Certificate[] contCertificateChains =
				CertUtils.getCertificateChainArr(CertUtils.CONTAINER_SUBJECT);

			GlobusCredential serviceCred =
				new GlobusCredential(contPrivateKey, contCertificateChains);
			serviceCred.verify();

			gssCred = new GlobusGSSCredentialImpl(serviceCred, 
					GSSCredential.INITIATE_AND_ACCEPT);
		} catch (GlobusCredentialException e) {
			logger.error("Can not load Container Credentials", e);
		} catch (GSSException e) {
			logger.error("Cant not initialize GSI Credentials", e);
		} catch (Exception e) {
			logger.error("Cant not load GSS Credentials", e);
		}
		return gssCred;
	}
}

