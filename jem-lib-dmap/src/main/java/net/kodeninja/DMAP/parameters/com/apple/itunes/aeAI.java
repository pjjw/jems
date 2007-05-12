package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: com.apple.itunes.itms-artistid
 */
public class aeAI extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.itms-artistid";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeAI() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeAI(int value) {
		super(PARAM_NAME, value);
	}
}

