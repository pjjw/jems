package net.kodeninja.UPnP.identifiers;

public abstract class URN extends SSDPIdentifier {

	public static URN createInstance(String identifier) throws SSDPMalformedIdentifierException {
		if (identifier.equals(RootDeviceURN.IDENTIFIER))
			return new RootDeviceURN();
		if (identifier.startsWith("urn:") == false)
			throw new SSDPMalformedIdentifierException("Identifier not a URN. Identifier: " + identifier);

		String schema = identifier.substring(4);
		String announcementType = schema.substring(schema.indexOf(SSDP_SEPERATOR) + 1);
		String type = announcementType.substring(announcementType.indexOf(SSDP_SEPERATOR) + 1);
		String version = type.substring(type.indexOf(SSDP_SEPERATOR) + 1);

		schema = schema.substring(0, schema.indexOf(SSDP_SEPERATOR)).toLowerCase();
		announcementType = announcementType.substring(0, announcementType.indexOf(SSDP_SEPERATOR)).toLowerCase();
		type = type.substring(0, type.indexOf(SSDP_SEPERATOR));

		if (announcementType.equals("device"))
			return new DeviceTypeURN(schema, type, SSDPVersion.createInstance(version));
		else if (announcementType.equals("service"))
			return new ServiceTypeURN(schema, type, SSDPVersion.createInstance(version));
		else
			throw new SSDPMalformedIdentifierException("Unknown announcement type: " + announcementType);
	}
}
