package net.kodeninja.util;

public final class MimeType implements Comparable<MimeType>, Cloneable {
	public static final MimeType WILDCARD = new MimeType("*", "*");
	private String PrimaryType;
	private String SubType;

	public MimeType(String Raw) throws MalformedMimeTypeException {
		int delim = Raw.indexOf('/');
		if (delim < 1)
			throw new MalformedMimeTypeException();

		PrimaryType = Raw.substring(0, delim);
		SubType = Raw.substring(delim + 1);
		if (PrimaryType.equals("") || SubType.equals(""))
			throw new MalformedMimeTypeException();
		if (SubType.indexOf('/') != -1)
			throw new MalformedMimeTypeException();
	}

	public MimeType(String primary, String subtype) {
		PrimaryType = primary;
		SubType = subtype;
	}

	/**
	 * Returns the primary type of the mime type.
	 *
	 * @return The primary type of the mime type.
	 */
	public String getPrimaryType() {
		return PrimaryType;
	}

	/**
	 * Returns the sub type of the mime type.
	 *
	 * @return The sub type of the mime type.
	 */
	public String getSubType() {
		return SubType;
	}

	@Override
	public String toString() {
		return PrimaryType + "/" + SubType;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object m) {
		if (this == m)
			return true;
		else if (m instanceof MimeType)
			return ((MimeType) m).toString().equals(toString());
		else
			return false;
	}

	public boolean matches(MimeType m) {
		return ((PrimaryType.equals("*") || m.getPrimaryType().equals("*")) || (m
				.getPrimaryType().equals(PrimaryType) && ((SubType.equals("*"))
				|| (m.getSubType().equals("*")) || SubType.equals(m
				.getSubType()))));
	}

	public int compareTo(MimeType m) {
		if (getPrimaryType().compareTo(m.getPrimaryType()) == 0) {
			if (getSubType().compareTo(m.getSubType()) == 0)
				return 0;
			else if (getSubType().equals("*"))
				return 1;
			else if (m.getSubType().equals("*"))
				return -1;
			else
				return getSubType().compareTo(m.getSubType());
		} else if (getPrimaryType().equals("*"))
			return 1;
		else if (m.getPrimaryType().equals("*"))
			return -1;
		else
			return getPrimaryType().compareTo(m.getPrimaryType());
	}

	@Override
	public MimeType clone() {
		return new MimeType(PrimaryType, SubType);
	}
}
