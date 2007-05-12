package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: dmap.sessionid
 */
public class mlid extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.sessionid";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mlid() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mlid(int value) {
		super(PARAM_NAME, value);
	}
}

