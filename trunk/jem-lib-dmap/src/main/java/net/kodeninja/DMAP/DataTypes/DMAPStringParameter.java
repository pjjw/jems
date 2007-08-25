package net.kodeninja.DMAP.DataTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Base class for DMAP string tags.
 * @author Charles Ikeson
 */
abstract public class DMAPStringParameter extends DMAPParameter {
	private String value = "";

	/**
	 * Constructs a string data type parameter with the passed long name.
	 * @param name The long name of the parameter.
	 */
	public DMAPStringParameter(String name) {
		super(DMAPParameter.DATATYPE_STRING, name);
	}

	/**
	 * Constructs a string data type parameter with the passed long name and sets
	 * it to the passed value.
	 * @param name The long name of the parameter.
	 * @param Value The value to set the parameter to.
	 */
	public DMAPStringParameter(String name, String Value) {
		super(DMAPParameter.DATATYPE_STRING, name);
		setValue(Value);
	}

	@Override
	public int dataLength() {
		return value.length();
	}

	@Override
	public void readDataFromStream(InputStream in, int Length)
			throws IOException {
		byte[] tmp = new byte[Length];
		in.read(tmp);

		value = new String(tmp);
	}

	@Override
	protected void writeDataToBuffer(OutputStream out) throws IOException {
		out.write(value.getBytes());
	}

	/**
	 * Returns the value of the parameter as a string.
	 * @return The string vale of the parameter.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the parameter to the passed string.
	 * @param value The value to set the parameter to.
	 */
	public void setValue(String value) {
		this.value = fixString(value);
	}

	@Override
	public String toString() {
		return getValue();
	}

	protected String fixString(String s) {
		StringBuffer sb = new StringBuffer(s);
		int i = 0;
		while (i < sb.length()) {
			char c = sb.charAt(i);
			if ((c == ' ') || (c == ',') || (c == '.') || (c == ':')
					|| (c == '/') || ((c >= '0') && (c <= '9'))
					|| ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')))
				i++;
			else
				sb.deleteCharAt(i);
		}

		return sb.toString();
	}

}
