package net.kodeninja.DMAP.DataTypes;

/**
 * Special constructor used to denote an unsigned version of the integer parameter.
 * @author Charles Ikeson
 *
 */
abstract public class DMAPUIntParameter extends DMAPIntParameter {
	/**
	 * Constructor that files in type of the DMAPParameter class.
	 * @param name The long name of the parameter.
	 */
	public DMAPUIntParameter(String name) {
		super(DMAPParameter.DATATYPE_UINT, name);
	}

	/**
	 * Constructor that files in type of the DMAPParameter class and sets its value.
	 * @param name The long name of the parameter.
	 * @param Value The value of the tag.
	 */
	public DMAPUIntParameter(String name, int Value) {
		super(DMAPParameter.DATATYPE_UINT, name, Value);
	}
}
