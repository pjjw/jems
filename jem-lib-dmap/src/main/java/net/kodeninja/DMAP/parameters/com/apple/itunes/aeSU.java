package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: com.apple.itunes.season-num
 */
public class aeSU extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.season-num";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeSU() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeSU(int value) {
		super(PARAM_NAME, value);
	}
}

