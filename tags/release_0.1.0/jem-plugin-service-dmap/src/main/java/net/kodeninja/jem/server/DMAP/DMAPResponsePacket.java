package net.kodeninja.jem.server.DMAP;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;

public class DMAPResponsePacket<B extends HTTPBody> extends
		HTTPPacket<HTTPHeader, B> {

	public DMAPResponsePacket(HTTPHeader Header, B Body, DMAPService service) {
		super(Header, Body);
		header.setParameter("DAAP-Server", service.getServerString());
		header.setParameter("DPAP-Server", service.getServerString());
	}

}
