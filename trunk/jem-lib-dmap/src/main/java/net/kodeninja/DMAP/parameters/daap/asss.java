package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.sortseriesname
 */
public class asss extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.sortseriesname";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asss() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asss(String value) {
		super(PARAM_NAME, value);
	}
}

