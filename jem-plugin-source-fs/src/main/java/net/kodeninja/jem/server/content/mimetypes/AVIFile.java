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

public class AVIFile extends GenericFile {

	private static final byte[] RIFF_HEADER = {'R', 'I', 'F', 'F'};
	private static final byte[] RIFF_AVI_FILETYPE = {'A', 'V', 'I', ' '};
	private static final byte[] RIFF_LIST_TAG = {'L', 'I', 'S', 'T'};
	private static final byte[] RIFF_FORMAT_HEADER = {'h', 'd', 'r', 'l'};
	private static final byte[] RIFF_AVI_HEADER = {'a', 'v', 'i', 'h'};

	private static final int dwMicroSecPerFrame = 0;
//	private static final int dwMaxBytesPerSec = 1;
//	private static final int dwPaddingGranularity = 2;
//	private static final int dwFlags = 3;
	private static final int dwTotalFrames = 4;
//	private static final int dwInitialFrames = 5;
//	private static final int dwStreams = 6;
//	private static final int dwSuggestedBufferSize = 7;
	private static final int dwWidth = 8;
	private static final int dwHeight = 9;

	@Override
	public void addMetadata(MediaItem item) {
		try {
			InputStream is = new BufferedInputStream(item.getA().toURL().openStream());
			byte[] tagBuf = new byte[4];
			byte[] buf = new byte[100];
			IntBuffer iBuf = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();

			if ((is.read(tagBuf) == 4) && (arrayEqual(tagBuf, RIFF_HEADER))) {
				is.read(buf, 0, 4);
				int fSize = iBuf.get(0);

				if (((is.read(tagBuf) == 4) && (arrayEqual(tagBuf, RIFF_AVI_FILETYPE))) &&				
						((is.read(tagBuf) == 4) && (arrayEqual(tagBuf, RIFF_LIST_TAG))) &&
						((is.read(buf, 0, 4) == 4)) &&
						((is.read(tagBuf) == 4) && (arrayEqual(tagBuf, RIFF_FORMAT_HEADER)))) {
					int listSize = iBuf.get(0);
					int bytesRead = 4;
					while (is.read(tagBuf) == 4) {
						bytesRead += 4;

						if (arrayEqual(tagBuf, RIFF_AVI_HEADER)) {
							is.read(buf, 0, 4);
							int toRead = Math.min(iBuf.get(0), buf.length);
							if (is.read(buf, 0, toRead) == toRead) {
								int timePerFrame = iBuf.get(dwMicroSecPerFrame) / 1000;
								long frames = iBuf.get(dwTotalFrames);

								long length = timePerFrame * frames;
								int width = iBuf.get(dwWidth);
								int height = iBuf.get(dwHeight);

								if (timePerFrame > 0) {
									JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.FrameRate, "" + (int)(1000f / (float)timePerFrame));
									if (length > 1000) {
										int bitrate = (int)((fSize / 1000) / (length / 1000));
										JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Length, "" + length);
										JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.BitRate, "" + bitrate);
									}
								}
								if ((width > 0) && (height > 0)) {
									JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Width, "" + width);
									JemServer.getMediaStorage().addMediaMetadata(item, MetadataType.Height, "" + height);
								}
							}
							break;
						}

						if (bytesRead >= listSize)
							break;
					}
				}				
			}
			is.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		super.addMetadata(item);
	}

}
