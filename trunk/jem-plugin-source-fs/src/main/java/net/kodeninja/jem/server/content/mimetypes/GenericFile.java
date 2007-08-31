package net.kodeninja.jem.server.content.mimetypes;

import java.io.IOException;
import java.io.InputStream;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.Metadata;
import net.kodeninja.jem.server.storage.MetadataType;

public class GenericFile implements MetadataFactory {

	public void addMetadata(MediaItem item) {
		boolean addTitle = true;
		boolean addFormat = true;
		
		for (Metadata metadata: item.getMetadataList()) {
			addTitle &= (metadata.getType().equals(MetadataType.Title) == false);
			addFormat &= (metadata.getType().equals(MetadataType.Format) == false);
			
			if ((addTitle && addFormat) == false)
				break;
		}
		
		if (addTitle || addFormat) {
			String tmp = item.getURI().toString().replace('_', ' ').replaceAll("%20", " ");
			tmp = tmp.substring(tmp.lastIndexOf("/") + 1);
			int pos = tmp.lastIndexOf(".");
			if (pos > 0) {
				if (addTitle)
					JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Title, tmp.substring(0, pos));
				if (addFormat)
					JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Format, tmp.substring(pos + 1));
			}
		}
	}

	//Utility function to ensure skip works
	protected void skipBytes(InputStream is, long toSkip) throws IOException {
		while (toSkip > 0) {
			long skipped = is.skip(toSkip);
			if (skipped <= 0)
				throw new IOException();
			else
				toSkip -= skipped;
		}
	}
	
	protected boolean arrayEqual(byte[] a1, byte[] a2) {
		if (a1.length != a2.length)
			return false;
		return arrayEqual(a1, a2, a1.length);
	}
	
	protected boolean arrayEqual(byte[] a1, byte[] a2, int len) {
		for (int i = 0; i < len; i++)
			if (a1[i] != a2[i])
				return false;
		return true;
	}
	
}
