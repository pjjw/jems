package net.kodeninja.jem.server.content;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.Metadata;
import net.kodeninja.jem.server.storage.MetadataType;
import net.kodeninja.util.KNModuleInitException;
import net.kodeninja.util.MalformedMimeTypeException;
import net.kodeninja.util.MimeType;

import org.w3c.dom.Node;

public class FilteredMediaCollection extends CustomMediaCollectionImpl {
	protected Set<MimeType> mimeList = Collections.synchronizedSet(new HashSet<MimeType>());
	protected Set<String> artistList = Collections.synchronizedSet(new HashSet<String>());
	protected Set<String> setList = Collections.synchronizedSet(new HashSet<String>());
	protected Set<String> pathList = Collections.synchronizedSet(new HashSet<String>());

	@Override
	public void xmlInit(Node xmlNode) throws KNModuleInitException {
		super.xmlInit(xmlNode);
		for (Node mimeNode = xmlNode.getFirstChild(); mimeNode != null; mimeNode = mimeNode
		.getNextSibling()) {
			if (mimeNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (mimeNode.getNodeName().equals("mimetype")) {
				try {
					mimeList.add(new MimeType(mimeNode.getTextContent().trim()));
				} catch (MalformedMimeTypeException e) {
					throw new KNModuleInitException("Invalid mimetype in collection.");
				}
			}
			else if (mimeNode.getNodeName().equals("artist"))
				artistList.add(mimeNode.getTextContent().trim());
			else if (mimeNode.getNodeName().equals("set"))
				setList.add(mimeNode.getTextContent().trim());
			else if (mimeNode.getNodeName().equals("path"))
				pathList.add(mimeNode.getTextContent().trim());
		}
	}

	private boolean matchesPath(URI uri) {
		String tmp = uri.getPath();
		for (String path: pathList)
			if (tmp.startsWith(path))
				return true;
		return false;
	}

	private boolean hasMetadata(MetadataType type, List<Metadata> metadata, Set<String> valid) {
		
		return false;
	}
	
	public boolean acceptMedia(MediaItem media) {
		List<Metadata> metadata = media.getMetadataList();
		if ((mimeList.contains(JemServer.getMediaStorage().getMimeType(media)) || (mimeList.isEmpty())) &&
				(hasMetadata(MetadataType.Artist, metadata, artistList) || (artistList.isEmpty())) &&
				(hasMetadata(MetadataType.Set, metadata, setList) || setList.isEmpty()) &&
				(matchesPath(media.getURI()) || pathList.isEmpty()))
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
		return 1;
	}

	public int getVersionRevision() {
		return 1;
	}

}
