package net.kodeninja.util.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Logger class that outputs the log messages to the file it was created with.
 * @author Charles Ikeson
 *
 */
public class FileLogger implements LoggerHook {
	private static File log = null;

	/**
	 * Creates the logger using the passed file path.
	 * This keeps the old file.
	 * @param LogFile The file to log to.
	 */
	public FileLogger(File LogFile) {
		this(LogFile, true);
	}

	/**
	 * Creates the logger using the passed file path, and if set,
	 * deletes the old file first.
	 * @param LogFile The file to send logs to.
	 * @param AppendLog If set to false, any existing log file is deleted.
	 */
	public FileLogger(File LogFile, boolean AppendLog) {
		log = LogFile;
		if (AppendLog == false)
			if (log.exists())
				log.delete();
	}

	public String getName() {
		return "Log File Logger";
	}

	public int getVersionMajor() {
		return 0;
	}

	public int getVersionMinor() {
		return 1;
	}

	public int getVersionRevision() {
		return 0;
	}

	public void addLog(String LogText) {

		LogText = "("
				+ (new java.util.Date(System.currentTimeMillis())).toString()
				+ ") " + LogText;
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(log, true));
			w.write(LogText);
			w.newLine();
			w.close();
		} catch (IOException e) {
			System.out.println("Error while writing to log file!");
			e.printStackTrace();
		}
	}

}
