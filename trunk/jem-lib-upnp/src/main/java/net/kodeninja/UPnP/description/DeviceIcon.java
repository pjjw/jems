package net.kodeninja.UPnP.description;

import java.net.URI;

import net.kodeninja.util.MimeType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DeviceIcon implements UPnPDescription {
	protected MimeType imageMime = null;
	protected int width = 0;
	protected int height = 0;
	protected int depth = 0;
	protected URI imageURL = null;
	
	public DeviceIcon(MimeType mime, int width, int height, int depth, URI uri) {
		this.imageMime = mime;
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.imageURL = uri;
	}
	
	public void writeToXML(Node root) {
		if ((imageMime == null) || (imageURL == null))
			return ;
		
		Document doc = root.getOwnerDocument();
		Element tmp;
		
		Element icon = doc.createElement("icon");
		
		icon.appendChild(tmp = doc.createElement("mimetype"));
		tmp.setTextContent("" + imageMime);
		
		icon.appendChild(tmp = doc.createElement("width"));
		tmp.setTextContent("" + width);
		
		icon.appendChild(tmp = doc.createElement("height"));
		tmp.setTextContent("" + height);
		
		icon.appendChild(tmp = doc.createElement("depth"));
		tmp.setTextContent("" + depth);
		
		icon.appendChild(tmp = doc.createElement("url"));
		tmp.setTextContent("" + imageURL);
		
		root.appendChild(icon);
	}
}
