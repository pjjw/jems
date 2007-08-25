package net.kodeninja.http.packet.extra;

import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.util.MimeType;

public class HTTPChunkedPacket extends HTTPPacket<HTTPBody> {

	private class ChunkedOutputStream extends FilterOutputStream {

		public ChunkedOutputStream(OutputStream out) {
			super(out);
		}
		
		private void writeChunkAmount(int size) throws IOException {
			String line = Integer.toHexString(size) + "\r\n";
			out.write(line.getBytes());
		}
		
		public void write(int b) throws IOException {
			writeChunkAmount(1);
			out.write(b);
		}
		
		public void write(byte[] b, int off, int len) throws IOException {
			writeChunkAmount(len);
			out.write(b, off, len);
		}

	}

	private class HTTPChunkedBody implements HTTPBody {
		
		HTTPBody base;
		
		public HTTPChunkedBody(HTTPBody base) {
			this.base = base;
		}

		public boolean forceChunked() {
			return false;
		}

		public boolean forceCompression() {
			return false;
		}

		public long getContentLength() {
			return -1;
		}

		public MimeType getMimeType() {
			return base.getMimeType();
		}
		
		public String getContentType() {
			return base.getContentType();
		}

		public void readFromStream(InputStream in, int ContentLength)
				throws IOException {
			throw new IOException();
		}

		public void writeToStream(OutputStream out) throws IOException {
			BufferedOutputStream bos;
			base.writeToStream(bos = new BufferedOutputStream(new ChunkedOutputStream(out)));
			bos.flush();
			String last = "0\r\n\r\n";
			out.write(last.getBytes());
		}

	}

	public HTTPChunkedPacket(HTTPPacket<? extends HTTPBody> base) {
		super(base.getHeader());
		body = new HTTPChunkedBody(base.getBody());
		header.setParameter("Transfer-Encoding", "chunked");
		header.removeParameter("Content-Length");
	}

}
