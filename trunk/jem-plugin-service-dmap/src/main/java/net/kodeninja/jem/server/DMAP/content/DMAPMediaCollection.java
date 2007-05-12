package net.kodeninja.jem.server.DMAP.content;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import net.kodeninja.jem.server.content.MediaCollection;
import net.kodeninja.jem.server.content.MediaItem;

public class DMAPMediaCollection implements MediaCollection {
	public static final String DEFAULT_NAME = "DAAP Media Collection";
	protected Set<MediaItem> mediaSet = new TreeSet<MediaItem>();
	protected String collectionName;
	protected String collectionType;
	protected MediaCollection collectionBase = null;

	public DMAPMediaCollection(String type) {
		this(DEFAULT_NAME, type);
	}

	public DMAPMediaCollection(String name, String type) {
		this(name, null, type);
	}

	public DMAPMediaCollection(MediaCollection mc, String type) {
		this(mc.getCollectionName(), mc, type);
	}

	public DMAPMediaCollection(String name, MediaCollection mc, String type) {
		collectionName = name;
		collectionType = type;
		collectionBase = mc;
		processCollection();
	}

	protected void processCollection() {
		if (collectionBase != null)
			for (MediaItem mi : collectionBase)
				addMedia(mi);
	}

	public boolean acceptMedia(MediaItem media) {
		if ((collectionBase == null) || (collectionBase.acceptMedia(media))) {
			if ((collectionType.equals("all"))
					&& (media.getMediaMimeType().getPrimaryType().toLowerCase()
							.equals("video")
							|| media.getMediaMimeType().getPrimaryType()
							.toLowerCase().equals("audio") || media
							.getMediaMimeType().getPrimaryType().toLowerCase()
							.equals("image")))
				return true;
			else if ((collectionType.equals("music"))
					&& ((media.getMediaMimeType().getPrimaryType().toLowerCase()
							.equals("video")) || (media.getMediaMimeType()
									.getPrimaryType().toLowerCase().equals("audio"))))
				return true;
			else if ((collectionType.equals("photo"))
					&& (media.getMediaMimeType().getPrimaryType().toLowerCase()
							.equals("image")))
				return true;
		}
		return false;
	}

	public synchronized boolean addMedia(MediaItem media) {
		if (acceptMedia(media)) {
			mediaSet.add(media);
			return true;
		} else
			return false;
	}

	public String getType() {
		return collectionType;
	}

	public synchronized Iterator<MediaItem> iterator() {
		return mediaSet.iterator();
	}

	public String getCollectionName() {
		return collectionName;
	}

	public int getMediaCount() {
		return mediaSet.size();
	}

	public void removeMedia(MediaItem media) {
		mediaSet.remove(media);
	}

	public String getName() {
		return "DAAP Media Collection";
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
