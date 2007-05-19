package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPUByteParameter;

/**
 * Generated class for parameter: daap.songrelativevolume
 */
public class asrv extends DMAPUByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songrelativevolume";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asrv() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asrv(byte value) {
		super(PARAM_NAME, value);
	}
}

