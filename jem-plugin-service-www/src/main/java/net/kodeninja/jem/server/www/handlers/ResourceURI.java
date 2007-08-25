package net.kodeninja.jem.server.www.handlers;

import java.io.FileNotFoundException;
import java.io.InputStream;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPStreamBody;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.URIHandler;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.util.MimeType;

public class ResourceURI implements URIHandler {
	private MimeType SWF_MIME = new MimeType("application", "x-shockwave-flash");

	public  HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<? extends HTTPBody> Packet) {
		String file = Packet.getHeader().getLocation().toString();
		file = file.substring(file.lastIndexOf("/") + 1);
		int qMark = file.indexOf("?");
		if (qMark > 0)
			file = file.substring(0, qMark);

		file = "res-www/" + file;

		if (file.endsWith("/") == false) {
			try {
				InputStream is = JemServer.getResourceAsStream(file);

				if (is != null) {
					HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1,
							HTTPResponseCode.HTTP_200_OK);
					HTTPStreamBody body = new HTTPStreamBody(is, SWF_MIME);
					return new HTTPPacket<HTTPBody>(header, body);
				}
			}
			catch (FileNotFoundException e) {}
		}

		return null;

	}

}
