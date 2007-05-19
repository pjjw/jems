package net.kodeninja.jem.server.content.mimetypes;

import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MetadataTypes;

public class AudioFile extends GenericFile {

	@Override
	public void addMetadata(MediaItem item) {
		int pos, pos2 = -2;
		String filename = item.getMediaURI().getPath();
		if ((pos = filename.lastIndexOf("/")) > -1)
			filename = filename.substring(pos + 1);

		if ((pos = filename.lastIndexOf(".")) > -1)
			filename = filename.substring(0, pos);

		if ((pos = filename.indexOf(" - ")) == -1)
			if ((pos = filename.indexOf(" - ")) == -1)
				if ((pos = filename.indexOf("_-_")) == -1) {
					pos = filename.indexOf("-");
					pos2 = pos + 1;
				}

		if (pos == -1)
			super.addMetadata(item);
		else {
			if (pos2 == -2)
				pos2 = pos + 2;
			if (item.getMetadata(MetadataTypes.Artist) == null)
				item.setMetadata(MetadataTypes.Artist, filename
						.substring(0, pos));
			if (item.getMetadata(MetadataTypes.Title) == null)
				item.setMetadata(MetadataTypes.Title, filename
						.substring(pos2 + 1));
		}
	}
}
