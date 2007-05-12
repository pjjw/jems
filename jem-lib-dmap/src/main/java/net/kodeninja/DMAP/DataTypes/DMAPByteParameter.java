package net.kodeninja.DMAP.DataTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Base class for DMAP byte tags.
 * @author Charles Ikeson
 */
abstract public class DMAPByteParameter extends DMAPParameter {
	/**
	 * Boolean constant for false.
	 */
	public final static byte BOOL_FALSE = 0;
	/**
	 * Boolean constant for true.
	 */
	public final static byte BOOL_TRUE = 1;

	/**
	 * Value of the parameter.
	 */
	private byte[] value = new byte[1];

	/**
	 * Creates parameter to 0 (False) and sets its long name to name.
	 * @param name The name long name.
	 */
	public DMAPByteParameter(String name) {
		super(DMAPParameter.DATATYPE_BYTE, name);
		setValue(BOOL_FALSE);
	}

	/**
	 * Creates parameter set to the passed value and sets it long name to name.
	 * @param name The long name of the parameter.
	 * @param Value The value to set the parameter to.
	 */
	public DMAPByteParameter(String name, byte Value) {
		super(DMAPParameter.DATATYPE_BYTE, name);
		setValue(Value);
	}

	/**
	 * Creates parameter set to the passed value and sets it long name to name.
	 * @param name The long name of the parameter.
	 * @param Value The value to set the parameter to.
	 */
	public DMAPByteParameter(String name, boolean Value) {
		super(DMAPParameter.DATATYPE_BYTE, name);
		setValue(Value);
	}

	/**
	 * Creates parameter with the passed derived data type and sets it to 0 (False)
	 * and sets it long name to name.
	 * @param type The derived data type.
	 * @param name The long name of the parameter.
	 */
	DMAPByteParameter(short type, String name) {
		super(type, name);
	}

	/**
	 * Creates parameter with the passed derived data type and sets it to the
	 * passed value and sets it long name to name.
	 * @param type The derived data type.
	 * @param name The long name of the parameter.
	 * @param Value The value to set the parameter to.
	 */
	DMAPByteParameter(short type, String name, byte Value) {
		super(type, name);
		setValue(Value);
	}

	/**
	 * Creates parameter with the passed derived data type and sets it to the
	 * passed value and sets it long name to name.
	 * @param type The derived data type.
	 * @param name The long name of the parameter.
	 * @param Value The value to set the parameter to.
	 */
	DMAPByteParameter(short type, String name, boolean Value) {
		super(type, name);
		setValue(Value);
	}

	/**
	 * Returns the length of the parameter.
	 * @return The length of the parameter.
	 */
	@Override
	public int dataLength() {
		return 1;
	}

	/**
	 * Reads the parameter in from the passed stream.
	 * @param in The stream to read from.
	 * @param Length How much of the stream to read.
	 */
	@Override
	public void readDataFromStream(InputStream in, int Length)
			throws IOException {
		if (Length != 1)
			throw new IOException("Invalid size passed to read.");
		in.read(value);
	}

	/**
	 * Writes the parameter to the passed stream.
	 * @param out The stream to write to.
	 */
	@Override
	protected void writeDataToBuffer(OutputStream out) throws IOException {
		out.write(value);
	}

	/**
	 * Returns the value of the parameter.
	 * @return The parameter value.
	 */
	public byte getValue() {
		return value[0];
	}

	/**
	 * Sets the parameter to the passed valued.
	 * @param value The value to set the parameter to.
	 */
	public void setValue(byte value) {
		this.value[0] = value;
	}

	/**
	 * Returns the value of the parameter as a boolean.
	 * @return The parameter value.
	 */
	public boolean getValueAsBoolean() {
		return (getValue() == BOOL_TRUE);
	}

	/**
	 * Sets the parameter to the passed valued.
	 * @param value The value to set the parameter to.
	 */
	public void setValue(boolean value) {
		if (value)
			setValue(BOOL_TRUE);
		else
			setValue(BOOL_FALSE);
	}

	/**
	 * Converts the parameter's value to a string.
	 * @return The parameter as a string.
	 */
	@Override
	public String toString() {
		return "" + getValue();
	}

}
