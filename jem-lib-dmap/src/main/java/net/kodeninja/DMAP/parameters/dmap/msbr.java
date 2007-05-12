package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: dmap.supportsbrowse
 */
public class msbr extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.supportsbrowse";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public msbr() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public msbr(byte value) {
		super(PARAM_NAME, value);
	}
}

