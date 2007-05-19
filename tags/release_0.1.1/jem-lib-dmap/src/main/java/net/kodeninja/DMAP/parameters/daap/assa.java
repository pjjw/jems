package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.sortartist
 */
public class assa extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.sortartist";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public assa() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public assa(String value) {
		super(PARAM_NAME, value);
	}
}

