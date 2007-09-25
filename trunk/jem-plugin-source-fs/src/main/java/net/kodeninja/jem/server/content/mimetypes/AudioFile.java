package net.kodeninja.jem.server.content.mimetypes;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.Metadata;
import net.kodeninja.jem.server.storage.MetadataType;

public class AudioFile extends GenericFile {

	@Override
	public void addMetadata(MediaItem item) {
		boolean addArtist = true;
		boolean addTitle = true;
		boolean addTrack = true;

		for (Metadata metadata: item.getMetadataList()) {
			addArtist &= (metadata.getType().equals(MetadataType.Artist) == false);
			addTitle &= (metadata.getType().equals(MetadataType.Title) == false);
			addTrack &= (metadata.getType().equals(MetadataType.SetPosition) == false);

			if ((addArtist && addTitle && addTrack) == false)
				break;
		}

		if (addArtist || addTitle || addTrack) {
			String track = null;
			String artist = null;
			String title = null;
			String file = item.getURI().toString().replace('_', ' ').replaceAll("%20", " ");
			file = file.substring(file.lastIndexOf("/") + 1);
			file = file.substring(0, file.lastIndexOf("."));

			int start = 0;
			for (int i = 0; i < file.length(); i++) {
				if (start == 0) {
					if ((file.charAt(i) == '.') || (file.charAt(i) == ' ')) { // Catch ##. Rest or ## Rest
						track = file.substring(0, i);
						try {
							int tmp = Integer.parseInt(track);
							if (tmp > 99)
								tmp = tmp % 100;
							track = "" + tmp;
							start = i + 1;
							continue;
						}
						catch (NumberFormatException e) {
							track = null;
						}
					}
				}
				if ((file.length() > i + 3) && file.substring(i, i + 3).equals(" - ") && (artist == null)) { // Catch Artist - Title
					artist = file.substring(start, i);
					title = file.substring(i + 3);
					break;
				}
			}
			if ((artist == null) && (title == null)) { // Catch Title
				title = file.substring(start);
			}
			else if (title.toLowerCase().startsWith("track")) { // Catch Artist - Track ## 
				track = title.substring(5).trim();
				try {
					int tmp = Integer.parseInt(track);
					track = "" + tmp;
				}
				catch (NumberFormatException e) {
					track = null;
				}
			}


			if ((addArtist) && (artist != null) && (artist.length() > 0))
				JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Artist, artist.trim());

			if ((addTitle) && (title != null) && (title.length() > 0))
				JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Title, title.trim());

			if ((addTrack) && (track != null) && (track.length() > 0))
				JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.SetPosition, track.trim());
		}

		super.addMetadata(item);
	}
}
