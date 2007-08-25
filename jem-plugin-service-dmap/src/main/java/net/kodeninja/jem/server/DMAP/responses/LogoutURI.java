package net.kodeninja.jem.server.DMAP.responses;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.jem.server.DMAP.DMAPService;

public class LogoutURI extends DMAPURI {

	public LogoutURI(DMAPService s) {
		super(s, "/logout");
	}

	@Override
	public  HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<? extends HTTPBody> Packet) {
		HTTPPacket<? extends HTTPBody> response = super.process(Socket, Packet);

		if (response != null)
			response.getHeader().setParameter("Connection", "close");

		return response;
	}

}
