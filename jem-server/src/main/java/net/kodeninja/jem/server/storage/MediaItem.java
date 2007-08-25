package net.kodeninja.jem.server.storage;

import java.net.URI;
import java.util.List;

import net.kodeninja.util.Pair;

public class MediaItem extends Pair<URI, List<Metadata>> {

	public MediaItem(URI a, List<Metadata> b) {
		super(a, b);
	}
	
	public URI getURI() {
		return getA();
	}
	
	public List<Metadata> getMetadataList() {
		return getB();
	}
	
	public boolean equals(Object o) {
		if (o == this)
			return true;
		else if (o instanceof MediaItem)
			return getURI().equals(((MediaItem)o).getURI());
		return false;
	}
	
	public int hashCode() {
		return getURI().hashCode();
	}
	
	public String toString() {
		return getURI().toString();
	}
	
}
