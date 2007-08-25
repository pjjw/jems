package net.kodeninja.UPnP.identifiers;

public class SSDPVersion {
	private int major;
	private int minor;
	
	public static SSDPVersion createInstance(String identifier) throws SSDPMalformedIdentifierException {
		int major = 0, minor = 0;
		int dotPos = identifier.indexOf(".");
		
		if (dotPos > 0) {
			major = Integer.parseInt(identifier.substring(0, dotPos));
			minor = Integer.parseInt(identifier.substring(dotPos + 1));
		}
		else
			major = Integer.parseInt(identifier);
		
		return new SSDPVersion(major, minor);
	}
	
	public SSDPVersion(int major) {
		this.major = major;
		this.minor = 0;
	}
	
	public SSDPVersion(int major, int minor) {
		this.major = major;
		this.minor = minor;
	}
	
	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public String toString() {
		if (minor != 0)
			return major + "." + minor;
		else
			return "" + major;
	}
	
	
}
