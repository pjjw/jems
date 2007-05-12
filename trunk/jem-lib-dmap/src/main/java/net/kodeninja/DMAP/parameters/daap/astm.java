package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: daap.songtime
 */
public class astm extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songtime";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public astm() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public astm(int value) {
		super(PARAM_NAME, value);
	}
}

