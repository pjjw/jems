package net.kodeninja.UPnP.identifiers;

public class ServiceIDURN extends URN {
	private String sID;
	protected String domain;
	public ServiceIDURN(String serviceID) {
		this("upnp-org", serviceID);
	}
	
	public ServiceIDURN(String domain, String serviceID) {
		sID = serviceID;
		this.domain = domain;
	}
	
	public String getServiceId() {
		return sID;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public String toString() {
		return "urn:" + domain + ":serviceId:" + sID;
	}

}
