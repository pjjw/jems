package net.kodeninja.jem.server.DMAP.responses;

import java.util.Iterator;

import net.kodeninja.DMAP.parameters.daap.aply;
import net.kodeninja.DMAP.parameters.dmap.miid;
import net.kodeninja.DMAP.parameters.dmap.mimc;
import net.kodeninja.DMAP.parameters.dmap.minm;
import net.kodeninja.DMAP.parameters.dmap.mlcl;
import net.kodeninja.DMAP.parameters.dmap.mlit;
import net.kodeninja.DMAP.parameters.dmap.mper;
import net.kodeninja.DMAP.parameters.dmap.mrco;
import net.kodeninja.DMAP.parameters.dmap.mstt;
import net.kodeninja.DMAP.parameters.dmap.mtco;
import net.kodeninja.DMAP.parameters.dmap.muty;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.http.service.handlers.URIHandler;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.DMAP.DMAPHTTPBody;
import net.kodeninja.jem.server.DMAP.DMAPResponsePacket;
import net.kodeninja.jem.server.DMAP.DMAPService;
import net.kodeninja.jem.server.DMAP.content.DMAPMediaCollection;
import net.kodeninja.jem.server.content.MediaCollection;

public class PlaylistListURI implements URIHandler {
	protected DMAPService service;

	protected String path;
	protected mstt statusCode = new mstt(200);

	public PlaylistListURI(DMAPService s) {
		service = s;
	}

	public DMAPHTTPBody getCollections() {
		DMAPHTTPBody body = new DMAPHTTPBody();

		aply root = new aply();
		body.addParameter(root);

		root.addParameter(statusCode);
		root.addParameter(new muty());

		mtco count1 = new mtco();
		mrco count2 = new mrco();

		root.addParameter(count1);
		root.addParameter(count2);

		mlcl list = new mlcl();

		int cid = 1;

		MediaCollection col = new DMAPMediaCollection("music");
		JemServer.getInstance().setupCollection(col);

		mlit item = new mlit();
		list.addParameter(item);
		item.addParameter(new miid(1));
		item.addParameter(new mper(1));
		item.addParameter(new minm(service.getServerName()));
		item.addParameter(new mimc(col.getMediaCount()));

		Iterator<MediaCollection> it = JemServer.getInstance().getCollections();
		while (it.hasNext()) {
			MediaCollection origCol = it.next();
			col = new DMAPMediaCollection(origCol, "all");

			item = new mlit();
			list.addParameter(item);

			item.addParameter(new miid(origCol.hashCode()));
			item.addParameter(new mper(origCol.hashCode()));
			item.addParameter(new minm(col.getCollectionName()));
			item.addParameter(new mimc(col.getMediaCount()));

			cid++;
		}

		root.addParameter(list);

		if (cid > 0) {
			count1.setValue(cid);
			count2.setValue(cid);
		}

		return body;
	}

	public HTTPPacket<HTTPHeader, HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<HTTPHeader, HTTPBody> Packet) {
		String loc = Packet.getHeader().getLocation().getPath();
		if (loc.endsWith("containers") == false)
			return null;

		DMAPHTTPBody body = getCollections();
		if (body == null)
			return null;

		HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1,
				HTTPResponseCode.HTTP_200_OK);
		HTTPPacket<HTTPHeader, HTTPBody> response = new DMAPResponsePacket<HTTPBody>(
				header, body, service);

		return response;
	}

}
