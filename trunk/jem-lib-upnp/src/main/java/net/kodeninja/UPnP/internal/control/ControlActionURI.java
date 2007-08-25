package net.kodeninja.UPnP.internal.control;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.UPnPAdvertiseOperation;
import net.kodeninja.UPnP.description.Service;
import net.kodeninja.UPnP.description.ServiceAction;
import net.kodeninja.UPnP.description.ServiceActionArgument;
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

public class ControlActionURI extends UPnPURIParser implements URIHandler {

	public ControlActionURI(UPnP host) {
		super(host);
	}

	public  HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<? extends HTTPBody> Packet) {

		if (parseURI(Packet.getHeader().getLocation().getPath()) == false)
			return null;

		try {
			if (serviceId == null)
				return null;

			String soap = Packet.getHeader().getParameter("SoapAction");
			if (soap == null)
				return null;

			soap = soap.substring(1, soap.length() - 1);
			int pos = soap.indexOf("#");
			if (pos < 0) return null;

			String soapService = soap.substring(0, pos);
			String soapActionName = soap.substring(pos + 1);

			UPnPAdvertiseOperation advertiser = host.getAdvertisterByRootUUID(deviceId);
			if (advertiser == null)
				return null;

			Service service = advertiser.getService(serviceId);

			if (service == null)
				return null;

			if (service.getType().toString().equals(soapService) == false)
				return null;

			ServiceAction action = service.getAction(soapActionName);

			if (action == null)
				throw new ControlException(401, "Invalid Action", service + " - Unimplemented Action: " + action);

			HTTPXMLBody soapBody = new HTTPXMLBody();
			if (Packet.readBody(soapBody) == false)
				return null;

			// Setup document
			Document inDoc = soapBody.getDocument();

			//Setup args
			Iterator<ServiceActionArgument> argIt;
			Map<ServiceActionArgument, Object> inArgs = new HashMap<ServiceActionArgument, Object>();
			Map<ServiceActionArgument, Object> outArgs = new HashMap<ServiceActionArgument, Object>();

			//Populate in args with defaults
			argIt = action.getInArgs();
			while (argIt.hasNext()) {
				ServiceActionArgument arg = argIt.next();
				Object defValue = arg.getRelatedStateVar().getDefaultValue();
				if (defValue != null)
					inArgs.put(arg, defValue);
			}

			//Populate out args with defaults
			argIt = action.getOutArgs();
			while (argIt.hasNext()) {
				ServiceActionArgument arg = argIt.next();
				Object defValue = arg.getRelatedStateVar().getDefaultValue();
				if (defValue != null)
					outArgs.put(arg, defValue);
			}

			//Parse the soap envelope
			Node tmpElement = inDoc.getDocumentElement().getElementsByTagName("u:" + soapActionName).item(0);
			nextArg:
				for (Node argElement = tmpElement.getFirstChild(); argElement != null; argElement = argElement.getNextSibling()) {
					if (argElement.getNodeType() != Node.ELEMENT_NODE)
						continue ;

					//Setup in args
					argIt = action.getInArgs();
					while (argIt.hasNext()) {
						ServiceActionArgument arg = argIt.next();
						if (argElement.getNodeName().equals(arg.getName())) {
							inArgs.put(arg, argElement.getTextContent());
							continue nextArg;
						}
					}

					//TODO Better handling of invalid argument
					System.err.println("Argument not registered: " + argElement.getNodeName());
				}


			service.processAction(action, inArgs, outArgs);

			//Build resulting envelope
			HTTPPacket<HTTPBody> response = null;
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

				Element actionElement = outDoc.createElement("u:" + action.getName() + "Response");
				bodyElement.appendChild(actionElement);

				actionElement.setAttribute("xmlns:u", soapService);

				//Set up return values
				argIt = action.getOutArgs();
				while (argIt.hasNext()) {
					ServiceActionArgument arg = argIt.next();
					Element argElement = outDoc.createElement(arg.getName());
					argElement.setAttribute("xmlns:dt", "urn:schemas-microsoft-com:datatypes");
					argElement.setAttribute("dt:dt", arg.getRelatedStateVar().getDataType());

					Object value = outArgs.get(arg);
					if (value != null)
						argElement.setTextContent(value.toString());
					//TODO Add error checking here!

					actionElement.appendChild(argElement);
				}

				//Build packet
				HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1, HTTPResponseCode.HTTP_200_OK);
				HTTPXMLBody body = new HTTPXMLBody(outDoc);
				response = new HTTPPacket<HTTPBody>(header, body);
			}
			catch (ParserConfigurationException e) {
				e.printStackTrace();
			}

			return response;
		} catch (ControlException e) {
			return new ControlErrorPacket(e.getCode(), e.getDescription());
		}
	}

}
