package net.kodeninja.http.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class HTTPPacket<B extends HTTPBody> {
	public static final DateFormat HTTPDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
	static {
		HTTPDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	protected HTTPHeader header;
	protected B body;
	public boolean allowHeaderUpdate;
	protected InputStream bodyStream = null;
	protected int bodyStreamLen;

	public HTTPPacket(HTTPHeader Header) {
		this.header = Header;
		this.body = null;
		allowHeaderUpdate = true;
	}

	public HTTPPacket(HTTPHeader Header, B Body) {
		this.header = Header;
		this.body = Body;
		allowHeaderUpdate = true;
	}

	public HTTPHeader getHeader() {
		return header;
	}

	public B getBody() {
		return body;
	}

	public static String getCurrentHTTPDate() {
		return HTTPDateFormat.format(Calendar.getInstance().getTime());
	}

	public void writeToStream(OutputStream out) throws IOException {
		boolean sendBody = true;

		if (header.getType() == HTTPHeader.HeaderType.UNDETERMINED)
			throw new IOException("Unable to send HTTP packet with undetermined type.");

		boolean chunked = ((header.getParameter("Transfer-Encoding") != null) && (header.getParameter("Transfer-Encoding").equalsIgnoreCase("chunked")));

		if (header != null) {
			if (header.getType() == HTTPHeader.HeaderType.RESPONSE) {
				HTTPResponseCode tmpRC = header.response;
				if ((tmpRC.getCode() / 100 == 1) || (tmpRC.getCode() == 204) || (tmpRC.getCode() == 304))
					sendBody = false;
			}
			
			if (allowHeaderUpdate) {
				header.setParameter("Date", getCurrentHTTPDate());
				if (body != null) {
					header.setParameter("Content-Type", body.getContentType());
					if ((chunked == false) && (body.getContentLength() > 0))
						header.setParameter("Content-Length", "" + body.getContentLength());
					if ((chunked == false) && (sendBody) && (body.getContentLength() < 0))
						header.setParameter("Connection", "close");
				}
			}
			
			header.writeToStream(out);

			if ((body != null) && (sendBody))
				body.writeToStream(out);
		}
	}

	public void readFromStream(InputStream in) throws InvalidHeaderException, IOException {
		int contentLength = -1;
		boolean hasBody = false;
		if (header != null) {
			header.readFromStream(in);
			if (header.getParameter("Content-Length") != null) {
				contentLength = Integer.parseInt(header.getParameter("Content-Length"));
				hasBody = true;
			} else if (header.getParameter("Transfer-Encoding") != null)
				hasBody = true;

			if (header.getType() == HTTPHeader.HeaderType.RESPONSE) {
				HTTPResponseCode tmpRC = header.response;
				if ((tmpRC.getCode() / 100 == 1) || (tmpRC.getCode() == 204)
						|| (tmpRC.getCode() == 304))
					hasBody = false;
			}

			try {
				if (hasBody)
					if (body != null)
						body.readFromStream(in, contentLength);
					else {
						bodyStream = in;
						bodyStreamLen = contentLength;
					}

			}
			catch (IOException e) {
			}
		}
	}

	@SuppressWarnings("unchecked")
	public boolean readBody(HTTPBody body) {
		if (this.body != null)
			return true;

		if (bodyStream == null)
			return false;

		this.body = (B)body;
		try {
			body.readFromStream(bodyStream, bodyStreamLen);
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
