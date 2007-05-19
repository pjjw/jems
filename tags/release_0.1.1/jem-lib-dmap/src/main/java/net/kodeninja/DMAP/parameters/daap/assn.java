package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.sortname
 */
public class assn extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.sortname";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public assn() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public assn(String value) {
		super(PARAM_NAME, value);
	}
}

