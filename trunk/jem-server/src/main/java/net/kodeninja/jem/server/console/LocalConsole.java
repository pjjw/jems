package net.kodeninja.jem.server.console;

import net.kodeninja.jem.server.JemServer;

public class LocalConsole extends ConsoleInterface {
	public LocalConsole() {
		super(System.in, System.out);
	}

	@Override
	public String getUser() {
		return "local";
	}

	public String getName() {
		return "Local Console Interface";
	}

	@Override
	public void stop() {
		super.stop();
		JemServer.getInstance().Commands.shutdown();
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
