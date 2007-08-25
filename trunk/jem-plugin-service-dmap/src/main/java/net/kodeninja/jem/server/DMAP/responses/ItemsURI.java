package net.kodeninja.jem.server.DMAP.responses;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

import net.kodeninja.DMAP.DataTypes.DMAPListParameter;
import net.kodeninja.DMAP.DataTypes.DMAPParameter;
import net.kodeninja.DMAP.parameters.com.apple.itunes.aeHV;
import net.kodeninja.DMAP.parameters.com.apple.itunes.aeMK;
import net.kodeninja.DMAP.parameters.daap.adbs;
import net.kodeninja.DMAP.parameters.daap.apso;
import net.kodeninja.DMAP.parameters.daap.asal;
import net.kodeninja.DMAP.parameters.daap.asar;
import net.kodeninja.DMAP.parameters.daap.asbr;
import net.kodeninja.DMAP.parameters.daap.ascm;
import net.kodeninja.DMAP.parameters.daap.ascr;
import net.kodeninja.DMAP.parameters.daap.asda;
import net.kodeninja.DMAP.parameters.daap.asdm;
import net.kodeninja.DMAP.parameters.daap.asfm;
import net.kodeninja.DMAP.parameters.daap.asgn;
import net.kodeninja.DMAP.parameters.daap.assz;
import net.kodeninja.DMAP.parameters.daap.astm;
import net.kodeninja.DMAP.parameters.daap.astn;
import net.kodeninja.DMAP.parameters.daap.asyr;
import net.kodeninja.DMAP.parameters.dmap.mcti;
import net.kodeninja.DMAP.parameters.dmap.miid;
import net.kodeninja.DMAP.parameters.dmap.mikd;
import net.kodeninja.DMAP.parameters.dmap.minm;
import net.kodeninja.DMAP.parameters.dmap.mlcl;
import net.kodeninja.DMAP.parameters.dmap.mlit;
import net.kodeninja.DMAP.parameters.dmap.mrco;
import net.kodeninja.DMAP.parameters.dmap.mstt;
import net.kodeninja.DMAP.parameters.dmap.mtco;
import net.kodeninja.DMAP.parameters.dmap.muty;
import net.kodeninja.DMAP.parameters.dpap.pasp;
import net.kodeninja.DMAP.parameters.dpap.pfdt;
import net.kodeninja.DMAP.parameters.dpap.pfmt;
import net.kodeninja.DMAP.parameters.dpap.phgt;
import net.kodeninja.DMAP.parameters.dpap.pifs;
import net.kodeninja.DMAP.parameters.dpap.pimf;
import net.kodeninja.DMAP.parameters.dpap.plsz;
import net.kodeninja.DMAP.parameters.dpap.prat;
import net.kodeninja.DMAP.parameters.dpap.pwth;
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
import net.kodeninja.jem.server.DMAP.content.DMAPQueryResultCollection;
import net.kodeninja.jem.server.storage.MediaCollection;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.Metadata;
import net.kodeninja.util.MimeType;

public class ItemsURI implements URIHandler {
	protected DMAPService service;
	protected mstt statusCode = new mstt(200);

	public ItemsURI(DMAPService s) {
		service = s;
	}

	protected String getPathVar(String path, String varName) {
		int posBegin = path.toLowerCase().indexOf(varName.toLowerCase());
		if (posBegin == -1)
			return null;
		posBegin += varName.length() + 1;

		int posEnd = path.indexOf("/", posBegin);

		return path.substring(posBegin, posEnd);
	}

	protected String getQueryVar(String query, String varName) {
		int posBegin = query.toLowerCase().indexOf(varName.toLowerCase());
		if (posBegin == -1)
			return null;
		posBegin += varName.length() + 1;

		int posEnd = query.indexOf("&", posBegin);
		if (posEnd == -1)
			posEnd = query.length();

		return query.substring(posBegin, posEnd);
	}

	protected boolean isMetaFlagSet(String meta, String name) {
		name = name.toLowerCase();
		meta = meta.toLowerCase();

		if (meta.equals("all"))
			return true;

		Scanner s = (new Scanner(meta)).useDelimiter("\\s*,\\s*");
		while (s.hasNext())
			if (s.next().equals(name))
				return true;

		return false;
	}

	protected void addMetadataToItem(mlit item, String metaString,
			DMAPParameter metadata) {
		if (isMetaFlagSet(metaString, metadata.getName()))
			item.addParameter(metadata);
	}

	protected void populateItem(mlit listItem, String meta, MediaItem mi,
			Integer containerId) {

		addMetadataToItem(listItem, meta, new mikd(mikd.ITEM_KIND_DAAP));
		if (containerId != null)
			addMetadataToItem(listItem, meta, new mcti(containerId));

		addMetadataToItem(listItem, meta, new miid(mi.hashCode()));

		MimeType mime = JemServer.getMediaStorage().getMimeType(mi);
		boolean isTranscoded = service.transcodeItem(mi);
		boolean dpap = mime.getPrimaryType().equals("image");
		boolean daap = mime.getPrimaryType().equalsIgnoreCase("audio") || mime.getPrimaryType().equalsIgnoreCase("video");

		for (Metadata metadata: mi.getMetadataList()) {
			switch (metadata.getType()) {
			
			case DateAdded:
				if (daap) {
					addMetadataToItem(listItem, meta, new asda(new Date(Long.parseLong(metadata.getValue()))));
					addMetadataToItem(listItem, meta, new asdm(new Date(Long.parseLong(metadata.getValue()))));
				}
				break;
				
			case Artist:
				addMetadataToItem(listItem, meta, new asar(metadata.getValue()));
				break;
				
			case Title:
				addMetadataToItem(listItem, meta, new minm(metadata.getValue()));
				break;
				
			case Genre:
				addMetadataToItem(listItem, meta, new asgn(metadata.getValue()));
				break;
				
			case Set:
				addMetadataToItem(listItem, meta, new asal(metadata.getValue()));
				break;
				
			case SetPosition:
				try {
					addMetadataToItem(listItem, meta, new astn(Short.parseShort(metadata.getValue())));
				} catch (NumberFormatException e) {}
				break;
				
			case Year:
				try {
					addMetadataToItem(listItem, meta, new asyr(Short.parseShort(metadata.getValue())));
				} catch (NumberFormatException e) {}
				break;
				
			case Description:
				addMetadataToItem(listItem, meta, new ascm(metadata.getValue()));
				break;
				
			case Length:
				try {
					addMetadataToItem(listItem, meta, new astm(Integer.parseInt(metadata.getValue())));
				} catch (NumberFormatException e) {}
				break;
				
			case FileSize:
				if (isTranscoded == false) {
					try {
						if (dpap) {
							addMetadataToItem(listItem, meta, new plsz(Long.parseLong(metadata.getValue())));					
							addMetadataToItem(listItem, meta, new pifs(Integer.parseInt(metadata.getValue())));						
						}
						else if (daap) {
							addMetadataToItem(listItem, meta, new assz(Integer.parseInt(metadata.getValue())));
						}
					} catch (NumberFormatException e) {}
				}
				break;
				
			case Format:
				if ((mime.getPrimaryType().equalsIgnoreCase("video")) && (isTranscoded))
					addMetadataToItem(listItem, meta, new asfm("mov"));
				else if (dpap)
					addMetadataToItem(listItem, meta, new pfmt(metadata.getValue()));
				else if (daap)
					addMetadataToItem(listItem, meta, new asfm(metadata.getValue()));
				break;
				
			case BitRate:
				try {
					addMetadataToItem(listItem, meta, new asbr(Short.parseShort(metadata.getValue())));
				} catch (NumberFormatException e) {}
				break;
				
			case AspectRatio:
				if (dpap)
					addMetadataToItem(listItem, meta, new pasp(metadata.getValue()));
				break;
				
			case Width:
				if (dpap)
					try {
						addMetadataToItem(listItem, meta, new pwth(Integer.parseInt(metadata.getValue())));
					} catch (NumberFormatException e) {}
					break;
					
			case Height:
				if (dpap)
					try {
						addMetadataToItem(listItem, meta, new phgt(Integer.parseInt(metadata.getValue())));
					} catch (NumberFormatException e) {}
					break;
					
			case Rating:
				try {
					if (dpap)
						addMetadataToItem(listItem, meta, new prat(Integer.parseInt(metadata.getValue())));
					else if (daap)
						addMetadataToItem(listItem, meta, new ascr(Byte.parseByte(metadata.getValue())));
				} catch (NumberFormatException e) {}
				break;
				
			default: break;
			}
		}

		//Image only metadata (iPhoto)
		if (mime.getPrimaryType().equalsIgnoreCase("image")) {
			String filename = mi.getURI().toString();
			addMetadataToItem(listItem, meta, new pimf(filename.substring(filename.lastIndexOf("/") + 1)));

			if (isMetaFlagSet(meta, "dpap.FileData") && (isMetaFlagSet(meta, "dpap.thumb") != isMetaFlagSet(meta, "dpap.hires")))
				addMetadataToItem(listItem, meta, new pfdt(new File(mi.getURI()), isMetaFlagSet(meta, "dpap.thumb")));
		}
		//Video & Audio only metadata (iTunes)
		else if (mime.getPrimaryType().equalsIgnoreCase("audio") || mime.getPrimaryType().equalsIgnoreCase("video")) {
			if (mime.getPrimaryType().equals("video")) {
				addMetadataToItem(listItem, meta, new aeHV(aeHV.BOOL_TRUE));
				addMetadataToItem(listItem, meta, new aeMK(aeMK.MEDIA_KIND_VIDEO));
			} else if (mime.getPrimaryType().equals("audio")) {
				addMetadataToItem(listItem, meta, new aeHV(aeHV.BOOL_FALSE));
				addMetadataToItem(listItem, meta, new aeMK(aeMK.MEDIA_KIND_AUDIO));
			}
		}
	}

	public  HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<? extends HTTPBody> Packet) {

		String path = Packet.getHeader().getLocation().getPath();
		String queryStr = Packet.getHeader().getLocation().getQuery();

		if (path.endsWith("/items") == false)
			return null;

		DMAPListParameter root;

		// String databaseStr = getPathVar(path, "databases");
		String containerStr = getPathVar(path, "containers");
		String meta = getQueryVar(queryStr, "meta");
		String type = getQueryVar(queryStr, "type");
		String query = getQueryVar(queryStr, "query");
		String revisionIdStr = getQueryVar(queryStr, "revision-id");
		int revisionId;
		Integer containerId = null;

		try {
			revisionId = Integer.parseInt(revisionIdStr);
		} catch (NumberFormatException e) {
			revisionId = service.getMediaRevision();
		}
		DMAPMediaCollection revision = service.getRevision(revisionId);

		MediaCollection collection = null;

		if (meta == null)
			meta = "all";

		if (type == null)
			type = "music";

		if (containerStr != null) {
			root = new apso();
			try {
				containerId = Integer.parseInt(containerStr);

				for (MediaCollection tmpCol: JemServer.getMediaStorage().getAllCollections()) {
					if (tmpCol.hashCode() == containerId) {
						collection = tmpCol;
						break;
					}
				}
				
			} catch (NumberFormatException e) {
			}
		} else
			root = new adbs();

		if (collection == null)
			collection = revision;

		if ((type != null) && (revision != null))
			revision = new DMAPMediaCollection(revision, type);

		if ((query != null) && (revision != null))
			revision = new DMAPQueryResultCollection(revision, query);

		root.addParameter(statusCode);
		root.addParameter(new muty());
		if (collection != null) {
			root.addParameter(new mtco(collection.getMediaCount()));
			root.addParameter(new mrco(collection.getMediaCount()));
		}
		else {
			root.addParameter(new mtco(0));
			root.addParameter(new mrco(0));	
		}

		mlcl list = new mlcl();
		root.addParameter(list);
		if (revision != null) {
			for (MediaItem mi : revision) {
				if (collection.acceptMedia(mi)) {
					mlit item = new mlit();
					list.addParameter(item);

					populateItem(item, meta, mi, containerId);
				}
			}
		}

		HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1,
				HTTPResponseCode.HTTP_200_OK);
		DMAPHTTPBody body = new DMAPHTTPBody();
		body.addParameter(root);

		HTTPPacket<HTTPBody> response = new DMAPResponsePacket<HTTPBody>(
				header, body, service);

		return response;
	}

}
