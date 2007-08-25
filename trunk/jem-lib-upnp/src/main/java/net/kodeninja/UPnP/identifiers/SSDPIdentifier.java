package net.kodeninja.UPnP.identifiers;

public abstract class SSDPIdentifier {
	public static final String SSDP_SEPERATOR = ":";
	
	public static SSDPIdentifier createInstance(String identifier) throws SSDPMalformedIdentifierException {
		if (identifier.equals(SSDPAll.IDENTIFIER))
			return new SSDPAll();
		
		if (identifier.startsWith("uuid:") && (identifier.lastIndexOf(":") == 4))
			return SSDPUUID.createInstance(identifier);
		
		try {
			return URN.createInstance(identifier);
		}
		catch (SSDPMalformedIdentifierException e1) {
			try {
				return USN.createInstance(identifier);
			}
			catch (SSDPMalformedIdentifierException e2) {
				throw new SSDPMalformedIdentifierException("Malformed SSDP Identifier. Identifier: " + identifier);		
			}
		}
	}
	
	public int hashCode() {
		return toString().hashCode();
	}
	
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (o instanceof SSDPIdentifier)
			return o.toString().equals(toString());
		return false;
	}
	
}
