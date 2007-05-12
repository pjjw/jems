package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.songdescription
 */
public class asdt extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songdescription";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asdt() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asdt(String value) {
		super(PARAM_NAME, value);
	}
}

