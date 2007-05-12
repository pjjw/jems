package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: daap.songcodecsubtype
 */
public class ascs extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songcodecsubtype";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ascs() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public ascs(int value) {
		super(PARAM_NAME, value);
	}
}

