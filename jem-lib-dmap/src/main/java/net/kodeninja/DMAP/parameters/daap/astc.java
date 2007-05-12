package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPShortParameter;

/**
 * Generated class for parameter: daap.songtrackcount
 */
public class astc extends DMAPShortParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songtrackcount";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public astc() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public astc(short value) {
		super(PARAM_NAME, value);
	}
}

