package net.kodeninja.http.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.io.StreamWriter;
import net.kodeninja.util.MimeType;

public class HTTPStreamBody implements HTTPBody {
	long contentLength;
	MimeType m;
	InputStream s;

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

	public void readFromStream(InputStream in, int ContentLength)
			throws IOException {
		throw new IOException("Unsupported operation.");
	}

	public void writeToStream(OutputStream out) throws IOException {
		StreamWriter sw = new StreamWriter(out);
		sw.writeEntireStream(s);
	}

}
