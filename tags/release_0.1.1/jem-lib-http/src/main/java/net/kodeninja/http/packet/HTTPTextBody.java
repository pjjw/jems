package net.kodeninja.http.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.io.StreamLineReader;
import net.kodeninja.util.MimeType;

public class HTTPTextBody implements HTTPBody {
	public static MimeType HTTP_MIMETYPE = new MimeType("text", "html");
	public static MimeType PLAIN_MIMETYPE = new MimeType("text", "plain");
	protected String text;
	protected MimeType mime;

	public HTTPTextBody() {
		this(PLAIN_MIMETYPE);
	}
	
	public HTTPTextBody(MimeType mimeType) {
		text = "";
		mime = mimeType;
	}

	public HTTPTextBody(String Text) {
		this(Text, PLAIN_MIMETYPE);
	}

	public HTTPTextBody(String Text, MimeType mimeType) {
		text = Text;
		mime = mimeType;
	}

	public void readFromStream(InputStream in, int ContentLength)
			throws IOException {
		StreamLineReader br = new StreamLineReader(in);

		while (true) {
			String line = br.readLine();
			if (line == null)
				break;

			text += line;

			if ((ContentLength >= 0) && (line.length() >= ContentLength))
				break;
		}
	}

	public void writeToStream(OutputStream out) throws IOException {
		out.write(text.getBytes());
	}

	public long getContentLength() {
		return text.length();
	}

	public MimeType getMimeType() {
		return mime;
	}

}
