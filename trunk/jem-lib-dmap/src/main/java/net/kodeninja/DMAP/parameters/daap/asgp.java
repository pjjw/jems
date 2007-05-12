package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: daap.songgapless
 */
public class asgp extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songgapless";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asgp() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asgp(byte value) {
		super(PARAM_NAME, value);
	}
}

