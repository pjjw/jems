package net.kodeninja.http.packet.extra;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;

public class HTTPBodylessPacket extends HTTPPacket<HTTPBody> {
	public HTTPBodylessPacket(HTTPPacket<? extends HTTPBody> packet) {
		super(packet.getHeader(), (HTTPBody) null);
		if ((header != null) && (packet.getBody() != null)) {
			if (packet.getBody().getContentLength() > 0)
				header.setParameter("Content-Length", "" + packet.getBody().getContentLength());
			header.setParameter("Content-Type", packet.getBody().getContentType());
		}
	}

}
