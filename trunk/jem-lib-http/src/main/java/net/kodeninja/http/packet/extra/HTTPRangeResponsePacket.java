package net.kodeninja.http.packet.extra;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;

public class HTTPRangeResponsePacket extends HTTPPacket<HTTPRangeBody> {
	public HTTPRangeResponsePacket(HTTPPacket<? extends HTTPBody> packet, String Range) {
		super(packet.getHeader(), new HTTPRangeBody(packet.getBody(), Range));
		if ((header != null) && (packet.getBody() != null)) {
			header.setParameter("Content-Length", "" + getBody().getContentLength());
			header.setParameter("Content-Type", packet.getBody().getContentType());
			header.setParameter("Content-Range", getBody().getContentRangeString());
		}
	}

}
