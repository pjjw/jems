package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPVersionParameter;

/**
 * Generated class for parameter: dmap.protocolversion
 */
public class mpro extends DMAPVersionParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.protocolversion";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mpro() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param major The major number of the version number
	 * @param minor The minor number of the version number
	 */
	public mpro(short major, short minor) {
		super(PARAM_NAME, major, minor);
	}
}

