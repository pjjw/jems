package net.kodeninja.UPnP.description;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ServiceAction implements UPnPDescription {

	protected String name;
	protected List<ServiceActionArgument> inArgs = new LinkedList<ServiceActionArgument>();
	protected List<ServiceActionArgument> outArgs = new LinkedList<ServiceActionArgument>();
	
	public ServiceAction(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void addArg(ServiceActionArgument arg) {
		if (arg.isDirIn())
			inArgs.add(arg);
		else
			outArgs.add(arg);
	}
	
	public void writeToXML(Node root) {
		Document doc = root.getOwnerDocument();
		
		Element base = doc.createElement("action");
		Element tmp;
		
		root.appendChild(base);
		
		base.appendChild(tmp = doc.createElement("name"));
		tmp.setTextContent(name);
		
		base.appendChild(tmp = doc.createElement("argumentList"));
		for (ServiceActionArgument arg: inArgs)
			arg.writeToXML(tmp);
		for (ServiceActionArgument arg: outArgs)
			arg.writeToXML(tmp);
	}
	
	public Iterator<ServiceActionArgument> getInArgs() {
		return inArgs.iterator();
	}
	
	public Iterator<ServiceActionArgument> getOutArgs() {
		return outArgs.iterator();
	}

	public String toString() {
		return name;
	}
	
}
