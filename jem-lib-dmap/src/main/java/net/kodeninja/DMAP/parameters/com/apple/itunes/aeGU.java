package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPLongParameter;

/**
 * Generated class for parameter: com.apple.itunes.gapless-dur
 */
public class aeGU extends DMAPLongParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "com.apple.itunes.gapless-dur";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public aeGU() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public aeGU(long value) {
		super(PARAM_NAME, value);
	}
}

