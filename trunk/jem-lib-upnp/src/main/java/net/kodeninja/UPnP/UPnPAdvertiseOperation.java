package net.kodeninja.UPnP;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.kodeninja.UPnP.description.Device;
import net.kodeninja.UPnP.description.RootDevice;
import net.kodeninja.UPnP.description.Service;
import net.kodeninja.UPnP.identifiers.RootDeviceURN;
import net.kodeninja.UPnP.identifiers.SSDPAll;
import net.kodeninja.UPnP.identifiers.SSDPIdentifier;
import net.kodeninja.UPnP.identifiers.SSDPUUID;
import net.kodeninja.UPnP.identifiers.ServiceIDURN;
import net.kodeninja.UPnP.identifiers.URN;
import net.kodeninja.UPnP.identifiers.USN;
import net.kodeninja.UPnP.internal.discovery.DiscoveryNotifyPacket;
import net.kodeninja.UPnP.internal.discovery.MSearchResponsePacket;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.extra.HTTPXMLBody;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.HTTPUDPServerSocket;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UPnPAdvertiseOperation implements UPnPOperation {
	public static final long UPNP_CACHE_FRESHEN_TIMEOUT = 1800000; // 60 secs * 20 mins * 1000 msec
	private UPnP host;
	HTTPUDPServerSocket sock;
	private RootDevice root;
	private long lastAdvertisement = 0;
	private LinkedList<USN> usnList = new LinkedList<USN>();
	private HTTPXMLBody deviceDescription;
	private Map<ServiceIDURN, HTTPXMLBody> serviceDescriptions = new HashMap<ServiceIDURN, HTTPXMLBody>();

	private void addServiceDescriptions(Device d, DocumentBuilder builder) {

		Iterator<Service> it = d.getServices();

		while (it.hasNext()) {
			Document doc = builder.newDocument();
			Service s = it.next();

			Element tmp, version;
			Element docRoot = doc.createElementNS("urn:schemas-upnp-org:service-1-0", "scpd");
			docRoot.appendChild(version = doc.createElement("specVersion"));

			version.appendChild(tmp = doc.createElement("major"));
			tmp.setTextContent("1");

			version.appendChild(tmp = doc.createElement("minor"));
			tmp.setTextContent("0");

			s.writeServiceDescriptionToXML(docRoot);

			doc.appendChild(docRoot);
			serviceDescriptions.put(s.getID(), new HTTPXMLBody(doc));
		}

	}

	UPnPAdvertiseOperation(UPnP h, RootDevice d) {
		this.host = h;
		this.root = d;
		this.sock = host.getBroadcastService().getTransport();

		// Add root device usn to usn list.
		usnList.add(new USN(root.getUDN(), new RootDeviceURN()));

		// Add root device to usn list
		addToUSNList(d);

		// Add embedded devices to usn list
		Iterator<Device> it = d.getEmbeddedDevices();
		while (it.hasNext())
			addToUSNList(it.next());


		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Build Device Description document 
			Document doc = builder.newDocument();

			Element tmp, version;
			Element docRoot = doc.createElementNS("urn:schemas-upnp-org:device-1-0", "root");
			docRoot.appendChild(version = doc.createElement("specVersion"));

			version.appendChild(tmp = doc.createElement("major"));
			tmp.setTextContent("1");

			version.appendChild(tmp = doc.createElement("minor"));
			tmp.setTextContent("0");

			root.writeToXML(docRoot);
			doc.appendChild(docRoot);

			deviceDescription = new HTTPXMLBody(doc);

			// Build service description documents
			addServiceDescriptions(root, builder);

			it = d.getEmbeddedDevices();
			while (it.hasNext())
				addServiceDescriptions(it.next(), builder);


		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void addToUSNList(Device d) {
		usnList.add(new USN(d.getUDN()));
		usnList.add(new USN(d.getUDN(), d.getType()));

		Iterator<Service> it = d.getServices();
		while (it.hasNext())
			usnList.add(new USN(d.getUDN(), it.next().getType()));
	}

	private void sendPacket(HTTPPacket<? extends HTTPBody> packet) {
		try {
			sock.sendPacket(UPnP.SSDP_MULTICAST, 1900, packet);
		}
		catch (IOException e) {}
	}

	private String getDescriptionURL() {
		return UPnP.getURLBase() + "device/" + root.getUDN().getUUID() + "/description.xml";
	}

	public boolean resendAdvertisement() {
		return (System.currentTimeMillis() - lastAdvertisement) > UPNP_CACHE_FRESHEN_TIMEOUT / 2;
	}

	public void sendAdvertisement() {
		for (USN usn: usnList)
			sendPacket(new DiscoveryNotifyPacket(getDescriptionURL(), usn, true));

		lastAdvertisement = System.currentTimeMillis();
	}

	public void sendAdvertisementOn(HTTPSocket Socket, SSDPIdentifier ident) {
		try {
			if (ident instanceof SSDPAll) {
				for (USN usn: usnList)
					Socket.sendPacket(new MSearchResponsePacket(getDescriptionURL(), usn));
			}
			else if (ident instanceof URN) {
				for (USN usn: usnList) {
					if ((usn.getURN() != null) && (usn.getURN().equals((URN)ident)))
						Socket.sendPacket(new MSearchResponsePacket(getDescriptionURL(), usn));
				}
			}
			else if (ident instanceof SSDPUUID) {
				for (USN usn: usnList) {
					if  ((usn.getUUID() != null) && (usn.getUUID().equals((SSDPUUID)ident) && (usn.getURN() == null))) {
						Socket.sendPacket(new MSearchResponsePacket(getDescriptionURL(), usn));
					}
				}
			}
		}
		catch (IOException e) {}
	}

	public boolean matches(SSDPUUID UUID) {
		return (root.getUDN().equals(UUID));
	}

	public RootDevice getRootDevice() {
		return root;
	}

	public HTTPXMLBody getDeviceDescriptionBody() {
		return deviceDescription;
	}

	public HTTPXMLBody getServiceDescriptionBody(ServiceIDURN service) {
		return serviceDescriptions.get(service);
	}

	private Service findService(Device d, ServiceIDURN serviceId) {
		Iterator<Service> it = d.getServices();
		while (it.hasNext()) {
			Service service = it.next();
			if (service.getID().getServiceId().equals(serviceId.getServiceId()))
				return service;
		}
		return null;
	}

	public Service getService(ServiceIDURN serviceId) {
		Service s = findService(root, serviceId);
		Iterator<Device> it = root.getEmbeddedDevices();
		while (it.hasNext() && (s == null)) {
			s = findService(it.next(), serviceId);
			if (s != null)
				return s;
		}
		return s;
	}

	public void stop() {
		host.removeAdvertiser(this);
		for (USN usn: usnList)
			sendPacket(new DiscoveryNotifyPacket("", usn, false));
	}

}
