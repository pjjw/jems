package net.kodeninja.jem.server.DMAP.responses;

import net.kodeninja.DMAP.parameters.dmap.mupd;
import net.kodeninja.DMAP.parameters.dmap.musr;
import net.kodeninja.jem.server.DMAP.DMAPService;
import net.kodeninja.jem.server.content.MediaUpdateHook;

public class UpdateURI extends DMAPURI implements MediaUpdateHook {
	protected musr revisionTag = new musr();
	protected int mediaRevision = 0;

	public UpdateURI(DMAPService s) {
		super(s, "/update");

		mupd root = new mupd();
		body.addParameter(root);

		// Status Tag
		root.addParameter(statusCode);

		// Revision tag
		root.addParameter(revisionTag);
		mediaChanged();
	}

	public void mediaChanged() {
		mediaRevision++;
		revisionTag.setValue(1);
	}

}
