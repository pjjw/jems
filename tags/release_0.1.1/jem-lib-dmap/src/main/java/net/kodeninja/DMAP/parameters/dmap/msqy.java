package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: dmap.supportsquery
 */
public class msqy extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.supportsquery";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public msqy() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public msqy(byte value) {
		super(PARAM_NAME, value);
	}
}

