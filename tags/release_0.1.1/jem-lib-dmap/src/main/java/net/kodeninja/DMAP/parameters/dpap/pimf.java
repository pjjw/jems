package net.kodeninja.DMAP.parameters.dpap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * DPAP.ImageFilename
 * The filename of the image.
 * @author Charles Ikeson
 */
public class pimf extends DMAPStringParameter {
	private static final String PARAM_NAME = "DPAP.ImageFilename";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public pimf() {
		super(PARAM_NAME);
	}

	/**
	 * Creates the parameter using the passed value.
	 * @param Filename The filename of the image.
	 */
	public pimf(String Filename) {
		super(PARAM_NAME, Filename);
	}

}
