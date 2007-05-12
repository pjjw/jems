package net.kodeninja.http.packet.extra;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;

public class HTTPBodylessPacket<H extends HTTPHeader> extends
		HTTPPacket<H, HTTPBody> {
	public HTTPBodylessPacket(HTTPPacket<H, ? extends HTTPBody> packet) {
		super(packet.getHeader(), (HTTPBody) null);
		if ((header != null) && (packet.getBody() != null)) {
			if (packet.getBody().getContentLength() > 0)
				header.setParameter("Content-Length", ""
						+ packet.getBody().getContentLength());
			header.setParameter("Content-Type", packet.getBody().getMimeType()
					.toString());
		}
	}

}
