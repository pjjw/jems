package net.kodeninja.jem.server.content;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.kodeninja.jem.server.storage.MediaItem;

public class AllMediaCollection extends CustomMediaCollectionImpl {

	protected Set<MediaItem> mediaList = Collections.synchronizedSet(new HashSet<MediaItem>());

	public boolean acceptMedia(MediaItem media) {
		return true;
	}

	public String getName() {
		return "All Media Collection";
	}

	public int getVersionMajor() {
		return 0;
	}

	public int getVersionMinor() {
		return 1;
	}

	public int getVersionRevision() {
		return 0;
	}

}
