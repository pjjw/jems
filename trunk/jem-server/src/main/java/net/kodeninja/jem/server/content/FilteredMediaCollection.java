package net.kodeninja.jem.server.content;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;

import net.kodeninja.util.KNXMLModuleInitException;
import net.kodeninja.util.MalformedMimeTypeException;
import net.kodeninja.util.MimeType;

public class FilteredMediaCollection extends CustomMediaCollectionImpl {
	protected Set<MimeType> mimeList = Collections
	.synchronizedSet(new HashSet<MimeType>());
	protected Set<String> artistList = Collections
	.synchronizedSet(new HashSet<String>());
	protected Set<String> setList = Collections
	.synchronizedSet(new HashSet<String>());

	@Override
	public void xmlInit(Node xmlNode) throws KNXMLModuleInitException {
		super.xmlInit(xmlNode);
		for (Node mimeNode = xmlNode.getFirstChild(); mimeNode != null; mimeNode = mimeNode
		.getNextSibling()) {
			if (mimeNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (mimeNode.getNodeName().equals("mimetype")) {
				try {
					mimeList.add(new MimeType(mimeNode.getTextContent().trim()));
				} catch (MalformedMimeTypeException e) {
					throw new KNXMLModuleInitException(
					"Invalid mimetype in collection.");
				}
			}
			else if (mimeNode.getNodeName().equals("artist"))
				artistList.add(mimeNode.getTextContent().trim());
			else if (mimeNode.getNodeName().equals("set"))
				setList.add(mimeNode.getTextContent().trim());
		}
	}

	public boolean acceptMedia(MediaItem media) {
		if ((mimeList.contains(media.getMediaMimeType()) || (mimeList.isEmpty())) &&
				(artistList.contains(media.getMetadata(MetadataTypes.Artist)) ||
						(artistList.isEmpty())) &&
						(setList.contains(media.getMetadata(MetadataTypes.Set)) ||
								setList.isEmpty()))
			return true;
		else
			return false;
	}

	public String getName() {
		return "Filtered Media Collection";
	}

	public int getVersionMajor() {
		return 0;
	}

	public int getVersionMinor() {
		return 2;
	}

	public int getVersionRevision() {
		return 0;
	}

}
