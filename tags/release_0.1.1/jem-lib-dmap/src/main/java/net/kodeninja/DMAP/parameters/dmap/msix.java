package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: dmap.supportsindex
 */
public class msix extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.supportsindex";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public msix() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public msix(byte value) {
		super(PARAM_NAME, value);
	}
}

