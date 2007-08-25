package net.kodeninja.jem.server.UPnP.description.internal;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BrowseResult {

	private Document doc;
	private Element root;
	private int count = 0;
	private int index = 0;

	private int startIndex;
	private int maxCount;

	private String[] filters;
	//private String[] sorting;

	public BrowseResult(int start, int count, String filter, String sort) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();

			root = doc.createElement("DIDL-Lite");
			doc.appendChild(root);
			root.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
			root.setAttribute("xmlns:upnp", "urn:schemas-upnp-org:metadata-1-0/upnp/");
			root.setAttribute("xmlns", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/");

			startIndex = start;
			maxCount = count;
			if (filter.trim().equals("") || filter.trim().equals("*"))
				filters = null;
			else
				filters = filter.toLowerCase().split(",");

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private boolean isFieldAllowed(String field) {
		if (filters == null)
			return true;
		field = field.toLowerCase();
		for (String filter: filters)
			if (filter.equals(field))
				return true;
		return false;
	}

	public void add(MediaTree item) {
		if ((index >= startIndex) && ((index <= startIndex + maxCount) || (maxCount == 0))) {
			Element element;
			if (item instanceof MediaTreeContainer) {
				element = doc.createElement("container");
				element.setAttribute("childCount", "" + item.getChildernCount());
			}
			else {
				element = doc.createElement("item");
			}

			element.setAttribute("id", item.getId());
			element.setAttribute("parentID", (item.getParent() != null ? item.getParent().getId() : "-1"));
			element.setAttribute("restricted", "1");
			if (isFieldAllowed("searchable"))
				element.setAttribute("searchable", "1");
			root.appendChild(element);

			Element titleElement = doc.createElement("dc:title");
			element.appendChild(titleElement);
			titleElement.setTextContent(item.getName());

			for (MediaTreeAttribute attr: item.getAttributes()) {
				if (attr.getName().equalsIgnoreCase("res") || (attr.getName().equalsIgnoreCase("upnp:class")) || isFieldAllowed(attr.getName())) {
					Element attrElement = doc.createElement(attr.getName());
					element.appendChild(attrElement);
					attrElement.setTextContent(attr.getValue().toString());
					Iterator<String> it = attr.getAttributeIterator();
					while (it.hasNext()) {
						String name = it.next();
						if ((attr.getName().equals("res") && name.equalsIgnoreCase("protocolInfo")) ||
								isFieldAllowed(attr.getName() + "@" + name))
							attrElement.setAttribute(name, attr.getAttributeValue(name).toString());
					}
				}
			}

			count++;
		}
		index++;
	}

	public int getCount() {
		return count;
	}

	public String toString() {
		try {
			StringWriter w = new StringWriter();
			StreamResult wr = new StreamResult(w);
			DOMSource src = new DOMSource(doc);
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.transform(src, wr);
			String result = w.getBuffer().toString(); 
			w.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (TransformerException e) {
			e.printStackTrace();
			return "";
		}
	}

}
