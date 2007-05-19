package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: com.apple.itunes.is-podcast
 */
public class aePC extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.is-podcast";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aePC() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aePC(byte value) {
		super(PARAM_NAME, value);
	}
}

