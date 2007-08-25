package net.kodeninja.UPnP.description;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ServiceStateVariableAllowedValueList implements ServiceStateVariableAllowedValue {

	protected List<String> allowedValues = new LinkedList<String>();
	
	public void addValue(String val) {
		allowedValues.add(val);
	}
	
	public void writeToXML(Node root) {
		Document doc = root.getOwnerDocument();
		
		Element base = doc.createElement("allowedValueList");
		Element tmp;
		
		root.appendChild(base);

		for (String val: allowedValues) {
			base.appendChild(tmp = doc.createElement("allowedValue"));
			tmp.setTextContent(val);
		}

	}

}
