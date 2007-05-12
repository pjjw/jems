package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: com.apple.itunes.network-name
 */
public class aeNN extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.network-name";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeNN() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeNN(String value) {
		super(PARAM_NAME, value);
	}
}

