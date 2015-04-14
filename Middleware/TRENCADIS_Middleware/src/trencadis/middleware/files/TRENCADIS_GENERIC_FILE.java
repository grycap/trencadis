package trencadis.middleware.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import trencadis.middleware.dsr.DSRHelper;

import trencadis.middleware.login.TRENCADIS_SESSION;


/**
 * This class provides the basis for loading and returning the contents of a file.
 * The rest of classes of this package inherit from it.
 */
public class TRENCADIS_GENERIC_FILE {

	protected String _contents = new String();
	protected DSRHelper dsr_helper = null;

	/**
	 * This constructor loads the contents of the file from the filesystem.
	 * 
	 * @param middleware_config Middleware configuration (contains tmp dir)
	 * @param file File from which the contents will be read
	 */
	protected TRENCADIS_GENERIC_FILE(TRENCADIS_SESSION session, File file) throws IOException {
	                  			            
            FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		String s = new String();
		while((s = br.readLine()) != null) {
			_contents += s;
		}
		fr.close();                                
                
		dsr_helper = new DSRHelper(session.getTmpDir());
	}

	/**
	 * This constructor loads the contents of the file from a string object.
	 * 
	 * @param middleware_config Middleware configuration (contains tmp dir)
	 * @param contents A string containing all the contents.
	 */
	protected TRENCADIS_GENERIC_FILE(TRENCADIS_SESSION session, String contents) {
		_contents = contents;
		dsr_helper = new DSRHelper(session.getTmpDir());
	}

	/**
	 * Getter method for file contents.
	 * @return File contents into a string.
	 */
	public String getContents() {
		return _contents;
	}

}
