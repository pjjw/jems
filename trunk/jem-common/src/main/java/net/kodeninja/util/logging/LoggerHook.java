package net.kodeninja.util.logging;

import net.kodeninja.util.KNModule;

/**
 * Class that defines a simple logger that messages can be sent to.
 * @author Charles Ikeson
 *
 */
public interface LoggerHook extends KNModule {
	/**
	 * Requests the passed logger line be added to the logger
	 *
	 * @param LogText
	 *            The line of text to add.
	 */
	public void addLog(String LogText);
}
