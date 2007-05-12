package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPLongParameter;

/**
 * Generated class for parameter: dmap.persistentid
 */
public class mper extends DMAPLongParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.persistentid";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mper() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mper(long value) {
		super(PARAM_NAME, value);
	}
}

