package net.kodeninja.http.service.handlers;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.util.logging.LoggerHook;

public class PacketLogger implements PacketHandler {
	private LoggerHook logger;

	public PacketLogger(LoggerHook Logger) {
		logger = Logger;
	}

	public boolean process(HTTPSocket Socket,
			HTTPPacket<HTTPHeader, HTTPBody> Packet) {

		logger.addLog("Request packet recieved by " + Socket.getName() + " V"
				+ Socket.getVersionMajor() + "." + Socket.getVersionMinor()
				+ "." + Socket.getVersionRevision());
		logger.addLog(Packet.getHeader().toString());

		return false;
	}

}
