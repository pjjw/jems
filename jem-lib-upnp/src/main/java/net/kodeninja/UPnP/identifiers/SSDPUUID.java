package net.kodeninja.UPnP.identifiers;

public class SSDPUUID extends SSDPIdentifier {
	private String uuid;
	
	public static SSDPUUID createInstance(String identifier) throws SSDPMalformedIdentifierException {
		if (identifier.startsWith("uuid:") == false)
			throw new SSDPMalformedIdentifierException("Identifier not a USN. Identifier: " + identifier);
		
		return new SSDPUUID(identifier.substring(5));
	}
	
	public SSDPUUID(String uuid) {
		this.uuid = uuid;
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public String toString() {
		return "uuid:" + uuid;
	}
	
	public boolean equals(Object o) {
		if (o == null)
			return false;
		else if (this == o)
			return true;
		else if (o instanceof SSDPUUID)
			return uuid.equals(((SSDPUUID)o).uuid);
		return false;
	}
}
