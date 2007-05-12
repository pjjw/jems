package net.kodeninja.http.packet.extra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.io.StreamWriter;
import net.kodeninja.util.MimeType;

public class HTTPGZipBody implements HTTPEncodedBody {

	protected ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	protected MimeType mime;

	public HTTPGZipBody(MimeType m) {
		mime = m;
	}

	public HTTPGZipBody(HTTPBody b) {
		mime = b.getMimeType();
		try {
			GZIPOutputStream stream = new GZIPOutputStream(buffer);
			b.writeToStream(stream);
			stream.finish();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long getContentLength() {
		return buffer.size();
	}

	public MimeType getMimeType() {
		return mime;
	}

	public void readFromUnencodedStream(InputStream in, int ContentLength)
			throws IOException {
		GZIPOutputStream stream = new GZIPOutputStream(buffer);
		StreamWriter sw = new StreamWriter(buffer);
		sw.writeStream(in, ContentLength);
		stream.finish();
	}

	public void writeToUnencodedStream(OutputStream out) throws IOException {
		GZIPInputStream stream = new GZIPInputStream(new ByteArrayInputStream(
				buffer.toByteArray()));
		StreamWriter sw = new StreamWriter(out);
		sw.writeStream(stream);
		stream.close();
	}

	public void readFromStream(InputStream in, int ContentLength)
			throws IOException {
		StreamWriter sw = new StreamWriter(buffer);
		sw.writeStream(in, ContentLength);
	}

	public void writeToStream(OutputStream out) throws IOException {
		buffer.writeTo(out);
	}

}
