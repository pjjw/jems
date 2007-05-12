package net.kodeninja.util.logging;

/**
 * Simple logger that outputs the log messages to stdout.
 * @author Charles Ikeson
 *
 */
public class ConsoleLogger implements LoggerHook {

	public void addLog(String LogText) {
		System.out.println(LogText);
	}

	public String getName() {
		return "Console Logger";
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

}
