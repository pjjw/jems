package net.kodeninja.UPnP.internal.control;

import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.UPnPAdvertiseOperation;
import net.kodeninja.UPnP.description.Service;
import net.kodeninja.UPnP.description.ServiceStateVariable;
import net.kodeninja.UPnP.internal.description.UPnPURIParser;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.packet.extra.HTTPXMLBody;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.URIHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ControlQueryURI extends UPnPURIParser implements URIHandler {

	public ControlQueryURI(UPnP host) {
		super(host);
	}

	public  HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<? extends HTTPBody> Packet) {

		if (parseURI(Packet.getHeader().getLocation().getPath()) == false)
			return null;

		if (serviceId == null)
			return null;

		String soap = Packet.getHeader().getParameter("SoapAction");
		if (soap == null)
			return null;
		
		if (soap.equals("urn:schemas-upnp-org:control-1-0#QueryStateVariable") == false)
			return null;
		
		UPnPAdvertiseOperation advertiser = host.getAdvertisterByRootUUID(deviceId);
		if (advertiser == null)
			return null;
		
		Service service = advertiser.getService(serviceId);

		if (service == null)
			return null;
		
		HTTPXMLBody soapBody = new HTTPXMLBody();
		if (Packet.readBody(soapBody) == false)
			return null;

		// Setup document
		Document inDoc = soapBody.getDocument();
		
		//Args
		ServiceStateVariable queriedArg = null; 
		
		//Parse the soap envelope
		Node tmpElement = inDoc.getDocumentElement().getElementsByTagName("u:QueryStateVariable").item(0);
		for (Node argElement = tmpElement.getFirstChild(); argElement != null; argElement = argElement.getNextSibling()) {
			if (argElement.getNodeType() != Node.ELEMENT_NODE)
				continue ;

			//Setup in args
			Iterator<ServiceStateVariable> argIt = service.stateVariables();
			while (argIt.hasNext()) {
				ServiceStateVariable arg = argIt.next();
				if (argElement.getNodeName().equals(arg.getName())) {
					queriedArg = arg;
					break;
				}
			}
				
			//TODO Better handling of invalid argument
			System.err.println("Could not find argument: " + argElement.getNodeName());
		}
		
		if (queriedArg == null)
			return null;
		
		//Build resulting envelope
		HTTPPacket<HTTPXMLBody> response = null;
		Document outDoc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			outDoc = builder.newDocument();
			
			//Set up root node
			Element root = outDoc.createElement("s:Envelope");
			outDoc.appendChild(root);
			
			root.setAttribute("xmlns:s", "http://schemas.xmlsoap.org/soap/envelope/");
			root.setAttribute("s:encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
			
			Element bodyElement = outDoc.createElement("s:Body");
			root.appendChild(bodyElement);
			
			Element actionElement = outDoc.createElement("u:QueryStateVariableResponse");
			bodyElement.appendChild(actionElement);
			
			actionElement.setAttribute("xmlns:u", "urn:schemas-upnp-org:control-1-0");
			
			//Set up return values
			Element returnElement = outDoc.createElement("return");
			actionElement.appendChild(returnElement);
			
			returnElement.setTextContent(service.getStateVarValue(queriedArg).toString());
			
			//Build packet
			HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1, HTTPResponseCode.HTTP_200_OK);
			HTTPXMLBody body = new HTTPXMLBody(outDoc);
			response = new HTTPPacket<HTTPXMLBody>(header, body);
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return response;
	}

}
