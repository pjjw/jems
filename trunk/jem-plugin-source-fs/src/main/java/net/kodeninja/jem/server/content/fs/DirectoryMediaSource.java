package net.kodeninja.jem.server.content.fs;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.content.MediaSource;
import net.kodeninja.jem.server.content.mimetypes.GenericFile;
import net.kodeninja.jem.server.content.mimetypes.MetadataFactory;
import net.kodeninja.scheduling.MinimumTimeJob;
import net.kodeninja.util.KNRunnableModule;
import net.kodeninja.util.KNModuleInitException;
import net.kodeninja.util.KNServiceModule;
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
 *      <!-- Tells the plugin to skip over file types it hasn't been told to look for -->
 *      <ignoreUnknown>true</ignoreUnknown>
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
KNRunnableModule, KNServiceModule, MediaSource {
	protected static final GenericFile DEFAULT_FACTORY = new GenericFile();
	protected static final int DEFAULT_REFRESH_TIME = 600000;
	protected int refreshTime = DEFAULT_REFRESH_TIME; // Use default wait time of 10 mins
	protected boolean ignoreUnknown = true;
	protected Set<File> mediaBase = new HashSet<File>();
	protected String sourceName = "Unnamed Media Source";
	protected Map<String, MimeType> extMap = new HashMap<String, MimeType>();
	protected Map<MimeType, MetadataFactory> metadataMap = new HashMap<MimeType, MetadataFactory>();
	Map<AddDirectoryJob, Integer> jobs = Collections.synchronizedMap(new HashMap<AddDirectoryJob, Integer>());
	protected int mediaAdded = 0;

	public DirectoryMediaSource() {
		super(true, JemServer.getScheduler(), DEFAULT_REFRESH_TIME, false);
	}

	public void xmlInit(Node xmlNode) throws KNModuleInitException {
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
			else if (modNode.getNodeName().equals("ignoreUnkown")) {
				try {
					String tmpString = modNode.getTextContent();
					ignoreUnknown = tmpString.equals("1") || tmpString.equalsIgnoreCase("yes") || tmpString.equalsIgnoreCase("true");
				}
				catch (NumberFormatException e) { /* Do nothing */ }
			}
			else if (modNode.getNodeName().equals("directory")) {
				String dirString = modNode.getTextContent().trim();
				if (dirString.length() > 0)
					mediaBase.add(new File(dirString));
				else
					throw new KNModuleInitException("Invalid directory.");
			} else if (modNode.getNodeName().equals("mimetype")) {
				Node attrMime = modNode.getAttributes().getNamedItem("type");
				Node attrClass = modNode.getAttributes().getNamedItem("class");

				if ((attrMime == null) || (attrClass == null))
					throw new KNModuleInitException("Malformed MimeType element.");

				MimeType mime = null;
				String className = attrClass.getNodeValue();
				MetadataFactory factory = null;

				try {
					mime = new MimeType(attrMime.getNodeValue());
				} catch (MalformedMimeTypeException e) {
					throw new KNModuleInitException("Invalid MimeType: "
					                                   + attrMime.getNodeValue());
				}

				try {
					Object o = JemServer.getInstance().getClassLoader()
					.loadClass(className).newInstance();
					if (o instanceof MetadataFactory)
						factory = (MetadataFactory) o;
					else
						throw new KNModuleInitException("'" + className
						                                   + "' is not a valid metadata factory.");
				} catch (ClassNotFoundException e) {
					throw new KNModuleInitException(
					                                   "Unable to location module: " + className);
				} catch (IllegalAccessException e) {
					throw new KNModuleInitException(
					                                   "Permission denied attempting to load module: "
					                                   + className);
				} catch (InstantiationException e) {
					throw new KNModuleInitException(
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
							throw new KNModuleInitException(
							"Blank extensions are not allowed.");
					} else
						throw new KNModuleInitException(
						                                   "Unexpected element encountered: "
						                                   + extNode.getNodeName());
			} else
				throw new KNModuleInitException(
				                                   "Unexpected element encountered: "
				                                   + modNode.getNodeName());
		JemServer.getInstance().addLog("[" + sourceName + "] Loaded "
		                               + extMap.size()
		                               + " extensions mapped to "
		                               + metadataMap.size()
		                               + " MimeTypes.");
	}
	
	public void init() throws KNModuleInitException {
		setTime(refreshTime);
		//start();
		runNow();
	}
	
	public void deinit() throws KNModuleInitException {
	}

	synchronized void incMediaCount() {
		mediaAdded++;
	}

	private String formatTime(long msec) {
		long days =  (long)Math.floor(msec / 86400000.0);
		long hours = (long)Math.floor(msec /  3600000.0);
		long mins =  (long)Math.floor(msec /    60000.0);
		long secs =  (long)Math.floor(msec /     1000.0);
		
		String result = "";
		
		if (days > 0) result += days + "d ";
		if (hours > 0) result += (hours % 24) + "h ";
		if (mins > 0) result += (mins % 60) + "m ";
		if (secs > 0) result += (secs % 60) + "s ";
		result += (msec % 1000) + "ms";
		
		return result;
	}
	
	@Override
	public void run() {
		long msec, end;
		JemServer.getMediaStorage().startUpdate();

		// Look for new media
		JemServer.getInstance().addLog("[" + getSourceName() + "] Starting directory scans for new media.");

		msec = System.currentTimeMillis();
		mediaAdded = 0;
		for (File tmpDir : mediaBase)
			(new AddDirectoryJob(this, tmpDir, ignoreUnknown)).start();

		JemServer.getScheduler().waitFor(jobs);
		end = System.currentTimeMillis();

		JemServer.getInstance().addLog("[" + getSourceName() + "] Scanning finished: " + formatTime(end - msec) + " elapsed.");

		// Prune media tree
		JemServer.getInstance().addLog("[" + getSourceName() + "] Starting media tree pruning to remove missing media.");
		msec = System.currentTimeMillis();
	
		for (MediaItem mi: JemServer.getMediaStorage().getAllMedia()) {
			if (mi.getURI().toString().startsWith("file:")) {
				File path = new File(mi.getURI().getPath());
				if (path.exists() == false)
					JemServer.getMediaStorage().removeMedia(mi);
			}
		}

		end = System.currentTimeMillis();
		JemServer.getInstance().addLog("[" + getSourceName() + "] Pruning finished: " + formatTime(end - msec) + " elapsed.");

		JemServer.getMediaStorage().finishUpdate();
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
		return 2;
	}

}
