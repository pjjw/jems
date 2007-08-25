package net.kodeninja.http.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.io.StreamWriter;
import net.kodeninja.util.MimeType;

public class HTTPStreamBody implements HTTPBody {
	protected long contentLength;
	protected MimeType m;
	protected InputStream s;

	public HTTPStreamBody(InputStream in, MimeType mt) {
		this(in, mt, -1);
	}

	public HTTPStreamBody(InputStream in, MimeType mt, long length) {
		m = mt;
		s = in;
		contentLength = length;
	}

	public long getContentLength() {
		return contentLength;
	}

	public MimeType getMimeType() {
		return m;
	}

	public String getContentType() {
		return getMimeType().toString();
	}
	
	public void readFromStream(InputStream in, int ContentLength)
			throws IOException {
		throw new IOException("Unsupported operation.");
	}

	public void writeToStream(OutputStream out) throws IOException {
		StreamWriter sw = new StreamWriter(out);
		if (s != null)
			sw.writeEntireStream(s);
	}
	
	public boolean forceCompression() {
		return false;
	}

	public boolean forceChunked() {
		return true;
	}
}
