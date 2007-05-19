package net.kodeninja.DMAP.DataTypes;

import java.util.Date;

/**
 * Base class for DMAP data tags.
 * @author Charles Ikeson
 */
abstract public class DMAPDateParameter extends DMAPIntParameter {
	
	/**
	 * Constructs a date data type parameter with the passed long name.
	 * @param name The long name of the parameter.
	 */
	public DMAPDateParameter(String name) {
		super(DMAPParameter.DATATYPE_DATE, name);
	}

	/**
	 * Constructs a date data type parameter with the passed long name and sets
	 * it to the passed value.
	 * @param name The long name of the parameter.
	 * @param Value The date to set the parameter to.
	 */
	public DMAPDateParameter(String name, int Value) {
		super(DMAPParameter.DATATYPE_DATE, name, Value);
	}

	/**
	 * Constructs a date data type parameter with the passed long name and sets
	 * it to the passed value.
	 * @param name The long name of the parameter.
	 * @param Value The date to set the parameter to.
	 */
	public DMAPDateParameter(String name, Date Value) {
		super(DMAPParameter.DATATYPE_DATE, name);
		setValue(Value);
	}

	/**
	 * Sets the parameter to the specified value.
	 * @param Value The value to set the parameter to.
	 */
	public void setValue(Date Value) {
		this.setValue((int) (Value.getTime() / 1000));
	}
	
	/**
	 * Returns the value of the parameter as a date.
	 * @return The parameter as a date object.
	 */
	public Date getValueAsDate() {
		return new Date(this.getValue());
	}
}
