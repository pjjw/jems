package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: daap.playlistshufflemode
 */
public class apsm extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.playlistshufflemode";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public apsm() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public apsm(byte value) {
		super(PARAM_NAME, value);
	}
}

