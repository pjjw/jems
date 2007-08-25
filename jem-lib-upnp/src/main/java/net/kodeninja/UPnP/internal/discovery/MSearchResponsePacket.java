package net.kodeninja.UPnP.internal.discovery;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.identifiers.USN;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.http.packet.HTTPVersion;

public class MSearchResponsePacket extends HTTPPacket<HTTPTextBody> {

	public MSearchResponsePacket(String location, USN ident) {
		super(new HTTPHeader(HTTPVersion.HTTP1_1, HTTPResponseCode.HTTP_200_OK), new HTTPTextBody(""));
		getHeader().setParameter("CACHE-CONTROL", "max-age=1800");
		getHeader().setParameter("DATE", getCurrentHTTPDate());
		getHeader().setParameter("EXT", "");
		getHeader().setParameter("LOCATION", location);
		getHeader().setParameter("SERVER", UPnP.getServerString());
		getHeader().setParameter("ST", ident.getURN() != null ? ident.getURN().toString() : ident.getUUID().toString());
		getHeader().setParameter("USN", ident.toString());
		allowHeaderUpdate = false;
	}

}
