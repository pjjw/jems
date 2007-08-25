package net.kodeninja.UPnP.internal.discovery;

import java.net.URL;
import java.net.MalformedURLException;

import net.kodeninja.UPnP.identifiers.SSDPIdentifier;
import net.kodeninja.UPnP.identifiers.SSDPMalformedIdentifierException;
import net.kodeninja.UPnP.identifiers.USN;
import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.UPnPCache;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.PacketHandler;

public class DiscoveryNotifyHandler implements PacketHandler {
	private static final String SSDP_ALIVE = "ssdp:alive";
	private static final String SSDP_BYEBYE = "ssdp:byebye";
	private UPnP host;
	
	public DiscoveryNotifyHandler(UPnP host) {
		this.host = host;
	}

	public boolean process(HTTPSocket Socket,
			HTTPPacket<HTTPBody> Packet) {
		if (Packet.getHeader().getType() != HTTPHeader.HeaderType.REQUEST)
			return false;
		if ((Packet.getHeader().getMethod().equalsIgnoreCase("NOTIFY") == false) ||
				(Packet.getHeader().getLocation().equals(UPnP.STAR_URI) == false))
			return false;
		
		
		if (Packet.getHeader().getParameter("USN") == null)
			return true;
		if (Packet.getHeader().getParameter("CACHE-CONTROL") == null)
			return true;
		if (Packet.getHeader().getParameter("LOCATION") == null)
			return true;
		if (Packet.getHeader().getParameter("SERVER") == null)
			return true;
		if (Packet.getHeader().getParameter("NT") == null)
			return true;
		if (Packet.getHeader().getParameter("NTS") == null)
			return true;
		
		try {
			UPnPCache cache;
			USN usn = USN.createInstance(Packet.getHeader().getParameter("USN"));
			String cacheControl = Packet.getHeader().getParameter("CACHE-CONTROL");
			int lifetime = Integer.parseInt(cacheControl.substring(cacheControl.indexOf("=") + 1));
			URL url = new URL(Packet.getHeader().getParameter("LOCATION"));

			String server = Packet.getHeader().getParameter("SERVER");
			SSDPIdentifier ident = SSDPIdentifier.createInstance(Packet.getHeader().getParameter("NT"));
			if (usn.getURN() != null) {
				if (usn.getURN().equals(ident) == false)
					throw new SSDPMalformedIdentifierException("URN described in USN and NT do not match.");
			}
			else if (usn.equals(ident) == false)
				throw new SSDPMalformedIdentifierException("UUID in USN and NT do not match.");
			
			cache = new UPnPCache(usn.getUUID(), ident, lifetime, url, server);
			if (Packet.getHeader().getParameter("NTS").equals(SSDP_ALIVE))
				host.addToCache(usn.getUUID(), cache);
			else if (Packet.getHeader().getParameter("NTS").equals(SSDP_BYEBYE))
				host.removeFromCache(usn.getUUID(), cache);
		}
		catch (SSDPMalformedIdentifierException e) {
			//Do nothing
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
