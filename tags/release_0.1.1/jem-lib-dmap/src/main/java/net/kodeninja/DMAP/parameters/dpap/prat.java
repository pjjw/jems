package net.kodeninja.DMAP.parameters.dpap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * DPAP.ImageRating
 * The rating of the image.
 * @author Charles Ikeson
 */
public class prat extends DMAPIntParameter {
	private static final String PARAM_NAME = "DPAP.ImageRating";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public prat() {
		super(PARAM_NAME);
	}

	/**
	 * Creates the parameter using the passed parameters.
	 * @param Rating The rating of the image.
	 */
	public prat(int Rating) {
		super(PARAM_NAME, Rating);
	}
}
