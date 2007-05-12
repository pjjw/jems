package net.kodeninja.http.packet.extra;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.http.packet.HTTPBody;

public interface HTTPEncodedBody extends HTTPBody {

	public void readFromUnencodedStream(InputStream in, int ContentLength)
			throws IOException;

	public void writeToUnencodedStream(OutputStream out) throws IOException;

}
