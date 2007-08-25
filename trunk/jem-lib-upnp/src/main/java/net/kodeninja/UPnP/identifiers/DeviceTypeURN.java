package net.kodeninja.UPnP.identifiers;

public class DeviceTypeURN extends URN {
	protected String schema;
	protected String type;
	protected SSDPVersion version;
	
	public DeviceTypeURN(String DeviceType, SSDPVersion DeviceVersion) {
		this("schemas-upnp-org", DeviceType, DeviceVersion);
	}
	
	public DeviceTypeURN(String DeviceSchema, String DeviceType, SSDPVersion DeviceVersion) {
		schema = DeviceSchema;
		type = DeviceType;
		version = DeviceVersion;
	}
	
	public String toString() {
		return "urn:" + schema + ":device:" + type + ":" + version;
	}
	
}
