package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: daap.songcompilation
 */
public class asco extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songcompilation";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asco() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asco(byte value) {
		super(PARAM_NAME, value);
	}
}

