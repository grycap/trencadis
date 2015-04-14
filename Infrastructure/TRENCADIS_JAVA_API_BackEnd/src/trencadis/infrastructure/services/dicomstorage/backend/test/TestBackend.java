package trencadis.infrastructure.services.dicomstorage.backend.test;

import java.io.File;

import org.apache.commons.io.FileUtils;

import trencadis.infrastructure.services.dicomstorage.backend.BackEnd;

public class TestBackend {

	public static void main(String args[]) {
		/*
		String backend_config =   "<BACKEND_PARAMETERS>"
								+ "	 <BACKEND_TYPE>FILE_SYSTEM</BACKEND_TYPE>"
								+ "  <FILE_SYSTEM_PARAMETERS>"
								+ "     <HOME_DIR>/home/locamo</HOME_DIR>"
								+ "  </FILE_SYSTEM_PARAMETERS>"
								+ "</BACKEND_PARAMETERS>";
		*/
		String backend_config =   "<BACKEND_PARAMETERS>"
								+ "	 <BACKEND_TYPE>GRID_FTP</BACKEND_TYPE>"
								+ "  <GRIDFTP_PARAMETERS>"
								+ "     <HOST_NAME>trencadisv06.i3m.upv.es</HOST_NAME>"
								+ "     <HOST_PORT>2811</HOST_PORT>"
								+ "     <HOME_DIR>/home/gridftp</HOME_DIR>"
								+ "  </GRIDFTP_PARAMETERS>"
								+ "</BACKEND_PARAMETERS>";
		
		int err = 0;
		BackEnd backend = new BackEnd(backend_config);
		
		
		try {
			String credential = FileUtils.readFileToString(new File("/home/locamo/proxy.txt"));
			
			/**************** TEST GET_CONFIGURATION of BACKEND **********************
			System.out.println(backend.iGetBackEndParameters());
			/*************************************************************************/
			
			/********************* TEST ADD_FILE to BACKEND **************************
			err = backend.iAddFile("33173300-C400-11E4-8D35-9DE56F0C43B3",
					         new File("/opt/trencadis/files/reports/ECO_11_750.xml"),
					         credential);
			if (err == 0)
				System.out.println("TEST ADD_FILE to BACKEND - SUCCESSFULLY");
			else
				System.out.println("TEST ADD_FILE to BACKEND - ERROR");
			/*************************************************************************
			
			/********************* TEST DELETE_FILE in BACKEND ***********************
			err = backend.iDeleteFile("33173300-C400-11E4-8D35-9DE56F0C43B3", credential);
			if (err == 0)
				System.out.println("TEST DELETE_FILE to BACKEND - SUCCESSFULLY");
			else
				System.out.println("TEST DELETE_FILE to BACKEND - ERROR");
			/*************************************************************************/
			
			/******************* TEST DOWNLOAD_FILE from BACKEND *********************/
			String dsr = backend.xmlGetDICOMSRFile("78CB1D60-C7DB-11E4-8D35-A6C174863CCA", credential);
			FileUtils.writeStringToFile(new File("/home/locamo/dsr.xml"), dsr);	
			if (dsr != null)
				System.out.println("TEST DOWNLOAD_FILE from BACKEND - SUCCESSFULLY");
			else
				System.out.println("TEST DOWNLOAD_FILE from BACKEND - ERROR");
			/*************************************************************************/
			
			/***************** TEST DOWNLOAD_ALL_FILES from BACKEND ******************
			String ids = "1A733740-C400-11E4-8D35-8B689B79275A,33173300-C400-11E4-8D35-9DE56F0C43B1";
			String dsr = backend.xmlGetAllDICOMSRFiles(ids, credential);
			FileUtils.writeStringToFile(new File("/home/locamo/dsr2.xml"), dsr);		
			if (dsr != null)
				System.out.println("TEST DOWNLOAD_ALL_FILES from BACKEND - SUCCESSFULLY");
			else
				System.out.println("TEST DOWNLOAD_ALL_FILES from BACKEND - ERROR");
			/*************************************************************************/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
