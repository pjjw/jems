package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.sortalbum
 */
public class assu extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.sortalbum";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public assu() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public assu(String value) {
		super(PARAM_NAME, value);
	}
}

