package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: dmap.itemname
 */
public class minm extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.itemname";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public minm() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public minm(String value) {
		super(PARAM_NAME, value);
	}
}

