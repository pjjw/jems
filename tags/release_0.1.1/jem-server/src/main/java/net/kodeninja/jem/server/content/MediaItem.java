package net.kodeninja.jem.server.content;

import java.net.URI;
import java.util.Iterator;

import net.kodeninja.util.KNModule;
import net.kodeninja.util.MimeType;

public interface MediaItem extends KNModule, Comparable<MediaItem> {

	/**
	 * Returns the name of the media.
	 *
	 * @return The media's name
	 */
	public String getMediaName();

	/**
	 * Returns the type of media.
	 *
	 * @return Media type.
	 */
	public MimeType getMediaMimeType();

	/**
	 * Returns a URI representing the media's location.
	 *
	 * @return The media's URI.
	 */
	public URI getMediaURI();

	/**
	 * Checks the media against the passed search.
	 *
	 * @param search
	 *            The object to search with.
	 * @return True if the MediaItem matches the search.
	 */
	public boolean matches(SearchRequest search);

	/**
	 * Attempts to return value associated the provided metadata tag. Returns
	 * null if the tag as not been set.
	 *
	 * @param Tag
	 *            The metadata tag to look for.
	 * @return The value or null if the tag has not been set.
	 */
	public String getMetadata(MetadataTypes Tag);

	/**
	 * Sets the metadata parameter with to the provided data.
	 *
	 * @param Tag
	 *            The metadata tag to set.
	 * @param data
	 *            The data to set it to.
	 */
	public void setMetadata(MetadataTypes Tag, String data);

	public Iterator<MetadataTypes> getAllSetMetadataTags();
}
