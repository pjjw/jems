package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: dmap.returnedcount
 */
public class mrco extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.returnedcount";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mrco() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mrco(int value) {
		super(PARAM_NAME, value);
	}
}

