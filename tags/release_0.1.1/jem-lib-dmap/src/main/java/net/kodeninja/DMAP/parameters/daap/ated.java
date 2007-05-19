package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPShortParameter;

/**
 * Generated class for parameter: daap.supportsextradata
 */
public class ated extends DMAPShortParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.supportsextradata";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ated() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public ated(short value) {
		super(PARAM_NAME, value);
	}
}

