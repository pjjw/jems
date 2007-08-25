package net.kodeninja.UPnP.internal.eventing;

import java.net.MalformedURLException;
import java.net.URL;

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

public class EventSubscribeURI extends UPnPURIParser implements URIHandler {

	public static final long DEFAULT_TIMEOUT = 30 * 60;
	
	public EventSubscribeURI(UPnP host) {
		super(host);
	}


	public  HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<? extends HTTPBody> Packet) {

		if (Packet.getHeader().getType() != HTTPHeader.HeaderType.REQUEST)
			return null;

		if (Packet.getHeader().getMethod().equalsIgnoreCase("SUBSCRIBE") == false)
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

		long timeout;
		String tmpTimeout = Packet.getHeader().getParameter("Timeout");

		if (tmpTimeout == null)
			timeout = DEFAULT_TIMEOUT;
		else {
			tmpTimeout = tmpTimeout.substring("Second-".length());

			if (tmpTimeout.equalsIgnoreCase("infinite"))
				timeout = -1;
			else {
				try {
					timeout = Long.parseLong(tmpTimeout);
				}
				catch (NumberFormatException e) {
					return new HTTPErrorResponse(HTTPResponseCode.HTTP_400_BAD_REQUEST, HTTPVersion.HTTP1_1);
				}
			}
		}

		SSDPUUID sid = null;

		if ((Packet.getHeader().getParameter("NT") != null) &&
				(Packet.getHeader().getParameter("Callback") != null)) {
			
			if (Packet.getHeader().getParameter("NT").equals("upnp:event") == false)
				return new HTTPErrorResponse(HTTPResponseCode.HTTP_412_PRECONDITON_FAILED, HTTPVersion.HTTP1_1);
			
			if (Packet.getHeader().getParameter("SID") != null)
				return new HTTPErrorResponse(HTTPResponseCode.HTTP_412_PRECONDITON_FAILED, HTTPVersion.HTTP1_1);
			
			//New subscription
			//FIXME This should actually be a list of URL callbacks
			URL callback = null;
			try { 
				String tmpCallback = Packet.getHeader().getParameter("Callback");
				if (tmpCallback.startsWith("<"))
					tmpCallback = tmpCallback.substring(1, tmpCallback.length() - 1);
				callback = new URL(tmpCallback);
			}
			catch (MalformedURLException e) {
				return new HTTPErrorResponse(HTTPResponseCode.HTTP_412_PRECONDITON_FAILED, HTTPVersion.HTTP1_1);
			}
			sid = host.addSubscription(service, callback, timeout);
		}
		else if (Packet.getHeader().getParameter("SID") != null) {
			try {
				sid = SSDPUUID.createInstance(Packet.getHeader().getParameter("SID"));
				if (host.renewSubscription(service, sid, timeout) == false)
					return new HTTPErrorResponse(HTTPResponseCode.HTTP_400_BAD_REQUEST, HTTPVersion.HTTP1_1);
			}
			catch (SSDPMalformedIdentifierException e) {
				return new HTTPErrorResponse(HTTPResponseCode.HTTP_400_BAD_REQUEST, HTTPVersion.HTTP1_1);
			}
		}
		else
			return new HTTPErrorResponse(HTTPResponseCode.HTTP_400_BAD_REQUEST, HTTPVersion.HTTP1_1);

		if (sid == null)
			return new HTTPErrorResponse(HTTPResponseCode.HTTP_400_BAD_REQUEST, HTTPVersion.HTTP1_1);

		HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1, HTTPResponseCode.HTTP_200_OK);
		header.setParameter("SID", sid.toString());
		header.setParameter("Timeout", "Second-" + (timeout == -1 ? "infinite" : timeout));
		HTTPPacket<HTTPBody> response = new HTTPPacket<HTTPBody>(header, null);
		
		return response;
	}

}
