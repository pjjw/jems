package net.kodeninja.jem.server.console.telnet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;

import net.kodeninja.jem.server.InterfaceHook;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.scheduling.JobImpl;
import net.kodeninja.util.KNXMLModule;
import net.kodeninja.util.KNXMLModuleInitException;
import net.kodeninja.util.logging.LoggerHook;

public class TelnetConsole extends JobImpl implements InterfaceHook,
		LoggerHook, KNXMLModule {
	public TelnetConsole() {
		super(true, JemServer.getInstance().getScheduler());
	}

	protected Set<TelnetConsoleChild> childern = new HashSet<TelnetConsoleChild>();
	final private static int DEFAULT_PORT = 23;
	static final int SOCKET_TIMEOUT = 100;
	private int port = DEFAULT_PORT;
	private ServerSocket socket = null;
	private String localName = "";

	public void xmlInit(Node xmlNode) throws KNXMLModuleInitException {
		localName = xmlNode.getAttributes().getNamedItem("name").getNodeValue();
		for (Node modNode = xmlNode.getFirstChild(); modNode != null; modNode = modNode
				.getNextSibling()) {
			if (modNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (modNode.getNodeName().equals("port")) {
				String portString = modNode.getTextContent().trim();
				if (portString.length() > 0)
					try {
						port = Integer.parseInt(portString);
					} catch (NumberFormatException e) {
						throw new KNXMLModuleInitException("Invalid port.");
					}
				else
					throw new KNXMLModuleInitException("Invalid port.");
			}
		}
	}

	@Override
	public synchronized void start() {
		if (socket != null)
			return;
		try {
			socket = new ServerSocket(port);
			socket.setSoTimeout(SOCKET_TIMEOUT);
		} catch (Exception e) {
			JemServer.getInstance()
					.addLog(
							getName() + " caught exception while starting: "
									+ e.toString());
			JemServer.getInstance().Commands.exception();
		}
		JemServer.getInstance().addLog(
										getName() + " started. (Port: "
												+ socket.getLocalPort() + ")");
		super.start();
	}

	@Override
	public synchronized void stop() {
		if (socket == null)
			return;

		try {
			socket.close();
		} catch (IOException e) {
		}

		for (TelnetConsoleChild tmpChild : childern)
			tmpChild.stop();
		JemServer.getInstance().addLog(getName() + " stopped.");
		socket = null;

		super.stop();
	}

	@Override
	public synchronized void run() {
		if (socket == null)
			return;
		try {
			Socket clientConnection;
			if ((clientConnection = socket.accept()) != null) {
				clientConnection.setSoTimeout(SOCKET_TIMEOUT);
				TelnetConsoleChild client = new TelnetConsoleChild(this,
						clientConnection);
				client.start();
				childern.add(client);
			}
		} catch (SocketTimeoutException e) {
		} catch (Exception e) {
			JemServer.getInstance().addLog(
											getName() + " caught exception: "
													+ e.toString());
			JemServer.getInstance().Commands.exception();
		}

		for (TelnetConsoleChild tmpChild : childern)
			if (tmpChild.isStarted() == false) {
				childern.remove(tmpChild);
				break;
			}

		super.run();
	}

	public String getName() {
		return "Telnet Console Interface [" + localName + "]";
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
		for (TelnetConsoleChild child : childern)
			child.addLogQueue(LogText);
	}

	public boolean canRun() {
		return true;
	}

	public boolean isUrgent() {
		return false;
	}
}
