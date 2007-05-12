package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPVersionParameter;

/**
 * Generated class for parameter: daap.protocolversion
 */
public class apro extends DMAPVersionParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.protocolversion";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public apro() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param major The major number of the version number
	 * @param minor The minor number of the version number
	 */
	public apro(short major, short minor) {
		super(PARAM_NAME, major, minor);
	}
}

