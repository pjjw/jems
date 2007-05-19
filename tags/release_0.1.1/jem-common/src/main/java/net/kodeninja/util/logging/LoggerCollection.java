package net.kodeninja.util.logging;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps a list of loggers and broadcasts log messages to each.
 * @author Charles Ikeson
 *
 */
public abstract class LoggerCollection implements LoggerHook {
	protected Set<LoggerHook> loggers = Collections
			.synchronizedSet(new HashSet<LoggerHook>());

	public void addLog(String LogText) {
		for (LoggerHook logger: loggers)
			logger.addLog(LogText);
	}

	/**
	 * Adds a logger to the list.
	 * @param logger The logger to add.
	 */
	public void addLogger(LoggerHook logger) {
		loggers.add(logger);
	}

	/**
	 * Removes a logger from the list.
	 * @param logger The logger to remove.
	 */
	public void removeLogger(LoggerHook logger) {
		loggers.remove(logger);
	}
}
