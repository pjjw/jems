package net.kodeninja.DMAP.parameters.dpap;

import net.kodeninja.DMAP.DataTypes.DMAPVersionParameter;

/**
 * DPAP.ProtocolVersion
 * Represents the protocol version of the current implemented DPAP 'standard'.
 * @author Charles Ikeson
 *
 */
public class ppro extends DMAPVersionParameter {

	private static final String PARAM_NAME = "DPAP.ProtocolVersion";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ppro() {
		super(PARAM_NAME);
	}

	/**
	 * Creates the parameter using the passed values.
	 * @param Major The major version number.
	 * @param Minor The minor version number.
	 */
	public ppro(short Major, short Minor) {
		super(PARAM_NAME, Major, Minor);
	}

}
