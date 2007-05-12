package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: daap.songstoptime
 */
public class assp extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songstoptime";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public assp() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public assp(int value) {
		super(PARAM_NAME, value);
	}
}

