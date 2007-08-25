package net.kodeninja.UPnP.app;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.UPnPBrowseListener;
import net.kodeninja.UPnP.UPnPCache;
import net.kodeninja.UPnP.UPnPException;
import net.kodeninja.UPnP.UPnPOperation;
import net.kodeninja.UPnP.identifiers.SSDPIdentifier;

public class UPnPScan implements UPnPBrowseListener {

	public static void main(String[] args) throws Exception {
		UPnPBrowseListener listener = new UPnPScan();
		UPnP.browse(SSDPIdentifier.createInstance((args.length > 0 ? args[0] : "upnp:rootdevice")), listener);
		
		while (true)
			Thread.sleep(10000);
	}

	public void serviceFound(UPnPOperation browser, UPnPCache service) {
		System.out.println("Found Service: " + service.getIdent());
		System.out.println("  UUID: " + service.getUUID());
		System.out.println("  URL: " + service.getLocation());
		System.out.println("  Server: " + service.getServer());
	}

	public void serviceLost(UPnPOperation browser, UPnPCache service) {
		System.out.println("Lost Service: " + service.getIdent());
	}

	public void operationFailed(UPnPOperation operation, UPnPException e) {
	}

}
