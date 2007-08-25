package net.kodeninja.http.service.handlers;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.util.logging.LoggerHook;

public class PacketHeaderLogger implements PacketHandler {
	private LoggerHook logger;
	private boolean verbose;

	public PacketHeaderLogger(LoggerHook Logger) {
		this(Logger, false);
	}
	
	public PacketHeaderLogger(LoggerHook Logger, boolean verbose) {
		logger = Logger;
		this.verbose = verbose;
	}

	public boolean process(HTTPSocket Socket,
			HTTPPacket<HTTPBody> Packet) {

		logger.addLog("Request packet recieved by " + Socket.getName() + " V" +
				Socket.getVersionMajor() + "." + Socket.getVersionMinor() +
				"." + Socket.getVersionRevision() +
				" [" + Socket.getRemoteHost() + ":" + Socket.getRemotePort() + "]");
		if (verbose)
			logger.addLog(Packet.getHeader().toString());
		else
			logger.addLog(Packet.getHeader().getFirstLine() + "\n");

		return false;
	}

}
