package net.kodeninja.jem.server.UPnP.description.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.Metadata;
import net.kodeninja.util.MimeType;

public class MediaTreeItem implements MediaTree {

	private Set<MediaTreeAttribute> attributes = new LinkedHashSet<MediaTreeAttribute>();
	private MediaTree parent = null;
	private String id;
	private String name;
	private MediaItem mediaItem;

	public MediaTreeItem(String id, MediaTree parent, MediaItem mi, String url) {
		this.id = id;
		this.name = JemServer.getMediaName(mi);
		this.parent = parent;
		this.mediaItem = mi;
		parent.addChild(this, false);

		MimeType mt = JemServer.getMediaStorage().getMimeType(mi);
		String width = null, height = null;
		String length = null;

		String itemClass = "object.item";
		if (mt.getPrimaryType().equalsIgnoreCase("audio")) {
			itemClass = "object.item.audioItem.musicTrack";
		}
		else if (mt.getPrimaryType().equalsIgnoreCase("video")) {
			itemClass = "object.item.videoItem";
		}
		else if (mt.getPrimaryType().equalsIgnoreCase("image")) {
			itemClass = "object.item.imageItem.photo";
		}


		attributes.add(new MediaTreeAttribute("upnp:class", itemClass));

		MediaTreeAttribute res = new MediaTreeAttribute("res", url);
		res.addAttribute("protocolInfo", "http-get:*:" + mt + ":*");
		attributes.add(res);

		HashMap<String, String> mdMap = new HashMap<String, String>();

		for (Metadata md: mi.getMetadataList()) {
			switch (md.getType()) {
			case Title:
				name = md.getValue();
				break;
			case Artist:
				if (itemClass.startsWith("object.item.audioItem.musicTrack") ||
						itemClass.startsWith("object.item.videoItem.musicVideoClip") ||
						itemClass.startsWith("object.item.playlistItem")) {
					attributes.add(new MediaTreeAttribute("upnp:artist", md.getValue()));
				}
				break;
			case Genre:
				if (itemClass.startsWith("object.item.audioItem") ||
						itemClass.startsWith("object.item.videoItem") ||
						itemClass.startsWith("object.item.playlistItem")) {
					attributes.add(new MediaTreeAttribute("upnp:genre", md.getValue()));
				}
				break;
			case Actor:
				if (itemClass.startsWith("object.item.videoItem")) {
					attributes.add(new MediaTreeAttribute("upnp:actor", md.getValue()));
				}
				break;
			case Director:
				if (itemClass.startsWith("object.item.videoItem")) {
					attributes.add(new MediaTreeAttribute("upnp:director", md.getValue()));
				}
				break;
			case Description:
				if (itemClass.startsWith("object.item.audioItem") ||
						itemClass.startsWith("object.item.videoItem") ||
						itemClass.startsWith("object.item.imageItem") ||
						itemClass.startsWith("object.item.playlistItem") ||
						itemClass.startsWith("object.item.textItem")) {
					mdMap.put("dc:description", md.getValue());
					mdMap.put("upnp:longDescription", md.getValue());
				}
				break;
			case Set:
				if (itemClass.startsWith("object.item.audioItem.musicTrack") ||
						itemClass.startsWith("object.item.videoItem.musicVideoClip") ||
						itemClass.startsWith("object.item.imageItem.photo")) {
					attributes.add(new MediaTreeAttribute("upnp:album", md.getValue()));
				}
				break;
			case SetPosition:
				if (itemClass.startsWith("object.item.audioItem.musicTrack")) {
					mdMap.put("upnp:originalTrackNumber", md.getValue());
				}
				break;
			case Width:
				width = md.getValue();
				break;
			case Height:
				height = md.getValue();
				break;
			case BitRate:
				//res.addAttribute("bitrate", md.getValue());
				break;
			case Length:
				try {
					int len = Integer.parseInt(md.getValue());
					int hour = len / (60 * 60 * 1000);
					len = len - (hour * 60 * 60 * 1000);
					int min = len / (60 * 1000);
					len = len - (min * 60 * 1000);
					float sec = (float)len / 1000.0f;
					length = hour + ":" + (min < 10 ? "0" + min : min) + ":" + sec;
					res.addAttribute("duration", length);
				}
				catch (NumberFormatException e) {}
				break;
			}
		}

		for (String attrName: mdMap.keySet())
			attributes.add(new MediaTreeAttribute(attrName, mdMap.get(attrName)));

		if ((width != null) && (height != null))
			res.addAttribute("resolution", width + "x" + height);
	}

	public MediaItem getMediaItem() {
		return mediaItem;
	}

	public void addChild(MediaTree child) {
	}

	public void addChild(MediaTree child, boolean setParent) {
	}

	public void removeChild(MediaTree child) {
	}

	public MediaTree getBranch(String id) {
		if (getId().equals(id))
			return this;
		return null;
	}

	public Iterator<MediaTree> getChildern() {
		return null;
	}

	public int getChildernCount() {
		return 0;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public MediaTree getParent() {
		return parent;
	}

	public void setParent(MediaTree parent) {
		setParent(parent, true);
	}

	public void setParent(MediaTree parent, boolean addAsChild) {
		this.parent = parent;
		if (addAsChild)
			parent.addChild(this, false);
	}

	public void addAttribute(MediaTreeAttribute tree) {
		attributes.add(tree);
	}

	public Set<MediaTreeAttribute> getAttributes() {
		return attributes;
	}

}
