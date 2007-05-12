package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.songlongcontentdescription
 */
public class aslc extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songlongcontentdescription";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aslc() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aslc(String value) {
		super(PARAM_NAME, value);
	}
}

