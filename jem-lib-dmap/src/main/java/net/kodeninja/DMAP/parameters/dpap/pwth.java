package net.kodeninja.DMAP.parameters.dpap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * DPAP.ImagePixelWidth
 * The width of the image in pixels.
 * @author Charles Ikeson
 *
 */
public class pwth extends DMAPIntParameter {
	private static final String PARAM_NAME = "DPAP.ImagePixelWidth";


	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public pwth() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor for the height parameter using the passed value. 
	 * @param Width The width of the image.
	 */
	public pwth(int Width) {
		super(PARAM_NAME, Width);
	}
}
