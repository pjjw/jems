package net.kodeninja.http.packet.extra;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;

public class HTTPGZipPacket extends HTTPPacket<HTTPGZipBody> {
	public HTTPGZipPacket(HTTPPacket<? extends HTTPBody> packet) {
		super(packet.getHeader(), new HTTPGZipBody(packet.getBody()));
		if ((header != null) && (packet.getBody() != null)) {
			header.setParameter("Content-Length", "" + getBody().getContentLength());
			header.setParameter("Content-Type", packet.getBody().getContentType());
			header.setParameter("Content-Encoding", "gzip");
		}
	}
}
