package net.kodeninja.jem.server.www.content;

import java.io.IOException;

import net.kodeninja.http.packet.HTTPStreamBody;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.util.MimeType;

public class MediaBody extends HTTPStreamBody {
	private static MimeType FLV_MIMETYPE = new MimeType("video", "x-flv");

	public MediaBody(MediaItem mi) throws IOException {
		super(JemServer.getInstance().requestTranscode( mi.getMediaMimeType(),
														FLV_MIMETYPE,
														mi.getMediaURI()
																.toURL()
																.openStream()),
				FLV_MIMETYPE);
	}

}
