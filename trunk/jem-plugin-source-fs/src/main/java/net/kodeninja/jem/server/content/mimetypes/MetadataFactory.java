package net.kodeninja.jem.server.content.mimetypes;

import net.kodeninja.jem.server.storage.MediaItem;

public interface MetadataFactory {
	public void addMetadata(MediaItem item);
}
