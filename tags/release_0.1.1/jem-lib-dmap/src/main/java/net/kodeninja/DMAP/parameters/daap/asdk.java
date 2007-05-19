package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: daap.songdatakind
 */
public class asdk extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songdatakind";
	
	public static final byte DATA_KIND_LOCAL = 0;
	public static final byte DATA_KIND_REMOTE = 1;

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asdk() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asdk(byte value) {
		super(PARAM_NAME, value);
	}
}

