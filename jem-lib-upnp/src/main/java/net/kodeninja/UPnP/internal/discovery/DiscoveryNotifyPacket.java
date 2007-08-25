package net.kodeninja.UPnP.internal.discovery;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.UPnPAdvertiseOperation;
import net.kodeninja.UPnP.identifiers.USN;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.http.packet.HTTPVersion;

public class DiscoveryNotifyPacket extends HTTPPacket<HTTPTextBody> {
	
	public DiscoveryNotifyPacket(String location, USN ident, boolean alive) {
		super(new HTTPHeader("NOTIFY", UPnP.STAR_URI, HTTPVersion.HTTP1_1), new HTTPTextBody(""));
		
		getHeader().setParameter("HOST", "239.255.255.250:1900");
		if (alive)
			getHeader().setParameter("CACHE-CONTROL", "max-age=" + (UPnPAdvertiseOperation.UPNP_CACHE_FRESHEN_TIMEOUT / 1000));
		if (alive)
			getHeader().setParameter("LOCATION", location);
		getHeader().setParameter("NT", ident.getURN() != null ? ident.getURN().toString() : ident.getUUID().toString());
		getHeader().setParameter("NTS", (alive ? "ssdp:alive" : "ssdp:byebye"));		
		if (alive)
			getHeader().setParameter("SERVER", UPnP.getServerString());
		getHeader().setParameter("USN", ident.toString());
		allowHeaderUpdate = false;
	}

}
