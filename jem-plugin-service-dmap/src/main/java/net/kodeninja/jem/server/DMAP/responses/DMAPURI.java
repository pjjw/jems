package net.kodeninja.jem.server.DMAP.responses;

import net.kodeninja.DMAP.parameters.dmap.mstt;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.URIHandler;
import net.kodeninja.jem.server.DMAP.DMAPHTTPBody;
import net.kodeninja.jem.server.DMAP.DMAPResponsePacket;
import net.kodeninja.jem.server.DMAP.DMAPService;

public abstract class DMAPURI implements URIHandler {
	protected DMAPService service;
	protected DMAPHTTPBody body = new DMAPHTTPBody();
	protected String path;
	protected mstt statusCode = new mstt(200);

	protected DMAPURI(DMAPService s, String p) {
		service = s;
		path = p;
	}

	public HTTPPacket<HTTPHeader, HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<HTTPHeader, HTTPBody> Packet) {
		if (Packet.getHeader().getLocation().getPath().equals(path) == false)
			return null;

		HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1,
				HTTPResponseCode.HTTP_200_OK);
		HTTPPacket<HTTPHeader, HTTPBody> response = new DMAPResponsePacket<HTTPBody>(
				header, body, service);

		return response;
	}

}
