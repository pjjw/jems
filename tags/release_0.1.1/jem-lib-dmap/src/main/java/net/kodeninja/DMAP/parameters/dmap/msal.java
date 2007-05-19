package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: dmap.supportsautologout
 */
public class msal extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.supportsautologout";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public msal() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public msal(byte value) {
		super(PARAM_NAME, value);
	}
}

