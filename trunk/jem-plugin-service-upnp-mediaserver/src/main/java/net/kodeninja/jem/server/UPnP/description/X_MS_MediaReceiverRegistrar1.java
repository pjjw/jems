package net.kodeninja.jem.server.UPnP.description;

import java.util.Map;

import net.kodeninja.UPnP.description.Service;
import net.kodeninja.UPnP.description.ServiceAction;
import net.kodeninja.UPnP.description.ServiceActionArgument;
import net.kodeninja.UPnP.identifiers.SSDPVersion;
import net.kodeninja.UPnP.identifiers.ServiceIDURN;
import net.kodeninja.UPnP.identifiers.ServiceTypeURN;
import net.kodeninja.UPnP.internal.control.ControlException;

public class X_MS_MediaReceiverRegistrar1 extends Service {

	private final static String SCHEMA_NAME = "microsoft.com";
	private final static String SERVICE_NAME = "X_MS_MediaReceiverRegistrar";
	
	public X_MS_MediaReceiverRegistrar1(MediaServer1 owner) {
		super(owner, new ServiceTypeURN(SCHEMA_NAME, SERVICE_NAME, new SSDPVersion(1)),
				 new ServiceIDURN(SCHEMA_NAME, SERVICE_NAME));
	}
	
	@Override
	public void processAction(ServiceAction action,
			Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs) throws ControlException {
		System.err.println("ConnectionManager:1 - Unimplemented Action: " + action);
	}

}
