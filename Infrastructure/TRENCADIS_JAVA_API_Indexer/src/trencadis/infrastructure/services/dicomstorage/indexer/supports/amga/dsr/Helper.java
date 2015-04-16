package trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.parsers.SAX_ErrorHandler;
import trencadis.infrastructure.services.dicomstorage.indexer.supports.amga.dsr.parsers.SAX_GetReportParams;

public class Helper {
	
	protected String _IDOntology       	= null;
    protected String _Description      	= null;
	protected String _IDReport         	= null;
    protected String _IDTRENCADISReport = null;
    protected String _DateTimeStart		= null;
    protected String _DateTimeEnd		= null;

	protected String _tmp_dir = null;

	protected Logger _logger = Logger.getLogger(this.getClass().getName());

	public Helper(String tmp_dir) {
		_tmp_dir = tmp_dir;
	}

	/**
	 * Gets the identifiers of a DICOM-SR xml file, if the xml file is a
	 * structure file reportID and patientsName will contain an empty string.
	 * 
	 * @param xmlDsr File with the xml DICOM-SR report
	 * @throws java.io.IOException
	 * @throws SAXException 
	 */
	public void getReportParams(File xmlDsr) throws IOException, SAXException {

		XMLReader xr = XMLReaderFactory.createXMLReader();
		SAX_GetReportParams contentHandler = new SAX_GetReportParams(this);
		SAX_ErrorHandler errorHandler = new SAX_ErrorHandler();
		xr.setContentHandler(contentHandler);
		xr.setErrorHandler(errorHandler);

		FileReader FILE = new FileReader(xmlDsr);
		xr.parse(new InputSource(FILE));
		FILE.close();

	}

	/**
	 * Gets the identifiers of a DICOM-SR xml file, if the xml file is a
	 * structure file reportID will contain an empty string.
	 * 
	 * @param xmlDsr String with the xml DICOM-SR report
	 * @throws java.io.IOException
	 * @throws SAXException 
	 */
	public void getReportParams(String xmlDsr) throws IOException, SAXException {

		XMLReader xr = XMLReaderFactory.createXMLReader();
		SAX_GetReportParams contentHandler = new SAX_GetReportParams(this);
		SAX_ErrorHandler errorHandler = new SAX_ErrorHandler();
		xr.setContentHandler(contentHandler);
		xr.setErrorHandler(errorHandler);

		StringReader STRING = new StringReader(xmlDsr);
		xr.parse(new InputSource(new BufferedReader(STRING)));
		STRING.close();

	}
	
	/**
	 * This method must only be called from parsers in order to notify the manager
	 * class about document attributes like the type of report or the id of the report.
	 * 
	 * @param reportType Value of the type of report
	 * @param reportID Value of the report identifier. Null if the parsed file is a structure file.
	 * @param studyUID Identifier of the study to which the report is related.
	 * @param patientsName  Name of the patient in DICOM format
	 */
	public void setReportParams(String IDOntology, String IDReport, String Description,
			String strIDTRENCADISReport, String strDateTimeStart, String strDateTimeEnd) {
		_IDOntology  = IDOntology;
		_IDReport = IDReport;	
        _Description = Description;
        _IDTRENCADISReport = strIDTRENCADISReport;
        _DateTimeStart = strDateTimeStart;
        _DateTimeEnd = strDateTimeEnd;
	}
	
	public String getIDTRENCADISReport() {
		return _IDTRENCADISReport;
	}
	
	public String getIDOntology() {
		return _IDOntology;
	}

	public String getIDReport() {
		return _IDReport;
	}
        
    public String getDescription() {
		return _Description;
	}

	public String get_DateTimeStart() {
		return _DateTimeStart;
	}

	public String get_DateTimeEnd() {
		return _DateTimeEnd;
	}


}
