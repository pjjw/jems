package net.kodeninja.UPnP.description;

import java.util.Iterator;
import java.util.LinkedList;
import java.net.URL;

import net.kodeninja.UPnP.identifiers.DeviceTypeURN;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class RootDevice extends Device {
	protected LinkedList<Device> embeddedDevices = new LinkedList<Device>();
	protected URL presentationURL = null;

	public RootDevice(DeviceTypeURN type) {
		super(type);
	}

	public void writeToXML(Node root) {
		super.writeToXML(root);
		for (int i = 0; i < root.getChildNodes().getLength(); i++)
			if (root.getChildNodes().item(i).getNodeName().equals("device")) {
				root = root.getChildNodes().item(i);
				break;
			}

		if (root.getNodeName().equals("device") == false)
			return ;

		Document doc = root.getOwnerDocument();

		if ((embeddedDevices != null) && (embeddedDevices.size() > 0)) {
			Element deviceList = doc.createElement("deviceList");
			root.appendChild(deviceList);

			for (Device d: embeddedDevices)
				d.writeToXML(deviceList);
		}
		
		if (presentationURL != null) {
			Element tmp = doc.createElement("presentationURL");
			root.appendChild(tmp);
			tmp.setTextContent("" + presentationURL);
		}
	}

	public Iterator<Device> getEmbeddedDevices() {
		return embeddedDevices.iterator();
	}
}
