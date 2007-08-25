package net.kodeninja.UPnP.description;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.identifiers.ServiceIDURN;
import net.kodeninja.UPnP.identifiers.ServiceTypeURN;
import net.kodeninja.UPnP.internal.control.ControlException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class Service implements UPnPDescription {
	private Device owner;
	private ServiceTypeURN serviceType = null;
	private ServiceIDURN serviceID = null;
	private boolean hasEventing;
	private Map<ServiceStateVariable, Object> stateVars = new HashMap<ServiceStateVariable, Object>();
	
	//Needs definition
	protected LinkedList<ServiceAction> actions = new LinkedList<ServiceAction>();
	
	public Service(Device owner, ServiceTypeURN serviceType) {
		this (owner, serviceType, new ServiceIDURN(serviceType.getType()));
	}
	
	public Service(Device owner, ServiceTypeURN serviceType, ServiceIDURN serviceID) {
		this(owner, serviceType, serviceID, true);
	}
	
	public Service(Device owner, ServiceTypeURN serviceType, ServiceIDURN serviceID, boolean hasEventing) {
		this.owner = owner;
		this.serviceType = serviceType;
		this.serviceID = serviceID;
		this.hasEventing = hasEventing;
	}
	
	public void writeServiceDescriptionToXML(Node root) {
		Document doc = root.getOwnerDocument();
		
		Element tmp;
		
		tmp = doc.createElement("actionList");
		root.appendChild(tmp);
		for (ServiceAction action: actions)
			action.writeToXML(tmp);
		
		
		tmp = doc.createElement("serviceStateTable");
		root.appendChild(tmp);
		for (ServiceStateVariable stateVar: stateVars.keySet())
			stateVar.writeToXML(tmp);
	}
	
	public void writeToXML(Node root) {

		if ((serviceType == null) || (serviceID == null))
			return;
		
		Document doc = root.getOwnerDocument();
		String baseURL =/* UPnP.getURLBase() + */ UPnP.getURIBase() + "device/" + owner.getUDN().getUUID() + "/service/" + serviceID.getServiceId();
		Element tmp;
		
		Element service = doc.createElement("service");
		
		service.appendChild(tmp = doc.createElement("serviceType"));
		tmp.setTextContent("" + serviceType);
		
		service.appendChild(tmp = doc.createElement("serviceId"));
		tmp.setTextContent("" + serviceID);
		
		service.appendChild(tmp = doc.createElement("SCPDURL"));
		tmp.setTextContent(baseURL + "/description.xml");
		
		service.appendChild(tmp = doc.createElement("controlURL"));
		tmp.setTextContent(baseURL + "/control.xml");
		
		service.appendChild(tmp = doc.createElement("eventSubURL"));
		if (hasEventing)
			tmp.setTextContent(baseURL + "/eventing.xml");
		else
			tmp.setTextContent("");
		
		root.appendChild(service);
	}

	public ServiceTypeURN getType() {
		return serviceType;
	}
	
	public ServiceIDURN getID() {
		return serviceID;
	}
	
	public ServiceAction getAction(String name) {
		for (ServiceAction action: actions)
			if (name.equals(action.getName()))
				return action;
		return null;
	}
	
	abstract public void processAction(ServiceAction action, Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs) throws ControlException; 

	protected void addStateVar(ServiceStateVariable stateVar) {
		 stateVars.put(stateVar, stateVar.getDefaultValue());
	}
	
	protected void setStateVarValue(ServiceStateVariable stateVar, Object value) {
		stateVars.put(stateVar, value);
		if (stateVar.sendsEvents())
			UPnP.announceStateChange(this, stateVar, value);
	}
	
	public Object getStateVarValue(ServiceStateVariable stateVar) {
		return stateVars.get(stateVar);
	}
	
	public Iterator<ServiceStateVariable> stateVariables() {
		return stateVars.keySet().iterator();
	}
	
	public String toString() {
		return serviceID.toString();
	}
	
}
