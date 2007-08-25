package net.kodeninja.http.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.util.MimeType;

public interface HTTPBody {
	public void writeToStream(OutputStream out) throws IOException;

	public void readFromStream(InputStream in, int ContentLength)
			throws IOException;

	public long getContentLength();

	public MimeType getMimeType();
	
	public String getContentType();
	
	public boolean forceCompression();
	
	public boolean forceChunked();
}
