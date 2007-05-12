package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: com.apple.itunes.episode-num-str
 */
public class aeEN extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.episode-num-str";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeEN() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeEN(String value) {
		super(PARAM_NAME, value);
	}
}

