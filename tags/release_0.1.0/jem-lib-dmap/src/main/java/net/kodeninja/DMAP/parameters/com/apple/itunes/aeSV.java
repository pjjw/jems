package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: com.apple.itunes.music-sharing-version
 */
public class aeSV extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.music-sharing-version";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeSV() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeSV(int value) {
		super(PARAM_NAME, value);
	}
}

