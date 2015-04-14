package trencadis.infrastructure.gateKeeper.security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DEROctetString;

import trencadis.infrastructure.voms.ac.AttributeCertificate;
import trencadis.infrastructure.voms.VOMSAttribute;

public class VOMSCertificate {

	static Log logger = LogFactory.getLog(VOMSCertificate.class);

	private String caTrustCertPath = "/etc/grid-security/certificates";

	private X509Certificate cert = null;

	public VOMSCertificate(String certStr) {
		readCertificate(certStr);
	}
        public VOMSCertificate(String certStr, String strcaTrustCertPath) {
            caTrustCertPath = strcaTrustCertPath;
            readCertificate(certStr);
        }


	public void readCertificate(String certStr) {
		try {
			// skipping possible garbage
			certStr = certStr.trim();
			// read certificate
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream certStream = null;
			certStream = new java.io.ByteArrayInputStream(certStr.getBytes("UTF-8"));
			this.cert = (X509Certificate)cf.generateCertificate(certStream);
		} catch (Exception ex) {
			logger.error("Could not get VOMS Certificate: " + ex);
		}
	}

	public List getAssertions() throws IOException {
		return this.getAssertions(true);
	}
        public String getSubjectDN(){
        	int iIndex = 1;

        	try{
                String strDN = cert.getSubjectDN().toString().replaceAll(", ","/");
                String[] astrDN = strDN.split("/");
                strDN = "";
                
            	if (astrDN[iIndex].equals("CN=host")) {
            		strDN = astrDN[iIndex] + "/" + astrDN[iIndex+1] + "/" + strDN;
            		iIndex+=2;
            	}
                
                for (; iIndex < astrDN.length; iIndex++) {
                		strDN = astrDN[iIndex] + "/" + strDN;
                }
                
                return "/" + strDN.substring(0,strDN.length()-1);
            } catch(Exception ex) {
                logger.error("Error to read DN: " + ex);
                return "";
            }
        }
        
        
        

	public List getAssertions(boolean validate) throws IOException {
		List fQNA = null;
		try {
			// extract VOMS assertions
			byte[] attCertRaw = this.cert.getExtensionValue("1.3.6.1.4.1.8005.100.100.5");
                                                
			if( attCertRaw != null ) {
                                                                                
				ByteArrayInputStream in = new ByteArrayInputStream(attCertRaw);

				byte[] payload = ((DEROctetString) new ASN1InputStream(in).readObject()).getOctets();
                                                                
				//ASN1Sequence seq = (ASN1Sequence) new org.bouncycastle.asn1.DERInputStream(new ByteArrayInputStream(payload)).readObject();
				ASN1Sequence seq = (ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(payload)).readObject();                                
                                
				// VOMS is a SEQUENCE of SET of AttributeCertificate!
				for (Enumeration e = seq.getObjects(); e.hasMoreElements();) {
					ASN1Sequence s2 = (ASN1Sequence) e.nextElement();                                                                                
					
                                        for (Enumeration f = s2.getObjects(); f.hasMoreElements();) {                                                                                           
                                            
						ASN1Sequence s3 = (ASN1Sequence) f.nextElement();
                                                
                                                AttributeCertificate ac = new AttributeCertificate(s3);                                                     
                                                

						/**
						 * Lista de elementos a validar:
						 * -----------------------------
						 * - No expirado.
						 * - Firmado por una fuente confiable.
						 * - Contiene al menos, una FQNA.
						 * - �VO? Creo que no tiene sentido.
						 */

						// check that proxy is still valid (i.e. not expired)
//						if (!ac.isValid())
//							throw new IOException("Proxy is not valid.");                                                
						// check that proxy is signed by a trusted CA certificate
						CATrustedStore caTrustStore = new CATrustedStore(caTrustCertPath);
						X509Certificate[] store = caTrustStore.getStore();

						boolean verified = false;
						int i = 0;

	                    while ( (!verified) && ( i < store.length ) ) {
	                    	verified = ac.verify(store[i].getPublicKey());
	                        i++;
	                    }
//						if (!verified)
//							throw new IOException("Could not check that proxy is signed by a trusted CA.");

						// and finally, we can extract assertions
						fQNA = new VOMSAttribute(ac).getFullyQualifiedAttributes();
					}
				}
			}
			else {
				// proxy must have at least one valid assertion
				throw new IOException("Could not find valid FQANs. No VOMS assertions found.");
			}
		} catch (Exception ex) {                        
			throw new IOException("Could not get certificate issues.: " + ex);
		}
		return fQNA;
	}

	/**
	 * Releases any sensitive information that the VOMSCredential object may be
	 * containing. Applications should call this method as soon as the credential
	 * is no longer needed to minimize the time any sensitive information is
	 * maintained.
	 */
	public void dispose() {
		cert = null;
	}

}
