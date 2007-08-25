package net.kodeninja.UPnP;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.kodeninja.UPnP.description.RootDevice;
import net.kodeninja.UPnP.description.Service;
import net.kodeninja.UPnP.description.ServiceStateVariable;
import net.kodeninja.UPnP.identifiers.SSDPIdentifier;
import net.kodeninja.UPnP.identifiers.SSDPUUID;
import net.kodeninja.UPnP.internal.control.ControlActionURI;
import net.kodeninja.UPnP.internal.control.ControlQueryURI;
import net.kodeninja.UPnP.internal.description.DeviceDescriptionURI;
import net.kodeninja.UPnP.internal.description.ServiceDescriptionURI;
import net.kodeninja.UPnP.internal.discovery.DiscoveryNotifyHandler;
import net.kodeninja.UPnP.internal.discovery.MSearchHandler;
import net.kodeninja.UPnP.internal.discovery.MSearchResponseHandler;
import net.kodeninja.UPnP.internal.eventing.EventSubscribeURI;
import net.kodeninja.UPnP.internal.eventing.EventUnsubscribeURI;
import net.kodeninja.UPnP.internal.eventing.Subscription;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.HTTPTCPService;
import net.kodeninja.http.service.HTTPUDPService;
import net.kodeninja.http.service.handlers.GetHandler;
import net.kodeninja.http.service.handlers.PacketHeaderLogger;
import net.kodeninja.http.service.handlers.PageRequestHandler;
import net.kodeninja.http.service.handlers.PostHandler;
import net.kodeninja.scheduling.MinimumTimeJob;
import net.kodeninja.scheduling.Scheduler;
import net.kodeninja.util.Pair;
import net.kodeninja.util.logging.LoggerCollection;

public final class UPnP extends LoggerCollection {

	public static final String SERVICE_ROOT_DEVICE = "upnp:rootdevice";
	public static InetAddress SSDP_MULTICAST;
	public static URI STAR_URI;

	private static UPnP instance = null;

	private HTTPUDPService notifyService, broadcastService;
	private HTTPTCPService descriptionService;
	private Scheduler sched;
	private Map<SSDPUUID, Set<UPnPCache>> cache = new HashMap<SSDPUUID, Set<UPnPCache>>();
	private Set<UPnPBrowseOperation> browsers = new HashSet<UPnPBrowseOperation>();
	private Set<UPnPAdvertiseOperation> advertisers = new HashSet<UPnPAdvertiseOperation>();
	private Map<Service, Map<SSDPUUID, Subscription>> subscriptions = new HashMap<Service, Map<SSDPUUID, Subscription>>();
	private Map<Service, Subscription> newSubs = new HashMap<Service, Subscription>();
	private Map<Pair<HTTPSocket, SSDPIdentifier>, Long> queuedResponses = new HashMap<Pair<HTTPSocket, SSDPIdentifier>, Long>();
	private UPnPPollingThread pollingThread;
	private boolean doLogging = false;

	class UPnPPollingThread extends MinimumTimeJob {
		private UPnP host;

		UPnPPollingThread(UPnP host) {
			super(true, host.getScheduler(), 500, false);
			this.host = host;
		}

		public void run() {
			host.checkCacheExpiry();
			host.checkAdvertisement();
			host.checkSubscriptions();
			host.checkQueuedResponses();
			super.run();
		}
	}

	private UPnP(LoggerCollection loggerCollection, Scheduler s, boolean verboseLogging) {
		//Make sure all the ads are stopped when program is stopped.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutdown();
			}});

		doLogging = verboseLogging;

		if (loggerCollection != null)
			this.addLogger(loggerCollection);

		sched = s;

		try {
			SSDP_MULTICAST = InetAddress.getByName("239.255.255.250");
			STAR_URI = new URI("*");
		}
		catch (Exception e) {
			e.printStackTrace();
			// This should never be reached.
		}

		if (sched == null) {
			notifyService = new HTTPUDPService(SSDP_MULTICAST, 1900, 1);
			sched = notifyService.getScheduler();
		}
		else
			notifyService = new HTTPUDPService(sched, SSDP_MULTICAST, 1900);
		if (doLogging)
			notifyService.getTransport().addHandler(new PacketHeaderLogger(this, true));
		notifyService.getTransport().addHandler(new DiscoveryNotifyHandler(this));
		notifyService.getTransport().addHandler(new MSearchHandler(this));
		notifyService.getTransport().addHandler(new PacketHeaderLogger(this, true));
		notifyService.addLogger(this);
		notifyService.getTransport().setServerString(UPnP.getServerString());
		notifyService.getTransport().setTTL(4);

		broadcastService = new HTTPUDPService(sched, UPnP.SSDP_MULTICAST, -1);
		if (doLogging)
			broadcastService.getTransport().addHandler(new PacketHeaderLogger(this, true));
		broadcastService.getTransport().addHandler(new MSearchResponseHandler(this));
		broadcastService.getTransport().addHandler(new PacketHeaderLogger(this, true));
		broadcastService.addLogger(this);
		broadcastService.getTransport().setServerString(UPnP.getServerString());
		broadcastService.getTransport().setTTL(4);

		GetHandler descriptionHandler = new GetHandler();
		descriptionHandler.enableChunked(false);
		descriptionHandler.enableByteRange(false);
		descriptionHandler.addURIHandler(new DeviceDescriptionURI(this));
		descriptionHandler.addURIHandler(new ServiceDescriptionURI(this));

		PostHandler controlHandler = new PostHandler();
		controlHandler.enableChunked(false);
		controlHandler.enableByteRange(false);
		controlHandler.addURIHandler(new ControlActionURI(this));
		controlHandler.addURIHandler(new ControlQueryURI(this));

		PageRequestHandler subscriptionHandler = new PageRequestHandler();
		subscriptionHandler.enableChunked(false);
		subscriptionHandler.enableByteRange(false);
		subscriptionHandler.addURIHandler(new EventSubscribeURI(this));
		subscriptionHandler.addURIHandler(new EventUnsubscribeURI(this));

		descriptionService = new HTTPTCPService(sched, -1);
		if (doLogging)
			descriptionService.getTransport().addHandler(new PacketHeaderLogger(this, true));
		descriptionService.getTransport().addHandler(descriptionHandler);
		descriptionService.getTransport().addHandler(controlHandler);
		descriptionService.getTransport().addHandler(subscriptionHandler);
		descriptionService.getTransport().addHandler(new PacketHeaderLogger(this, true));
		descriptionService.getTransport().setServerString(UPnP.getServerString());
		descriptionService.addLogger(this);


		//Start services
		notifyService.start();
		broadcastService.start();
		descriptionService.start();

		if (doLogging) {
			addLog("UPnP: Notify Service Started on port " + notifyService.getBoundPort());
			addLog("UPnP: Broadcast Service Started on port " + broadcastService.getBoundPort());
			addLog("UPnP: Description Service Started on port " + descriptionService.getBoundPort());
		}

		//Start polling thread
		pollingThread = new UPnPPollingThread(this);
		pollingThread.start();
	}

	private synchronized void shutdown() {
		instance = null;
		Set<UPnPAdvertiseOperation> oldAds = advertisers;
		advertisers = null;
		for (UPnPAdvertiseOperation ad: oldAds)
			ad.stop();
	}

	private synchronized static UPnP getInstance() {
		if (instance == null)
			instance = new UPnP(null, null, false);
		return instance;
	}

	public synchronized static void init(LoggerCollection loggerCollection, Scheduler sched, boolean verboseLogging) {
		if (instance == null)
			instance = new UPnP(loggerCollection, sched, verboseLogging);
		else
			instance.doLogging = verboseLogging;
	}

	public Scheduler getScheduler() {
		return sched;
	}

	public HTTPUDPService getBroadcastService() {
		return broadcastService;
	}

	private synchronized void checkCacheExpiry() {
		for (Set<UPnPCache> identSet : cache.values())
			for (UPnPCache ident : identSet)
				if (ident.hasExpired()) {
					identSet.remove(ident);
					if (doLogging)
						notifyService.addLog("REMOVED: Removed '" + ident + "' from uuid " + ident.getUUID() + " due to expiry.");
					break;
				}
	}

	private synchronized void checkQueuedResponses() {
		Iterator<Pair<HTTPSocket, SSDPIdentifier>> it = queuedResponses.keySet().iterator();
		long time = System.currentTimeMillis();

		while (it.hasNext()) {
			Pair<HTTPSocket, SSDPIdentifier> ad = it.next();
			Long queuedTime = queuedResponses.get(ad);
			if (queuedTime < time) {
				sendAdvertisementOn(ad.getA(), ad.getB());
				it.remove();
			}
		}
	}

	private synchronized void checkAdvertisement() {
		if (advertisers != null)
			for (UPnPAdvertiseOperation ad: advertisers) {
				if (ad.resendAdvertisement())
					ad.sendAdvertisement();
			}
	}

	private void checkSubscriptions() {
		Map<Service, Subscription> oldSubs = null;

		synchronized (this) {
			oldSubs = newSubs;
			newSubs = new HashMap<Service, Subscription>();
		}

		for (Service s: oldSubs.keySet()) {
			Subscription sub = oldSubs.get(s);
			Map<ServiceStateVariable, Object> firstEvent = new HashMap<ServiceStateVariable, Object>();

			Iterator<ServiceStateVariable> varIt = s.stateVariables();
			while (varIt.hasNext()) {
				ServiceStateVariable var = varIt.next();
				if (var.sendsEvents())
					firstEvent.put(var, s.getStateVarValue(var));
			}

			sub.announceStateChange(firstEvent);
		}
		oldSubs.clear();

		Iterator<Service> serviceIt = subscriptions.keySet().iterator();
		while (serviceIt.hasNext()) {
			Service service = serviceIt.next();
			Map<SSDPUUID, Subscription> sidMapping = subscriptions.get(service);
			Iterator<SSDPUUID> sidIt = sidMapping.keySet().iterator();

			while (sidIt.hasNext())
				if (sidMapping.get(sidIt.next()).expired())
					sidIt.remove();

			if (sidMapping.size() == 0)
				serviceIt.remove();
		}
	}

	public synchronized void queueAdvertisement(HTTPSocket Socket, SSDPIdentifier ident, long time) {
		Pair<HTTPSocket, SSDPIdentifier> ad = new Pair<HTTPSocket, SSDPIdentifier>(Socket, ident);
		queuedResponses.put(ad, new Long(time));
	}

	private synchronized void sendAdvertisementOn(HTTPSocket Socket, SSDPIdentifier ident) {
		if (advertisers != null)
			for (UPnPAdvertiseOperation ad: advertisers) {
				ad.sendAdvertisementOn(Socket, ident);
			}
	}

	public synchronized void addToCache(SSDPUUID uuid, UPnPCache ident) {
		Set<UPnPCache> identSet = cache.get(uuid);
		if (identSet == null) {
			identSet = new HashSet<UPnPCache>();
			cache.put(uuid, identSet);
			if (doLogging)
				notifyService.addLog("UUID POOL: Added " + uuid);
		}

		if (identSet.remove(ident) == false) {
			if (doLogging)
				notifyService.addLog("NEW: Added '" + ident + "' to uuid " + uuid);
			for (UPnPBrowseOperation browse : browsers)
				browse.added(ident);
		}
		else if (doLogging)
			notifyService.addLog("UPDATED: Replaced '" + ident + "' on uuid " + uuid);
		identSet.add(ident);
	}

	public synchronized void removeFromCache(SSDPUUID uuid, UPnPCache ident) {
		Set<UPnPCache> identSet = cache.get(uuid);
		if (identSet == null) {
			if (doLogging)
				notifyService.addLog("MISSING UUID: Unannounced uuid removing ident '" + ident + "' from uuid " + uuid);
			return;
		}
		if (identSet.remove(ident)) {
			if (doLogging)
				notifyService.addLog("REMOVED: Removed '" + ident + "' from uuid " + uuid + " due to BYEBYE.");
			for (UPnPBrowseOperation browse : browsers)
				browse.removed(ident);
		}
		else if (doLogging)
			notifyService.addLog("MISSING IDENT: Unannounced ident '" + ident + "' from uuid " + uuid);

		if (identSet.isEmpty()) {
			if (doLogging)
				notifyService.addLog("UUID POOL: Removed " + uuid + " due to empty set.");
			cache.remove(uuid);
		}
	}

	private synchronized void addBrowser(UPnPBrowseOperation browser) {
		browsers.add(browser);
	}

	public synchronized void removeBrowser(UPnPBrowseOperation browser) {
		browsers.remove(browser);
	}

	public static UPnPBrowseOperation browse(SSDPIdentifier ident, UPnPBrowseListener listener) {
		UPnPBrowseOperation retVal = new UPnPBrowseOperation(getInstance(), ident, listener);
		getInstance().addBrowser(retVal);

		return retVal;
	}

	private synchronized void addAdvertiser(UPnPAdvertiseOperation advertiser) {
		if (advertisers != null)
			advertisers.add(advertiser);
	}

	public synchronized void removeAdvertiser(UPnPAdvertiseOperation advertiser) {
		if (advertisers != null)
			advertisers.remove(advertiser);
	}

	public UPnPAdvertiseOperation getAdvertisterByRootUUID(SSDPUUID UUID) {
		if (advertisers != null)
			for (UPnPAdvertiseOperation ad: advertisers)
				if (ad.matches(UUID))
					return ad;
		return null;
	}

	public static UPnPOperation advertise(RootDevice d, UPnPAdvertiseListener advertiser) {
		UPnPAdvertiseOperation retVal = new UPnPAdvertiseOperation(getInstance(), d);
		getInstance().addAdvertiser(retVal);
		retVal.sendAdvertisement();
		return retVal;
	}

	public static String getFullName() {
		return "KodeNinja UPnP Stack/0.1";
	}

	public static String getUserAgentString() {
		return getFullName().replace(' ', '-') + " (compatible; UPnP/1.0; " +
		System.getProperty("os.name") + "/" +
		System.getProperty("os.version") + ")";
	}

	public static String getServerString() {
		return System.getProperty("os.name").replace(' ', '-') + "/" +
		System.getProperty("os.version").replace(' ', '-') +
		" UPnP/1.0 " + getFullName().replace(' ', '-');
	}

	public static String getURLBase() {
		return UPnP.getInstance().descriptionService.getURLBase() + getURIBase();
	}

	public static String getURIBase() {
		return "/upnp/";
	}

	public synchronized SSDPUUID addSubscription(Service s, URL callback, long timeout) {
		pollingThread.reset();

		SSDPUUID sid = new SSDPUUID(UUID.randomUUID().toString());

		Map<SSDPUUID, Subscription> serviceSubscriptions = subscriptions.get(s);

		if (serviceSubscriptions == null) {
			serviceSubscriptions = new HashMap<SSDPUUID, Subscription>();
			subscriptions.put(s, serviceSubscriptions);
		}

		Subscription sub = new Subscription(s, sid, callback, timeout);
		serviceSubscriptions.put(sid, sub);

		newSubs.put(s, sub);

		return sid;
	}

	public synchronized boolean renewSubscription(Service s, SSDPUUID sid, long timeout) {
		Map<SSDPUUID, Subscription> mapping = subscriptions.get(s);
		if (mapping != null) {
			Subscription sub = mapping.get(sid);
			if (sub != null) {
				sub.renew(timeout);
				return true;
			}
		}
		return false;
	}

	public synchronized void removeSubscription(Service s, SSDPUUID sid) {
		Map<SSDPUUID, Subscription> serviceSubscriptions = subscriptions.get(s);

		if (serviceSubscriptions == null)
			return;

		serviceSubscriptions.remove(sid);
		if (serviceSubscriptions.isEmpty())
			subscriptions.remove(s);
	}

	public static void announceStateChange(Service s, ServiceStateVariable var, Object val) {
		Map<ServiceStateVariable, Object> tmp = new HashMap<ServiceStateVariable, Object>();
		tmp.put(var, val);
		announceStateChange(s, tmp);
	}

	public static void announceStateChange(Service s, Map<ServiceStateVariable, Object> vals) {
		Map<SSDPUUID, Subscription> mapping = null;
		if (instance == null)
			return;
		synchronized (instance) {
			mapping = instance.subscriptions.get(s);
			if (mapping == null)
				return;
		}
		for (Subscription sub: mapping.values())
			sub.announceStateChange(vals);
	}

	//Update getFullName when this changes
	public String getName() {
		return "KodeNinja UPnP Stack";
	}

	//Update getFullName when this changes
	public int getVersionMajor() {
		return 0;
	}

	//Update getFullName when this changes
	public int getVersionMinor() {
		return 1;
	}

	public int getVersionRevision() {
		return 0;
	}
}
