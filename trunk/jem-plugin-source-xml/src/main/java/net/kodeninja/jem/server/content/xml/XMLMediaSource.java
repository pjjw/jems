package net.kodeninja.jem.server.content.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.content.MediaSource;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.MediaUpdateHook;
import net.kodeninja.jem.server.storage.Metadata;
import net.kodeninja.jem.server.storage.MetadataType;
import net.kodeninja.util.KNModuleInitException;
import net.kodeninja.util.KNServiceModule;
import net.kodeninja.util.MalformedMimeTypeException;
import net.kodeninja.util.MimeType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLMediaSource implements MediaSource, KNServiceModule, MediaUpdateHook {
	protected File listFile = new File("JemsMediaList.xml");
	protected String localName = "";
	protected boolean disabled = false;

	public void newItemFromXML(Node xmlNode) {
		MimeType mimetype;
		URI uri;

		Node mtNode = xmlNode.getAttributes().getNamedItem("MimeType");
		Node pathNode = xmlNode.getAttributes().getNamedItem("URI");

		if ((mtNode == null) || (pathNode == null))
			return ;

		try {
			mimetype = new MimeType(mtNode.getNodeValue());
			uri = new URI(pathNode.getNodeValue());
		} catch (MalformedMimeTypeException e) {
			return ;
		} catch (URISyntaxException e) {
			return ;
		}

		if (JemServer.getMediaStorage().mediaExists(uri) == false) {
			MediaItem item = JemServer.getMediaStorage().addNewMedia(uri, mimetype);

			for (int i = 0; i < xmlNode.getChildNodes().getLength(); i++) {
				Node mdNode = xmlNode.getChildNodes().item(i);
				if (mdNode.getNodeType() == Node.ELEMENT_NODE)
					try {
						MetadataType type = MetadataType.valueOf(mdNode.getNodeName());
						if (type != null)
							JemServer.getMediaStorage().addMediaMetadata(item, type, mdNode.getTextContent());
					} catch (IllegalArgumentException e) {
					}
			}
		}
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
		int count = 0;

		JemServer.getMediaStorage().startUpdate();
		if (listFile.exists()) {
			Document mediaList;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
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
						newItemFromXML(mediaNode);
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
		JemServer.getInstance().addLog("[JemsMediaList.xml] Restored " + count + " files.");
		JemServer.getMediaStorage().finishUpdate();

		JemServer.getMediaStorage().hookMediaUpdate(this);
	}
	
	public void deinit() throws KNModuleInitException {
		mediaChanged();
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
		return 2;
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
	
	public void mediaChanged() {
		if (disabled)
			return;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Node root = doc.createElement("MediaList");
			doc.appendChild(root);

			for (MediaItem item: JemServer.getMediaStorage().getAllMedia()) {
				Element miNode = doc.createElement("MediaItem");
				root.appendChild(miNode);
				miNode.setAttribute("MimeType", JemServer.getMediaStorage().getMimeType(item).toString());
				miNode.setAttribute("URI", item.getURI().toString());

				for (Metadata metadata: item.getMetadataList()) {
					Element mdNode = doc.createElement(metadata.getType().name());
					mdNode.setTextContent(filterValue(metadata.getValue()));
					miNode.appendChild(mdNode);
				}
			}

			File outputFile = new File(listFile.toString() + ".tmp");
			Writer w = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
			StreamResult wr = new StreamResult(w);
			DOMSource src = new DOMSource(doc);
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.transform(src, wr);
			w.close();
			if (listFile.exists())
				listFile.delete();
			outputFile.renameTo(listFile);

		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		catch (TransformerException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
