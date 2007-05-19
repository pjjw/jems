package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: dmap.parentcontainerid
 */
public class mpco extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.parentcontainerid";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mpco() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mpco(int value) {
		super(PARAM_NAME, value);
	}
}

