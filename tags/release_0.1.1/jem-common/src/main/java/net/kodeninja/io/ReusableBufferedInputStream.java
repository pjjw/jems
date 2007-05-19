package net.kodeninja.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simple class override to allow the input stream source of
 * a BufferedInputStream to be changed. 
 * @author Charles Ikeson
 *
 */
public class ReusableBufferedInputStream extends BufferedInputStream {

	/**
	 * Creates a BufferedInputStream pointing to nothing at the
	 * default buffer size.
	 */
	public ReusableBufferedInputStream() {
		super(null);
	}

	/**
	 * Creates a BufferedInputStream pointing to nothing at the passed
	 * buffer size.
	 * @param size The size of the buffer.
	 */
	public ReusableBufferedInputStream(int size) {
		super(null, size);
	}

	/**
	 * Creates a BufferedInputStream pointing to passed input stream at the 
	 * default buffer size.
	 * @param in The InputStream to point at.
	 */
	public ReusableBufferedInputStream(InputStream in) {
		super(in);
	}

	/**
	 * Creates a BufferedInputStream pointing to the passed input stream at the
	 * passed buffer size.
	 * @param in The InputStream to point at.
	 * @param size The size of the buffer.
	 */
	public ReusableBufferedInputStream(InputStream in, int size) {
		super(in, size);
	}

	/**
	 * Switches the input stream to a new one and resets its state.
	 * @param in The input stream to switch to.
	 * @throws IOException Thrown if a problem occurs during the switch.
	 */
	public void switchStream(InputStream in) throws IOException {
		markpos = -1;
		pos = 0;
		count = 0;
		this.in = in;
	}

	/**
	 * Returns the size of the buffer.
	 * @return The size of the buffer.
	 */
	public int getBufferSize() {
		return buf.length;
	}

}
