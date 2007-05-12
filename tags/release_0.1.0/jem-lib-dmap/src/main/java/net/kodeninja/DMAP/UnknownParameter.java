package net.kodeninja.DMAP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.DMAP.DataTypes.DMAPParameter;

/**
 * Handler for when an unknown tag is encountered while parsing a stream.
 * @author Charles Ikeson
 *
 */
public class UnknownParameter extends DMAPParameter {

	/**
	 * Constructor.
	 */
	public UnknownParameter() {
		super(DATATYPE_UNKNOWN, "");
	}

	/**
	 * Returns the length of the tag.
	 * @return The tag's length.
	 */
	@Override
	public int dataLength() {
		return -1;
	}

	/**
	 * Throws an exception, since an unknown tag cannot be written back to a stream.
	 * @param out The stream to write to.
	 * @exception IOException Thrown by default.
	 */
	@Override
	public void writeToStream(OutputStream out) throws IOException {
		throw new IOException("Cannot write unknown parameter");
	}

	/**
	 * Skips over the tag's reported length to continue reading other tags.
	 * @param in The stream to read from.
	 * @param Length The amount to read from the stream.
	 * @exception IOException Thrown if there was a problem skipping. 
	 */
	@Override
	public void readDataFromStream(InputStream in, int Length)
			throws IOException {
		in.skip(Length);
	}

	/**
	 * Throws an exception since the unknown tag has no stored data.
	 * @param out The stream to write to.
	 * @exception IOException Thrown by default since the tag cannot be written.
	 */
	@Override
	protected void writeDataToBuffer(OutputStream out) throws IOException {
		throw new IOException("Cannot write unknown parameter");
	}

	/**
	 * Returns a string representation of the tag.
	 * @return String representation stating its an unknown tag, followed by its code.
	 */
	@Override
	public String toString() {
		return "Unknown Parameter (" + getTag() + ")";
	}

}
