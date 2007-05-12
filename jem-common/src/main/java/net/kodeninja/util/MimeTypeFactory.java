package net.kodeninja.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Node;

public class MimeTypeFactory implements KNModule, KNXMLModule {
	private Map<String, MimeType> mimeTypes = new HashMap<String, MimeType>();

	public MimeType getMimeType(File filename) {
		Iterator<String> it = mimeTypes.keySet().iterator();
		while (it.hasNext()) {
			String tmp = it.next();
			if (filename.toString().toLowerCase().endsWith(tmp.toLowerCase()))
				return mimeTypes.get(tmp);
		}
		return MimeType.WILDCARD;
	}

	public void setMimeType(String extension, MimeType mime) {
		mimeTypes.put(extension, mime);
	}

	public void xmlInit(Node xmlNode) throws KNXMLModuleInitException {
		for (Node modNode = xmlNode.getFirstChild(); modNode != null; modNode = modNode
				.getNextSibling()) {
			if (modNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (modNode.getNodeName().equals("mimetype")) {
				MimeType mime = null;
				try {
					mime = new MimeType(modNode.getAttributes()
							.getNamedItem("type").getNodeValue().trim()
							.toLowerCase());
				} catch (MalformedMimeTypeException e) {
					throw new KNXMLModuleInitException("Invalid MimeType.");
				}

				for (Node subNode = modNode.getFirstChild(); subNode != null; subNode = subNode
						.getNextSibling()) {
					if (subNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (subNode.getNodeName().equals("extension"))
						mimeTypes.put(subNode.getTextContent(), mime);
					else
						xmlMimeTypeInit(mime, subNode);
				}
			}
		}
	}

	public void xmlMimeTypeInit(MimeType mime, Node xmlNode)
			throws KNXMLModuleInitException {
	}

	public String getName() {
		return "Mime Type Factory";
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

}
