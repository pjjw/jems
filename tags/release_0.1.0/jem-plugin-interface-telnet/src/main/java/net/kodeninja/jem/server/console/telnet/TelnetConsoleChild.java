package net.kodeninja.jem.server.console.telnet;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.console.ConsoleInterface;

public class TelnetConsoleChild extends ConsoleInterface {
	protected static final int MSG_BURST = 5;
	protected TelnetConsole telnetParent;
	protected Socket telnetSocket;
	private LinkedList<String> msgQueue = new LinkedList<String>();
	protected String user = "";

	TelnetConsoleChild(TelnetConsole Parent, Socket clientSocket)
			throws IOException {
		super(new TelnetFilterInputStream(clientSocket.getInputStream(),
				clientSocket.getOutputStream()), new PrintStream(clientSocket
				.getOutputStream()));
		telnetParent = Parent;
		telnetSocket = clientSocket;
		user = "Telnet [" + telnetSocket.getInetAddress().toString() + "]";

		addLog(JemServer.getInstance().getName() + " "
				+ JemServer.getInstance().getVersionMajor() + "."
				+ JemServer.getInstance().getVersionMinor() + "."
				+ JemServer.getInstance().getVersionRevision() + " - "
				+ telnetParent.getName());
	}

	@Override
	public void run() {
		if (telnetSocket == null)
			return;

		int msgs = 0;
		while ((logQueueSize() > 0) && (msgs < MSG_BURST)) {
			addLog(popLogQueue());
			msgs++;
		}

		super.run();
	}

	@Override
	public synchronized void stop() {
		if (telnetSocket == null)
			return;

		((TelnetFilterInputStream) inStream).stop();
		try {
			telnetSocket.close();
			telnetSocket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		super.stop();
	}

	@Override
	protected boolean inputWaiting() {
		try {
			return (inStream.available() != 0);
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public String getUser() {
		return user;
	}

	public String getName() {
		return telnetParent.getName() + " (Child)";
	}

	public int getVersionMajor() {
		return telnetParent.getVersionMajor();
	}

	public int getVersionMinor() {
		return telnetParent.getVersionMinor();
	}

	public int getVersionRevision() {
		return telnetParent.getVersionRevision();
	}

	public synchronized void addLogQueue(String m) {
		msgQueue.addLast(m);
	}

	public synchronized String popLogQueue() {
		return msgQueue.removeFirst();
	}

	public synchronized int logQueueSize() {
		return msgQueue.size();
	}

}
