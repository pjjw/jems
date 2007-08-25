package net.kodeninja.jem.server.DMAP.responses;

import net.kodeninja.DMAP.parameters.dmap.mlid;
import net.kodeninja.DMAP.parameters.dmap.mlog;
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

public class LoginURI implements URIHandler {
	protected DMAPService service;
	protected mstt statusCode = new mstt(200);

	public LoginURI(DMAPService s) {
		service = s;
	}

	public  HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<? extends HTTPBody> Packet) {
		if (Packet.getHeader().getLocation().getPath().equals("/login") == false)
			return null;

		DMAPHTTPBody body = new DMAPHTTPBody();
		HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1,
				HTTPResponseCode.HTTP_200_OK);
		HTTPPacket<HTTPBody> response = new DMAPResponsePacket<HTTPBody>(
				header, body, service);

		mlog root = new mlog();
		body.addParameter(root);

		// Status Code
		root.addParameter(statusCode);

		// Session ID
		root.addParameter(new mlid(Socket.hashCode()));

		return response;
	}

}
