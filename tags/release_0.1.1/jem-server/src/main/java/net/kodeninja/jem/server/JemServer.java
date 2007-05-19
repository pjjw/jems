package net.kodeninja.jem.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.kodeninja.io.JARFilenameFilter;
import net.kodeninja.jem.server.console.ConsoleInterface;
import net.kodeninja.jem.server.console.LocalConsole;
import net.kodeninja.jem.server.content.CustomMediaCollection;
import net.kodeninja.jem.server.content.InvalidSearchTermException;
import net.kodeninja.jem.server.content.MediaCollection;
import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MediaUpdateHook;
import net.kodeninja.jem.server.content.MetadataTypes;
import net.kodeninja.jem.server.content.SearchRequest;
import net.kodeninja.jem.server.content.transcoding.Transcoder;
import net.kodeninja.scheduling.FIFOScheduler;
import net.kodeninja.scheduling.Job;
import net.kodeninja.scheduling.JobImpl;
import net.kodeninja.scheduling.Scheduler;
import net.kodeninja.util.KNModule;
import net.kodeninja.util.KNRunnableModule;
import net.kodeninja.util.KNXMLModule;
import net.kodeninja.util.KNXMLModuleInitException;
import net.kodeninja.util.MimeType;
import net.kodeninja.util.logging.FileLogger;
import net.kodeninja.util.logging.LoggerCollection;
import net.kodeninja.util.logging.LoggerHook;

public final class JemServer extends LoggerCollection {
	public final class CommandClass {
		JemServer owner;

		private CommandClass(JemServer Owner) {
			owner = Owner;
		}

		public void shutdown() {
			owner.setStatus(Statuses.Exiting);
			owner.sched.stop();
		}

		public void exception() {
			owner.setStatus(Statuses.Exiting);
			owner.errorFlag = true;
		}

		public Map<KNRunnableModule, Boolean> getModuleStatus() {

			Map<KNRunnableModule, Boolean> retVal = new HashMap<KNRunnableModule, Boolean>();
			Iterator<KNRunnableModule> it = owner.services.iterator();
			while (it.hasNext()) {
				KNRunnableModule tmpJob = it.next();

				retVal.put(tmpJob, new Boolean(tmpJob.isStarted()));
			}
			return retVal;
		}

		public Iterator<MediaItem> searchMedia(SearchRequest query)
				throws InvalidSearchTermException {
			Set<MediaItem> retVal = new LinkedHashSet<MediaItem>();

			Iterator<MediaItem> it = owner.getAllMedia();
			while (it.hasNext()) {
				MediaItem tmpMi = it.next();
				if (tmpMi.matches(query))
					retVal.add(tmpMi);
			}
			return retVal.iterator();
		}
	}

	protected class MediaChangedJob extends JobImpl {
		private Map<Job, Integer> mediaUpdaters = Collections
				.synchronizedMap(new HashMap<Job, Integer>());

		public MediaChangedJob() {
			super(true, JemServer.getInstance().getScheduler());
		}

		@Override
		public void run() {

			if (mediaUpdaters.size() == 0) {
				moveMedia();
				stop();
			} else {
				Job j = (Job) mediaUpdaters.keySet().toArray()[0];
				if (j.runId() != mediaUpdaters.get(j))
					mediaUpdaters.remove(j);
				super.run();
			}
		}

		public boolean canRun() {
			return true;
		}

		public boolean isUrgent() {
			return false;
		}

	}

	public static enum Statuses {
		Starting, Running, Exiting
	}

	private static final int HELPER_THREAD_COUNT = 4;
	public CommandClass Commands = new CommandClass(this);

	// Module Datatypes
	private Map<String, KNModule> loadedModules = Collections
			.synchronizedMap(new HashMap<String, KNModule>());
	private Set<InterfaceHook> interfaces = Collections
			.synchronizedSet(new HashSet<InterfaceHook>());
	private Set<KNRunnableModule> services = Collections
			.synchronizedSet(new HashSet<KNRunnableModule>());
	private Set<Transcoder> transcoders = Collections
			.synchronizedSet(new HashSet<Transcoder>());

	// Media Datatypes
	private Set<MediaCollection> mediaCollections = Collections
			.synchronizedSet(new HashSet<MediaCollection>());
	private Map<URI, MediaItem> media = Collections
			.synchronizedMap(new HashMap<URI, MediaItem>());
	private Map<URI, MediaItem> tmpMediaList = Collections
			.synchronizedMap(new HashMap<URI, MediaItem>());
	private Set<MediaUpdateHook> MediaUpdateHooks = Collections
			.synchronizedSet(new HashSet<MediaUpdateHook>());
	private MediaChangedJob mediaChanged;

	// System Datatypes
	protected static JemServer instance;
	private Statuses Status = Statuses.Starting;
	private boolean errorFlag = false;
	protected URLClassLoader classLoader = null;
	protected Scheduler sched = new FIFOScheduler(HELPER_THREAD_COUNT);

	private JemServer() {
		instance = this;
		File[] plugins = (new File("plugins/"))
				.listFiles(new JARFilenameFilter());
		URL[] pluginsURLs;

		if (plugins != null) {
			pluginsURLs = new URL[plugins.length];

			for (int i = 0; i < plugins.length; i++)
				try {
					pluginsURLs[i] = plugins[i].toURI().toURL();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

		} else
			pluginsURLs = new URL[0];

		classLoader = new URLClassLoader(pluginsURLs, ClassLoader
				.getSystemClassLoader());
		mediaChanged = new MediaChangedJob();
	}

	public static JemServer getInstance() {
		if (instance == null)
			new JemServer();
		return instance;
	}

	public static InputStream getResourceAsStream(String name) {
		return getInstance().classLoader.getResourceAsStream(name);
	}

	public boolean start() {
		if (getStatus().equals(Statuses.Exiting))
			return false;

		sched.start(true);
		return true;
	}

	public Scheduler getScheduler() {
		return sched;
	}

	public URLClassLoader getClassLoader() {
		return classLoader;
	}

	public synchronized Statuses getStatus() {
		if (errorFlag)
			return Statuses.Exiting;
		else
			return Status;
	}

	private synchronized void setStatus(Statuses NewStatus) {
		if (!errorFlag)
			Status = NewStatus;
	}

	public KNModule getModule(String Name) {
		return loadedModules.get(Name);
	}

	public void addInterface(String name, InterfaceHook Interface) {
		interfaces.add(Interface);
		services.add(Interface);
		Interface.start();
		loadedModules.put(name, Interface);
	}

	public void addMediaUpdateHook(MediaUpdateHook h) {
		MediaUpdateHooks.add(h);
	}

	public void removeMediaUpdateHook(MediaUpdateHook h) {
		MediaUpdateHooks.remove(h);
	}

	public Iterator<MediaCollection> getCollections() {
		return mediaCollections.iterator();
	}

	public int getCollectionCount() {
		return mediaCollections.size();
	}

	public void addCollection(MediaCollection mc) {
		setupCollection(mc);
		mediaCollections.add(mc);
	}

	public void setupCollection(MediaCollection mc) {
		Iterator<MediaItem> it = getAllMedia();
		while (it.hasNext())
			mc.addMedia(it.next());
	}

	public Iterator<MediaItem> getAllMedia() {
		return media.values().iterator();
	}

	public int getMediaCount() {
		return media.size();
	}

	private synchronized void moveMedia() {
		if (tmpMediaList.size() > 0) {
			System.out.println("Announcing media change!");
			Iterator<MediaItem> mIt = tmpMediaList.values().iterator();
			while (mIt.hasNext()) {
				MediaItem mi = mIt.next();
				media.put(mi.getMediaURI(), mi);
				if (mi.getMetadata(MetadataTypes.DateAdded) == null)
					mi.setMetadata(MetadataTypes.DateAdded, ""
							+ Calendar.getInstance().getTime().getTime());

				Iterator<MediaCollection> cIt = getCollections();
				while (cIt.hasNext())
					cIt.next().addMedia(mi);
			}

			Iterator<MediaUpdateHook> it = MediaUpdateHooks.iterator();
			while (it.hasNext())
				it.next().mediaChanged();

			System.gc();
		}
	}

	public synchronized void startMediaUpdate(Job j) {
		mediaChanged.mediaUpdaters.put(j, j.runId());
		mediaChanged.start();
	}

	public synchronized boolean addMedia(MediaItem mi) {
		if (mi == null)
			return false;

		if ((media.containsKey(mi.getMediaURI()) == false)
				&& (tmpMediaList.containsKey(mi.getMediaURI()) == false)) {
			tmpMediaList.put(mi.getMediaURI(), mi);
			return true;
		}
		return false;
	}

	public boolean removeMedia(MediaItem mi) {
		return (media.remove(mi.getMediaURI()) != null);
	}
	
	public boolean removeMedia(Collection<URI> miList) {
		return (media.remove(miList) != null);
	}

	public MediaItem getMedia(URI mi) {
		return media.get(mi);
	}

	public InputStream requestTranscode(MimeType from, MimeType to,
			InputStream src) throws IOException {
		InputStream retVal = null;

		for (Transcoder t : transcoders)
			if ((retVal = t.transcode(from, to, src)) != null)
				break;

		return retVal;
	}

	public boolean hasError() {
		return errorFlag;
	}

	public int getVersionMajor() {
		return 0;
	}

	public int getVersionMinor() {
		return 3;
	}

	public int getVersionRevision() {
		return 0;
	}

	public String getName() {
		return "Java Extendable Media Server";
	}

	protected void parseConfiguration(Node root)
			throws JemMalformedConfigurationException {
		// Run through all the root's childern
		for (Node sectionNode = root.getFirstChild(); sectionNode != null; sectionNode = sectionNode
				.getNextSibling()) {
			if (sectionNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			String nodeName = sectionNode.getNodeName();

			// Code to handle the modules section
			if (nodeName.equals("modules"))
				for (Node moduleNode = sectionNode.getFirstChild(); moduleNode != null; moduleNode = moduleNode
						.getNextSibling()) {
					if (moduleNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (moduleNode.getNodeName().equals("module"))
						try {
							initModule(moduleNode);
						} catch (JemMalformedConfigurationException e) {
							System.err.println("Error while reading config: "
									+ e.toString());
						}
					else
						throw new JemMalformedConfigurationException(
								"Invalid config file. Malformed module entry: "
										+ moduleNode.getNodeName());
				}
			else if (nodeName.equals("services"))
				for (Node serviceNode = sectionNode.getFirstChild(); serviceNode != null; serviceNode = serviceNode
						.getNextSibling()) {
					if (serviceNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (serviceNode.getNodeName().equals("service"))
						try {
							initService(serviceNode);
						} catch (JemMalformedConfigurationException e) {
							System.err.println("Error while reading config: "
									+ e.toString());
						}
					else
						throw new JemMalformedConfigurationException(
								"Invalid config file. Malformed service entry: "
										+ serviceNode.getNodeName());
				}
			else if (nodeName.equals("loggers"))
				for (Node loggerNode = sectionNode.getFirstChild(); loggerNode != null; loggerNode = loggerNode
						.getNextSibling()) {
					if (loggerNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (loggerNode.getNodeName().equals("logger"))
						try {
							initLogger(loggerNode);
						} catch (JemMalformedConfigurationException e) {
							System.err.println("Error while reading config: "
									+ e.toString());
						}
					else
						throw new JemMalformedConfigurationException(
								"Invalid config file. Malformed logger entry: "
										+ loggerNode.getNodeName());
				}
			else if (nodeName.equals("interfaces"))
				for (Node interfaceNode = sectionNode.getFirstChild(); interfaceNode != null; interfaceNode = interfaceNode
						.getNextSibling()) {
					if (interfaceNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (interfaceNode.getNodeName().equals("interface"))
						try {
							initInterface(interfaceNode);
						} catch (JemMalformedConfigurationException e) {
							System.err.println("Error while reading config: "
									+ e.toString());
						}
					else
						throw new JemMalformedConfigurationException(
								"Invalid config file. Malformed interface entry: "
										+ interfaceNode.getNodeName());
				}
			else if (nodeName.equals("transcoders"))
				for (Node transcoderNode = sectionNode.getFirstChild(); transcoderNode != null; transcoderNode = transcoderNode
						.getNextSibling()) {
					if (transcoderNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (transcoderNode.getNodeName().equals("transcoder"))
						try {
							initTranscoder(transcoderNode);
						} catch (JemMalformedConfigurationException e) {
							System.err.println("Error while reading config: "
									+ e.toString());
						}
					else
						throw new JemMalformedConfigurationException(
								"Invalid config file. Malformed transcoder entry: "
										+ transcoderNode.getNodeName());
				}
			else if (nodeName.equals("media"))
				for (Node moduleNode = sectionNode.getFirstChild(); moduleNode != null; moduleNode = moduleNode
						.getNextSibling()) {
					if (moduleNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (moduleNode.getNodeName().equals("collection"))
						try {
							initCollection(moduleNode);
						} catch (JemMalformedConfigurationException e) {
							System.err.println("Error while reading config: "
									+ e.toString());
						}
					else
						throw new JemMalformedConfigurationException(
								"Invalid config file. Malformed media entry: "
										+ moduleNode.getNodeName());
				}
			else
				System.out.println("Invalid config file. Unreconized key: "
						+ nodeName);
		}
	}

	protected void initModule(Node module)
			throws JemMalformedConfigurationException {
		Node nameAttr = module.getAttributes().getNamedItem("name");
		if ((nameAttr == null) || (nameAttr.getNodeValue().trim().equals("")))
			throw new JemMalformedConfigurationException(
					"Unnamed module found! All modules must have names.");
		String modName = nameAttr.getNodeValue();

		Node classAttr = module.getAttributes().getNamedItem("class");
		if ((classAttr == null) || (classAttr.getNodeValue().trim().equals("")))
			throw new JemMalformedConfigurationException(
					"No class is associated with module: " + modName);
		String className = classAttr.getNodeValue();

		Object o;
		KNXMLModule mod;

		try {
			o = classLoader.loadClass(className).newInstance();
		} catch (ClassNotFoundException e) {
			throw new JemMalformedConfigurationException(
					"Unable to location module: " + modName);
		} catch (IllegalAccessException e) {
			throw new JemMalformedConfigurationException(
					"Permission denied attempting to load module: " + modName);
		} catch (InstantiationException e) {
			throw new JemMalformedConfigurationException(
					"An error occured while attempting to load the module: "
							+ modName);
		}

		if (!(o instanceof KNXMLModule))
			throw new JemMalformedConfigurationException("'" + modName
					+ "' is not a valid module.");
		else
			mod = (KNXMLModule) o;

		if (loadedModules.containsKey(modName))
			throw new JemMalformedConfigurationException(
					"A module with the name '" + modName + "' allready exists.");

		try {
			mod.xmlInit(module);
		} catch (KNXMLModuleInitException e) {
			throw new JemMalformedConfigurationException(e.getMessage());
		}

		loadedModules.put(modName, mod);
		addLog("Loaded module: " + mod.getName() + " - "
				+ mod.getVersionMajor() + "." + mod.getVersionMinor() + "."
				+ mod.getVersionRevision());
	}

	protected void initService(Node service)
			throws JemMalformedConfigurationException {
		Node modAttr = service.getAttributes().getNamedItem("module");
		if ((modAttr == null) || (modAttr.getNodeValue().trim().equals("")))
			throw new JemMalformedConfigurationException(
					"Service found without module reference found! All services must reference a module name.");
		String modName = modAttr.getNodeValue();

		KNModule tmpModule = loadedModules.get(modName);
		if (tmpModule == null)
			throw new JemMalformedConfigurationException(
					"Service references unloaded module: " + modName);

		if (tmpModule instanceof KNRunnableModule)
			((KNRunnableModule) tmpModule).start();
		else
			throw new JemMalformedConfigurationException("'" + modName
					+ "' is not a valid service.");

		services.add(((KNRunnableModule) tmpModule));
	}

	protected void initLogger(Node logger)
			throws JemMalformedConfigurationException {
		Node modAttr = logger.getAttributes().getNamedItem("module");
		if ((modAttr == null) || (modAttr.getNodeValue().trim().equals("")))
			throw new JemMalformedConfigurationException(
					"Logger found without module reference found! All loggers must reference a module name.");
		String modName = modAttr.getNodeValue();

		KNModule tmpModule = loadedModules.get(modName);
		if (tmpModule == null)
			throw new JemMalformedConfigurationException(
					"Logger references unloaded module: " + modName);

		if (tmpModule instanceof LoggerHook)
			loggers.add((LoggerHook) tmpModule);
		else
			throw new JemMalformedConfigurationException("'" + modName
					+ "' is not a valid logger.");
	}

	protected void initInterface(Node userInterface)
			throws JemMalformedConfigurationException {
		Node modAttr = userInterface.getAttributes().getNamedItem("module");
		if ((modAttr == null) || (modAttr.getNodeValue().trim().equals("")))
			throw new JemMalformedConfigurationException(
					"Interface found without module reference found! All interfaces must reference a module name.");
		String modName = modAttr.getNodeValue();

		KNModule tmpModule = loadedModules.get(modName);
		if (tmpModule == null)
			throw new JemMalformedConfigurationException(
					"Interface references unloaded module: " + modName);

		if (tmpModule instanceof InterfaceHook) {
			interfaces.add((InterfaceHook) tmpModule);
			services.add((InterfaceHook) tmpModule);
			((InterfaceHook) tmpModule).start();
		} else
			throw new JemMalformedConfigurationException("'" + modName
					+ "' is not a valid interface.");
	}

	protected void initTranscoder(Node transcoder)
			throws JemMalformedConfigurationException {
		Node modAttr = transcoder.getAttributes().getNamedItem("module");
		if ((modAttr == null) || (modAttr.getNodeValue().trim().equals("")))
			throw new JemMalformedConfigurationException(
					"Logger found without module reference found! All loggers must reference a module name.");
		String modName = modAttr.getNodeValue();

		KNModule tmpModule = loadedModules.get(modName);
		if (tmpModule == null)
			throw new JemMalformedConfigurationException(
					"Transcoder references unloaded module: " + modName);

		if (tmpModule instanceof Transcoder)
			transcoders.add((Transcoder) tmpModule);
		else
			throw new JemMalformedConfigurationException("'" + modName
					+ "' is not a valid transcoder.");
	}

	protected void initCollection(Node collection)
			throws JemMalformedConfigurationException {
		Node nameAttr = collection.getAttributes().getNamedItem("name");
		if ((nameAttr == null) || (nameAttr.getNodeValue().trim().equals("")))
			throw new JemMalformedConfigurationException(
					"Unnamed module found! All modules must have names.");
		String collectionName = nameAttr.getNodeValue();

		Node classAttr = collection.getAttributes().getNamedItem("class");
		if ((classAttr == null) || (classAttr.getNodeValue().trim().equals("")))
			throw new JemMalformedConfigurationException(
					"No class is associated with collection: " + collectionName);
		String className = classAttr.getNodeValue();

		Object o;
		CustomMediaCollection mediaCollection;

		try {
			o = classLoader.loadClass(className).newInstance();
		} catch (ClassNotFoundException e) {
			throw new JemMalformedConfigurationException(
					"Unable to location collection module: " + collectionName);
		} catch (IllegalAccessException e) {
			throw new JemMalformedConfigurationException(
					"Permission denied attempting to load collection module: "
							+ collectionName);
		} catch (InstantiationException e) {
			throw new JemMalformedConfigurationException(
					"Unable to create collection (Invalid construction signature): "
							+ collectionName);
		}

		if (!(o instanceof CustomMediaCollection))
			throw new JemMalformedConfigurationException("'" + collectionName
					+ "' is not a valid collection module.");
		else
			mediaCollection = (CustomMediaCollection) o;

		try {
			mediaCollection.xmlInit(collection);
		} catch (KNXMLModuleInitException e) {
			throw new JemMalformedConfigurationException(e.getMessage());
		}
		addLog("[" + mediaCollection.getName() + "] Loaded collection: "
				+ mediaCollection.getCollectionName());

		addCollection(mediaCollection);
	}

	protected boolean initAll() {
		// Init server
		addLog("Server Started - " + getVersionMajor() + "."
				+ getVersionMinor() + "." + getVersionRevision());
		addLog("----------------------");

		// Read in configuration
		Document configFile;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder builder = factory.newDocumentBuilder();
			configFile = builder.parse(new File("JemsConfig.xml"));

			// Find the root key
			if (configFile.getDocumentElement().getNodeName().equals("jems") != true)
				throw new Exception(
						"Invalid config file. Root node should be 'jems', got '"
								+ configFile.getDocumentElement().getNodeName()
								+ "'.");

			parseConfiguration(configFile.getDocumentElement());
			moveMedia();
		} catch (Throwable e) {
			e.printStackTrace();
			Commands.exception();
		}

		// Set status as running
		setStatus(Statuses.Running);

		return !errorFlag;
	}

	public void deinit() {
		Commands.shutdown();

		// Stop all services
		for (KNRunnableModule service : services)
			service.stop();

		addLog("Server Stopped");
		if (errorFlag == true)
			addLog("Server exited due to error");
		addLog("Server exited normally\n");
	}

	public static void main(String[] args) {
		JemServer app = JemServer.getInstance();
		ConsoleInterface localConsole = new LocalConsole();

		// Add default loggers
		app.addLogger(new FileLogger(new File("JemServer.log"), false));
		app.addLogger(localConsole);

		// Add default interfaces
		app.addInterface("Built-in Console", localConsole);

		app.initAll();

		if (app.start() == false)
			app.addLog("Error occured during startup.");

		// Interface called for quit, log and exit program.
		app.deinit();

		if (app.hasError())
			System.exit(-1);
		else
			System.exit(0);
	}
}
