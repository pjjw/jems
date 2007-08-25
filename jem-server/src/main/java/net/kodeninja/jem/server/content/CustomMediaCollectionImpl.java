package net.kodeninja.jem.server.content;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import org.w3c.dom.Node;

import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.util.KNModuleInitException;

public abstract class CustomMediaCollectionImpl implements
		CustomMediaCollection {
	protected Set<MediaItem> mediaList = Collections.synchronizedSet(new HashSet<MediaItem>());
	protected String collectionName;

	public void xmlInit(Node xmlNode) throws KNModuleInitException {
		collectionName = xmlNode.getAttributes().getNamedItem("name")
				.getNodeValue();
	}

	public Iterator<MediaItem> iterator() {
		return mediaList.iterator();
	}

	public String getCollectionName() {
		return collectionName;
	}

	public int getMediaCount() {
		return mediaList.size();
	}

	public boolean addMedia(MediaItem media) {
		if (acceptMedia(media)) {
			mediaList.add(media);
			return true;
		} else
			return false;
	}

	public void removeMedia(MediaItem media) {
		mediaList.remove(media);
	}
}
