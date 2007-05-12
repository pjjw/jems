package net.kodeninja.http.packet.extra;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;

public class HTTPGZipPacket<H extends HTTPHeader> extends
		HTTPPacket<H, HTTPGZipBody> {
	public HTTPGZipPacket(HTTPPacket<H, ? extends HTTPBody> packet) {
		super(packet.getHeader(), new HTTPGZipBody(packet.getBody()));
		if ((header != null) && (packet.getBody() != null)) {
			header.setParameter("Content-Length", ""
					+ getBody().getContentLength());
			header.setParameter("Content-Type", packet.getBody().getMimeType()
					.toString());
		}
	}
}
