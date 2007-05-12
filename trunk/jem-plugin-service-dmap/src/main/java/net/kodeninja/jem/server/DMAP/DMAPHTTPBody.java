package net.kodeninja.jem.server.DMAP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import net.kodeninja.DMAP.ParameterFactory;
import net.kodeninja.DMAP.ParameterLinkedList;
import net.kodeninja.DMAP.DataTypes.DMAPParameter;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.util.MimeType;

public class DMAPHTTPBody extends ParameterLinkedList implements HTTPBody {
	public static MimeType MIME_TYPE = new MimeType("application",
			"x-dmap-tagged");

	public long getContentLength() {
		Iterator<DMAPParameter> it = getParameters();
		int totalSize = 0;
		while (it.hasNext())
			totalSize += it.next().length();

		return totalSize;
	}

	public MimeType getMimeType() {
		return MIME_TYPE;
	}

	public void readFromStream(InputStream in, int ContentLength)
			throws IOException {
		ParameterFactory.readFromStream(this, in, ContentLength);
	}

	public void writeToStream(OutputStream out) throws IOException {
		Iterator<DMAPParameter> it = getParameters();
		while (it.hasNext())
			it.next().writeToStream(out);
	}

}
