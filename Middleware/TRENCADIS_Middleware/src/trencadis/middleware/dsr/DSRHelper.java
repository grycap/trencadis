package trencadis.middleware.dsr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.Helper;
import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.parsers.SAX_ErrorHandler;
import trencadis.common.util.TRENCADIS_UTILS;
import trencadis.middleware.dsr.parsers.*;

public class DSRHelper extends Helper {

	public DSRHelper(String tmp_dir) {
		super(tmp_dir);
	}

	/**
	 * Lists a DSR structure, and shows the attribute keys of its nodes.
	 * This is useful for manual querys to the AMGA server.
	 * 
	 * @param xmlDsrStr String with the report structure description
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public void listStructure(String xmlDsrStr) throws SAXException, IOException {

		XMLReader xr = XMLReaderFactory.createXMLReader();
		SAX_AMGAStructureLister contentHandler = new SAX_AMGAStructureLister();
		SAX_ErrorHandler errorHandler = new SAX_ErrorHandler();
		xr.setContentHandler(contentHandler);
		xr.setErrorHandler(errorHandler);

		StringReader STRING = new StringReader(xmlDsrStr);
		xr.parse(new InputSource(STRING));
		STRING.close();

	}

	/**
	 * Lists a DSR structure, and shows the attribute keys of its nodes.
	 * This is useful for manual querys to the AMGA server.
	 * 
	 * @param xmlDsrStr File with the report structure description
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public void listStructure(File xmlDsrStr) throws SAXException, IOException {

			XMLReader xr = XMLReaderFactory.createXMLReader();
			SAX_AMGAStructureLister contentHandler = new SAX_AMGAStructureLister();
			SAX_ErrorHandler errorHandler = new SAX_ErrorHandler();
			xr.setContentHandler(contentHandler);
			xr.setErrorHandler(errorHandler);

			FileReader FILE = new FileReader(xmlDsrStr);
			xr.parse(new InputSource(FILE));
			FILE.close();

	}
	
			
	/**
	 * Generates a standard compliant DICOM-SR file from an xml
	 * representation.
	 * This is useful as we use xml to define the reports, instead
	 * of working with DICOM-SR standard files.
	 * Each report introduced in the AMGA catalogue will reference a
	 * DICOM-SR binary file which will be created with this method.
	 * 
	 * @param xmlDsr Xml file representing the DICOM-SR report
	 * @return A file descriptor of the generated DICOM-SR file
	 * @throws Exception 
	 */
	public File generateDICOMSR(File xmlDsr) throws Exception {

		try {getReportParams(xmlDsr);} catch (Exception e) {}
                    
		if (_IDOntology==null)
			throw new Exception("Can not read report parameters");

		String randomID = TRENCADIS_UTILS.getRandomUUID();

		ProcessBuilder pb = new ProcessBuilder("xmlDsr2dsr", xmlDsr.getAbsolutePath(), _tmp_dir + "/" + randomID + ".dcm");
		Process p = pb.start();
		p.waitFor();

		if (p.exitValue() != 0) {
			throw new IOException(TRENCADIS_UTILS.getProcessError(p));
		}

		File dsr = new File(_tmp_dir + "/" + _IDOntology + ".dcm." + randomID);

		if (dsr.exists() == false) {
			throw new IOException("DICOM-SR file cannot be accessed:");
		}

		return dsr;
	}

	/**
	 * Generates a standard compliant DICOM-SR file from an xml
	 * representation.
	 * This is useful as we use xml to define the reports, instead
	 * of working with DICOM-SR standard files.
	 * Each report introduced in the AMGA catalogue will reference a
	 * DICOM-SR binary file which will be created with this method.
	 * 
	 * @param xmlDsr Xml string representing the DICOM-SR report
	 * @return A file descriptor of the generated DICOM-SR file
	 * @throws Exception 
	 */
	public File generateDICOMSR(String xmlDsr) throws Exception {

		try {getReportParams(xmlDsr);} catch (Exception e) {}
		if (_IDOntology==null)
			throw new Exception("Can not read report parameters");

		String randomID = TRENCADIS_UTILS.getRandomUUID();

		File xmlDsrFile = new File(_tmp_dir + "/" + _IDOntology + ".xmlDsr." + randomID);
		if (xmlDsrFile.exists()) {
			xmlDsrFile.delete();
		}

		xmlDsrFile.createNewFile();

		BufferedWriter out = new BufferedWriter(new FileWriter(xmlDsrFile));
		out.write(xmlDsr);
		out.close();

		ProcessBuilder pb = new ProcessBuilder("xmlDsr2dsr", xmlDsrFile.getAbsolutePath(), _tmp_dir + "/" + randomID + ".dcm");
		Process p = pb.start();
		p.waitFor();

		if (p.exitValue() != 0) {
			throw new IOException(TRENCADIS_UTILS.getProcessError(p));
		}

		File dsr = new File(_tmp_dir + "/" + randomID + ".dcm");

		if (dsr.exists() == false) {
			throw new IOException("DICOM-SR file cannot be accessed:");
		}

		return dsr;
	}			

}
