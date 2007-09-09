package net.kodeninja.jem.server.content.mimetypes;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.MetadataType;

public class MPEG4File extends GenericFile {

	private static final byte[][] NON_LEAF_TAGS = {{'m','o','o','v'}};
	private static final byte[] MOVIE_HEADER_TAG = {'m', 'v', 'h', 'd'};
	private static final int MOVIE_HEADER_TAG_SZE = 102;
	private static final int TimeScale_INDEX = 3;
	private static final int Duration_INDEX = 4;
	
	@Override
	public void addMetadata(MediaItem item) {
		try {
			InputStream is = new BufferedInputStream(item.getA().toURL().openStream());

			while (parseAtom(item, is, -1) == false) ;//Parse all atoms until we find what we want
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		super.addMetadata(item);
	}
	
	protected boolean parseAtom(MediaItem item, InputStream is, int size) throws IOException {
		byte[] tagSizeBuf = new byte[4];
		byte[] tagBuf = new byte[4];
		
		IntBuffer tagSize = ByteBuffer.wrap(tagSizeBuf).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
		
		int bytesRead = 0;
		
		while ((size < 0) || (bytesRead < size)) {
			if ((is.read(tagSizeBuf) != 4) || (is.read(tagBuf) != 4))
				break;
			
			bytesRead += 8;
			
			if (arrayEqual(tagBuf, MOVIE_HEADER_TAG)) {
				byte[] buf = new byte[Math.min(tagSize.get(0) - 8, MOVIE_HEADER_TAG_SZE)];
				IntBuffer iBuf = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
				is.read(buf);
				
				int timescale = iBuf.get(TimeScale_INDEX);
				int duration = iBuf.get(Duration_INDEX);
				long length = duration * 1000 / timescale;
				
				JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Length, "" + length);
				
				return true;
			}
			
			boolean skip = true;
			for (byte[] tag: NON_LEAF_TAGS)
				if (arrayEqual(tagBuf, tag)) {
					skip = false;
					break;
				}
			
			if (skip)
				skipBytes(is, tagSize.get(0) - 8);
			else if (parseAtom(item, is, tagSize.get(0) - 8))
				return true;
			
			bytesRead += tagSize.get(0) - 8;
		}
		
		if (bytesRead == 0)
			return true;
		
		return false;
	}
	
}
