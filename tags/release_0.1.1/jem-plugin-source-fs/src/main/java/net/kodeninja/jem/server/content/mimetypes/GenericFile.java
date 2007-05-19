package net.kodeninja.jem.server.content.mimetypes;

import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MetadataTypes;

public class GenericFile implements MetadataFactory {

	public void addMetadata(MediaItem item) {
		if (item.getMetadata(MetadataTypes.Title) == null) {
			String tmpName = item.getMediaName();
			tmpName = tmpName.substring(tmpName.lastIndexOf("/") + 1);
			item.setMetadata(MetadataTypes.Title, tmpName.substring(0, tmpName
					.lastIndexOf(".")));
		}
		if (item.getMetadata(MetadataTypes.Format) == null) {
			String filename = item.getMediaURI().toString();
			filename = filename.substring(filename.lastIndexOf("/") + 1);
			item.setMetadata(MetadataTypes.Format, filename.substring(filename
					.indexOf(".") + 1));
		}
	}

}
