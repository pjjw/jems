package net.kodeninja.jem.server.www.content;

import java.io.IOException;

import net.kodeninja.http.packet.HTTPStreamBody;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.util.MimeType;

public class MediaBody extends HTTPStreamBody {
	private static MimeType FLV_MIMETYPE = new MimeType("video", "x-flv");

	public MediaBody(MediaItem mi) throws IOException {
		super(JemServer.getInstance().requestTranscode( JemServer.getMediaStorage().getMimeType(mi),
				FLV_MIMETYPE, mi.getURI().toURL().openStream()), FLV_MIMETYPE);
	}

}
