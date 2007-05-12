package net.kodeninja.jem.server.content.fs;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.net.URI;

import org.w3c.dom.Node;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MediaSource;
import net.kodeninja.jem.server.content.mimetypes.GenericFile;
import net.kodeninja.jem.server.content.mimetypes.MetadataFactory;
import net.kodeninja.scheduling.MinimumTimeJob;
import net.kodeninja.util.KNRunnableModule;
import net.kodeninja.util.KNXMLModuleInitException;
import net.kodeninja.util.MalformedMimeTypeException;
import net.kodeninja.util.MimeType;

/**
 * Module that scans specified directories for media.
 * 
 * XML Configuration:
 * 
 *   <!-- Plugin Module Definition -->
 *   <module name="FS Plugin" class="net.kodeninja.jem.server.content.fs.DirectoryMediaSource">
 *      <!-- Specifies the amount of time to wait in between scans -->
 *      <refresh>60000</refresh>
 *      <!-- Specifies directories to scan -->
 *      <directory>shared/music</directory>
 *      <directory>shared/videos</directory>
 *      ...
 *      <!-- Specifies extension mappings to metadata extractor classes and mimetypes -->
 *      <mimetype type="audio/mpeg" class="net.kodeninja.jem.server.content.mimetypes.MPEGAudioFile">
 *        <extension>mp3</extension>
 *      </mimetype>
 *      <mimetype type="video/mpeg" class="net.kodeninja.jem.server.content.mimetypes.GenericFile">
 *        <extension>mpg</extension>
 *      </mimetype>
 *    </module>
 *    ...
 *    <services>
 *      <service module="FS Plugin"/>
 *    </services>
 *    
 * Available metadata extractor classes:
 *   Image Files (Jpg, gif, bmp, etc... All java supported types.)
 *     net.kodeninja.jem.server.content.mimetypes.ImageFile
 *   MPEG Audio Files (ie. mp3)
 *     net.kodeninja.jem.server.content.mimetypes.MPEGAudioFile
 *   Other Files (Attempts to extract metadata from file path...)
 *     net.kodeninja.jem.server.content.mimetypes.GenericFile
 *     
 * @author Charles Ikeson
 */
public class DirectoryMediaSource extends MinimumTimeJob implements
KNRunnableModule, MediaSource {
	protected static final GenericFile DEFAULT_FACTORY = new GenericFile();
	protected int refreshTime = 600000; // Use default wait time of 10 mins
	protected Set<File> mediaBase = new HashSet<File>();
	protected String sourceName = "Unnamed Media Source";
	protected Map<String, MimeType> extMap = new HashMap<String, MimeType>();
	protected Map<MimeType, MetadataFactory> metadataMap = new HashMap<MimeType, MetadataFactory>();
	Map<AddDirectoryJob, Integer> jobs = Collections.synchronizedMap(new HashMap<AddDirectoryJob, Integer>());
	protected int mediaAdded = 0;

	public DirectoryMediaSource() {
		// Wait 2 seconds
		super(true, JemServer.getInstance().getScheduler(), 2000, false);
	}

	public void xmlInit(Node xmlNode) throws KNXMLModuleInitException {
		sourceName = xmlNode.getAttributes().getNamedItem("name").getNodeValue();
		for (Node modNode = xmlNode.getFirstChild(); modNode != null; modNode = modNode.getNextSibling())
			if (modNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			else if (modNode.getNodeName().equals("refresh")) {
				try {
					refreshTime = Integer.parseInt(modNode.getTextContent());
				}
				catch (NumberFormatException e) { /* Do nothing */ }
			}
			else if (modNode.getNodeName().equals("directory")) {
				String dirString = modNode.getTextContent().trim();
				if (dirString.length() > 0)
					mediaBase.add(new File(dirString));
				else
					throw new KNXMLModuleInitException("Invalid directory.");
			} else if (modNode.getNodeName().equals("mimetype")) {
				Node attrMime = modNode.getAttributes().getNamedItem("type");
				Node attrClass = modNode.getAttributes().getNamedItem("class");

				if ((attrMime == null) || (attrClass == null))
					throw new KNXMLModuleInitException("Malformed MimeType element.");

				MimeType mime = null;
				String className = attrClass.getNodeValue();
				MetadataFactory factory = null;

				try {
					mime = new MimeType(attrMime.getNodeValue());
				} catch (MalformedMimeTypeException e) {
					throw new KNXMLModuleInitException("Invalid MimeType: "
					                                   + attrMime.getNodeValue());
				}

				try {
					Object o = JemServer.getInstance().getClassLoader()
					.loadClass(className).newInstance();
					if (o instanceof MetadataFactory)
						factory = (MetadataFactory) o;
					else
						throw new KNXMLModuleInitException("'" + className
						                                   + "' is not a valid metadata factory.");
				} catch (ClassNotFoundException e) {
					throw new KNXMLModuleInitException(
					                                   "Unable to location module: " + className);
				} catch (IllegalAccessException e) {
					throw new KNXMLModuleInitException(
					                                   "Permission denied attempting to load module: "
					                                   + className);
				} catch (InstantiationException e) {
					throw new KNXMLModuleInitException(
					                                   "An error occured while attempting to load the module: "
					                                   + className);
				}

				metadataMap.put(mime, factory);

				for (Node extNode = modNode.getFirstChild(); extNode != null; extNode = extNode
				.getNextSibling())
					if (extNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					else if (extNode.getNodeName().equals("extension")) {
						if (extNode.getTextContent().trim().length() > 0)
							extMap.put(extNode.getTextContent().toLowerCase(),
							           mime);
						else
							throw new KNXMLModuleInitException(
							"Blank extensions are not allowed.");
					} else
						throw new KNXMLModuleInitException(
						                                   "Unexpected element encountered: "
						                                   + extNode.getNodeName());
			} else
				throw new KNXMLModuleInitException(
				                                   "Unexpected element encountered: "
				                                   + modNode.getNodeName());
		JemServer.getInstance().addLog("[" + sourceName + "] Loaded "
		                               + extMap.size()
		                               + " extensions mapped to "
		                               + metadataMap.size()
		                               + " MimeTypes.");
		reset();
	}

	synchronized void incMediaCount() {
		mediaAdded++;
	}

	@Override
	public void run() {
		long msec, end;
		JemServer.getInstance().startMediaUpdate(this);

		// Look for new media
		JemServer.getInstance().addLog("[" + getSourceName() + "] Starting directory scans for new media.");

		msec = System.currentTimeMillis();
		mediaAdded = 0;
		for (File tmpDir : mediaBase)
			(new AddDirectoryJob(this, tmpDir)).start();

		JemServer.getInstance().getScheduler().waitFor(jobs);
		end = System.currentTimeMillis();

		JemServer.getInstance().addLog("Scanning finished: Added " + 
		                               mediaAdded + " new files. (" +
		                               ((end - msec) / 1000.0) + "s)");

		// Prune media tree
		JemServer.getInstance().addLog("[" + getSourceName() + "] Starting media tree pruning to remove missing media.");
		msec = System.currentTimeMillis();
		Iterator<MediaItem> it = JemServer.getInstance().getAllMedia();
		LinkedList<URI> removeList = new LinkedList<URI>();
		while (it.hasNext()) {
			MediaItem mi = it.next();
			if (mi.getMediaURI().toString().startsWith("file:")) {
				File path = new File(mi.getMediaURI().getPath());
				if (path.exists() == false)
					removeList.add(mi.getMediaURI());
			}
		}
		JemServer.getInstance().removeMedia(removeList);
		end = System.currentTimeMillis();
		JemServer.getInstance().addLog("Pruning finished: Removed " +
		                               removeList.size() + " dead files. (" +
		                               ((end - msec) / 1000.0) + "s)");

		setTime(refreshTime);
		super.run();
	}

	public MimeType getMimeTypeFactory(File f) {
		for (String ext : extMap.keySet())
			if (f.toString().toLowerCase().endsWith(ext))
				return extMap.get(ext);

		return MimeType.WILDCARD;
	}

	public MetadataFactory getMetadataFactory(MimeType mime) {
		MetadataFactory mdFactory;
		if ((mdFactory = metadataMap.get(mime)) != null)
			return mdFactory;
		else
			mdFactory = DEFAULT_FACTORY;

		return mdFactory;
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getName() {
		return "Directory Media Source";
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
