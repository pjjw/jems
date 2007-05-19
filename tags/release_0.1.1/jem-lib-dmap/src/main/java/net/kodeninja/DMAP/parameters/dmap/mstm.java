package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: dmap.timeoutinterval
 */
public class mstm extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.timeoutinterval";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mstm() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mstm(int value) {
		super(PARAM_NAME, value);
	}
}

