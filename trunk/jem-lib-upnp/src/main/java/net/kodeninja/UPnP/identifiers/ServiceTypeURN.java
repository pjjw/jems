package net.kodeninja.UPnP.identifiers;

public class ServiceTypeURN extends URN {
	protected String schema;
	protected String type;
	protected SSDPVersion version;
	
	public ServiceTypeURN(String ServiceType, SSDPVersion ServiceVersion) {
		this("schemas-upnp-org", ServiceType, ServiceVersion);
	}
	
	public ServiceTypeURN(String ServiceSchema, String ServiceType, SSDPVersion ServiceVersion) {
		schema = ServiceSchema;
		type = ServiceType;
		version = ServiceVersion;
	}
	
	public String getType() {
		return type;
	}
	
	public SSDPVersion getVersion() {
		return version;
	}
	
	public String toString() {
		return "urn:" + schema + ":service:" + type + ":" + version;
	}
}
