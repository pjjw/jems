package net.kodeninja.UPnP.internal.description;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.identifiers.SSDPUUID;
import net.kodeninja.UPnP.identifiers.ServiceIDURN;

public class UPnPURIParser {
	
	protected SSDPUUID deviceId = null;
	protected ServiceIDURN serviceId = null;
	protected String uriAction = null;
	
	protected UPnP host;

	public UPnPURIParser(UPnP host) {
		this.host = host;
	}
	
	public boolean parseURI(String path) {
		deviceId = null;
		serviceId = null;
		uriAction = null;
		
		int baseSize = (UPnP.getURIBase() + "device/").length();
		int pos;
		String tmp;
		
		if (path.length() < baseSize) return false;	
		path = path.substring(baseSize);
		
		pos = path.indexOf("/");
		if (pos < 0) return false;
		deviceId = new SSDPUUID(path.substring(0, pos));
		if (path.length() < pos) return false;
		path = path.substring(pos + 1);
		
		pos = path.indexOf("/");
		if (pos < 0) {
			uriAction = path;
			return true;
		}
		tmp = path.substring(0, pos);
		if (path.length() < pos) return false;
		path = path.substring(pos + 1);
		
		 if (tmp.equals("service") == false)
			return false;
		
		pos = path.indexOf("/");
		if (pos < 0) return false;
		serviceId = new ServiceIDURN(path.substring(0, pos));
		if (path.length() < pos) return false;
		path = path.substring(pos + 1);
		
		pos = path.indexOf("/");
		if (pos > 0) return false;
		
		uriAction = path;
		
		return true;
	}

}
