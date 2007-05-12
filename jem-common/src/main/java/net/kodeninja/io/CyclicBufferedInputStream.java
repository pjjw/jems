package net.kodeninja.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A thread-safe buffer stream class to allow the reading and
 * writing of a buffer in a cyclic nature. 
 * @author Charles Ikeson
 */
public class CyclicBufferedInputStream extends FilterInputStream {
	/**
	 * The default size of the buffer.
	 */
	public final static int DEFAULT_BUFFER_SIZE = 8192;
	
	/**
	 * The buffer itself.
	 */
	protected volatile byte[] buf;
	
	/**
	 * Current read position in the buffer.
	 */
	protected volatile int bufReadPos;
	
	/**
	 * Current write position in the buffer.
	 */
	protected volatile int bufWritePos;
	
	/**
	 * Current marked position in the buffer.
	 */
	protected volatile int bufMarkedPos;
	
	/**
	 * Current marked size of the buffer.
	 */
	protected volatile int bufMarkedSize;

	/**
	 * Creates a buffered input stream with the default size.
	 */
	public CyclicBufferedInputStream() {
		this(null, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * Creates a buffered input stream based on the passed size.
	 * @param bufSize
	 */
	public CyclicBufferedInputStream(int bufSize) {
		this(null, bufSize);
	}

	/**
	 * Creates a buffered input stream pointing at the passed input stream
	 * and based on the default size.
	 * @param in The stream to read from.
	 */
	public CyclicBufferedInputStream(InputStream in) {
		this(in, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 
	 * @param in
	 * @param bufSize
	 */
	public CyclicBufferedInputStream(InputStream in, int bufSize) {
		super(in);
		buf = new byte[bufSize];
		bufReadPos = 0;
		bufWritePos = 0;
		bufMarkedPos = -1;
		bufMarkedSize = 0;
	}

	/**
	 * Function to switch the input stream to the passed stream.
	 * @param in The stream to pass to.
	 * @throws IOException Thrown if unable to close previous stream.
	 */
	public void switchStream(InputStream in) throws IOException {
		close();

		bufReadPos = 0;
		bufWritePos = 0;
		bufMarkedPos = -1;
		bufMarkedSize = 0;
		this.in = in;
	}

	/**
	 * Returns the current buffer size.
	 * @return Buffer size.
	 */
	public int getBufferSize() {
		return buf.length;
	}

	/**
	 * Returns the distance from the first index to the second,
	 * taking into account looping at the bounds..
	 * @param from First index to measure from.
	 * @param to Second index to measure to.
	 * @return The distance between the two indicies.
	 */
	protected int getDistance(int from, int to) {
		if (to < from)
			return (buf.length - from) + to;
		else
			return to - from;
	}

	/**
	 * Returns the maximum amount that can be read before blocking.
	 * @return The amount that can be read before blocking.
	 */
	protected int getMaxReadSize() {
		return getDistance(bufReadPos, bufWritePos);
	}

	/**
	 * Returns how for back one can jump.
	 * @return The remaining marked area, or -1 if none exists.
	 */
	protected int getMarkedAreaLeft() {
		if (bufMarkedPos < 0)
			return -1;
		return getDistance(bufReadPos, bufMarkedPos + bufMarkedSize);
	}

	/**
	 * Returns the maximum amount that can be written before blocking.
	 * @return The amount that can be written.
	 */
	protected int getMaxWriteSize() {
		int bufReadPosCopy = bufReadPos, bufWritePosCopy = bufWritePos, bufMarkedPosCopy = bufMarkedPos;
		int maxWritePos;

		if (bufMarkedPosCopy != -1)
			maxWritePos = bufMarkedPosCopy;
		else
			maxWritePos = bufReadPosCopy;

		if (maxWritePos == 0)
			maxWritePos = buf.length - 1;
		else
			maxWritePos--;

		return getDistance(bufWritePosCopy, maxWritePos);
	}

	/**
	 * Fills the buffer as much as it can before blocking.
	 * @return The success of the fill.
	 * @throws IOException Thrown during IO operations on the input stream.
	 */
	protected boolean fillBuffer() throws IOException {
		while (true) {
			int bufToRead = getMaxWriteSize();

			if (bufToRead <= 0)
				return true;

			if (bufToRead + bufWritePos > buf.length)
				bufToRead = buf.length - bufWritePos;

			if (in.available() <= 0)
				return false;

			bufToRead = Math.min(bufToRead, in.available());

			if ((bufToRead = in.read(buf, bufWritePos, bufToRead)) == -1)
				return false;

			bufWritePos += bufToRead;
			if (bufWritePos >= buf.length)
				bufWritePos -= buf.length;
		}
	}

	/**
	 * Reads a single byte from the buffer.
	 * @return The value of the byte, or -1 if no more buffer exists.
	 */
	@Override
	public int read() throws IOException {
		byte[] tmpBuf = new byte[1];
		if (read(tmpBuf) == -1)
			return -1;

		return tmpBuf[0] & 0xFF;
	}

	/**
	 * Reads in bytes to the passed array starting at the passed offset
	 * for the passed length in bytes.
	 * @param b The array to read into.
	 * @param off The offset to start at.
	 * @param len The amount to read.
	 * @return The actual amount read.
	 */
	@Override
	public synchronized int read(byte[] b, int off, int len) throws IOException {
		int totalAmountRead = 0;

		if ((bufMarkedPos > -1) && (getMarkedAreaLeft() < len))
			bufMarkedPos = -1;

		while (len > 0) {
			if (getMaxReadSize() < len - totalAmountRead)
				if (fillBuffer() == false)
					if (totalAmountRead == 0)
						return -1;
					else
						return totalAmountRead;

			int amountRead;
			synchronized (this) {
				amountRead = Math.min(getDistance(bufReadPos, buf.length), Math
						.min(getMaxReadSize(), len));
			}

			if (amountRead == 0)
				continue;

			System.arraycopy(buf, bufReadPos, b, off, amountRead);
			synchronized (this) {
				bufReadPos += amountRead;
				if (bufReadPos == buf.length)
					bufReadPos = 0;
			}

			len -= amountRead;
			totalAmountRead += amountRead;
		}
		return totalAmountRead;
	}

	/**
	 * Skips over the amount of bytes passed.
	 * @param n The amount to skip over.
	 * @return The actual amount skipped.
	 */
	@Override
	public long skip(long n) throws IOException {

		if (n < buf.length) { // If we wish to skip into current read buffer,
			// just increment the read position
			// if we are skipping past the marked area, invalidate the marked
			// position
			if (getMarkedAreaLeft() < n)
				bufMarkedPos = -1;

			if (fillBuffer() == false)
				return -1;

			bufReadPos += n;
			if (bufReadPos >= buf.length)
				bufReadPos -= buf.length;

		} else {
			// Re-calc distance to skip
			in.skip(n - getDistance(bufReadPos, bufWritePos));

			// Reset counters
			bufReadPos = bufWritePos = 0;
			bufMarkedPos = -1;
		}
		if (fillBuffer() == false)
			return -1;
		return n;
	}

	/**
	 * Returns the amount that can be read from the buffer.
	 * @return The amount that could be read
	 */
	@Override
	public int available() throws IOException {
		return getMaxReadSize();
	}

	/**
	 * Closes the stream.
	 * @exception IOException Occurs if the was a problem closing
	 * the base stream.
	 */
	@Override
	public void close() throws IOException {
		if (in != null)
			super.close();
	}

	/**
	 * Marks the amount passed.
	 * @param readlimit The amount to mark
	 */
	@Override
	public void mark(int readlimit) {

		if (readlimit >= 0) {
			bufMarkedPos = bufReadPos;
			bufMarkedSize = Math.min(buf.length, readlimit);
		} else {
			bufMarkedPos = -1;
			bufMarkedSize = 0;
		}

	}

	/**
	 * Resets the read pointer to the beginning of the remaining marked area.
	 * @exception IOException Thrown if the marked position was not valid.
	 */
	@Override
	public void reset() throws IOException {
		if (bufMarkedPos == -1)
			throw new IOException("Marked position not valid.");
		bufReadPos = bufMarkedPos;
	}

	/**
	 * Returns true to signify that marking is supported.
	 * @return True.
	 */
	@Override
	public boolean markSupported() {
		return true;
	}

}
