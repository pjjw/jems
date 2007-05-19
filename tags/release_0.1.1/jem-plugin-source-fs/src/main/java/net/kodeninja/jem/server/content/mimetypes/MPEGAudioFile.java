package net.kodeninja.jem.server.content.mimetypes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.LinkedList;

import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MetadataTypes;
import net.kodeninja.util.Pair;

public class MPEGAudioFile extends AudioFile {
	private final static int MPEG_VERSION_1 = 3;
	private final static int MPEG_VERSION_2 = 2;
	private final static int MPEG_VERSION_2_5 = 0;

	private final static int MPEG_LAYER_I = 3;
	private final static int MPEG_LAYER_II = 2;
	private final static int MPEG_LAYER_III = 1;

	private final static String[] ID3_GENRE_TAGS = { "Blues", "Classic Rock",
			"Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz",
			"Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap",
			"Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska",
			"Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient",
			"Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical",
			"Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel",
			"Noise", "AlternRock", "Bass", "Soul", "Punk", "Space",
			"Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic",
			"Gothic", "Darkwave", "Techno-Industrial", "Electronic",
			"Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy",
			"Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle",
			"Native American", "Cabaret", "New Wave", "Psychadelic", "Rave",
			"Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk",
			"Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll",
			"Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing",
			"Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass",
			"Avantgarde", "Gothic Rock", "Progressive Rock",
			"Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band",
			"Chorus", "Easy Listening", "Acoustic", "Humour", "Speech",
			"Chanson", "Opera", "Chamber Music", "Sonata", "Symphony",
			"Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam",
			"Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad",
			"Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo",
			"Acapella", "Euro-House", "Dance Hall" };

	// private static byte ID3V2_FLAG_UNSYNCHRONISATION = (byte)0x128;
	private static byte ID3V2_FLAG_EXTENDED_HEADER = (byte) 0x64;
	// private static byte ID3V2_FLAG_EXPERIMENTAL_INDICATOR = (byte)0x32;
	private static byte ID3V2_FLAG_FOOTER_PRESENT = (byte) 0x16;

	private static String[] ID3V2_TEXT_ENCODINGS = { "ISO-8859-1", "UTF-16",
			"UTF-16BE", "UTF-8" };

	class InvalidMPEGFrameHeaderException extends Exception {
		private static final long serialVersionUID = -201817604501570961L;
	}

	public void readNext(InputStream is, byte[] data) throws IOException {
		data[0] = data[1];
		data[1] = data[2];
		data[2] = data[3];
		int b = is.read();
		if ((b >= 0) && (b < 256))
			data[3] = (byte) b;
		else
			throw new IOException();
	}

	public void fillBuffer(InputStream is, byte[] data) throws IOException {
		for (int i = 0; i < 4; i++)
			readNext(is, data);
	}

	public void skipBytes(InputStream is, long toSkip) throws IOException {
		while (toSkip > 0) {
			long skipped = is.skip(toSkip);
			if (skipped < 0)
				throw new IOException();
			else
				toSkip -= skipped;
		}
	}

	public boolean isID3v1(byte[] data) {
		return ((data[1] == 'T') && (data[2] == 'A') && (data[3] == 'G'));
	}

	public boolean isID3v2(byte[] data) {
		return ((data[1] == 'I') && (data[2] == 'D') && (data[3] == '3'));
	}

	public int getMPEGVersion(byte[] data)
			throws InvalidMPEGFrameHeaderException {
		int retVal = (((int) data[1]) >> 3) & 3;
		if (retVal == 1)
			throw new InvalidMPEGFrameHeaderException();
		return retVal;
	}

	public int getMPEGLayer(byte[] data) throws InvalidMPEGFrameHeaderException {
		int retVal = (((int) data[1]) >> 1) & 3;
		if (retVal == 0)
			throw new InvalidMPEGFrameHeaderException();
		return retVal;
	}

	public int getBitRate(byte[] data) throws InvalidMPEGFrameHeaderException {
		final int BITRATE_TABLE[][] = { { 0, 0, 0, 0, 0 },
				{ 32, 32, 32, 32, 8 }, { 64, 48, 40, 48, 16 },
				{ 96, 56, 48, 56, 24 },

				{ 128, 64, 56, 64, 32 }, { 160, 80, 64, 80, 40 },
				{ 192, 96, 80, 96, 48 }, { 224, 112, 96, 112, 56 },

				{ 256, 128, 112, 128, 64 }, { 288, 160, 128, 144, 80 },
				{ 320, 192, 160, 160, 96 }, { 352, 224, 192, 176, 112 },

				{ 384, 256, 224, 192, 128 }, { 416, 320, 256, 224, 144 },
				{ 448, 320, 256, 224, 144 }, { -1, -1, -1, -1, -1 } };

		int row = ((int) (data[2] >> 4) & 0x0F);
		int col = 0;

		int v = getMPEGVersion(data);
		int l = getMPEGLayer(data);

		if (v == MPEG_VERSION_1) {
			if (l == MPEG_LAYER_I)
				col = 0;
			else if (l == MPEG_LAYER_II)
				col = 1;
			else if (l == MPEG_LAYER_III)
				col = 2;
		} else if (l == MPEG_LAYER_I)
			col = 3;
		else
			col = 4;

		if (BITRATE_TABLE[row][col] == -1)
			throw new InvalidMPEGFrameHeaderException();

		return BITRATE_TABLE[row][col];
	}

	public int getSampleRate(byte[] data)
			throws InvalidMPEGFrameHeaderException {
		final int SAMPLERATE_TABLE[][] = { { 44100, 48000, 32000 },
				{ 22050, 24000, 12000 }, { 32000, 16000, 8000 }, };

		int v = getMPEGVersion(data);
		int row = 0;
		if (v == MPEG_LAYER_I)
			row = 0;
		else if (v == MPEG_VERSION_2)
			row = 1;
		else if (v == MPEG_VERSION_2_5)
			row = 2;

		int col = (data[2] >> 2) & 3;
		if (col > 2)
			throw new InvalidMPEGFrameHeaderException();

		return SAMPLERATE_TABLE[row][col];
	}

	public int getSamplesPerFrame(byte[] data)
			throws InvalidMPEGFrameHeaderException {
		int v = getMPEGVersion(data);
		int l = getMPEGLayer(data);

		if (l == MPEG_LAYER_I)
			return 384;
		else if ((l == MPEG_LAYER_II) || (v == MPEG_VERSION_1))
			return 1152;
		else
			return 576;
	}

	public int getPaddingSize(byte[] data)
			throws InvalidMPEGFrameHeaderException {
		if (((data[3] >> 1) & 1) == 1)
			if (getMPEGLayer(data) == MPEG_LAYER_I)
				return 4;
			else
				return 1;
		return 0;
	}

	public boolean hasCRC(byte[] data) {
		return (data[1] & 1) != 1;
	}

	public boolean isFrameHeader(byte[] data) {
		final byte FRAME_SIG = (byte) 0xE0;

		if (((data[0] == (byte) 255) && ((data[1] & FRAME_SIG) == FRAME_SIG)) == false)
			return false;

		try {
			getMPEGVersion(data);
			getMPEGLayer(data);
			getBitRate(data);
			getSampleRate(data);
		} catch (InvalidMPEGFrameHeaderException e) {
			return false;
		}

		return true;
	}

	public int readID3v2Int(InputStream is, boolean useSafeSync)
			throws IOException {
		byte[] tmpBuf = new byte[4];
		is.read(tmpBuf);

		// Check the safesync bytes
		if (useSafeSync) {
			for (byte b : tmpBuf)
				if ((b & 128) != 0)
					throw new IOException();

			tmpBuf[3] = (byte) ((tmpBuf[3] & 127) | ((tmpBuf[2] & 1) << 7));
			tmpBuf[2] = (byte) (((tmpBuf[2] >> 1) & 63) | ((tmpBuf[1] & 3) << 6));
			tmpBuf[1] = (byte) (((tmpBuf[1] >> 2) & 31) | ((tmpBuf[0] & 7) << 5));
			tmpBuf[0] = (byte) ((tmpBuf[0] >> 3) & 15);
		}

		IntBuffer iBuf = (ByteBuffer.wrap(tmpBuf).order(ByteOrder.BIG_ENDIAN))
				.asIntBuffer();

		return iBuf.get();

	}

	public void addMetadata(MediaItem item) {
		File filename = new File(item.getMediaURI().getPath());
		int nFrames = 0;
		int bitRate = 999;
		float frameLength = 0;
		int goodFrames = 0;

		try {
			InputStream is = new BufferedInputStream(new FileInputStream(
					filename));
			byte buf[] = { 0, 0, 0, 0 };
			// fillBuffer(is, buf);
			int bytePos = 0;
			LinkedList<Pair<Integer, Integer>> lastFrameRefs = new LinkedList<Pair<Integer, Integer>>();

			while (is.available() > 0) {
				if (isFrameHeader(buf))
					try {
						int frameSize = (int) ((float) getSamplesPerFrame(buf)
								* (float) getBitRate(buf) * (1000.0 / 8.0) / (float) getSampleRate(buf))
								+ getPaddingSize(buf);

						nFrames++;

						if (goodFrames >= 3) {
							skipBytes(is, frameSize - 4);
							bytePos += frameSize - 4;
							frameLength = (float) getSamplesPerFrame(buf)
									/ (float) getSampleRate(buf);
							bitRate = Math.min(bitRate, getBitRate(buf));
						} else {
							boolean found = false;
							Iterator<Pair<Integer, Integer>> it = lastFrameRefs
									.iterator();

							while (it.hasNext()) {
								Pair<Integer, Integer> p = it.next();
								if (p.getA() + p.getB() + 40 < bytePos)
									it.remove();
								else if (Math.abs((bytePos - p.getA())
										- p.getB()) <= 40) {
									it.remove();
									goodFrames++;
									if (goodFrames == 3) {

									}
									found = true;
								}
							}

							if (found == false)
								goodFrames = 0;
						}

						if (goodFrames < 3)
							lastFrameRefs.addLast(new Pair<Integer, Integer>(
									bytePos, frameSize));
					} catch (InvalidMPEGFrameHeaderException e) {
					}
				else if (isID3v1(buf)) {
					String tmpString = "";
					byte[] songTitle = new byte[30];
					byte[] songArtist = new byte[30];
					byte[] songAlbum = new byte[30];
					byte[] songYear = new byte[4];
					byte[] songComment = new byte[28];
					byte[] track = new byte[2];
					int genre = -1;

					is.read(songTitle);
					is.read(songArtist);
					is.read(songAlbum);
					is.read(songYear);
					is.read(songComment);
					is.read(track);
					genre = is.read();
					bytePos += 125;

					if (((tmpString = new String(songTitle)).trim().length() > 0)
							&& (item.getMetadata(MetadataTypes.Title) == null))
						item.setMetadata(MetadataTypes.Title, tmpString.trim());

					if (((tmpString = new String(songArtist)).trim().length() > 0)
							&& (item.getMetadata(MetadataTypes.Artist) == null))
						item
								.setMetadata(MetadataTypes.Artist, tmpString
										.trim());

					if (((tmpString = new String(songAlbum)).trim().length() > 0)
							&& (item.getMetadata(MetadataTypes.Set) == null))
						item.setMetadata(MetadataTypes.Set, tmpString.trim());

					if (((tmpString = new String(songYear)).trim().length() > 0)
							&& (item.getMetadata(MetadataTypes.Year) == null))
						item.setMetadata(MetadataTypes.Year, tmpString.trim());

					if (((genre >= 0) && (genre < ID3_GENRE_TAGS.length))
							&& (item.getMetadata(MetadataTypes.Genre) == null))
						item.setMetadata(MetadataTypes.Genre,
											ID3_GENRE_TAGS[genre]);

					tmpString = new String(songComment);
					if (track[0] != 0)
						tmpString += new String(track);
					else if ((track[1] > 0)
							&& (item.getMetadata(MetadataTypes.SetPosition) == null))
						item.setMetadata(MetadataTypes.SetPosition, ""
								+ track[1]);

					if (tmpString.trim().length() > 0)
						item.setMetadata(MetadataTypes.Description, tmpString
								.trim());
				} else if (isID3v2(buf)) {
					// Read in the header
					int major = is.read();

					// Skip over the minor number
					is.read();

					bytePos += 2;
					if ((major >= 3) && (major <= 4)) {
						boolean useSafeSync = (major == 4);
						try {
							// Finish reading the header
							int startPos = bytePos - 3;
							int flags = is.read();
							int totalSize, size = readID3v2Int(is, useSafeSync);
							bytePos += 5;

							totalSize = size + 10;
							if ((flags & ID3V2_FLAG_FOOTER_PRESENT) == ID3V2_FLAG_FOOTER_PRESENT)
								totalSize += 10;

							if ((flags & ID3V2_FLAG_EXTENDED_HEADER) == ID3V2_FLAG_EXTENDED_HEADER) {
								int extSize = readID3v2Int(is, useSafeSync);
								is.skip(extSize - 4);
								bytePos += extSize;
							}

							// Frame tags
							while (startPos + totalSize > bytePos) {
								byte[] tagName = new byte[4];
								int tagSize = 0;
								byte[] tagFlags = new byte[2];

								bytePos += is.read(tagName);
								tagSize = readID3v2Int(is, useSafeSync);
								bytePos += 4;
								bytePos += is.read(tagFlags);

								String tag = new String(tagName);

								// Title tag
								if (tag.startsWith("T")
										&& (tag.equals("TXXX") == false)) {
									byte[] encoding = { 0 };
									byte[] bText = new byte[tagSize - 1];
									bytePos += is.read(encoding);
									bytePos += is.read(bText);

									if (encoding[0] < ID3V2_TEXT_ENCODINGS.length) {
										String text = new String(
												bText,
												ID3V2_TEXT_ENCODINGS[encoding[0]]);
										if (tag.equals("TIT2")
												&& (item
														.getMetadata(MetadataTypes.Title) == null))
											item
													.setMetadata(
																	MetadataTypes.Title,
																	text.trim());
										else if (tag.equals("TALB")
												&& (item
														.getMetadata(MetadataTypes.Set) == null))
											item.setMetadata(MetadataTypes.Set,
																text.trim());
										else if (tag.equals("TRCK")
												&& (item
														.getMetadata(MetadataTypes.SetPosition) == null))
											item
													.setMetadata(
																	MetadataTypes.SetPosition,
																	text.trim());
										else if (tag.equals("TPE1")
												&& (item
														.getMetadata(MetadataTypes.Artist) == null)) {
											if (text.indexOf("/") > -1)
												item
														.setMetadata(
																		MetadataTypes.Artist,
																		text
																				.substring(
																							0,
																							text
																									.indexOf("/"))
																				.trim());
											else
												item
														.setMetadata(
																		MetadataTypes.Artist,
																		text
																				.trim());
										} else if ((tag.equals("TPRO") || (tag
												.equals("TYER")))
												&& (text.length() >= 4)
												&& (item
														.getMetadata(MetadataTypes.Year) == null))
											item
													.setMetadata(
																	MetadataTypes.Year,
																	text
																			.trim()
																			.substring(
																						0,
																						4));
										else if (tag.equals("TCON")
												&& (item
														.getMetadata(MetadataTypes.Genre) == null))
											try {
												int g = Integer.parseInt(text);
												if ((g >= 0)
														&& (g < ID3_GENRE_TAGS.length))
													item
															.setMetadata(
																			MetadataTypes.Genre,
																			ID3_GENRE_TAGS[g]);
											} catch (NumberFormatException e) {
												if (text.toUpperCase()
														.equals("RX"))
													text = "Remix";
												else if (text.toUpperCase()
														.equals("CR"))
													text = "Cover";
												item
														.setMetadata(
																		MetadataTypes.Genre,
																		text
																				.trim());
											}
									}
								} else {
									is.skip(tagSize);
									bytePos += tagSize;
								}
							}
						} catch (IOException e) {
							System.out.println("Corrupt ID3v2 Tag on "
									+ item.getMediaURI().toString() + " @ "
									+ bytePos);
						}
					}
				}
				bytePos++;
				readNext(is, buf);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		if (nFrames > 0)
			item.setMetadata(MetadataTypes.Length, ""
					+ (long) (nFrames * frameLength * 1000));
		if (bitRate < 999)
			item.setMetadata(MetadataTypes.BitRate, "" + bitRate);

		super.addMetadata(item);
	}

}
