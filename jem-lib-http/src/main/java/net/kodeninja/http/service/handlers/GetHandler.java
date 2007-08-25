package net.kodeninja.http.service.handlers;

import java.io.IOException;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.service.HTTPSocket;

public class GetHandler extends PageRequestHandler {

	public boolean process(HTTPSocket Socket,
			HTTPPacket<HTTPBody> Packet) throws IOException {
		if (Packet.getHeader().getType() != HTTPHeader.HeaderType.REQUEST)
			return false;

		if ((Packet.getHeader().getMethod().equalsIgnoreCase("GET") == false) &&
				(Packet.getHeader().getMethod().equalsIgnoreCase("HEAD") == false))
			return false;

		return super.process(Socket, Packet);
	}

}
