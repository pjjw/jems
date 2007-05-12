package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPShortParameter;

/**
 * Generated class for parameter: daap.songextradata
 */
public class ased extends DMAPShortParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songextradata";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ased() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public ased(short value) {
		super(PARAM_NAME, value);
	}
}

