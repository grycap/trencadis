package trencadis.common.util;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Vector;

import javax.crypto.Cipher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CryptUtils {
	
	static Log logger = LogFactory.getLog(CryptUtils.class);

	/**
	 * Cipher para la firma de mensajes.
	 */
	public static final String CIPHER_PROVIDER = "BC";
	public static final String CIPHER_OPERATION_MODE ="RSA/None/NoPadding";	
	
	/**
	 * Load Private Key from file.
	 * @param keyFile
	 * @return
	 */
	public static PrivateKey loadPrivKey(String keyFile) {
		PrivateKey privKey = null;
		try {
			OpenSSLKey key = new BouncyCastleOpenSSLKey(keyFile);

			if (key.isEncrypted())
				throw new Exception("Error: Failed to load encrypted key");

			privKey = key.getPrivateKey();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return privKey;
	}	
	
	/**
	 * Extrae la clave publica de un certificado de host.
	 * @param certChainX509
	 * @return
	 */
	public static RSAPublicKey getPublicKey(X509Certificate certChainX509) {
		return (RSAPublicKey)certChainX509.getPublicKey();
	}
	
	/**
	 * Formats the input message to be signed in the service
	 * authorization process.
	 * @param guid
	 * @param ontology
	 * @return
	 * @throws Exception
	 */
	public static String getMsgStr(String guid,
			Vector<Integer> ontology) throws Exception {
		
		if ((guid == null) || (ontology == null))
			throw new Exception("Malformed message headers"); 
		
		String msgStr = guid;
		Collections.sort(ontology);
		
		for (int i = 0; i < ontology.size(); i++) {
			msgStr = msgStr + "-" + ontology.get(i).toString();
		}
		
		return msgStr;
	}
	
	/**
	 * Sign message with a RSA Private Key.
	 * @param privKey
	 * @param msgStr
	 * @return
	 */
	public static String getSignedMsg(PrivateKey privKey, String msgStr) {
		String base64MsgStr = "";
		try {
			Cipher cipher = Cipher.getInstance(CryptUtils.CIPHER_OPERATION_MODE,
					CryptUtils.CIPHER_PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, privKey);
			byte[] cipherText = cipher.doFinal(msgStr.getBytes());

			BASE64Encoder base64Encoder = new BASE64Encoder();
			base64MsgStr = base64Encoder.encode(cipherText);
		} catch (Exception e) {                        
			logger.error("Could no generate signed message: " + e);
                        return null;
		}
		return base64MsgStr;
	}

	/**
	 * Decrypt message.
	 * @param pubKey
	 * @param base64CryptMsgStr
	 * @return
	 */
	public static String getDecryptedMsg(PublicKey pubKey, String base64CryptMsgStr) {
		String msgStr = "";
		try {
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] cipherText = base64Decoder.decodeBuffer(base64CryptMsgStr);

			Cipher cipher = Cipher.getInstance(CryptUtils.CIPHER_OPERATION_MODE,
					CryptUtils.CIPHER_PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, pubKey);

			byte[] plainText = cipher.doFinal(cipherText);
			msgStr = new String(plainText);
		} catch (Exception e) {
			logger.error("Could no decrypt message: " + e);
		}
		return msgStr;
	}
}
