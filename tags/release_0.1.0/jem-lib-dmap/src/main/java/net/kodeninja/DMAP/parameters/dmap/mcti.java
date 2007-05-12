package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: dmap.containeritemid
 */
public class mcti extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.containeritemid";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mcti() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mcti(int value) {
		super(PARAM_NAME, value);
	}
}

