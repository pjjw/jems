package net.kodeninja.jem.server.content.transcoding;

import java.io.IOException;
import java.io.InputStream;

import net.kodeninja.util.MimeType;

public interface Transcoder {
	public InputStream transcode(MimeType from, MimeType to, InputStream src)
			throws IOException;
}
