package net.kodeninja.DMAP.parameters.dmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: dmap.contentcodesnumber
 */
public class mcnm extends DMAPIntParameter {
	/**
	 * Parameter Type
	 */
	private static final String PARAM_NAME = "dmap.contentcodesnumber";

	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public mcnm() {
		super(PARAM_NAME);
	}

	/**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
	public mcnm(int value) {
		super(PARAM_NAME, value);
	}

	/**
	 * Special constructor to create the value from a 4 byte string.
	 * @param Value 4 byte string to convert to integer code.
	 */
	public mcnm(String Value) {
		super(PARAM_NAME);
		setValue(Value);
	}

	/**
	 * Sets the integer value based on the passed 4 byte string valued.
	 * @param Value 4 byte string to convert to integer code.
	 */
	public void setValue(String Value) {
		byte[] buf = Value.getBytes();
		ByteBuffer iBuf = ByteBuffer.wrap(buf);
		iBuf.order(ByteOrder.BIG_ENDIAN);
		setValue(iBuf.getInt());
	}

	/**
	 * Converts the integer value into a 4 character string.
	 * @return A string representation of the code.
	 */
	@Override
	public String toString() {
		ByteBuffer iBuf = ByteBuffer.allocate(4);
		iBuf.order(ByteOrder.BIG_ENDIAN);
		iBuf.putInt(getValue());

		return new String(iBuf.array());
	}
}

