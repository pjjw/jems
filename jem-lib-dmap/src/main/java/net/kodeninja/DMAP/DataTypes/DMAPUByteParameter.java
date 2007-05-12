package net.kodeninja.DMAP.DataTypes;

/**
 * Special constructor used to denote an unsigned version of the byte parameter.
 * @author Charles Ikeson
 *
 */
abstract public class DMAPUByteParameter extends DMAPByteParameter {
	/**
	 * Constructor that files in type of the DMAPParameter class.
	 * @param name The long name of the parameter.
	 */
	public DMAPUByteParameter(String name) {
		super(DMAPParameter.DATATYPE_UBYTE, name);
	}

	/**
	 * Constructor that files in type of the DMAPParameter class and sets its value.
	 * @param name The long name of the parameter.
	 * @param Value The value of the tag.
	 */
	public DMAPUByteParameter(String name, byte Value) {
		super(DMAPParameter.DATATYPE_UBYTE, name, Value);
	}
}
