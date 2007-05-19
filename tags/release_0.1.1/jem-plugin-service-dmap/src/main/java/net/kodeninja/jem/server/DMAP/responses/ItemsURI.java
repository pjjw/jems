package net.kodeninja.jem.server.DMAP.responses;

import java.io.File;
import java.util.Date;
import java.util.Scanner;
import java.util.Iterator;

import net.kodeninja.DMAP.DataTypes.DMAPListParameter;
import net.kodeninja.DMAP.DataTypes.DMAPParameter;
import net.kodeninja.DMAP.parameters.com.apple.itunes.aeHV;
import net.kodeninja.DMAP.parameters.com.apple.itunes.aeMK;
import net.kodeninja.DMAP.parameters.daap.adbs;
import net.kodeninja.DMAP.parameters.daap.apso;
import net.kodeninja.DMAP.parameters.daap.asal;
import net.kodeninja.DMAP.parameters.daap.asar;
import net.kodeninja.DMAP.parameters.daap.asbr;
import net.kodeninja.DMAP.parameters.daap.asbt;
import net.kodeninja.DMAP.parameters.daap.ascm;
import net.kodeninja.DMAP.parameters.daap.ascr;
import net.kodeninja.DMAP.parameters.daap.asda;
import net.kodeninja.DMAP.parameters.daap.asdc;
import net.kodeninja.DMAP.parameters.daap.asdk;
import net.kodeninja.DMAP.parameters.daap.asdm;
import net.kodeninja.DMAP.parameters.daap.asdn;
import net.kodeninja.DMAP.parameters.daap.asfm;
import net.kodeninja.DMAP.parameters.daap.asgn;
import net.kodeninja.DMAP.parameters.daap.assz;
import net.kodeninja.DMAP.parameters.daap.astm;
import net.kodeninja.DMAP.parameters.daap.astn;
import net.kodeninja.DMAP.parameters.daap.asur;
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
import net.kodeninja.jem.server.content.MediaCollection;
import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MetadataTypes;

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
		String tmpMetadata;

		addMetadataToItem(listItem, meta, new mikd(mikd.ITEM_KIND_DAAP));
		if (containerId != null)
			addMetadataToItem(listItem, meta, new mcti(containerId));

		if (service.transcodeItem(mi) == false)
			addMetadataToItem(listItem, meta, new asdk(asdk.DATA_KIND_LOCAL));
		// else {
		// addMetadataToItem(listItem, meta, new asdk((byte)1));
		// addMetadataToItem(listItem, meta, new
		// asul("http://192.168.0.146:3689/databases/1/items/" + mi.hashCode() +
		// ".mpg"));
		// }
		addMetadataToItem(listItem, meta, new asbt());
		addMetadataToItem(listItem, meta, new asdc());
		addMetadataToItem(listItem, meta, new asdn());
		addMetadataToItem(listItem, meta, new asur());
		addMetadataToItem(listItem, meta, new ascr());

		addMetadataToItem(listItem, meta, new miid(mi.hashCode()));

		tmpMetadata = mi.getMetadata(MetadataTypes.Title);
		if (tmpMetadata != null)
			addMetadataToItem(listItem, meta, new minm(tmpMetadata));

		tmpMetadata = mi.getMetadata(MetadataTypes.Artist);
		if (tmpMetadata != null)
			addMetadataToItem(listItem, meta, new asar(tmpMetadata));

		tmpMetadata = mi.getMetadata(MetadataTypes.Set);
		if (tmpMetadata != null)
			addMetadataToItem(listItem, meta, new asal(tmpMetadata));

		try {
			tmpMetadata = mi.getMetadata(MetadataTypes.SetPosition);
			if (tmpMetadata != null)
				addMetadataToItem(listItem, meta, new astn(Short
				                                           .parseShort(tmpMetadata)));
		} catch (NumberFormatException e) {
		}

		tmpMetadata = mi.getMetadata(MetadataTypes.Genre);
		if (tmpMetadata != null)
			addMetadataToItem(listItem, meta, new asgn(tmpMetadata));

		tmpMetadata = mi.getMetadata(MetadataTypes.Description);
		if (tmpMetadata != null)
			addMetadataToItem(listItem, meta, new ascm(tmpMetadata));

		try {
			tmpMetadata = mi.getMetadata(MetadataTypes.Length);
			if (tmpMetadata != null)
				addMetadataToItem(listItem, meta, new astm(Integer
				                                           .parseInt(tmpMetadata)));
		} catch (NumberFormatException e) {
		}

		try {
			tmpMetadata = mi.getMetadata(MetadataTypes.Year);
			if (tmpMetadata != null)
				addMetadataToItem(listItem, meta, new asyr(Short
				                                           .parseShort(tmpMetadata)));
		} catch (NumberFormatException e) {
		}

		try {
			tmpMetadata = mi.getMetadata(MetadataTypes.BitRate);
			if (tmpMetadata != null)
				addMetadataToItem(listItem, meta, new asbr(Short
				                                           .parseShort(tmpMetadata)));
		} catch (NumberFormatException e) {
		}

		if (mi.getMediaMimeType().getPrimaryType().equals("image")) {
			String filename = mi.getMediaURI().toString();
			addMetadataToItem(listItem, meta, new pimf(filename
			                                           .substring(filename.lastIndexOf("/") + 1)));

			tmpMetadata = mi.getMetadata(MetadataTypes.Format);
			if (tmpMetadata != null)
				addMetadataToItem(listItem, meta, new pfmt(tmpMetadata));

			tmpMetadata = mi.getMetadata(MetadataTypes.Width);
			try {
				if (tmpMetadata != null)
					addMetadataToItem(listItem, meta, new pwth(Integer
					                                           .parseInt(tmpMetadata)));
			} catch (NumberFormatException e) {
			}

			tmpMetadata = mi.getMetadata(MetadataTypes.Height);
			try {
				if (tmpMetadata != null)
					addMetadataToItem(listItem, meta, new phgt(Integer
					                                           .parseInt(tmpMetadata)));
			} catch (NumberFormatException e) {
			}

			tmpMetadata = mi.getMetadata(MetadataTypes.AspectRatio);
			if (tmpMetadata != null)
				addMetadataToItem(listItem, meta, new pasp(tmpMetadata));

			tmpMetadata = mi.getMetadata(MetadataTypes.FileSize);
			try {
				addMetadataToItem(listItem, meta, new pifs(Integer
				                                           .parseInt(tmpMetadata)));
			} catch (NumberFormatException e) {
			}
			try {
				addMetadataToItem(listItem, meta, new plsz(Long
				                                           .parseLong(tmpMetadata)));
			} catch (NumberFormatException e) {
			}

			tmpMetadata = mi.getMetadata(MetadataTypes.Rating);
			try {
				addMetadataToItem(listItem, meta, new prat(Integer
				                                           .parseInt(tmpMetadata)));
			} catch (NumberFormatException e) {
			}

			if (isMetaFlagSet(meta, "dpap.FileData")
					&& (isMetaFlagSet(meta, "dpap.thumb") != isMetaFlagSet(
					                                                       meta,
					"dpap.hires")))
				addMetadataToItem(listItem, meta, new pfdt(new File(mi
				                                                    .getMediaURI()), isMetaFlagSet(meta, "dpap.thumb")));
		} else {
			if (service.transcodeItem(mi) == false)
				try {
					tmpMetadata = mi.getMetadata(MetadataTypes.FileSize);
					if (tmpMetadata != null)
						addMetadataToItem(listItem, meta, new assz(Integer
						                                           .parseInt(tmpMetadata)));
				} catch (NumberFormatException e) {
				}

				try {
					tmpMetadata = mi.getMetadata(MetadataTypes.DateAdded);
					if (tmpMetadata != null) {
						addMetadataToItem(listItem, meta, new asda(new Date(Long
						                                                    .parseLong(tmpMetadata))));
						addMetadataToItem(listItem, meta, new asdm(new Date(Long
						                                                    .parseLong(tmpMetadata))));
					}
				} catch (NumberFormatException e) {
				}

				if (service.transcodeItem(mi)
						&& (mi.getMediaMimeType().getPrimaryType().equals("video")))
					addMetadataToItem(listItem, meta, new asfm("mpg"));
				else {
					tmpMetadata = mi.getMetadata(MetadataTypes.Format);
					if (tmpMetadata != null)
						addMetadataToItem(listItem, meta, new asfm(tmpMetadata));
				}

				if (mi.getMediaMimeType().getPrimaryType().equals("video")) {
					addMetadataToItem(listItem, meta, new aeHV(aeHV.BOOL_TRUE));
					addMetadataToItem(listItem, meta, new aeMK(
					                                           aeMK.MEDIA_KIND_VIDEO));
				} else if (mi.getMediaMimeType().getPrimaryType().equals("audio")) {
					addMetadataToItem(listItem, meta, new aeHV(aeHV.BOOL_FALSE));
					addMetadataToItem(listItem, meta, new aeMK(
					                                           aeMK.MEDIA_KIND_AUDIO));
				}

				tmpMetadata = mi.getMetadata(MetadataTypes.Rating);
				try {
					addMetadataToItem(listItem, meta, new ascr(Byte
					                                           .parseByte(tmpMetadata)));
				} catch (NumberFormatException e) {
				}
		}
	}

	public HTTPPacket<HTTPHeader, HTTPBody> process(HTTPSocket Socket,
	                                                HTTPPacket<HTTPHeader, HTTPBody> Packet) {

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
				Iterator<MediaCollection> it = JemServer.getInstance().getCollections();
				while (it.hasNext()) {
					MediaCollection tmpCol = it.next();
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

		if (type != null)
			revision = new DMAPMediaCollection(revision, type);

		if (query != null)
			revision = new DMAPQueryResultCollection(revision, query);

		root.addParameter(statusCode);
		root.addParameter(new muty());
		root.addParameter(new mtco(collection.getMediaCount()));
		root.addParameter(new mrco(collection.getMediaCount()));

		mlcl list = new mlcl();
		root.addParameter(list);
		for (MediaItem mi : revision) {
			if (collection.acceptMedia(mi)) {
				mlit item = new mlit();
				list.addParameter(item);

				populateItem(item, meta, mi, containerId);
			}
		}

		HTTPHeader header = new HTTPHeader(HTTPVersion.HTTP1_1,
		                                   HTTPResponseCode.HTTP_200_OK);
		DMAPHTTPBody body = new DMAPHTTPBody();
		body.addParameter(root);

		HTTPPacket<HTTPHeader, HTTPBody> response = new DMAPResponsePacket<HTTPBody>(
				header, body, service);

		return response;
	}

}
