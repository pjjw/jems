package net.kodeninja.UPnP.description;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ServiceStateVariableAllowedValueRange implements ServiceStateVariableAllowedValue {

	protected String min = null;
	protected String max = null;
	protected String step = null;
	
	public void writeToXML(Node root) {
		Document doc = root.getOwnerDocument();
		
		Element base = doc.createElement("allowedValueRange");
		Element tmp;
		
		root.appendChild(base);
		
		base.appendChild(tmp = doc.createElement("minimum"));
		tmp.setTextContent(min);
		
		base.appendChild(tmp = doc.createElement("maximum"));
		tmp.setTextContent(max);
		
		base.appendChild(tmp = doc.createElement("step"));
		tmp.setTextContent(step);
	}

}
