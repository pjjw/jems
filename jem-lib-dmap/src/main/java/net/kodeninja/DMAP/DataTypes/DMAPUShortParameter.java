package net.kodeninja.DMAP.DataTypes;

/**
 * Special constructor used to denote an unsigned version of the short parameter.
 * @author Charles Ikeson
 *
 */
abstract public class DMAPUShortParameter extends DMAPShortParameter {
	/**
	 * Constructor that files in type of the DMAPParameter class.
	 * @param name The long name of the parameter.
	 */
	public DMAPUShortParameter(String name) {
		super(DMAPParameter.DATATYPE_USHORT, name);
	}
	
	/**
	 * Constructor that files in type of the DMAPParameter class and sets its value.
	 * @param name The long name of the parameter.
	 * @param Value The value of the tag.
	 */
	public DMAPUShortParameter(String name, short Value) {
		super(DMAPParameter.DATATYPE_USHORT, name, Value);
	}
}
