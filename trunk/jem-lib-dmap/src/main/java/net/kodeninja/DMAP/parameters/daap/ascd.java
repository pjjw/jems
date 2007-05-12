package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: daap.songcodectype
 */
public class ascd extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songcodectype";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ascd() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public ascd(int value) {
		super(PARAM_NAME, value);
	}
}

