package net.kodeninja.http.service.handlers;

import java.io.IOException;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.service.HTTPSocket;

public interface PacketHandler {
	/**
	 * Attempts to handle the request.
	 *
	 * @param Socket
	 *            The socket the packet came from.
	 * @param Packet
	 *            The actual packet to process.
	 * @return Returns success.
	 */
	public boolean process(HTTPSocket Socket,
			HTTPPacket<HTTPBody> Packet)
			throws IOException;
}
