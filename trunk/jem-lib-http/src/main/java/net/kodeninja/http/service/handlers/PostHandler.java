package net.kodeninja.http.service.handlers;

import java.io.IOException;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.service.HTTPSocket;

public class PostHandler extends PageRequestHandler {

	public boolean process(HTTPSocket Socket,
			HTTPPacket<HTTPBody> Packet) throws IOException {
		if (Packet.getHeader().getType() != HTTPHeader.HeaderType.REQUEST)
			return false;

		if (Packet.getHeader().getMethod().equalsIgnoreCase("POST") == false)
			return false;

		return super.process(Socket, Packet);
	}
	
}
