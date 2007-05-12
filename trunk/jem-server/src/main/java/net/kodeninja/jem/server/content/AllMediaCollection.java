package net.kodeninja.jem.server.content;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class AllMediaCollection extends CustomMediaCollectionImpl {

	protected Set<MediaItem> mediaList = Collections
			.synchronizedSet(new TreeSet<MediaItem>());

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
