package net.kodeninja.UPnP.description;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ServiceActionArgument implements UPnPDescription {

	protected String name;
	protected boolean dirIn;
	protected boolean isRetVal;
	protected ServiceStateVariable relatedStateVar;
	
	public ServiceActionArgument(String name, boolean dirIn, boolean isRetVal, ServiceStateVariable relatedStateVar) {
		this.name = name;
		this.dirIn = dirIn;
		this.isRetVal = isRetVal;
		this.relatedStateVar = relatedStateVar;	
	}
	
	public String getName() {
		return name;
	}

	public boolean isDirIn() {
		return dirIn;
	}

	public boolean isRetVal() {
		return isRetVal;
	}

	public ServiceStateVariable getRelatedStateVar() {
		return relatedStateVar;
	}
	
	public void writeToXML(Node root) {
		Document doc = root.getOwnerDocument();
		
		Element base = doc.createElement("argument");
		Element tmp;
		
		root.appendChild(base);
		
		base.appendChild(tmp = doc.createElement("name"));
		tmp.setTextContent(name);

		base.appendChild(tmp = doc.createElement("direction"));
		if (dirIn)
			tmp.setTextContent("in");
		else
			tmp.setTextContent("out");
		
		if (isRetVal)
			base.appendChild(doc.createElement("retval"));
		
		base.appendChild(tmp = doc.createElement("relatedStateVariable"));
		tmp.setTextContent(relatedStateVar.getName());
	}

	public String toString() {
		return name + "[" + relatedStateVar.getDataType() + "]";
	}
	
}
