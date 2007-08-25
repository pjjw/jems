package net.kodeninja.UPnP.internal.discovery;

import java.net.MalformedURLException;
import java.net.URL;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.UPnPCache;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.PacketHandler;
import net.kodeninja.UPnP.identifiers.SSDPIdentifier;
import net.kodeninja.UPnP.identifiers.SSDPMalformedIdentifierException;
import net.kodeninja.UPnP.identifiers.USN;

public class MSearchResponseHandler implements PacketHandler {

	private UPnP host;
	
	public MSearchResponseHandler(UPnP host) {
		this.host = host;
	}
	
	public boolean process(HTTPSocket Socket, HTTPPacket<HTTPBody> Packet) {
		if (Packet.getHeader().getType() != HTTPHeader.HeaderType.RESPONSE)
			return false;
		
		if ((Packet.getHeader().getResponse().equals(HTTPResponseCode.HTTP_200_OK) == false) ||
				(Packet.getHeader().getParameter("EXT") == null) ||
				(Packet.getHeader().getParameter("ST") == null) ||
				(Packet.getHeader().getParameter("ST").equals("")))
			return false;
		
		try {
			UPnPCache cache;
			USN usn = USN.createInstance(Packet.getHeader().getParameter("USN"));
			String cacheControl = Packet.getHeader().getParameter("CACHE-CONTROL"); 
			int lifetime = Integer.parseInt(cacheControl.substring(cacheControl.indexOf("=") + 1));
			URL url = new URL(Packet.getHeader().getParameter("LOCATION"));
			String server = Packet.getHeader().getParameter("SERVER");
			SSDPIdentifier ident = SSDPIdentifier.createInstance(Packet.getHeader().getParameter("ST"));
			if (usn.getURN() != null) {
				if (usn.getURN().equals(ident) == false)
					throw new SSDPMalformedIdentifierException("URN described in USN and ST do not match.");
			}
			else if (usn.equals(ident) == false)
				throw new SSDPMalformedIdentifierException("UUID in USN and ST do not match.");
			
			cache = new UPnPCache(usn.getUUID(), ident, lifetime, url, server);
			host.addToCache(usn.getUUID(), cache);
		}
		catch (SSDPMalformedIdentifierException e) {
			e.printStackTrace();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
