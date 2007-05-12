package net.kodeninja.jem.server.DMAP.responses;

import java.io.InputStream;
import java.util.Iterator;

import net.kodeninja.DMAP.parameters.dmap.mstt;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPStreamBody;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.URIHandler;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.DMAP.DMAPHTTPBody;
import net.kodeninja.jem.server.DMAP.DMAPResponsePacket;
import net.kodeninja.jem.server.DMAP.DMAPService;
import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MetadataTypes;

public class MediaRequestURI implements URIHandler {
	protected DMAPService service;
	protected mstt statusCode = new mstt(200);

	public MediaRequestURI(DMAPService s) {
		service = s;
	}

	public HTTPPacket<HTTPHeader, HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<HTTPHeader, HTTPBody> Packet) {
		String loc = Packet.getHeader().getLocation().getPath();
		if (loc.contains("/items/") == false)
			return null;

		long mediaRequested;
		String mediaId = loc.substring(loc.lastIndexOf("/") + 1);
		mediaId = mediaId.substring(0, mediaId.indexOf("."));
		mediaRequested = Long.parseLong(mediaId);
		if (mediaRequested > Integer.MAX_VALUE)
			mediaRequested = Integer.MIN_VALUE
					- (Integer.MAX_VALUE - mediaRequested) - 1;

		InputStream src = null;
		long length = -1;

		Iterator<MediaItem> mIt = JemServer.getInstance().getAllMedia();
		while (mIt.hasNext()) {
			MediaItem mi = mIt.next();
			if (mi.hashCode() == mediaRequested) {
				src = service.getItemStream(mi);

				if (service.transcodeItem(mi) == false)
					try {
						String size;
						if ((size = mi.getMetadata(MetadataTypes.FileSize)) != null)
							length = Integer.parseInt(size);
					} catch (NumberFormatException e) {
					}

				break;
			}
		}

		if (src == null)
			return null;

		HTTPStreamBody body = new HTTPStreamBody(src, DMAPHTTPBody.MIME_TYPE,
				length);

		HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1,
				HTTPResponseCode.HTTP_200_OK);
		HTTPPacket<HTTPHeader, HTTPBody> response = new DMAPResponsePacket<HTTPBody>(
				header, body, service);

		return response;
	}

}
