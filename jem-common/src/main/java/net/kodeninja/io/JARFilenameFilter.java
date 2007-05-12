package net.kodeninja.io;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Simple filter for getting JAR files.
 * @author Charles Ikeson
 *
 */
public class JARFilenameFilter implements FilenameFilter {

	/**
	 * Returns true if the filename is a jar file.
	 * @param dir The directory of the file.
	 * @param name The name of the file.
	 * @return True if the passed filename was a jar file.
	 */
	public boolean accept(File dir, String name) {
		return name.endsWith(".jar");
	}

}
