package net.kodeninja.jem.server.content.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MediaSource;
import net.kodeninja.jem.server.content.MediaUpdateHook;
import net.kodeninja.jem.server.content.MetadataTypes;
import net.kodeninja.util.KNXMLModuleInitException;

public class XMLMediaSource implements MediaSource, MediaUpdateHook {
	protected File listFile = new File("JemsMediaList.xml");
	protected String localName = "";
	protected boolean disabled = false;

	public XMLMediaSource() {
		int count = 0;

		if (listFile.exists()) {
			Document mediaList;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				mediaList = builder.parse(listFile);

				// Find the root key
				Node baseNode = mediaList.getChildNodes().item(0);
				if (baseNode.getNodeName().equals("MediaList") != true)
					return;

				for (int i = 0; i < baseNode.getChildNodes().getLength(); i++) {
					Node mediaNode = baseNode.getChildNodes().item(i);
					if ((mediaNode.getNodeType() == Node.ELEMENT_NODE)
							&& (mediaNode.getNodeName().equals("MediaItem"))) {
						MediaItem mi = XMLMediaItem.createFromXML(mediaNode);
						if (mi != null)
							if (JemServer.getInstance().addMedia(mi))
								count++;
					}
				}
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				System.err.println("Disabling plugin...");
				disabled = true;
			} catch (SAXException e) {
				e.printStackTrace();
				System.err.println("Disabling plugin...");
				disabled = true;
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Disabling plugin...");
				disabled = true;
			}
		}
		JemServer.getInstance().addLog(
										"[JemsMediaList.xml] Added " + count
												+ " new files.");
		JemServer.getInstance().addMediaUpdateHook(this);
	}

	public void xmlInit(Node xmlNode) throws KNXMLModuleInitException {
		localName = xmlNode.getAttributes().getNamedItem("name").getNodeValue();
		for (Node modNode = xmlNode.getFirstChild(); modNode != null; modNode = modNode
				.getNextSibling()) {
			if (modNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (modNode.getNodeName().equals("path"))
				listFile = new File(modNode.getTextContent().trim());
		}
	}

	public String getSourceName() {
		return "Saved Media List";
	}

	public String getName() {
		return "XML Media Source [" + localName + "]";
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

	public void mediaChanged() {
		if (disabled)
			return;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Node root = doc.createElement("MediaList");
			doc.appendChild(root);

			Iterator<MediaItem> miIt = JemServer.getInstance().getAllMedia();
			while (miIt.hasNext()) {
				MediaItem mi = miIt.next();
				Element miNode = doc.createElement("MediaItem");
				root.appendChild(miNode);
				miNode.setAttribute("MimeType", mi.getMediaMimeType()
						.toString());
				miNode.setAttribute("URI", mi.getMediaURI().toString());

				Iterator<MetadataTypes> mdIt = mi.getAllSetMetadataTags();
				while (mdIt.hasNext()) {
					MetadataTypes md = mdIt.next();
					Element mdNode = doc.createElement(md.name());
					mdNode.setTextContent(mi.getMetadata(md));
					miNode.appendChild(mdNode);
				}
			}

			Writer w = new OutputStreamWriter(new FileOutputStream(new File(
					"JemsMediaList.xml")), "UTF-8");
			StreamResult wr = new StreamResult(w);
			DOMSource src = new DOMSource(doc);
			Transformer serializer = TransformerFactory.newInstance()
					.newTransformer();
			serializer.transform(src, wr);
			w.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
