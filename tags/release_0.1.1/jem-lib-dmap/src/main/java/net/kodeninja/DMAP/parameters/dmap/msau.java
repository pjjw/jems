package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: dmap.authenticationmethod
 */
public class msau extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.authenticationmethod";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public msau() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public msau(byte value) {
		super(PARAM_NAME, value);
	}
}

