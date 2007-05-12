package net.kodeninja.http.service.handlers;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.service.HTTPSocket;

public interface URIHandler {
	/**
	 * Attempts to handle the request.
	 *
	 * @param Socket
	 *            The socket the packet came from.
	 * @param Packet
	 *            The actual packet to process.
	 * @return Returns success.
	 */
	public HTTPPacket<HTTPHeader, HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<HTTPHeader, HTTPBody> Packet);
}
