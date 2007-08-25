package net.kodeninja.jem.server.www.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPStreamBody;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.URIHandler;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaCollection;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.www.WWWService;
import net.kodeninja.jem.server.www.content.CollectionListBody;
import net.kodeninja.jem.server.www.content.IndexBody;
import net.kodeninja.jem.server.www.content.MediaBody;
import net.kodeninja.jem.server.www.content.MediaListBody;
import net.kodeninja.jem.server.www.content.MediaMetadataBody;
import net.kodeninja.jem.server.www.content.PlayerBody;

public class MediaURI implements URIHandler {
	private WWWService owner;

	public MediaURI(WWWService owner) {
		this.owner = owner;
	}

	public  HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<? extends HTTPBody> Packet) {
		String location = Packet.getHeader().getLocation().toString();

		HTTPBody body = null;

		if (location.equals("/"))
			body = new IndexBody();
		else if (location.startsWith("/collections/")) {
			if (location.equals("/collections/"))
				body = new CollectionListBody(JemServer.getMediaStorage().getAllCollections());
			else {
				String sid = location.substring(13);
				sid = sid.substring(0, sid.indexOf("/"));

				try {
					int id = Integer.parseInt(sid);

					for (MediaCollection col: JemServer.getMediaStorage().getAllCollections()) {
						if (col.hashCode() == id) {
							body = new MediaListBody(owner, col);
							break;
						}
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

		} else if (location.startsWith("/items/"))
			if (location.equals("/items/"))
				body = new MediaListBody(owner, JemServer.getMediaStorage().getAllMedia());
			else {
				String sid = location.substring(7);
				sid = sid.substring(0, sid.indexOf("/"));

				try {
					int id = Integer.parseInt(sid);

					for (MediaItem mi: JemServer.getMediaStorage().getAllMedia()) {

						if (mi.hashCode() == id) {
							if (location.endsWith("/play/")) {
								try {
									body = new PlayerBody(JemServer.getMediaStorage().getMimeType(mi).getPrimaryType());
								}
								catch (FileNotFoundException e) {}
							}
							else if (location.endsWith("video.flv"))
								body = new MediaBody(mi);
							else if (location.endsWith("/info/"))
								body = new MediaMetadataBody(mi);
							else if (location.endsWith("/stream/"))
								body = new HTTPStreamBody(mi.getURI().toURL().openStream(), JemServer.getMediaStorage().getMimeType(mi));
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
			return new HTTPPacket<HTTPBody>(header, body);
		} else
			return null;
	}

}
