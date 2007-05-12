package net.kodeninja.jem.server.content.mimetypes;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MetadataTypes;

public class ImageFile extends GenericFile {

	@Override
	public void addMetadata(MediaItem item) {
		try {
			BufferedImage image = ImageIO.read(item.getMediaURI().toURL());
			if (image != null) {
				item.setMetadata(MetadataTypes.Width, "" + image.getWidth());
				item.setMetadata(MetadataTypes.Height, "" + image.getHeight());
				item
						.setMetadata(MetadataTypes.AspectRatio, ""
								+ ((float) image.getWidth() / (float) image
										.getHeight()));
			}
		} catch (IOException e) {
		}

		// Free up allocated image data
		System.gc();

		super.addMetadata(item);
	}

}
