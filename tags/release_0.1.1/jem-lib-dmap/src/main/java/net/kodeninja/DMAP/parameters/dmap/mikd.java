package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPByteParameter;

/**
 * Generated class for parameter: dmap.itemkind
 */
public class mikd extends DMAPByteParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.itemkind";

	/**
	 * Constant to define media item as a DAAP tag...?
	 */
	public static final byte ITEM_KIND_DAAP = 2;
	
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mikd() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mikd(byte value) {
		super(PARAM_NAME, value);
	}
}

