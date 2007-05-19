package net.kodeninja.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Acts as a bridge between an input stream and output stream,
 * reading from one, and writing to the other.
 * @author Charles Ikeson
 */
public class StreamWriter {
	/**
	 * The output stream.
	 */
	protected OutputStream outStream;
	
	/**
	 * The maximum buffer size.
	 */
	public final int MAX_BUFFER = 1024 * 8;

	/**
	 * Creates one using the passed output stream.
	 * @param out The output stream to use.
	 */
	public StreamWriter(OutputStream out) {
		outStream = new BufferedOutputStream(out);
	}

	/**
	 * Writes the entire stream.
	 * @param in The stream to read from.
	 * @throws IOException Thrown if an error occurred during the writing.
	 */
	public void writeStream(InputStream in) throws IOException {
		while (in.available() > 0)
			writeAmountToStream(in, 0, in.available());
		outStream.flush();
	}

	/**
	 * Writes the passed length of the stream.
	 * @param in The stream to read from.
	 * @param Length The length to read.
	 * @throws IOException Thrown if an error occurred during the writing.
	 */
	public void writeStream(InputStream in, long Length) throws IOException {
		writeAmountToStream(in, 0, Length);
		outStream.flush();
	}

	/**
	 * Writes a range to the stream.
	 * @param in The stream to read from.
	 * @param start The index to start at.
	 * @param end The index to end at.
	 * @throws IOException Thrown if an error occurred during the writing.
	 */
	public void writeRangeToStream(InputStream in, long start, long end)
			throws IOException {
		writeAmountToStream(in, start, end - start);
		outStream.flush();
	}

	/**
	 * Writes the entire stream to the stream.
	 * @param in The stream to read from.
	 * @throws IOException Thrown if an error occurred during the writing.
	 */
	public void writeEntireStream(InputStream in) throws IOException {
		byte buffer[] = new byte[2048];
		int length;
		int amountWritten = 0;
		while ((length = in.read(buffer)) > -1) {
			amountWritten += length;
			outStream.write(buffer, 0, length);
			outStream.flush();
		}

	}

	/**
	 * Writes the passed length of the stream, starting at the passed offset..
	 * @param in The stream to read from.
	 * @param offset The offset to start from.
	 * @param Length The length to read.
	 * @throws IOException Thrown if an error occurred during the writing.
	 */
	protected void writeAmountToStream(InputStream in, long offset, long Length)
			throws IOException {
		int bufferSize;
		long bytesLeft = Length;
		if (Length > MAX_BUFFER)
			bufferSize = MAX_BUFFER;
		else
			bufferSize = (int) Length;

		byte Buffer[] = new byte[bufferSize];
		in.skip(offset);

		while (bytesLeft > 0) {
			in.read(Buffer, 0, (int) Math.min(bytesLeft, bufferSize));
			outStream.write(Buffer, 0, (int) Math.min(bytesLeft, bufferSize));
			bytesLeft -= (int) Math.min(bytesLeft, bufferSize);
		}
	}
}
