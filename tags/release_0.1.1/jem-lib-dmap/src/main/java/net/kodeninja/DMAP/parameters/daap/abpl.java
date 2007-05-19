package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: daap.baseplaylist
 */
public class abpl extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.baseplaylist";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public abpl() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public abpl(byte value) {
		super(PARAM_NAME, value);
	}
}

