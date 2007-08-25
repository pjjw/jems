package net.kodeninja.http.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.io.StreamLineReader;
import net.kodeninja.util.MimeType;

public class HTTPTextBody implements HTTPBody {
	public static MimeType HTML_MIMETYPE = new MimeType("text", "html");
	public static MimeType PLAIN_MIMETYPE = new MimeType("text", "plain");
	protected StringBuffer text;
	protected MimeType mime;

	public HTTPTextBody() {
		this(PLAIN_MIMETYPE);
	}
	
	public HTTPTextBody(MimeType mimeType) {
		this(new StringBuffer(), mimeType);
	}

	public HTTPTextBody(String Text) {
		this(Text, PLAIN_MIMETYPE);
	}

	public HTTPTextBody(String Text, MimeType mimeType) {
		this(new StringBuffer(Text), mimeType);
	}
	
	public HTTPTextBody(StringBuffer textBuffer) {
		this(textBuffer, PLAIN_MIMETYPE);
	}

	public HTTPTextBody(StringBuffer textBuffer, MimeType mimeType) {
		text = textBuffer;
		mime = mimeType;
	}

	public void readFromStream(InputStream in, int ContentLength)
			throws IOException {
		text = new StringBuffer();
		StreamLineReader br = new StreamLineReader(in);

		int amountRead = 0;
		
		while (true) {
			try {
			String line;
			if (ContentLength >= 0)
				line = br.readLine(ContentLength - amountRead);
			else
				line = br.readLine();
			
			amountRead += br.lastAmountRead();
			
			if (line == null)
				break;

			text.append(line);
			

			if ((ContentLength >= 0) && (amountRead > ContentLength))
				break;
			}
			catch (IOException e) {
				break;
			}
		}
	}

	public void writeToStream(OutputStream out) throws IOException {
		out.write(text.toString().getBytes());
	}

	public long getContentLength() {
		return text.length();
	}

	public MimeType getMimeType() {
		return mime;
	}
	
	public String getContentType() {
		return getMimeType().toString();
	}
	
	public boolean forceCompression() {
		return true;
	}
	
	public String toString() {
		return text.toString();
	}
	
	public boolean forceChunked() {
		return false;
	}
	
}
