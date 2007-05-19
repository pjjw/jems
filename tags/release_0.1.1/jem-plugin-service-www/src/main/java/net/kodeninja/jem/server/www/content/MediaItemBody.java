package net.kodeninja.jem.server.www.content;

import java.io.IOException;

import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MetadataTypes;
import net.kodeninja.http.packet.HTTPStreamBody;

public class MediaItemBody extends HTTPStreamBody {

	public MediaItemBody(MediaItem mi) throws IOException {
		super(mi.getMediaURI().toURL().openStream(), mi.getMediaMimeType());
		String len = mi.getMetadata(MetadataTypes.FileSize);
		if (len != null)
			contentLength = Long.parseLong(len);
	}

}
