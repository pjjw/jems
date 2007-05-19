package net.kodeninja.http.packet.extra;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.io.RangeOutputStream;
import net.kodeninja.util.MimeType;

public class HTTPRangeBody implements HTTPBody {

	private class LongPair {
		long begin, end;

		LongPair(long b, long e) {
			begin = b;
			end = e;
		}
	}

	protected HTTPBody body;
	protected long size = 0;
	protected LongPair[] dataRanges;

	public HTTPRangeBody(HTTPBody b, String range) {
		body = b;
		int pos = range.indexOf("=");

		String type = range.substring(0, pos).toLowerCase();
		int mod = 1;

		if (type.equals("bytes"))
			mod = 1;

		range = range.substring(pos + 1);
		String[] ranges = range.split("\\,");
		dataRanges = new LongPair[ranges.length];

		for (int i = 0; i < ranges.length; i++) {
			String begin, end;
			pos = ranges[i].indexOf("-");
			if (pos == 0)
				begin = "";
			else
				begin = ranges[i].substring(0, pos);

			if (pos == ranges[i].length() - 1)
				end = "";
			else
				end = ranges[i].substring(pos + 1);

			if (begin == "")
				dataRanges[i] = new LongPair(body.getContentLength()
						- (mod * Long.parseLong(end)),
						body.getContentLength() - 1);
			else if (end == "")
				dataRanges[i] = new LongPair(mod * Long.parseLong(begin), body
						.getContentLength() - 1);
			else
				dataRanges[i] = new LongPair(mod * Integer.parseInt(begin), mod
						* Integer.parseInt(end));

			size += dataRanges[i].end - dataRanges[i].begin;
		}
	}

	public long getContentLength() {
		// TODO Auto-generated method stub
		return size;
	}

	public MimeType getMimeType() {
		// TODO Auto-generated method stub
		return body.getMimeType();
	}

	public void readFromStream(InputStream in, int ContentLength)
			throws IOException {
		throw new IOException("Operation not supported");
	}

	public void writeToStream(OutputStream out) throws IOException {
		for (LongPair element : dataRanges)
			body.writeToStream(new RangeOutputStream(out, element.begin,
					element.end));
	}

	public String getContentRangeString() {
		String retVal = "";
		for (LongPair element : dataRanges) {
			if (retVal.length() > 0)
				retVal += ",";
			retVal += element.begin + "-" + element.end + "/"
					+ body.getContentLength();
		}

		return "bytes " + retVal;
	}

}
