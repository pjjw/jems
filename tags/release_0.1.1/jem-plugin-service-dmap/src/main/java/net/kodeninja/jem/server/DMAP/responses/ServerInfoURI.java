package net.kodeninja.jem.server.DMAP.responses;

import net.kodeninja.DMAP.parameters.daap.apro;
import net.kodeninja.DMAP.parameters.dmap.minm;
import net.kodeninja.DMAP.parameters.dmap.mpro;
import net.kodeninja.DMAP.parameters.dmap.msbr;
import net.kodeninja.DMAP.parameters.dmap.msdc;
import net.kodeninja.DMAP.parameters.dmap.msex;
import net.kodeninja.DMAP.parameters.dmap.msix;
import net.kodeninja.DMAP.parameters.dmap.mspi;
import net.kodeninja.DMAP.parameters.dmap.msqy;
import net.kodeninja.DMAP.parameters.dmap.msrv;
import net.kodeninja.DMAP.parameters.dmap.mstm;
import net.kodeninja.DMAP.parameters.dpap.ppro;
import net.kodeninja.jem.server.DMAP.DMAPService;

public class ServerInfoURI extends DMAPURI {
	public ServerInfoURI(DMAPService s) {
		super(s, "/server-info");

		msrv root = new msrv();
		body.addParameter(root);

		// Status Tag
		root.addParameter(statusCode);

		// Version Tags
		root.addParameter(new mpro((short) 3, (short) 4));
		root.addParameter(new apro((short) 3, (short) 5));
		root.addParameter(new ppro((short) 1, (short) 1));

		// Service Name
		root.addParameter(new minm(service.getServerName()));

		// Feature support
		// Login Required
		// root.addParameter(new mslr());

		// Timeout
		root.addParameter(new mstm(1800));

		// Auto login
		// root.addParameter(new msal());

		// Supports indexing
		root.addParameter(new msix());

		// Supports update
		// root.addParameter(new msup());

		// Supports Persistent Ids
		root.addParameter(new mspi());

		// Supports Extensions
		root.addParameter(new msex());

		// Supports Browse
		root.addParameter(new msbr());

		// Supports Queries
		root.addParameter(new msqy());

		// Supports resolve
		// root.addParameter(new msrs());

		// Database count
		root.addParameter(new msdc(1));
	}

}
