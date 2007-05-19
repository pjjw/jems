package net.kodeninja.DMAP.DataTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Base class for DMAP short tags.
 * @author Charles Ikeson
 */
abstract public class DMAPShortParameter extends DMAPParameter {

	private short value = 0;

	/**
	 * Constructs a short data type parameter with the passed long name.
	 * @param name The long name of the parameter.
	 */
	public DMAPShortParameter(String name) {
		super(DMAPParameter.DATATYPE_SHORT, name);
	}

	/**
	 * Constructs a short data type parameter with the passed long name and sets
	 * it to the passed value.
	 * @param name The long name of the parameter.
	 * @param Value The value to set the parameter to.
	 */
	public DMAPShortParameter(String name, short Value) {
		super(DMAPParameter.DATATYPE_SHORT, name);
		setValue(Value);
	}

	DMAPShortParameter(short type, String name) {
		super(type, name);
	}

	DMAPShortParameter(short type, String name, short Value) {
		super(type, name);
		setValue(Value);
	}

	@Override
	public int dataLength() {
		return 2;
	}

	@Override
	public void readDataFromStream(InputStream in, int Length)
			throws IOException {
		ByteBuffer buf;

		switch (Length) {
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
		ByteBuffer buf = ByteBuffer.allocate(2);
		buf.order(ByteOrder.BIG_ENDIAN);
		buf.putShort(value);
		out.write(buf.array());
	}

	/**
	 * Returns the parameter's value as a short.
	 * @return The parameter's value as a short.
	 */
	public short getValue() {
		return value;
	}

	/**
	 * Sets the parameter to the passed value.
	 * @param value The value to set the parameter to.
	 */
	public void setValue(short value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "" + getValue();
	}

}
