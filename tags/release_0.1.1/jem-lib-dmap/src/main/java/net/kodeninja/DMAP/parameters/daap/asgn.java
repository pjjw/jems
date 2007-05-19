package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.songgenre
 */
public class asgn extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songgenre";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asgn() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asgn(String value) {
		super(PARAM_NAME, value);
	}
}

