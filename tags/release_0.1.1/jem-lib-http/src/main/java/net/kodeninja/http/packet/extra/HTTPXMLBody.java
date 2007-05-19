package net.kodeninja.http.packet.extra;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.dom.Document;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.util.MimeType;

public class HTTPXMLBody implements HTTPBody {
	private int contentLen = 0;
	public static final MimeType XML_MIMETYPE = new MimeType("text", "xml");

	public HTTPXMLBody() {
		throw new RuntimeException("Opps! Not implemented");
	}

	public HTTPXMLBody(Document d) {
		throw new RuntimeException("Opps! Not implemented");
	}

	public long getContentLength() {
		return contentLen;
	}

	public MimeType getMimeType() {
		return XML_MIMETYPE;
	}

	public void readFromStream(InputStream in, int ContentLength)
			throws IOException {
		// TODO Auto-generated method stub

	}

	public void writeToStream(OutputStream out) throws IOException {

	}

}
