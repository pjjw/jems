package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.songcategory
 */
public class asct extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songcategory";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asct() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asct(String value) {
		super(PARAM_NAME, value);
	}
}

