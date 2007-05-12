package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.sortalbumartist
 */
public class assl extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.sortalbumartist";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public assl() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public assl(String value) {
		super(PARAM_NAME, value);
	}
}

