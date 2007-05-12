package net.kodeninja.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Special output stream for grabbing only part of a passed stream.
 * @author Charles Ikeson
 */
public class RangeOutputStream extends FilterOutputStream {

	/**
	 * The starting index.
	 */
	protected long s;
	/**
	 * The ending index.
	 */
	protected long e;
	/**
	 * The current position.
	 */
	protected long pos;

	/**
	 * Creates the stream with the passed start and end. 
	 * @param out The stream to write to.
	 * @param start The starting position.
	 * @param end The ending position.
	 */
	public RangeOutputStream(OutputStream out, long start, long end) {
		super(out);
		s = start;
		e = end;
		pos = 0;
	}

	/**
	 * Writes the byte IF it is within the range, otherwise it only
	 * increments the position.
	 * @param b The byte to write.
	 * @exception IOException Thrown if there was a problem writing to the stream.
	 */
	@Override
	public void write(int b) throws IOException {
		if ((pos >= s) && (pos <= e))
			super.write(b);
		pos++;
	}

	/**
	 * Writes the bytes IF it is within the range, otherwise it only
	 * increments the position.
	 * @param b The bytes to write.
	 * @param off The offset to start at.
	 * @param len The length to write.
	 * @exception IOException Thrown if there was a problem writing to the stream.
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		boolean allowWrite = false;
		long newPos = pos + off, newOff = off, newLen = len;

		if (newPos <= e)
			// Check to see if we are in the range
			if (newPos >= s) {
				// We are so check to make sure we don't leave it
				if (newPos + newLen > e) // Make
					newLen = e - newPos;

				// Enable writing
				allowWrite = true;
			}
			// We aren't yet in the range, but if see if the proposed write
			// would place us there
			else if (newPos + newLen >= s) {
				// It would so, update the offset
				newOff += s - newPos;

				// New offset means, a new position, so update that too
				newPos = newOff + pos;
				newLen = len + (pos - newPos);

				// Make sure we don't go past the range
				if (newPos + newLen > e)
					newLen = e - newPos;

				// Enable writting
				allowWrite = true;
			}

		// If we have enabled writting, do the write!
		if (allowWrite)
			out.write(b, (int) newOff, (int) newLen);

		// Move the cursor along
		pos += off + len;
	}

}
