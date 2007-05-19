package net.kodeninja.DMAP.DataTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 
 * @author Charles Ikeson
 *
 */
abstract public class DMAPVersionParameter extends DMAPParameter {

	private short major, minor;

	/**
	 * Creates a version tag with the specified long name.
	 * @param name The long name of the tag.
	 */
	public DMAPVersionParameter(String name) {
		super(DMAPParameter.DATATYPE_VERSION, name);
	}

	/**
	 * Creates a version tag with the specified long name and sets its values.
	 * @param name The long name of the tag.
	 * @param Major The major number of the version.
	 * @param Minor The minor number of the version. 
	 */
	public DMAPVersionParameter(String name, short Major, short Minor) {
		super(DMAPParameter.DATATYPE_VERSION, name);
		setValue(Major, Minor);
	}

	@Override
	public int dataLength() {
		return 4;
	}

	@Override
	public void readDataFromStream(InputStream in, int Length)
			throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.order(ByteOrder.BIG_ENDIAN);
		in.read(buf.array());
		major = buf.getShort();
		minor = buf.getShort();
	}

	@Override
	protected void writeDataToBuffer(OutputStream out) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.order(ByteOrder.BIG_ENDIAN);
		buf.putShort(major);
		buf.putShort(minor);
		out.write(buf.array());
	}

	/**
	 * Returns the major number of the version number.
	 * @return Returns the major number of the version.
	 */
	public short getMajor() {
		return major;
	}

	
	/**
	 * Sets the major number for the version number.
	 * @param major The major number to set.
	 */
	public void setMajor(short major) {
		this.major = major;
	}

	/**
	 * Returns the minor number of the version number.
	 * @return Returns the minor number of the version.
	 */
	public short getMinor() {
		return minor;
	}

	/**
	 * Sets the minor number of the version tag.
	 * @param minor
	 */
	public void setMinor(short minor) {
		this.minor = minor;
	}

	/**
	 * Sets both the major and minor numbers of the version.
	 * @param major The major number to set.
	 * @param minor The minor number to set.
	 */
	public void setValue(short major, short minor) {
		setMajor(major);
		setMinor(minor);
	}

	@Override
	public String toString() {
		return getMajor() + "." + getMinor();
	}

}
