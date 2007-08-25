package net.kodeninja.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LimitedSizeInputStream extends FilterInputStream {
	private int bytesLeft;
	private boolean stopClose;
	
	public LimitedSizeInputStream(InputStream in, int limit) {
		this(in, limit, false);
	}
	
	public LimitedSizeInputStream(InputStream in, int limit, boolean stopClose) {
		super(in);
		bytesLeft = limit;
		this.stopClose = stopClose;
	}
	
	public int getBytesLeft() {
		return bytesLeft;
	}
	
	public int read() throws IOException {
		if (bytesLeft > 0) {
			bytesLeft--;
			return in.read();
		}
		else
			return -1;
	}
	
	public int read(byte b[], int off, int len) throws IOException {
		if (bytesLeft > 0) {
			int bytesRead;
			if (bytesLeft < len)
				bytesRead = in.read(b, off, bytesLeft);
			else
				bytesRead = in.read(b, off, len);
			
			if (bytesRead == -1)
				return -1;
			
			bytesLeft = bytesLeft - bytesRead;
			return bytesRead;
		}
		else
			return -1;
		
	}
	
	public void close() throws IOException {
		if (stopClose == false)
			in.close();
	}

}
