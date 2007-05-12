package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: dmap.supportsextensions
 */
public class msex extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.supportsextensions";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public msex() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public msex(byte value) {
		super(PARAM_NAME, value);
	}
}

