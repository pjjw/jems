package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: com.apple.itunes.itms-genreid
 */
public class aeGI extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.itms-genreid";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeGI() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeGI(int value) {
		super(PARAM_NAME, value);
	}
}

