package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPShortParameter;

/**
 * Generated class for parameter: daap.songtracknumber
 */
public class astn extends DMAPShortParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songtracknumber";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public astn() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public astn(short value) {
		super(PARAM_NAME, value);
	}
}

