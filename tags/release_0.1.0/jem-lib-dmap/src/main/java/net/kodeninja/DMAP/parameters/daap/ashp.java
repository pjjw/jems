package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: daap.songhasbeenplayed
 */
public class ashp extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songhasbeenplayed";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ashp() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public ashp(byte value) {
		super(PARAM_NAME, value);
	}
}

