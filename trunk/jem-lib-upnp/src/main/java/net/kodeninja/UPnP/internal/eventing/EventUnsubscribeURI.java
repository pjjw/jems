package net.kodeninja.UPnP.internal.eventing;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.UPnPAdvertiseOperation;
import net.kodeninja.UPnP.description.Service;
import net.kodeninja.UPnP.identifiers.SSDPMalformedIdentifierException;
import net.kodeninja.UPnP.identifiers.SSDPUUID;
import net.kodeninja.UPnP.internal.description.UPnPURIParser;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.packet.extra.HTTPErrorResponse;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.URIHandler;

public class EventUnsubscribeURI extends UPnPURIParser implements URIHandler {

	public EventUnsubscribeURI(UPnP host) {
		super(host);
	}

	public  HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<? extends HTTPBody> Packet) {
		
		if (Packet.getHeader().getType() != HTTPHeader.HeaderType.REQUEST)
			return null;
		
		if (Packet.getHeader().getMethod().equalsIgnoreCase("UNSUBSCRIBE") == false)
			return null;
		
		if (parseURI(Packet.getHeader().getLocation().getPath()) == false)
			return new HTTPErrorResponse(HTTPResponseCode.HTTP_400_BAD_REQUEST, HTTPVersion.HTTP1_1);

		if (serviceId == null)
			return new HTTPErrorResponse(HTTPResponseCode.HTTP_412_PRECONDITON_FAILED, HTTPVersion.HTTP1_1);
		
		UPnPAdvertiseOperation advertiser = host.getAdvertisterByRootUUID(deviceId);
		if (advertiser == null)
			return new HTTPErrorResponse(HTTPResponseCode.HTTP_412_PRECONDITON_FAILED, HTTPVersion.HTTP1_1);

		Service service = advertiser.getService(serviceId);
		
		if (service == null)
			return new HTTPErrorResponse(HTTPResponseCode.HTTP_412_PRECONDITON_FAILED, HTTPVersion.HTTP1_1);
		
		try {
			SSDPUUID sid = SSDPUUID.createInstance(Packet.getHeader().getParameter("SID"));
			host.removeSubscription(service, sid);
		}
		catch (SSDPMalformedIdentifierException e) {
			return new HTTPErrorResponse(HTTPResponseCode.HTTP_412_PRECONDITON_FAILED, HTTPVersion.HTTP1_1);
		}
		
		HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1, HTTPResponseCode.HTTP_200_OK);
		HTTPPacket<HTTPBody> response = new HTTPPacket<HTTPBody>(header, null);
		
		return response;
	}
	
	
}
