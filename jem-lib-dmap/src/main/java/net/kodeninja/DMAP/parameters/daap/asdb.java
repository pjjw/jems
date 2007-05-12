package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: daap.songdisabled
 */
public class asdb extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songdisabled";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asdb() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asdb(byte value) {
		super(PARAM_NAME, value);
	}
}

