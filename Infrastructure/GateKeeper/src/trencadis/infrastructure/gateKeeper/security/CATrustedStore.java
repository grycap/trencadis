package trencadis.infrastructure.gateKeeper.security;

import java.io.IOException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileReader;

import java.security.cert.X509Certificate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bouncycastle.openssl.PEMReader;

public class CATrustedStore {

	static Log logger = LogFactory.getLog(CATrustedStore.class);

	private X509Certificate[] store;

	public CATrustedStore(String caTrustCertPath) {
		try {
			loadCACerts(caTrustCertPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadCACerts(String caTrustCertPath) throws IOException {

		String[] CAtrustedFilenamesList = null;
		CAtrustedFilenamesList = CATrustedStore.listDir(caTrustCertPath, ".0");
		if ((CAtrustedFilenamesList == null) && (CAtrustedFilenamesList.length < 1))
			throw new IOException("No trusted ACs found.");

		try {
			this.store = new X509Certificate[CAtrustedFilenamesList.length];
			for (int i = 0; i < CAtrustedFilenamesList.length; i++) {
				this.store[i] = (X509Certificate) new PEMReader(new FileReader(caTrustCertPath + System.getProperty("file.separator") + CAtrustedFilenamesList[i]), null, "SUN").readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public X509Certificate[] getStore() {
		return this.store;
	}

	public static String[] listDir(String dirName, String suFFix) {
	    File dir = new File(dirName);
	    File[] listedFiles = null;

	    // final for use in class below
	    final String suffix = suFFix;

	    FilenameFilter filter = new FilenameFilter() {
	    	public boolean accept(File dir, String name) {
	        if (name.endsWith(suffix)) return true;
	        	else return (new File(dir, name)).isDirectory();
	        }
	    };

	    if (!dir.isDirectory())
	        throw new IllegalArgumentException(dirName + " is not a directory.");
	    try {
	        listedFiles = dir.listFiles(filter);
	    }
	    catch (SecurityException ex) {
	        ex.printStackTrace();
	    }

	    if (listedFiles == null) return null;

	    String[] fileNames = new String[listedFiles.length];

	    for (int i = 0; i < listedFiles.length; i++) {
	        fileNames[i] = listedFiles[i].getName();
	    }

	    return fileNames;
	}
}
