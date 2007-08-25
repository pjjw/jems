package net.kodeninja.jem.server.storage;

import java.net.URI;
import java.util.Set;

import net.kodeninja.util.KNModule;
import net.kodeninja.util.MimeType;

public interface StorageModule extends KNModule {

	public void hookMediaUpdate(MediaUpdateHook hook);
	
	public void unhookMediaUpdate(MediaUpdateHook hook);
	
	public void startUpdate();
	
	public void finishUpdate();
	
	public Set<MediaItem> getAllMedia();
	
	public int mediaCount();
	
	public Set<MediaItem> getMediaMatching(MimeType type);
	
	public Set<MediaItem> getMediaMatching(MetadataType type, String value);
	
	public boolean mediaExists(URI uri);
	
	public MediaItem addNewMedia(URI uri, MimeType type);
	
	public boolean removeMedia(MediaItem item);
	
	public MimeType getMimeType(MediaItem item);
		
	public Set<String> getMetadataValues(MetadataType type);
	
	public void addMediaMetadata(MediaItem item, MetadataType type, String value);
	
	public boolean removeMediaMetadata(MediaItem item, MetadataType type, String value);
	
	public Set<MediaCollection> getAllCollections();
 
	public int collectionCount();
	
	public void addCollection(MediaCollection collection);
	
	public boolean removeCollection(MediaCollection collection);
	
	public void setupCollection(MediaCollection collection);
}
