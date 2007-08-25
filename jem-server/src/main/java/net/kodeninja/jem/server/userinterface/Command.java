package net.kodeninja.jem.server.userinterface;

import net.kodeninja.util.logging.LoggerHook;

public interface Command extends UIBase {

	public void activate(LoggerHook output, String[] args);
	
}
