package net.kodeninja.jem.server.content.mimetypes;

import java.io.IOException;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.content.fs.ext.ImageInfo;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.MetadataType;

public class ImageFile extends GenericFile {

	@Override
	public void addMetadata(MediaItem item) {
		try {
			ImageInfo ii = new ImageInfo();
			ii.setInput(item.getURI().toURL().openStream());
			if (ii.check()) {
				JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Width, "" + ii.getWidth());
				JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Height, "" + ii.getHeight());
				JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.AspectRatio, "" + ((float)ii.getWidth() / (float)ii.getHeight()));
			}
		} catch (IOException e) {
		}
		
		super.addMetadata(item);
	}

}
