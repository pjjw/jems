package net.kodeninja.DMAP.DataTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Base class for DMAP int tags.
 * @author Charles Ikeson
 */
abstract public class DMAPIntParameter extends DMAPParameter {
	private int value = 0;

	/**
	 * Constructs an integer data type parameter with the passed long name.
	 * @param name The long name of the parameter.
	 */
	public DMAPIntParameter(String name) {
		super(DMAPParameter.DATATYPE_INT, name);
	}

	/**
	 * Constructs an integer data type parameter with the passed long name and sets
	 * it to the passed value.
	 * @param name The long name of the parameter.
	 * @param Value The value to set the parameter to.
	 */
	public DMAPIntParameter(String name, int Value) {
		super(DMAPParameter.DATATYPE_INT, name);
		setValue(Value);
	}

	DMAPIntParameter(short type, String name) {
		super(type, name);
	}

	DMAPIntParameter(short type, String name, int Value) {
		super(type, name);
	}

	@Override
	public int dataLength() {
		return 4;
	}

	@Override
	public void readDataFromStream(InputStream in, int Length)
			throws IOException {
		ByteBuffer buf;

		switch (Length) {
			case 4:
				buf = ByteBuffer.allocate(4);
				buf.order(ByteOrder.BIG_ENDIAN);
				in.read(buf.array());
				value = buf.getInt();
				break;

			case 2:
				buf = ByteBuffer.allocate(2);
				buf.order(ByteOrder.BIG_ENDIAN);
				in.read(buf.array());
				value = buf.getShort();
				break;

			case 1:
				buf = ByteBuffer.allocate(1);
				buf.order(ByteOrder.BIG_ENDIAN);
				in.read(buf.array());
				value = buf.get();
				break;

			default:
				throw new IOException("Invalid size passed to read.");
		}
	}

	@Override
	protected void writeDataToBuffer(OutputStream out) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.order(ByteOrder.BIG_ENDIAN);
		buf.putInt(value);
		out.write(buf.array());
	}

	/**
	 * Returns the parameter's value as an integer.
	 * @return The integer value of the parameter.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the value of the parameter to the passed value.
	 * @param value The value to set the parameter to.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "" + getValue();
	}

}
