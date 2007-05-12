package net.kodeninja.DMAP.parameters.dpap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * DPAP.AspectRatio parameter
 * Stores the aspect ratio as a float converted to a string.
 * @author Charles Ikeson
 *
 */
public class pasp extends DMAPStringParameter {

	private static final String PARAM_NAME = "DPAP.AspectRatio";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public pasp() {
		super(PARAM_NAME);
	}

	/**
	 * Creates the parameter and sets it to the passed value. 
	 * @param AspectRatio The aspect ratio to set the tag to.
	 */
	public pasp(String AspectRatio) {
		super(PARAM_NAME, AspectRatio);
	}
}
