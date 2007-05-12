package net.kodeninja.http.packet.extra;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;

public class HTTPRangeResponsePacket<H extends HTTPHeader> extends
		HTTPPacket<H, HTTPRangeBody> {
	public HTTPRangeResponsePacket(HTTPPacket<H, ? extends HTTPBody> packet,
			String Range) {
		super(packet.getHeader(), new HTTPRangeBody(packet.getBody(), Range));
		if ((header != null) && (packet.getBody() != null)) {
			header.setParameter("Content-Length", ""
					+ getBody().getContentLength());
			header.setParameter("Content-Type", packet.getBody().getMimeType()
					.toString());
			header.setParameter("Content-Range", getBody()
					.getContentRangeString());
		}
	}

}
