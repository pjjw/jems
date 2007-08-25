package net.kodeninja.UPnP.internal.discovery;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.identifiers.SSDPIdentifier;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.http.packet.HTTPVersion;

public class MSearchPacket extends HTTPPacket<HTTPTextBody> {

	public MSearchPacket(SSDPIdentifier ident) {
		super(new HTTPHeader("M-SEARCH", UPnP.STAR_URI, HTTPVersion.HTTP1_1), new HTTPTextBody(""));
		getHeader().setParameter("Host", "239.255.255.250:1900");
		getHeader().setParameter("ST", ident.toString());
		getHeader().setParameter("Man", "\"ssdp:discover\"");
		getHeader().setParameter("MX", "3");
		allowHeaderUpdate = false;
	}

}
