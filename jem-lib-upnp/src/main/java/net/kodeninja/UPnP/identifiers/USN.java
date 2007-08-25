package net.kodeninja.UPnP.identifiers;

public class USN extends SSDPIdentifier {
	private URN urn = null;
	private SSDPUUID uuid = null;
	
	public static USN createInstance(String identifier) throws SSDPMalformedIdentifierException {
				if (identifier.startsWith("uuid:") == false)
			throw new SSDPMalformedIdentifierException("Identifier not a USN. Identifier: " + identifier);

		int urnPos = identifier.indexOf(SSDP_SEPERATOR + SSDP_SEPERATOR);
		if (urnPos > 0)
			return new USN(SSDPUUID.createInstance(identifier.substring(0, urnPos)),
					URN.createInstance(identifier.substring(urnPos + 2)));
		else
			return new USN(SSDPUUID.createInstance(identifier));
	}
	
	public USN(SSDPUUID id) {
		this(id, null);
	}
	
	public USN(SSDPUUID uuid, URN urn) {
		this.uuid = uuid;
		this.urn = urn;
	}
	
	public SSDPUUID getUUID() {
		return uuid;
	}
	
	public URN getURN() {
		return urn;
	}
	
	public String toString() {
		if (urn == null)
			return uuid.toString();
		else
			return uuid.toString() + "::" + urn;
	}
	
}
