package net.kodeninja.DMAP.DataTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Base class for DMAP long tags.
 * @author Charles Ikeson
 */
abstract public class DMAPLongParameter extends DMAPParameter {
	private long value;

	/**
	 * Constructs an integer data type parameter with the passed long name.
	 * @param name The long name of the parameter.
	 */
	public DMAPLongParameter(String name) {
		super(DMAPParameter.DATATYPE_LONG, name);
	}

	/**
	 * Constructs a long data type parameter with the passed long name and sets
	 * it to the passed value.
	 * @param name The long name of the parameter.
	 * @param Value The value to set the parameter to.
	 */
	public DMAPLongParameter(String name, long Value) {
		super(DMAPParameter.DATATYPE_LONG, name);
		setValue(Value);
	}

	DMAPLongParameter(short type, String name) {
		super(type, name);
	}

	DMAPLongParameter(short type, String name, long Value) {
		super(type, name);
		setValue(Value);
	}

	@Override
	public int dataLength() {
		return 8;
	}

	@Override
	public void readDataFromStream(InputStream in, int Length)
			throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.order(ByteOrder.BIG_ENDIAN);
		in.read(buf.array());
		value = buf.getLong();
	}

	@Override
	protected void writeDataToBuffer(OutputStream out) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.order(ByteOrder.BIG_ENDIAN);
		buf.putLong(value);
		out.write(buf.array());
	}

	/**
	 * Returns the value of the parameter as a long.
	 * @return The parameter's value as a long.
	 */
	public long getValue() {
		return value;
	}

	/**
	 * Sets the parameter's value to the passed long value.
	 * @param value The value to set the parameter to.
	 */
	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "" + getValue();
	}

}
