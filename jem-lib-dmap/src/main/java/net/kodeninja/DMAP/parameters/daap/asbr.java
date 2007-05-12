package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPShortParameter;

/**
 * Generated class for parameter: daap.songbitrate
 */
public class asbr extends DMAPShortParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songbitrate";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asbr() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asbr(short value) {
		super(PARAM_NAME, value);
	}
}

