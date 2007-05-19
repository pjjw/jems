package net.kodeninja.http.packet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.io.StreamWriter;
import net.kodeninja.util.MimeType;
import net.kodeninja.util.MimeTypeFactory;

public class HTTPFileBody implements HTTPBody {
	protected File file;
	protected MimeType mime = MimeType.WILDCARD;

	public HTTPFileBody(File f, MimeTypeFactory TypeFactory) {
		file = f;
		if (TypeFactory != null)
			mime = TypeFactory.getMimeType(file);
	}

	public HTTPFileBody(File f, MimeType m) {
		file = f;
		mime = m;
	}

	public long getContentLength() {
		return file.length();
	}

	public MimeType getMimeType() {
		return mime;
	}

	public void readFromStream(InputStream in, int ContentLength)
			throws IOException {
		StreamWriter sw = new StreamWriter(new FileOutputStream(file));
		if (ContentLength >= 0)
			sw.writeStream(in, ContentLength);
		else
			sw.writeStream(in);
	}

	public void writeToStream(OutputStream out) throws IOException {
		StreamWriter sw = new StreamWriter(out);
		sw.writeStream(new FileInputStream(file));
	}

}
