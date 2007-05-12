package net.kodeninja.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Simple reader to read lines from a passed input stream.
 * @author Charles Ikeson
 *
 */
public final class StreamLineReader {
	/**
	 * The input stream to read from.
	 */
	private InputStream inStream;
	
	/**
	 * The last byte that was read in.
	 */
	int lastByte = -1;

	/**
	 * Creates a line reader pointing at the passed input stream.
	 * @param in The input stream to read from.
	 */
	public StreamLineReader(InputStream in) {
		inStream = in;
	}

	/**
	 * Reads in a single line.
	 * @return The line that was read.
	 * @throws IOException Thrown on EOF or re-thrown during a read.
	 */
	public String readLine() throws IOException {
		String retVal = "";

		int curByte = inStream.read();

		if (curByte < 0)
			throw new IOException("EOF Reached.");

		if (((curByte == 13) || (curByte == 10)) && (curByte != lastByte))
			curByte = inStream.read();

		while ((curByte != 13) && (curByte != 10)) {
			if (curByte < 0)
				return retVal;
			else if (curByte > 31)
				retVal += (char) curByte;

			lastByte = curByte;
			curByte = inStream.read();
		}

		return retVal;
	}

}
