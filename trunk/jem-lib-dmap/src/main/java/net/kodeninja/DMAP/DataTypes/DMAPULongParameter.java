package net.kodeninja.DMAP.DataTypes;

/**
 * Special constructor used to denote an unsigned version of the long parameter.
 * @author Charles Ikeson
 *
 */
abstract public class DMAPULongParameter extends DMAPLongParameter {
	/**
	 * Constructor that files in type of the DMAPParameter class.
	 * @param name The long name of the parameter.
	 */
	public DMAPULongParameter(String name) {
		super(DMAPParameter.DATATYPE_ULONG, name);
	}

	/**
	 * Constructor that files in type of the DMAPParameter class and sets its value.
	 * @param name The long name of the parameter.
	 * @param Value The value of the tag.
	 */
	public DMAPULongParameter(String name, long Value) {
		super(DMAPParameter.DATATYPE_ULONG, name, Value);
	}
}
