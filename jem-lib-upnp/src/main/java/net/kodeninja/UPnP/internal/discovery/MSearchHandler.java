package net.kodeninja.UPnP.internal.discovery;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.identifiers.SSDPIdentifier;
import net.kodeninja.UPnP.identifiers.SSDPMalformedIdentifierException;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.PacketHandler;

public class MSearchHandler implements PacketHandler {
	
	protected UPnP host;
	
	public MSearchHandler(UPnP host) {
		this.host = host;
	}
	
	public boolean process(HTTPSocket Socket,
			HTTPPacket<HTTPBody> Packet) {
		if (Packet.getHeader().getType() != HTTPHeader.HeaderType.REQUEST)
			return false;
		if (Packet.getHeader().getMethod().equalsIgnoreCase("M-SEARCH") == false)
			return false;
		if (Packet.getHeader().getLocation().equals(UPnP.STAR_URI) == false)
			return true;
		if ((Packet.getHeader().getParameter("st") == null) || (Packet.getHeader().getParameter("st").equals("")))
			return true;
		if (Packet.getHeader().getVersion().equals(HTTPVersion.HTTP1_1) == false)
			return true;
		
		if ((Packet.getHeader().getParameter("man") == null) ||
				(Packet.getHeader().getParameter("man").equals("\"ssdp:discover\"") == false))
			return true;

		try {
			long mx = Long.parseLong(Packet.getHeader().getParameter("mx"));
			if ((mx < 1) || (mx > 120))
				return true;
			long time = System.currentTimeMillis() + (long)(Math.random() * 1000 * mx); 
			host.queueAdvertisement(Socket, SSDPIdentifier.createInstance(Packet.getHeader().getParameter("st")), time);
		}
		catch (SSDPMalformedIdentifierException e) {}
		catch (NumberFormatException e) {}
		
		return true;
	}

}
