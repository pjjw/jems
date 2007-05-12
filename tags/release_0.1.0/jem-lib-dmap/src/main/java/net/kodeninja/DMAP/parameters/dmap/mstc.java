package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPDateParameter;
import java.util.Date;

/**
 * Generated class for parameter: dmap.utctime
 */
public class mstc extends DMAPDateParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.utctime";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mstc() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mstc(Date value) {
		super(PARAM_NAME, value);
	}
}

