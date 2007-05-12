package net.kodeninja.DMAP.parameters.dpap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * DPAP.ImageFileSize
 * The file size of the image.
 * @author Charles Ikeson
 *
 */
public class pifs extends DMAPIntParameter {
	private static final String PARAM_NAME = "DPAP.ImageFileSize";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public pifs() {
		super(PARAM_NAME);
	}

	/**
	 * Creates the parameter using the passed value.
	 * @param filesize The filesize of the image.
	 */
	public pifs(int filesize) {
		super(PARAM_NAME, filesize);
	}
}
