package net.kodeninja.http.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HTTPPacket<H extends HTTPHeader, B extends HTTPBody> {
	public static final DateFormat HTTPDateFormat = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss zzz");
	protected H header;
	protected B body;
	protected boolean allowHeaderUpdate;

	public HTTPPacket(H Header, B Body) {
		this.header = Header;
		this.body = Body;
		allowHeaderUpdate = true;
	}

	public H getHeader() {
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
		if (header != null) {
			if ((allowHeaderUpdate) && (body != null)) {
				header.setParameter("Date", getCurrentHTTPDate());
				header.setParameter("Content-Type", body.getMimeType()
						.toString());
				if (body.getContentLength() > 0) {
					header.setParameter("Content-Length", ""
							+ body.getContentLength());
					header.setParameter("Accept-Ranges", "bytes");
				}
			}
			if (header.getType() == HTTPHeader.HeaderType.RESPONSE) {
				HTTPResponseCode tmpRC = header.response;
				if ((tmpRC.getCode() / 100 == 1) || (tmpRC.getCode() == 204)
						|| (tmpRC.getCode() == 304))
					sendBody = false;
			}

			if ((body != null) && (sendBody) && (body.getContentLength() < 0))
				header.setParameter("connection", "close");

			header.writeToStream(out);

			if ((body != null) && (sendBody))
				body.writeToStream(out);
		}
	}

	public void readFromStream(InputStream in) throws InvalidHeaderException,
			IOException {
		int contentLength = -1;
		boolean hasBody = false;
		if (header != null) {
			header.readFromStream(in);
			if (header.getParameter("Content-Length") != null) {
				contentLength = Integer.parseInt(header
						.getParameter("Content-Length"));
				hasBody = true;
			} else if (header.getParameter("Transfer-Encoding") != null)
				hasBody = true;

			if (header.getType() == HTTPHeader.HeaderType.RESPONSE) {
				HTTPResponseCode tmpRC = header.response;
				if ((tmpRC.getCode() / 100 == 1) || (tmpRC.getCode() == 204)
						|| (tmpRC.getCode() == 304))
					hasBody = false;
			}

			if ((hasBody) && (body != null))
				body.readFromStream(in, contentLength);
		}
	}

}
