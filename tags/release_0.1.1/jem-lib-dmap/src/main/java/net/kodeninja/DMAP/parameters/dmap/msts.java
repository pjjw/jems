package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: dmap.statusstring
 */
public class msts extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.statusstring";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public msts() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public msts(String value) {
		super(PARAM_NAME, value);
	}
}

