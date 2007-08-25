package net.kodeninja.jem.server.storage;

import net.kodeninja.util.KNModule;

public interface MediaCollection extends KNModule, Iterable<MediaItem> {

	/**
	 * Returns a descriptive name of the collection.
	 *
	 * @return A descriptive name of the collection.
	 */
	public String getCollectionName();

	/**
	 * Returns the number of media items stored in the collection.
	 *
	 * @return The size of the collection.
	 */
	public int getMediaCount();

	/**
	 * Checks to see if the passed media would be accepted by this collection's
	 * filter.
	 *
	 * @param media
	 *            The media item to check.
	 * @return Media item was accepted.
	 */
	public boolean acceptMedia(MediaItem media);

	/**
	 * Calls acceptMedia, and adds the media item to the collection if it is
	 * accepted.
	 *
	 * @param media
	 *            The media item to add.
	 * @return Media item was accepted.
	 */
	public boolean addMedia(MediaItem media);

	/**
	 * Removes the media item to the collection.
	 *
	 * @param media
	 *            The media item to remove.
	 */
	public void removeMedia(MediaItem media);
}
