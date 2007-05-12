package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: daap.bookmarkable
 */
public class asbk extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.bookmarkable";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asbk() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asbk(byte value) {
		super(PARAM_NAME, value);
	}
}

