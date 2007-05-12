package net.kodeninja.jem.server.content.fs;

import java.io.File;

import net.kodeninja.jem.server.content.MediaItemImpl;
import net.kodeninja.jem.server.content.MetadataTypes;
import net.kodeninja.util.MimeType;

public class FileMediaItem extends MediaItemImpl {

	public FileMediaItem(MimeType Type, File path) {
		super(Type, path.toURI());
		String tmpName = path.getName();
		setMetadata(MetadataTypes.FileSize, "" + path.length());
		setMetadata(MetadataTypes.Format, tmpName.substring(tmpName
				.lastIndexOf(".") + 1));
	}

	@Override
	public int getVersionMajor() {
		return 0;
	}

	@Override
	public int getVersionMinor() {
		return 1;
	}

	@Override
	public int getVersionRevision() {
		return 0;
	}

}
