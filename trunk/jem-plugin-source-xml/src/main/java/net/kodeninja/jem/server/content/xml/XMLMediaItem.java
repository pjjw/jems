package net.kodeninja.jem.server.content.xml;

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Node;

import net.kodeninja.jem.server.content.MediaItemImpl;
import net.kodeninja.jem.server.content.MetadataTypes;
import net.kodeninja.util.MalformedMimeTypeException;
import net.kodeninja.util.MimeType;

public class XMLMediaItem extends MediaItemImpl {
	public static XMLMediaItem createFromXML(Node xmlNode) {
		MimeType mt;
		URI path;

		Node mtNode = xmlNode.getAttributes().getNamedItem("MimeType");
		Node pathNode = xmlNode.getAttributes().getNamedItem("URI");

		if ((mtNode == null) || (pathNode == null))
			return null;

		try {
			mt = new MimeType(mtNode.getNodeValue());
			path = new URI(pathNode.getNodeValue());
		} catch (MalformedMimeTypeException e) {
			return null;
		} catch (URISyntaxException e) {
			return null;
		}

		XMLMediaItem retVal = new XMLMediaItem(mt, path);
		for (int i = 0; i < xmlNode.getChildNodes().getLength(); i++) {
			Node mdNode = xmlNode.getChildNodes().item(i);
			if (mdNode.getNodeType() == Node.ELEMENT_NODE)
				try {
					MetadataTypes type = MetadataTypes.valueOf(mdNode
							.getNodeName());
					if (type != null)
						retVal.setMetadata(type, mdNode.getTextContent());
				} catch (IllegalArgumentException e) {
				}
		}

		return retVal;
	}

	private XMLMediaItem(MimeType Type, URI path) {
		super(Type, path);
	}

	@Override
	public int getVersionMajor() {
		return 0;
	}

	@Override
	public int getVersionMinor() {
		return 1;
	}

	@Override
	public int getVersionRevision() {
		return 0;
	}

}
