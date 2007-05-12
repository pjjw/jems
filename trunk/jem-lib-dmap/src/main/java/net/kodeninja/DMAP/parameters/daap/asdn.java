package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPShortParameter;

/**
 * Generated class for parameter: daap.songdiscnumber
 */
public class asdn extends DMAPShortParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songdiscnumber";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asdn() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asdn(short value) {
		super(PARAM_NAME, value);
	}
}

