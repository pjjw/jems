package net.kodeninja.http.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.io.StreamLineReader;
import net.kodeninja.util.MimeType;

public class HTTPTextBody implements HTTPBody {
	protected String text;
	protected MimeType mime;

	public HTTPTextBody() {
		text = "";
		mime = new MimeType("text", "plain");
	}

	public HTTPTextBody(String Text) {
		text = Text;
		mime = new MimeType("text", "plain");
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
