package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: com.apple.itunes.smart-playlist
 */
public class aeSP extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.smart-playlist";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeSP() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeSP(byte value) {
		super(PARAM_NAME, value);
	}
}

