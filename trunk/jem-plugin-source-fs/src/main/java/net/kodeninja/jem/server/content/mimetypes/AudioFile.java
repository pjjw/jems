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
		boolean addAlbum = true;

		for (Metadata metadata: item.getMetadataList()) {
			addArtist &= (metadata.getType().equals(MetadataType.Format) == false);
			addTitle &= (metadata.getType().equals(MetadataType.Title) == false);
			addTrack &= (metadata.getType().equals(MetadataType.SetPosition) == false);
			addAlbum &= (metadata.getType().equals(MetadataType.Set) == false);

			if ((addArtist && addTitle && addTrack) == false)
				break;
		}

		int pos, pos2 = -2;
		String origFilename = item.getURI().getPath().trim().replace('_', ' ').replaceAll("%20", " ");
		String filename = origFilename;

		//Trim off the filename junk
		if ((pos = filename.lastIndexOf("/")) > -1)
			filename = filename.substring(pos + 1);
		if ((pos = filename.lastIndexOf(".")) > -1)
			filename = filename.substring(0, pos);

		int track = -1;
		//Get the first space
		int pos3 = filename.indexOf(" ");

		if ((pos3 == 2) || ((pos3 == 3) && (filename.charAt(2) == '.'))) { // "## Rest" or "##. Rest" format
			String tmp = filename.substring(0, 2).trim();
			try {
				track = Integer.parseInt(tmp);
				if (filename.charAt(2) == '.')
					filename = filename.substring(3).trim();
				else
					filename = filename.substring(2).trim();
			}
			catch (NumberFormatException e) {}
		}
		else if (filename.toLowerCase().startsWith("track ")) { // "Track ## Rest" or "Track ##. Rest"
			String tmp = filename.substring(6, 8).trim();
			try {
				track = Integer.parseInt(tmp);
				if (filename.charAt(8) == '.')
					filename = filename.substring(9).trim();
				else
					filename = filename.substring(8).trim();
			}
			catch (NumberFormatException e) {}
		}

		pos = 0;
		while (pos == 0) { //Ignore - at the very beginning of filenames
			if ((pos = filename.indexOf(" - ")) == -1) {
				pos = filename.indexOf("-");

				if ((pos > 0) && (pos < filename.length() - 1))
					pos2 = pos + 1;
				else
					pos = -1;
			}
			else //Move the secondary position (song title) beyond the name
				pos2 = pos + 2;

			if (pos == 0) //Cut the - from the beginning
				filename = filename.substring(1);
		}

		if (pos == filename.length()) { //Fixes - at end of filename bug
			filename = filename.substring(0, filename.length() - 1);
			pos = -1;
		}


		String title = null;
		String album = null;
		String artist = null;

		if (pos == -1) { // "Title" format
			title = filename;
		}
		else { // "Artist - Title"
			artist = filename.substring(0, pos).trim();
			title = filename.substring(pos2 + 1);
		}


		if (track >= 0) {
			String tmp = origFilename.substring(0, origFilename.lastIndexOf("/"));
			pos = tmp.lastIndexOf("/");
			if (pos > 0) {
				album = tmp.substring(pos + 1, tmp.length());
				if ((artist == null) || (artist.length() == 0)) {
					tmp = tmp.substring(0, pos);
					pos = tmp.lastIndexOf("/");
					if (pos > 0)
						artist = tmp.substring(pos + 1, tmp.length());
				}
			}
		}

		if ((addArtist) && (artist != null) && (artist.length() > 0))
			JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Artist, artist);

		if ((addTitle) && (title != null) && (title.length() > 0))
			JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Title, title);

		if ((addAlbum) && (album != null) && (album.length() >= 0))
			JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Set, album);

		if ((addTrack) && (track >= 0))
			JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.SetPosition, "" + track);

		super.addMetadata(item);
	}
}
