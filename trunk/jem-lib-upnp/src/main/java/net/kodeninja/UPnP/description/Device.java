package net.kodeninja.UPnP.description;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

import net.kodeninja.UPnP.identifiers.DeviceTypeURN;
import net.kodeninja.UPnP.identifiers.SSDPUUID;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class Device implements UPnPDescription {
	private DeviceTypeURN deviceType = null;
	
	//Need definition
	protected SSDPUUID UDN = null;
	protected String friendlyName = "";
	protected String manufacturer = "";
	protected URL manufacturerURL = null;
	protected String modelName = "";
	protected String modelNumber = null;
	protected URL modelURL = null;
	protected String serialNumber = "";
	protected LinkedList<DeviceIcon> iconList = new LinkedList<DeviceIcon>();
	protected LinkedList<Service> services = new LinkedList<Service>();
	
	public Device(DeviceTypeURN type) {
		deviceType = type;
	}
	
	protected Element createDescription(Document doc) {
		Element device = doc.createElement("device");
		Element tmp;
		
		device.appendChild(tmp = doc.createElement("deviceType"));
		tmp.setTextContent("" + deviceType);
		
		device.appendChild(tmp = doc.createElement("friendlyName"));
		tmp.setTextContent("" + friendlyName);
		
		device.appendChild(tmp = doc.createElement("manufacturer"));
		tmp.setTextContent("" + manufacturer);
		
		device.appendChild(tmp = doc.createElement("manufacturerURL"));
		tmp.setTextContent("" + manufacturerURL);
		
		device.appendChild(tmp = doc.createElement("modelName"));
		tmp.setTextContent("" + modelName);
		
		device.appendChild(tmp = doc.createElement("modelNumber"));
		tmp.setTextContent("" + modelNumber);
		
		device.appendChild(tmp = doc.createElement("modelURL"));
		tmp.setTextContent("" + modelURL);
		
		if (serialNumber.equals(""))
			serialNumber = UDN.getUUID();
		device.appendChild(tmp = doc.createElement("serialNumber"));
		tmp.setTextContent("" + serialNumber);
		
		device.appendChild(tmp = doc.createElement("UDN"));
		tmp.setTextContent("" + UDN);
		
		return device;
	}
	
	public void writeToXML(Node root) {
		Document doc = root.getOwnerDocument();
		Element device = createDescription(doc);
		Element tmp;
		if ((iconList != null) && (iconList.size() > 0)) { 
			device.appendChild(tmp = doc.createElement("iconList"));
			for (DeviceIcon icon : iconList)
				icon.writeToXML(tmp);
		}
		
		if ((services != null) && (services.size() > 0)) {
			device.appendChild(tmp = doc.createElement("serviceList"));
			for (Service service : services)
				service.writeToXML(tmp);
		}
		root.appendChild(device);
	}

	public SSDPUUID getUDN() {
		return UDN;
	}

	public DeviceTypeURN getType() {
		return deviceType; 
	}
	
	public Iterator<Service> getServices() {
		return services.iterator();
	}
	
}
