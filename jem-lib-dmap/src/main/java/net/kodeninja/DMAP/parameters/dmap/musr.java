package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: dmap.serverrevision
 */
public class musr extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.serverrevision";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public musr() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public musr(int value) {
		super(PARAM_NAME, value);
	}
}

