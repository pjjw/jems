package net.kodeninja.jem.server.www.content;

import java.io.IOException;

import net.kodeninja.http.packet.HTTPStreamBody;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.Metadata;
import net.kodeninja.jem.server.storage.MetadataType;

public class MediaItemBody extends HTTPStreamBody {

	public MediaItemBody(MediaItem mi) throws IOException {
		super(mi.getURI().toURL().openStream(), JemServer.getMediaStorage().getMimeType(mi));

		for (Metadata metadata: mi.getMetadataList())
			if (metadata.getType() == MetadataType.FileSize) {
				String len = metadata.getValue();
				try {
					contentLength = Long.parseLong(len);
					break;
				} catch (NumberFormatException e) {}
			}
	}
}
