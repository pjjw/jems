package net.kodeninja.DMAP.parameters.dpap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * DPAP.ImageFormat
 * 
 * @author Charles Ikeson
 */
public class pfmt extends DMAPStringParameter {

	private static final String PARAM_NAME = "DPAP.ImageFormat";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public pfmt() {
		super(PARAM_NAME);
	}

	/**
	 * The format of the image data. Usually the file extension of the file.
	 * @param ImageFormat The format of the image data.
	 */
	public pfmt(String ImageFormat) {
		super(PARAM_NAME, ImageFormat);
	}
}
