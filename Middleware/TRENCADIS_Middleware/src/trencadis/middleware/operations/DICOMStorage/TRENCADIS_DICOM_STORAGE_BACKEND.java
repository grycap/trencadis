package trencadis.middleware.operations.DICOMStorage;

import trencadis.infrastructure.services.DICOMStorage.impl.wrapper.xmlOutputDownloadAllReportsID.BACKEND_PARAMETERS;

/**
 * This class contains the basic information of a backend of a DICOM Storage
 * for download the reports.
 * 
 * @author Lorena Calabuig <locamo@inf.upv.es>
 */
public class TRENCADIS_DICOM_STORAGE_BACKEND {
	
	private BACKEND_PARAMETERS backend_param = null;
	private String backend_type = null;
	private String host_name = null;
	private String host_port = null;
	private String home_dir = null;

	public TRENCADIS_DICOM_STORAGE_BACKEND(BACKEND_PARAMETERS backend_param) {
		this.backend_param = backend_param;
		this.backend_type = backend_param.get_BACKEND_TYPE();
		this.home_dir = backend_param.get_HOME_DIR();
		if (this.backend_type.equals("GRID_FTP")) {
			this.host_name = backend_param.get_HOST_NAME();
			this.host_port = backend_param.get_HOST_PORT();
		}
		
	}

	public String getBackendType() {
		return backend_type;
	}

	public void setBackendType(String backend_type) {
		this.backend_type = backend_type;
	}

	public String getHostName() {
		return host_name;
	}

	public void setHostName(String host_name) {
		this.host_name = host_name;
	}

	public String getHostPort() {
		return host_port;
	}

	public void setHostPort(String host_port) {
		this.host_port = host_port;
	}

	public String getHomeDir() {
		return home_dir;
	}

	public void setHomeDir(String home_dir) {
		this.home_dir = home_dir;
	}
	
	@Override
	public String toString() {
		return backend_param.str_to_XML();
	}
}
