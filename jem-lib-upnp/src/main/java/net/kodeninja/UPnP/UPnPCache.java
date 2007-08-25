package net.kodeninja.UPnP;

import java.net.URL;

import net.kodeninja.UPnP.identifiers.SSDPIdentifier;
import net.kodeninja.UPnP.identifiers.SSDPUUID;

public class UPnPCache {
	private long expires;
	private SSDPUUID uuid;
	private SSDPIdentifier ident;
	private URL location;
	private String server;
	
	public UPnPCache(SSDPUUID uuid, SSDPIdentifier ident, int lifetime, URL location, String server) {
		this.uuid = uuid;
		this.ident = ident;
		this.location = location;
		this.server = server;
		
		expires = System.currentTimeMillis() + (lifetime * 1000);
	}

	public boolean hasExpired() {
		return (System.currentTimeMillis() > expires);
	}
	
	public SSDPUUID getUUID() {
		return uuid;
	}
	
	public SSDPIdentifier getIdent() {
		return ident;
	}

	public URL getLocation() {
		return location;
	}

	public String getServer() {
		return server;
	}
	
	public boolean equals(Object o) {
		if (o == null)
			return false;
		else if (this == o)
			return true;
		else if (o instanceof UPnPCache) {
			UPnPCache t = (UPnPCache)o;
			return ((t.ident.equals(ident)) &&
					(t.location.equals(location)) &&
					(t.server.equals(server)) &&
					(t.uuid.equals(uuid)));
		}
		return false;
	}
	
	public int hashCode() {
		return (uuid + "::" + ident).hashCode();
	}
	
	public String toString() {
		return ident + "(Expires in " + ((expires - System.currentTimeMillis()) / 1000) + "s)";
	}
}
