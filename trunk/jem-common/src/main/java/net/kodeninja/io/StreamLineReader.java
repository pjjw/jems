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
	
	/*
	/**
	 * The last byte that was read in.
	 * /
	private int lastByte = -1;
	*/
	
	/**
	 * This is the amount of bytes that were read in during the last read.
	 */
	private int bytesRead = 0;

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
		return readLine(-1);
	}
	
	/**
	 * Reads in a single line.
	 * @param The maximum amount of bytes to read.
	 * @return The line that was read.
	 * @throws IOException Thrown on EOF or re-thrown during a read.
	 */
	/*
	public String readLine(int maxBytes) throws IOException {
		String retVal = "";

		int curByte = inStream.read();

		if (curByte < 0)
			throw new IOException("EOF Reached.");
		
		bytesRead = 1;

		if (((curByte == 13) || (curByte == 10)) && (curByte != lastByte))
			curByte = inStream.read();

		while ((curByte != 13) && (curByte != 10)) {
			if (curByte < 0)
				return retVal;
			else if (curByte > 31)
				retVal += (char) curByte;
			
			bytesRead++;
			
			if ((maxBytes >= 0) && (maxBytes <= bytesRead))
				break;
			
			lastByte = curByte;
			curByte = inStream.read();
		}

		return retVal;
	}
	*/
	
	public String readLine(int maxBytes) throws IOException {
		String result = "";
		int cByte = inStream.read();
		
		if (cByte < 0)
			throw new IOException("EOF Reached.");
		
		while ((cByte != 10) && (cByte != 13) && (cByte >= 0)) {
			result += (char)cByte;
			cByte = inStream.read();
		}
		
		if (inStream.markSupported() && (cByte >= 0)) {
			inStream.mark(1);
			int nextByte = inStream.read();
			if ((nextByte == cByte) || ((nextByte != 10) && (nextByte != 13)))
				inStream.reset();
		}
		
		return result;
	}
	
	public int lastAmountRead() {
		return bytesRead;
	}

}
