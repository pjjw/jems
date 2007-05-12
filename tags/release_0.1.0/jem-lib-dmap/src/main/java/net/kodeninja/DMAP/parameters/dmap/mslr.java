package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: dmap.loginrequired
 */
public class mslr extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.loginrequired";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mslr() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mslr(byte value) {
		super(PARAM_NAME, value);
	}
}

