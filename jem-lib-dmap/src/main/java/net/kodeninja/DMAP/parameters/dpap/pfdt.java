package net.kodeninja.DMAP.parameters.dpap;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.kodeninja.DMAP.DataTypes.DMAPBinaryParameter;

/**
 * DPAP.FileDate
 * This tag holds the raw file data of a picture or thumb nail.
 * The data the is stored depends on what is requested by the client.
 * This differs from the DAAP protocol where the file is requested separately from the tag. 
 * @author Charles Ikeson
 *
 */
public class pfdt extends DMAPBinaryParameter {

	private static final String PARAM_NAME = "DPAP.FileData";

	InputStream in;
	int size;

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public pfdt() {
		super(PARAM_NAME);
	}
	
	/**
	 * Creates the parameter and points it to the original file.
	 * @param f The file to read from.
	 * @param asThumb Resizes the image to a max of 240x240 before sending it back.
	 */
	public pfdt(File f, boolean asThumb) {
		super(PARAM_NAME);
		try {
			in = new FileInputStream(f);
			size = (int)f.length();
			if (asThumb) {

				BufferedImage image = ImageIO.read(in);
				int max = Math.max(image.getWidth(), image.getHeight());
				float scale = 240.0f / max;
				int newW = (int) (image.getWidth() * scale);
				int newH = (int) (image.getHeight() * scale);
				BufferedImage scaledImage = new BufferedImage(newW, newH, image
						.getType());

				Graphics g = scaledImage.getGraphics();
				g.drawImage(image, 0, 0, newW, newH, null);
				ByteArrayOutputStream tmpOut = new ByteArrayOutputStream();
				ImageIO.write(scaledImage, "jpeg", tmpOut);
				g.dispose();

				in = new ByteArrayInputStream(tmpOut.toByteArray());
				size = tmpOut.size();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected OutputStream getRecieveStream() throws IOException {
		return null;
	}

	@Override
	protected InputStream getSendStream() throws IOException {
		return in;
	}

	@Override
	public int dataLength() {
		return size;
	}

}
