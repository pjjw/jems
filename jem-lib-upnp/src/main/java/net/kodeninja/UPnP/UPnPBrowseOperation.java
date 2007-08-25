package net.kodeninja.UPnP;

import java.io.IOException;

import net.kodeninja.scheduling.MinimumTimeJob;
import net.kodeninja.http.service.HTTPUDPService;
import net.kodeninja.UPnP.internal.discovery.MSearchPacket;
import net.kodeninja.UPnP.identifiers.SSDPAll;
import net.kodeninja.UPnP.identifiers.SSDPIdentifier;

class UPnPBrowseOperation implements UPnPOperation {
	private static SSDPAll all = new SSDPAll();
	private UPnP host;
	private SSDPIdentifier ident;
	private UPnPBrowseListener listener;
	private MSearchThread searchThread;
	
	class MSearchThread extends MinimumTimeJob {
		private SSDPIdentifier ident;
		private HTTPUDPService service;
		
		MSearchThread(UPnP host, SSDPIdentifier ident) {
			super(true, host.getScheduler(), 60 * 1000, true);
			this.ident = ident;
			this.service = host.getBroadcastService();
		}
		
		public void run() {
			try {
				service.getTransport().sendPacket(UPnP.SSDP_MULTICAST, 1900, new MSearchPacket(ident));
			}
			catch (IOException e) {}
			super.run();
		}
	}
	
	UPnPBrowseOperation(UPnP host, SSDPIdentifier ident, UPnPBrowseListener listener) {
		this.host = host;
		this.ident = ident;
		this.listener = listener;
		
		searchThread = new MSearchThread(host, ident);
		searchThread.start();
	}
	
	void added(UPnPCache cache) {
		if (this.ident.equals(all) || (this.ident.equals(cache.getIdent())))
			listener.serviceFound(this, cache);
	}
	
	void removed(UPnPCache cache) {
		if (this.ident.equals(all) || (this.ident.equals(cache.getIdent())))
			listener.serviceLost(this, cache);		
	}
	
	public void stop() {
		host.removeBrowser(this);
		searchThread.stop();
	}

}
