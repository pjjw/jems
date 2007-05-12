package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPShortParameter;

/**
 * Generated class for parameter: daap.songbeatsperminute
 */
public class asbt extends DMAPShortParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songbeatsperminute";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asbt() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asbt(short value) {
		super(PARAM_NAME, value);
	}
}

