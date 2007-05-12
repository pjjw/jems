package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: dmap.updatetype
 */
public class muty extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.updatetype";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public muty() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public muty(byte value) {
		super(PARAM_NAME, value);
	}
}

