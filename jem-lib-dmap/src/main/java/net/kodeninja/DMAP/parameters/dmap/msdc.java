package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: dmap.databasescount
 */
public class msdc extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.databasescount";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public msdc() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public msdc(int value) {
		super(PARAM_NAME, value);
	}
}

