package net.kodeninja.UPnP.description;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ServiceStateVariable implements UPnPDescription {

	protected boolean sendEvents = true;
	protected String name = null;
	protected String dataType = null;
	protected Object defaultValue = null;
	protected ServiceStateVariableAllowedValue allowedValue = null;

	public ServiceStateVariable(String name, String dataType) {
		this(name, dataType, null, null);
	}
	
	public ServiceStateVariable(String name, String dataType, boolean sendEvents) {
		this(name, dataType, null, null, sendEvents);
	}
	
	public ServiceStateVariable(String name, String dataType, Object defaultValue) {
		this(name, dataType, defaultValue, null);
	}
	
	public ServiceStateVariable(String name, String dataType, Object defaultValue, boolean sendEvents) {
		this(name, dataType, defaultValue, null, sendEvents);
	}
	
	public ServiceStateVariable(String name, String dataType, ServiceStateVariableAllowedValue allowedValue) {
		this(name, dataType, null, allowedValue, true);
	}
	
	public ServiceStateVariable(String name, String dataType, ServiceStateVariableAllowedValue allowedValue, boolean sendEvents) {
		this(name, dataType, null, allowedValue, sendEvents);
	}
	
	public ServiceStateVariable(String name, String dataType, Object defaultValue, ServiceStateVariableAllowedValue allowedValue) {
		this(name, dataType, defaultValue, allowedValue, true);
	}
	
	public ServiceStateVariable(String name, String dataType, Object defaultValue, ServiceStateVariableAllowedValue allowedValue, boolean sendEvents) {
		this.name = name;
		this.dataType = dataType;
		this.defaultValue = defaultValue;
		this.allowedValue = allowedValue;
		this.sendEvents = sendEvents;
	}
	
	public boolean sendsEvents() {
		return sendEvents;
	}

	public String getName() {
		return name;
	}

	public String getDataType() {
		return dataType;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public ServiceStateVariableAllowedValue getAllowedValue() {
		return allowedValue;
	}
	
	public void writeToXML(Node root) {
		Document doc = root.getOwnerDocument();
		
		Element base = doc.createElement("stateVariable");
		Element tmp;
		
		root.appendChild(base);
		
		if (sendEvents)
			base.setAttribute("sendEvents", "yes");
		else
			base.setAttribute("sendEvents", "no");

		base.appendChild(tmp = doc.createElement("name"));
		tmp.setTextContent(name);
		
		base.appendChild(tmp = doc.createElement("dataType"));
		tmp.setTextContent(dataType);
		
		if ((defaultValue != null) && (defaultValue.equals("") == false)) {
			base.appendChild(tmp = doc.createElement("defaultValue"));
			tmp.setTextContent(defaultValue.toString());
		}
		
		if (allowedValue != null)
			allowedValue.writeToXML(base);
		
	}
	
	public String toString() {
		return name + "[" + dataType + "]";
	}

}
