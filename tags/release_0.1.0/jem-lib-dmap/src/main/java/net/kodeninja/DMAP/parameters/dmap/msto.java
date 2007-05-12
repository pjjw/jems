package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPUIntParameter;

/**
 * Generated class for parameter: dmap.utcoffset
 */
public class msto extends DMAPUIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.utcoffset";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public msto() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public msto(int value) {
		super(PARAM_NAME, value);
	}
}

