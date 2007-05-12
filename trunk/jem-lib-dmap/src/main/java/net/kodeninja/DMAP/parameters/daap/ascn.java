package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.songcontentdescription
 */
public class ascn extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songcontentdescription";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ascn() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public ascn(String value) {
		super(PARAM_NAME, value);
	}
}

