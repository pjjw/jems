package net.kodeninja.jem.server.DMAP.responses;

import net.kodeninja.DMAP.parameters.daap.avdb;
import net.kodeninja.DMAP.parameters.dmap.mctc;
import net.kodeninja.DMAP.parameters.dmap.miid;
import net.kodeninja.DMAP.parameters.dmap.mimc;
import net.kodeninja.DMAP.parameters.dmap.minm;
import net.kodeninja.DMAP.parameters.dmap.mlcl;
import net.kodeninja.DMAP.parameters.dmap.mlit;
import net.kodeninja.DMAP.parameters.dmap.mrco;
import net.kodeninja.DMAP.parameters.dmap.mtco;
import net.kodeninja.DMAP.parameters.dmap.muty;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.DMAP.DMAPService;
import net.kodeninja.jem.server.content.MediaUpdateHook;

public class DatabasesURI extends DMAPURI implements MediaUpdateHook {

	protected avdb root = new avdb();
	protected mimc mediaCountTag = new mimc(0);
	protected mctc collectionCountTag = new mctc();

	public DatabasesURI(DMAPService s) {
		super(s, "/databases");
		body.addParameter(root);

		// Status code
		root.addParameter(statusCode);

		// Update type
		root.addParameter(new muty((byte) 0));

		// Record counts
		root.addParameter(new mtco(1));
		root.addParameter(new mrco(1));

		// Records
		mlcl list = new mlcl();
		root.addParameter(list);

		mlit item = new mlit();
		list.addParameter(item);

		item.addParameter(new miid(1));
		// item.addParameter(new mper(0));
		item.addParameter(new minm(service.getServerName()));
		item.addParameter(mediaCountTag);
		item.addParameter(collectionCountTag);
	}

	public void mediaChanged() {
		mediaCountTag.setValue(JemServer.getInstance().getMediaCount());
		collectionCountTag.setValue(JemServer.getInstance()
				.getCollectionCount() + 1);
	}

}
