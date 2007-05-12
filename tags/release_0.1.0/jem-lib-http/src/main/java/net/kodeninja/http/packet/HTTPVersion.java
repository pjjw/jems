package net.kodeninja.http.packet;

public final class HTTPVersion {
	public static HTTPVersion HTTP1_0 = new HTTPVersion(1, 0);
	public static HTTPVersion HTTP1_1 = new HTTPVersion(1, 1);

	private String protocol;
	private int minor;
	private int major;

	public HTTPVersion(String VersionString) throws InvalidHTTPVersionException {
		VersionString = VersionString.trim().toUpperCase();
		if (VersionString.startsWith("HTTP") == false)
			throw new InvalidHTTPVersionException();

		int slashPos = VersionString.indexOf("/");
		if (slashPos < 0)
			throw new InvalidHTTPVersionException();

		protocol = VersionString.substring(0, slashPos);
		int dotPos = VersionString.indexOf(".");
		if (dotPos == -1) {
			major = Integer.parseInt(VersionString.substring(5));
			minor = 0;
		} else {
			major = Integer.parseInt(VersionString.substring(5, dotPos));
			minor = Integer.parseInt(VersionString.substring(dotPos + 1));
		}
	}

	public HTTPVersion(String Protocol, int Major, int Minor) {
		protocol = Protocol.toUpperCase();
		major = Major;
		minor = Minor;
	}

	public HTTPVersion(int Major, int Minor) {
		protocol = "HTTP";
		major = Major;
		minor = Minor;
	}

	@Override
	public String toString() {
		return protocol + "/" + major + "." + minor;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		else if (o instanceof HTTPVersion)
			return (protocol.equals(((HTTPVersion) o).protocol))
					&& (major == ((HTTPVersion) o).major)
					&& (minor == ((HTTPVersion) o).minor);
		else
			return false;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public String getProtocol() {
		return protocol;
	}
}
