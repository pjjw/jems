package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: daap.songbookmark
 */
public class asbo extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songbookmark";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asbo() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asbo(int value) {
		super(PARAM_NAME, value);
	}
}

