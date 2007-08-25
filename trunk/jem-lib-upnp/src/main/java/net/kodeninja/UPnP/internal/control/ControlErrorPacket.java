package net.kodeninja.UPnP.internal.control;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.packet.extra.HTTPXMLBody;

public class ControlErrorPacket extends HTTPPacket<HTTPXMLBody> {

	public ControlErrorPacket(int errorCode, String errorDescription) {
		super(new HTTPHeader(HTTPVersion.HTTP1_1, HTTPResponseCode.HTTP_500_INTERNAL_SERVER_ERROR));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			//Set up root node
			Element root = doc.createElement("s:Envelope");
			doc.appendChild(root);
			
			root.setAttribute("xmlns:s", "http://schemas.xmlsoap.org/soap/envelope/");
			root.setAttribute("s:encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
			
			Element bodyElement = doc.createElement("s:Body");
			root.appendChild(bodyElement);
			
			Element faultElement = doc.createElement("s:Fault");
			bodyElement.appendChild(faultElement);
			
			Element faultCode = doc.createElement("faultcode");
			bodyElement.appendChild(faultCode);
			faultCode.setTextContent("s:Client");
			
			Element faultString = doc.createElement("faultstring");
			bodyElement.appendChild(faultString);
			faultCode.setTextContent("UPnPError");
			
			Element detail = doc.createElement("detail");
			bodyElement.appendChild(detail);
			
			Element upnpError = doc.createElement("UPnPError");
			upnpError.setAttribute("xmlns", "urn:schemas-upnp-org:control-1-0");
			detail.appendChild(upnpError);
			
			Element errorCodeElement = doc.createElement("errorCode");
			errorCodeElement.setTextContent("" + errorCode);
			detail.appendChild(errorCodeElement);
			
			Element errorDescriptionElement = doc.createElement("errorDescription");
			errorDescriptionElement.setTextContent(errorDescription);
			detail.appendChild(errorDescriptionElement);

			body = new HTTPXMLBody(doc);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}
