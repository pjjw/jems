package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPShortParameter;

/**
 * Generated class for parameter: dmap.contentcodestype
 */
public class mcty extends DMAPShortParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.contentcodestype";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mcty() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mcty(short value) {
		super(PARAM_NAME, value);
	}
}

