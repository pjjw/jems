package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: com.apple.itunes.req-fplay
 */
public class aeFP extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.req-fplay";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeFP() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeFP(byte value) {
		super(PARAM_NAME, value);
	}
}

