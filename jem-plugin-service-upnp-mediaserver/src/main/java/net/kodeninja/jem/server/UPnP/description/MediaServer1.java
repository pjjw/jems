package net.kodeninja.jem.server.UPnP.description;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.kodeninja.UPnP.description.DeviceIcon;
import net.kodeninja.UPnP.description.RootDevice;
import net.kodeninja.UPnP.identifiers.DeviceTypeURN;
import net.kodeninja.UPnP.identifiers.SSDPUUID;
import net.kodeninja.UPnP.identifiers.SSDPVersion;
import net.kodeninja.jem.server.UPnP.MediaServer;
import net.kodeninja.util.MimeType;

public class MediaServer1 extends RootDevice {

	protected MediaServer owner;
	protected ContentDirectory1 contentDirectory;
	protected ConnectionManager1 connectionManager;
	
	public MediaServer1(MediaServer owner, String name, String UUID) {
		super(new DeviceTypeURN("MediaServer", new SSDPVersion(1)));
		
		try {
			presentationURL = new URL(owner.getWWWService().getURLBase());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		this.owner = owner;
		friendlyName = "JEMS: " + name;
		UDN = new SSDPUUID(UUID);
		
		manufacturer = "KodeNinja.net";
		modelName = "Windows Media Connect";//owner.getName();
		modelNumber = "4.0";//owner.getVersionMajor() + "." + owner.getVersionMinor() + "." + owner.getVersionRevision();
		try {
			manufacturerURL = new URL("http://code.google.com/p/jems/");
			modelURL = new URL("http://code.google.com/p/jems/");
		}
		catch (MalformedURLException e) {
			// Do nothing
		}
		serialNumber = "";
		
		services.add(connectionManager = new ConnectionManager1(this));
		services.add(contentDirectory = new ContentDirectory1(owner, this));
		services.add(new X_MS_MediaReceiverRegistrar1(this));
		
		try {
			iconList.add(new DeviceIcon(new MimeType("image", "jpeg"), 120, 120, 24, new URI(presentationURL + "/deviceIcon1.png")));
			iconList.add(new DeviceIcon(new MimeType("image", "jpeg"), 48, 48, 24, new URI(presentationURL + "/deviceIcon2.png")));
			iconList.add(new DeviceIcon(new MimeType("image", "jpeg"), 32, 32, 24, new URI(presentationURL + "/deviceIcon3.png")));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	protected Element createDescription(Document doc) {
		Element device = super.createDescription(doc);
		Element dlnaTag = doc.createElement("dlna:X_DLNADOC");
		dlnaTag.setTextContent("DMS-1.50");
		dlnaTag.setAttribute("xmlns:dlna", "urn:schemas-dlna-org:device-1-0");
		//Uncomment to pretend to be DLNA compliant
		//device.appendChild(dlnaTag);
		return device;
	}
	
	public ContentDirectory1 getContentDirectory() {
		return contentDirectory;
	}

	public ConnectionManager1 getConnectionManager() {
		return connectionManager;
	}

}
