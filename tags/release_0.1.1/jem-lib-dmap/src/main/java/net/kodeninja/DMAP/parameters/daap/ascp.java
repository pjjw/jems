package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.songcomposer
 */
public class ascp extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songcomposer";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ascp() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public ascp(String value) {
		super(PARAM_NAME, value);
	}
}

