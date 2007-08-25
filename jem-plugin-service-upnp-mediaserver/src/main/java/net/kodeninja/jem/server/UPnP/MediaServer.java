package net.kodeninja.jem.server.UPnP;

import java.util.UUID;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.UPnPAdvertiseListener;
import net.kodeninja.UPnP.UPnPException;
import net.kodeninja.UPnP.UPnPOperation;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.UPnP.description.MediaServer1;
import net.kodeninja.jem.server.www.WWWService;
import net.kodeninja.util.KNModule;
import net.kodeninja.util.KNModuleInitException;
import net.kodeninja.util.KNServiceModule;
import net.kodeninja.util.KNXMLModule;

import org.w3c.dom.Node;

public class MediaServer implements KNXMLModule, KNServiceModule, UPnPAdvertiseListener {

	protected MediaServer1 mediaServer;
	protected String friendlyName = "Jems UPnP Media Server";;
	protected UPnPOperation adOp;
	protected WWWService www = null;
	protected UUID uuid = null;
	
	public MediaServer() {
		UPnP.init(JemServer.getInstance(), JemServer.getScheduler(), false);
	}
	
	public void xmlInit(Node xmlNode) throws KNModuleInitException {
		for (Node modNode = xmlNode.getFirstChild(); modNode != null; modNode = modNode
				.getNextSibling()) {
			if (modNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (modNode.getNodeName().equals("www-module")) {
				KNModule tmpMod = JemServer.getInstance().getModule(modNode.getTextContent());
				if ((tmpMod != null) && (tmpMod instanceof WWWService))
					www = (WWWService)tmpMod;
				else
					throw new KNModuleInitException("Invalid module. (Must be of type WWWService)");
			}
			else if (modNode.getNodeName().equals("friendlyname")) {
				friendlyName = modNode.getTextContent();
			}
			else if (modNode.getNodeName().equals("uuid")) {
				uuid = UUID.fromString(modNode.getTextContent());
			}
		}

		if (www == null)
			throw new KNModuleInitException("Must specify www-module option.");
		if (uuid == null) {
			uuid = UUID.randomUUID();
			JemServer.getInstance().addLog("No uuid option specified. Please set to generated one: " + uuid);
		}
		mediaServer = new MediaServer1(this, friendlyName, uuid.toString());
		JemServer.getMediaStorage().hookMediaUpdate(mediaServer.getContentDirectory());
	}

	public void init() throws KNModuleInitException {
		adOp = UPnP.advertise(mediaServer, this);
	}
	
	public void deinit() throws KNModuleInitException {
		adOp.stop();
	}
	
	public WWWService getWWWService() {
		return www;
	}
	
	public String getName() {
		return "JEM UPnP MediaServer";
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

	public void operationFailed(UPnPOperation operation, UPnPException e) {
		e.printStackTrace();
	}

}
