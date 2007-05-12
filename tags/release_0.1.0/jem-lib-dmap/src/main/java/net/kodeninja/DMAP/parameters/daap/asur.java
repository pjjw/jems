package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: daap.songuserrating
 */
public class asur extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songuserrating";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asur() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asur(byte value) {
		super(PARAM_NAME, value);
	}
}

