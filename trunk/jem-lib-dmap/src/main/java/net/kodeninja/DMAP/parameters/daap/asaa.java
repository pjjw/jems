package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.songalbumartist
 */
public class asaa extends DMAPStringParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "daap.songalbumartist";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public asaa() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public asaa(String value) {
		super(PARAM_NAME, value);
	}
}

