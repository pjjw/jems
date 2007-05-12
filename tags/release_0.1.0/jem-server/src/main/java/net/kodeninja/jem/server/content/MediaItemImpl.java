package net.kodeninja.jem.server.content;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.kodeninja.util.MimeType;

public abstract class MediaItemImpl implements MediaItem {
	protected MediaCollection owner;
	protected MimeType mimetype;
	protected URI mediaURI;
	protected Map<MetadataTypes, String> metadata = new HashMap<MetadataTypes, String>();

	public MediaItemImpl(MimeType Type, URI path) {
		mimetype = Type;
		mediaURI = path;
	}

	public MimeType getMediaMimeType() {
		return mimetype;
	}

	public String getMediaName() {
		String artist = getMetadata(MetadataTypes.Artist);
		String title = getMetadata(MetadataTypes.Title);

		if ((artist != null) && (title != null))
			return "[A]" + artist + " - " + "[T]" + title;
		else if (artist != null)
			return "[A]" + artist + " - " + "[P]" + getMediaURI().getPath();
		else if (title != null)
			return "[T]" + title;
		else
			return "[P]" + getMediaURI().getPath();
	}

	public URI getMediaURI() {
		return mediaURI;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (this == null)
			return true;
		if (o instanceof MediaItemImpl)
			return mediaURI.toString().equals(
												((MediaItemImpl) o).mediaURI
														.toString());
		return false;
	}

	@Override
	public int hashCode() {
		return mediaURI.hashCode();
	}

	public String getName() {
		return "Simple Media File";
	}

	public int getVersionMajor() {
		return 0;
	}

	public int getVersionMinor() {
		return 2;
	}

	public int getVersionRevision() {
		return 0;
	}

	public boolean matches(SearchRequest search) {
		return (search.matches(getMediaName()) || search.matches(getMediaURI()
				.toString()));
	}

	public int compareTo(MediaItem o) {
		return getMediaName().compareTo(o.getMediaName());
	}

	protected String fixString(String s) {
		StringBuffer sb = new StringBuffer(s);
		int i = 0;
		while (i < sb.length()) {
			char c = sb.charAt(i);
			if (c >= ' ')
				i++;
			else
				sb.deleteCharAt(i);
		}

		return sb.toString();
	}

	public String getMetadata(MetadataTypes Tag) {
		return metadata.get(Tag);
	}

	public void setMetadata(MetadataTypes Tag, String data) {
		metadata.put(Tag, fixString(data));
	}

	public Iterator<MetadataTypes> getAllSetMetadataTags() {
		return metadata.keySet().iterator();
	}

}
