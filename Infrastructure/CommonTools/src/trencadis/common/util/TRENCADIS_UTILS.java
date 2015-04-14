package trencadis.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.axis.components.uuid.SimpleUUIDGen;

public class TRENCADIS_UTILS {

	/**
	 * Returns the error output of an executed command.
	 * Internal use.
	 * 
	 * @param p The executed process from which to get the errors
	 * @return The string with the error output
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException 
	 */
	public static String getProcessError(Process p) throws IOException {

		String error = new String();

		BufferedInputStream bin = new BufferedInputStream(p.getErrorStream());
		InputStreamReader chin = new InputStreamReader(bin);

		while (true) {
			int ch = chin.read();
			if (ch == -1) {
				break;
			}
			error = error + (char) ch;
		}

		return error;
	}

	public static File saveProxy(String tmp_dir, String certificate) throws IOException, InterruptedException {

		File fProxy = new File(tmp_dir + "/x509up_uDSRAMGA_" + getRandomUUID());

		if (fProxy.exists()) {
			fProxy.delete();
		}

		fProxy.createNewFile();

		FileWriter fWriter = new FileWriter(fProxy);
		BufferedWriter bWriter = new BufferedWriter(fWriter);
		bWriter.write(certificate);
		bWriter.close();
		fWriter.close();

		ArrayList<String> command = new ArrayList<String>();
		command.add("chmod");
		command.add("600");
		command.add(fProxy.getAbsolutePath());
		ProcessBuilder pb = new ProcessBuilder(command);

		Process p = pb.start();
		p.waitFor();

		if (p.exitValue() != 0) {
			throw new IOException("Failure changing proxy file permissions: " + getProcessError(p));
		}
		
		return fProxy;

	}
	
	public static String getRandomUUID() {
		SimpleUUIDGen entropy = new SimpleUUIDGen();
		return entropy.nextUUID();
	}

}
