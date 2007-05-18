package net.kodeninja.jem.server.www.handlers;

import java.io.IOException;
import java.util.Iterator;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.URIHandler;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.content.MediaCollection;
import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.www.content.CollectionListBody;
import net.kodeninja.jem.server.www.content.IndexBody;
import net.kodeninja.jem.server.www.content.MediaBody;
import net.kodeninja.jem.server.www.content.MediaItemBody;
import net.kodeninja.jem.server.www.content.MediaListBody;
import net.kodeninja.jem.server.www.content.MediaMetadataBody;
import net.kodeninja.jem.server.www.content.PlayerBody;

public class MediaURI implements URIHandler {

	public HTTPPacket<HTTPHeader, HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<HTTPHeader, HTTPBody> Packet) {
		String location = Packet.getHeader().getLocation().toString();

		HTTPBody body = null;

		if (location.equals("/"))
			body = new IndexBody();
		else if (location.startsWith("/collections/")) {
			if (location.equals("/collections/"))
				body = new CollectionListBody(JemServer.getInstance()
						.getCollections());
			else {
				String sid = location.substring(13);
				sid = sid.substring(0, sid.indexOf("/"));

				try {
					int id = Integer.parseInt(sid);

					Iterator<MediaCollection> colIt = JemServer.getInstance()
							.getCollections();
					while (colIt.hasNext()) {
						MediaCollection col = colIt.next();

						if (col.hashCode() == id) {
							body = new MediaListBody(col.iterator());
							break;
						}
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

		} else if (location.startsWith("/items/"))
			if (location.equals("/items/"))
				body = new MediaListBody(JemServer.getInstance().getAllMedia());
			else if (location.endsWith("/play/"))
				body = new PlayerBody();
			else {
				String sid = location.substring(7);
				sid = sid.substring(0, sid.indexOf("/"));

				try {
					int id = Integer.parseInt(sid);

					Iterator<MediaItem> mIt = JemServer.getInstance()
							.getAllMedia();
					while (mIt.hasNext()) {
						MediaItem mi = mIt.next();

						if (mi.hashCode() == id) {
							if (location.endsWith("video.flv"))
								body = new MediaBody(mi);
							else if (location.endsWith("/info/"))
								body = new MediaMetadataBody(mi);
							else if (location.endsWith("/stream/"))
								body = new MediaItemBody(mi);
							break;
						}
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		if (body != null) {
			HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1,
					HTTPResponseCode.HTTP_200_OK);
			header.setParameter("Cache-Control", "no-cache");
			return new HTTPPacket<HTTPHeader, HTTPBody>(header, body);
		} else
			return null;
	}

}
