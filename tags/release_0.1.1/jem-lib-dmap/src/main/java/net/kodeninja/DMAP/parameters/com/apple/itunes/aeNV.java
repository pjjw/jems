package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: com.apple.itunes.norm-volume
 */
public class aeNV extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.norm-volume";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeNV() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeNV(int value) {
		super(PARAM_NAME, value);
	}
}

