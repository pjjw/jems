package net.kodeninja.jem.server.content.xml;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.content.MediaSource;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.Metadata;
import net.kodeninja.jem.server.storage.MetadataType;
import net.kodeninja.util.KNModuleInitException;
import net.kodeninja.util.KNServiceModule;
import net.kodeninja.util.MalformedMimeTypeException;
import net.kodeninja.util.MimeType;

import org.w3c.dom.Node;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class XMLMediaSource2 extends DefaultHandler implements MediaSource, KNServiceModule {

	protected File listFile = new File("JemsMediaList.xml");
	protected String localName = "";
	protected MediaItem currentItem = null;
	protected MetadataType currentType = null;
	protected int itemCount;

	public String getSourceName() {
		return "Source Name";
	}

	public String getName() {
		return "XML SAX Media Storage";
	}

	public int getVersionMajor() {
		return 0;
	}

	public int getVersionMinor() {
		return 1;
	}

	public int getVersionRevision() {
		return 0;
	}

	public void xmlInit(Node xmlNode) throws KNModuleInitException {
		localName = xmlNode.getAttributes().getNamedItem("name").getNodeValue();
		for (Node modNode = xmlNode.getFirstChild(); modNode != null; modNode = modNode.getNextSibling()) {
			if (modNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (modNode.getNodeName().equals("path"))
				listFile = new File(modNode.getTextContent().trim());
		}
	}

	public void init() throws KNModuleInitException {
		if (listFile.exists() == false)
			return;
		try {
			JemServer.getMediaStorage().startUpdate();
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(this);
			itemCount = 0;
			xr.parse(new InputSource(new FileReader(listFile)));
			JemServer.getInstance().addLog("[" + localName + "] Restored " + itemCount + (itemCount == 1 ? " item." : " items."));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JemServer.getMediaStorage().finishUpdate();
	}

	public void deinit() throws KNModuleInitException {
		try {
			StreamResult sr = new StreamResult(new FileWriter(listFile));
			SAXTransformerFactory t = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

			TransformerHandler hd = t.newTransformerHandler();
			Transformer serializer = hd.getTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT,"yes");
			hd.setResult(sr);

			hd.startDocument();

			AttributesImpl atts = new AttributesImpl();
			hd.startElement("", "", "MediaList", atts);
			for (MediaItem item: JemServer.getMediaStorage().getAllMedia()) {
				atts.addAttribute("", "", "MimeType", "CDATA", JemServer.getMediaStorage().getMimeType(item).toString());
				atts.addAttribute("", "", "URI", "CDATA", item.getURI().toString());
				hd.startElement("", "", "MediaItem", atts);
				atts.clear();

				for (Metadata metadata: item.getMetadataList()) {
					String name = metadata.getType().toString();
					hd.startElement("", "", name, atts);
					char[] buf = filterValue(metadata.getValue()).toCharArray();
					hd.characters(buf, 0, buf.length);
					hd.endElement("", "", name);
				}
				hd.endElement("", "", "MediaItem");
			}
			hd.endElement("", "", "MediaList");

			hd.endDocument();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}

	private String filterValue(String in) {
		StringBuffer buf = new StringBuffer(in);
		int i = 0;
		while (i < buf.length())
			if (buf.charAt(i) < 32)
				buf.deleteCharAt(i);
			else
				i++;

		return buf.toString();
	}

	@Override
	public void startElement (String uri, String name, String qName, Attributes atts) {
		if (name.equals("MediaList") == false) {
			if (name.equals("MediaItem")) {
				try {
					URI itemURI = new URI(atts.getValue("URI"));
					MimeType itemType = new MimeType(atts.getValue("MimeType"));
					currentItem = JemServer.getMediaStorage().addNewMedia(itemURI, itemType);
					currentType = null;
					itemCount++;
				} catch (URISyntaxException e) {
				} catch (MalformedMimeTypeException e) {
				}
			}
			else if (currentItem != null) {
				currentType = MetadataType.valueOf(name);
			}
		}
	}

	@Override
	public void endElement (String uri, String name, String qName) {
		if (currentType != null)
			currentType = null;
		else if (currentItem != null)
			currentItem = null;
	}

	@Override
	public void characters (char ch[], int start, int length) {
		if ((currentItem != null) && (currentType != null))
			JemServer.getMediaStorage().addMediaMetadata(currentItem, currentType, new String(ch, start, length));
	}

}
