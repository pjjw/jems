package net.kodeninja.DMAP.DataTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Represents the base class for all DMAP based parameters and their types.
 * @author Charles Ikeson
 *
 */
public abstract class DMAPParameter {

	/**
	 * Constant denoting an unknown data type.
	 */
	public static final short DATATYPE_UNKNOWN = -1;
	/**
	 * Constant denoting a byte data type.
	 */
	public static final short DATATYPE_BYTE = 1;
	/**
	 * Constant denoting an unsigned byte data type.
	 */
	public static final short DATATYPE_UBYTE = 2;
	/**
	 * Constant denoting a short data type.
	 */
	public static final short DATATYPE_SHORT = 3;
	/**
	 * Constant denoting an unsigned short data type.
	 */
	public static final short DATATYPE_USHORT = 4;
	/**
	 * Constant denoting an integer data type.
	 */
	public static final short DATATYPE_INT = 5;
	/**
	 * Constant denoting an unsigned integer data type.
	 */
	public static final short DATATYPE_UINT = 6;
	/**
	 * Constant denoting a long type.
	 */
	public static final short DATATYPE_LONG = 7;
	/**
	 * Constant denoting an unsigned long data type.
	 */
	public static final short DATATYPE_ULONG = 8;
	/**
	 * Constant denoting a string data type.
	 */
	public static final short DATATYPE_STRING = 9;
	/**
	 * Constant denoting a date data type.
	 */
	public static final short DATATYPE_DATE = 10;
	/**
	 * Constant denoting a version data type.
	 */
	public static final short DATATYPE_VERSION = 11;
	/**
	 * Constant denoting a list data type.
	 */
	public static final short DATATYPE_LIST = 12;
	/**
	 * Constant denoting a binary data type. (Unoffical)
	 */
	public static final short DATATYPE_BINARY = 13;

	private short t;
	private String n;

	/**
	 * Constructor that sets the type and long name of the parameter.
	 * @param type The data type. See constants.
	 * @param name The long name of the parameter.
	 */
	public DMAPParameter(short type, String name) {
		t = type;
		n = name;
	}

	/**
	 * Returns the short 4 name of the parameter based on its class name.
	 * @return The 4 byte short name of the parameter.
	 */
	final public String getTag() {
		String retVal = this.getClass().getName();
		int pos = retVal.lastIndexOf(".");
		if (pos > 0)
			return retVal.substring(pos + 1);
		else
			return retVal;
	}

	/**
	 * Returns the data type of the parameter. 
	 * @return Parameter's data type.
	 */
	final public short getType() {
		return t;
	}

	/**
	 * Returns the long name of the parameter. 
	 * @return Parameter's long name.
	 */
	final public String getName() {
		return n;
	}

	/**
	 * Returns the length of the entire parameter in a stream.
	 * @return Parameter's size.
	 */
	final public int length() {
		return dataLength() + 8;
	}

	/**
	 * Writes the parameter to the specified data stream.
	 * @param out The stream to write to.
	 * @throws IOException Thrown if an exception was raised during the write.
	 */
	public void writeToStream(OutputStream out) throws IOException {
		ByteBuffer iBuf = ByteBuffer.allocate(8);
		iBuf.order(ByteOrder.BIG_ENDIAN);
		iBuf.put(getTag().getBytes("US-ASCII"), 0, 4);
		iBuf.putInt(dataLength());
		out.write(iBuf.array());
		writeDataToBuffer(out);
	}

	/**
	 * Returns the length of the data of the parameter.
	 * @return Parameter's data length.
	 */
	abstract public int dataLength();

	/**
	 * Reads the parameter from a stream.
	 * @param in The stream to read from.
	 * @param Length The length to read.
	 * @throws IOException Thrown if an exception was raised during the read.
	 */
	abstract public void readDataFromStream(InputStream in, int Length)
			throws IOException;

	/**
	 * Writes the data part of the parameter to the stream. 
	 * @param out The stream to write to.
	 * @throws IOException Thrown if an exception was raised during the write.
	 */
	abstract protected void writeDataToBuffer(OutputStream out)
			throws IOException;
}
