package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: daap.songcontentrating
 */
public class ascr extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songcontentrating";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ascr() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public ascr(byte value) {
		super(PARAM_NAME, value);
	}
}

