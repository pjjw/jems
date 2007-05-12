package net.kodeninja.DMAP.DataTypes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.io.StreamWriter;

/**
 * Base class for DMAP binary tags.
 * @author Charles Ikeson
 */
abstract public class DMAPBinaryParameter extends DMAPParameter {

	/**
	 * Creates binary data type parameter with the specified long name.
	 * @param name The long name of the tag.
	 */
	public DMAPBinaryParameter(String name) {
		super(DATATYPE_BINARY, name);
	}

	@Override
	public void readDataFromStream(InputStream in, int ContentLength)
			throws IOException {
		OutputStream out = getRecieveStream();
		if (out != null) {
			out = new BufferedOutputStream(out);
			StreamWriter sw = new StreamWriter(out);
			if (ContentLength >= 0)
				sw.writeStream(in, ContentLength);
		} else
			in.skip(ContentLength);
	}

	@Override
	protected void writeDataToBuffer(OutputStream out) throws IOException {
		InputStream in = getSendStream();
		if (in != null) {
			in = new BufferedInputStream(in);
			StreamWriter sw = new StreamWriter(out);
			sw.writeStream(in, dataLength());
		}
	}

	abstract protected OutputStream getRecieveStream() throws IOException;

	abstract protected InputStream getSendStream() throws IOException;

}
