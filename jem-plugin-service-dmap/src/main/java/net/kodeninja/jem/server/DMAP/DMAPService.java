package net.kodeninja.jem.server.DMAP;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.w3c.dom.Node;

import com.apple.dnssd.DNSSD;
import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDRegistration;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.RegisterListener;
import com.apple.dnssd.TXTRecord;

import net.kodeninja.http.service.HTTPTCPService;
import net.kodeninja.http.service.handlers.GetHandler;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.DMAP.content.DMAPMediaCollection;
import net.kodeninja.jem.server.DMAP.responses.ContentCodesURI;
import net.kodeninja.jem.server.DMAP.responses.DatabasesURI;
import net.kodeninja.jem.server.DMAP.responses.ItemsURI;
import net.kodeninja.jem.server.DMAP.responses.LoginURI;
import net.kodeninja.jem.server.DMAP.responses.LogoutURI;
import net.kodeninja.jem.server.DMAP.responses.MediaRequestURI;
import net.kodeninja.jem.server.DMAP.responses.PlaylistListURI;
import net.kodeninja.jem.server.DMAP.responses.ServerInfoURI;
import net.kodeninja.jem.server.DMAP.responses.UpdateURI;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.MediaUpdateHook;
import net.kodeninja.util.KNRunnableModule;
import net.kodeninja.util.KNXMLModule;
import net.kodeninja.util.KNModuleInitException;
import net.kodeninja.util.MalformedMimeTypeException;
import net.kodeninja.util.MimeType;

public class DMAPService extends HTTPTCPService implements KNRunnableModule,
		KNXMLModule, RegisterListener, MediaUpdateHook {
	protected String serviceName = "JemServer";
	public final static int DAAP_PORT = 3689;
	protected int lastSession = 0;
	protected int mediaRevision = 0;
	private DNSSDRegistration daapReg = null;
	private DNSSDRegistration dpapReg = null;
	private DMAPRevisionCache revisions = new DMAPRevisionCache();
	private LinkedList<MimeType> transcodingTypes = new LinkedList<MimeType>();

	public DMAPService() {
		super(JemServer.getScheduler(), DAAP_PORT);
	}

	public void xmlInit(Node xmlNode) throws KNModuleInitException {
		for (Node modNode = xmlNode.getFirstChild(); modNode != null; modNode = modNode
				.getNextSibling()) {
			if (modNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (modNode.getNodeName().equals("servicename"))
				serviceName = modNode.getTextContent().trim();
			else if (modNode.getNodeName().equals("port")) {
				String portString = modNode.getTextContent().trim();
				if (portString.length() > 0)
					try {
						setPort(Integer.parseInt(portString));
					} catch (NumberFormatException e) {
						throw new KNModuleInitException("Invalid port.");
					}
				else
					throw new KNModuleInitException("Invalid port.");
			} else if (modNode.getNodeName().equals("transcode"))
				try {
					MimeType m = new MimeType(modNode.getTextContent());
					transcodingTypes.add(m);
				} catch (MalformedMimeTypeException e) {
					throw new KNModuleInitException(
							"Invalid transcode target mimetype: "
									+ modNode.getTextContent());
				}
		}
		mediaChanged();
	}

	@Override
	public void start() {
		if (isStarted() == false) {
			super.start();
			
			JemServer.getMediaStorage().hookMediaUpdate(this);

			// Setup logger for server
			addLogger(JemServer.getInstance());

			// The main get handler
			GetHandler getter = new GetHandler();
			getter.enableChunked(false);
			getter.enableCompression(true);

			// Add handlers to server
			getTransport().addHandler(getter);

			// The URI handlers for the different requests
			ServerInfoURI siURI = new ServerInfoURI(this);
			ContentCodesURI ccURI = new ContentCodesURI(this);
			LoginURI liURI = new LoginURI(this);
			LogoutURI loURI = new LogoutURI(this);
			UpdateURI upURI = new UpdateURI(this);
			DatabasesURI dlURI = new DatabasesURI(this);
			PlaylistListURI plURI = new PlaylistListURI(this);
			MediaRequestURI mrURI = new MediaRequestURI(this);
			ItemsURI itURI = new ItemsURI(this);

			// Add URI handlers to get handler
			getter.addURIHandler(itURI);
			getter.addURIHandler(siURI);
			getter.addURIHandler(ccURI);
			getter.addURIHandler(liURI);
			getter.addURIHandler(loURI);
			getter.addURIHandler(upURI);
			getter.addURIHandler(dlURI);
			getter.addURIHandler(plURI);
			getter.addURIHandler(mrURI);

			// Add URI class hooks
			JemServer.getMediaStorage().hookMediaUpdate(upURI);
			JemServer.getMediaStorage().hookMediaUpdate(dlURI);

			// Set to 1800 seconds, the itunes default
			getTransport().setTimeout(1800000);

			TXTRecord daapDetails = new TXTRecord();
			TXTRecord dpapDetails = new TXTRecord();

			try {
				daapDetails.set("txtvers", "1");
				daapDetails.set("Machine Name", getServerName());
				daapDetails.set("Password", "false");
				daapReg = DNSSD.register(0, 0, getServerName(), "_daap._tcp",
											(String) null, (String) null,
											getBoundPort(), daapDetails, this);

				dpapDetails.set("txtvers", "1");
				dpapDetails.set("Version", "65537");
				dpapDetails.set("iPSh Version", "131072");
				dpapDetails.set("Machine Name", getServerName());
				dpapDetails.set("Password", "false");
				dpapReg = DNSSD.register(0, 0, getServerName(), "_dpap._tcp",
											(String) null, (String) null,
											getBoundPort(), dpapDetails, this);
			} catch (DNSSDException e) {
				e.printStackTrace();
				daapReg = null;
				dpapReg = null;
			}

			if ((daapReg == null) || (dpapReg == null)) {
				addLog(getName() + ": Error registering service with DNS-SD.");
				JemServer.command().exception();
				return;
			}

			JemServer.getInstance().addLog(
											getName() + " [" + getServerName()
													+ "] started. (Port: "
													+ getBoundPort() + ")");
		}
	}

	@Override
	public void stop() {
		if (daapReg != null) {
			daapReg.stop();
			daapReg = null;
		}

		if (dpapReg != null) {
			dpapReg.stop();
			dpapReg = null;
		}

		super.stop();
	}

	public void serviceRegistered(DNSSDRegistration registration, int flags,
			String serviceName, String regType, String domain) {

	}

	public void operationFailed(DNSSDService service, int errorCode) {
		addLog("[" + getName() + "] DNS-SD Error Code: " + errorCode);
		JemServer.command().exception();
	}

	public String getServerName() {
		return serviceName;
	}

	public String getServerString() {
		return getName() + "/" + getVersionMajor() + "." + getVersionMinor()
				+ "." + getVersionRevision();
	}

	public int createSession() {
		return lastSession++;
	}

	public void closeSession(int sessionId) {
	}

	public synchronized void mediaChanged() {
		mediaRevision++;
		DMAPMediaCollection tmpCol = new DMAPMediaCollection("all");
		JemServer.getMediaStorage().setupCollection(tmpCol);
		revisions.put(new Integer(mediaRevision), tmpCol);
	}

	public synchronized DMAPMediaCollection getRevision(int revision) {
		return revisions.get(new Integer(revision));
	}

	public synchronized int getMediaRevision() {
		return mediaRevision;
	}

	public boolean transcodeItem(MediaItem mi) {
		for (MimeType m : transcodingTypes)
			if (m.equals(JemServer.getMediaStorage().getMimeType(mi)))
				return true;
		return false;
	}

	public InputStream getItemStream(MediaItem mi) {

		InputStream retVal = null;
		try {
			retVal = mi.getURI().toURL().openStream();
			MimeType mimetype = JemServer.getMediaStorage().getMimeType(mi);

			if (transcodeItem(mi)) {
				MimeType destType = MimeType.WILDCARD;
				if (mimetype.getPrimaryType().equals("video"))
					destType = new MimeType("video", "mov");
				else if (mimetype.getPrimaryType().equals("audio"))
					destType = new MimeType("audio", "mpeg");
				else if (mimetype.getPrimaryType().equals("image"))
					destType = new MimeType("image", "jpeg");

				InputStream transRetVal = JemServer.getInstance().requestTranscode(mimetype, destType, retVal);
				if (transRetVal != null)
					retVal = transRetVal;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	@Override
	public String getName() {
		return "JEM DAAP Plugin";
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
