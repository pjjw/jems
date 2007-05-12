package net.kodeninja.http.service.handlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.extra.HTTPBodylessPacket;
import net.kodeninja.http.packet.extra.HTTPErrorResponse;
import net.kodeninja.http.packet.extra.HTTPGZipPacket;
import net.kodeninja.http.packet.extra.HTTPRangeResponsePacket;
import net.kodeninja.http.service.HTTPSocket;

public class GetHandler implements PacketHandler {
	Set<URIHandler> handlers = Collections
			.synchronizedSet(new HashSet<URIHandler>());

	public boolean process(HTTPSocket Socket,
			HTTPPacket<HTTPHeader, HTTPBody> Packet) {
		if (Packet.getHeader().getType() != HTTPHeader.HeaderType.REQUEST)
			return false;

		boolean sendBody;
		if (Packet.getHeader().getMethod().toUpperCase().equals("GET"))
			sendBody = true;
		else if (Packet.getHeader().getMethod().toUpperCase().equals("HEAD"))
			sendBody = false;
		else
			return false;

		HTTPPacket<HTTPHeader, ? extends HTTPBody> response = null;
		Iterator<URIHandler> it = handlers.iterator();
		while (it.hasNext())
			if ((response = it.next().process(Socket, Packet)) != null)
				break;

		if (response == null)
			response = new HTTPErrorResponse(
					HTTPResponseCode.HTTP_404_NOT_FOUND, Packet.getHeader()
							.getVersion());
		else {
			String rangeRequest = Packet.getHeader().getParameter("range");
			if (rangeRequest != null) {
				response = new HTTPRangeResponsePacket<HTTPHeader>(response,
						rangeRequest);
				response
						.getHeader()
						.setResponseCode(
											response.getHeader().getVersion(),
											HTTPResponseCode.HTTP_206_PARTIAL_CONTENT);
			}
		}

		if ((response.getBody() != null)
				&& (response.getBody().getContentLength() > 0)
				&& (response.getBody().getContentLength() < 1048576)) {
			String encoding = Packet.getHeader()
					.getParameter("Accept-Encoding");
			if ((encoding != null) && (encoding.toLowerCase().contains("gzip"))) {
				response = new HTTPGZipPacket<HTTPHeader>(response);
				response.getHeader().setParameter("Content-Encoding", "gzip");
			}
		}

		if (sendBody == false)
			response = new HTTPBodylessPacket<HTTPHeader>(response);

		Socket.sendPacket(response);

		String conn = response.getHeader().getParameter("connection");
		if ((conn != null) && (conn.equals("close")))
			Socket.close();

		return true;
	}

	public void addURIHandler(URIHandler uriHandler) {
		handlers.add(uriHandler);
	}

	public void removeURIHandler(URIHandler uriHandler) {
		handlers.remove(uriHandler);
	}

}
