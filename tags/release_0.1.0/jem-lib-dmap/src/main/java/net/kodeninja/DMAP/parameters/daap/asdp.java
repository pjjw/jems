package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPDateParameter;
import java.util.Date;

/**
 * Generated class for parameter: daap.songdatepurchased
 */
public class asdp extends DMAPDateParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songdatepurchased";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asdp() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asdp(Date value) {
		super(PARAM_NAME, value);
	}
}

