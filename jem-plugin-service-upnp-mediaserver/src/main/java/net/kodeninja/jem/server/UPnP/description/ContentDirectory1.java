package net.kodeninja.jem.server.UPnP.description;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.kodeninja.UPnP.description.Service;
import net.kodeninja.UPnP.description.ServiceAction;
import net.kodeninja.UPnP.description.ServiceActionArgument;
import net.kodeninja.UPnP.description.ServiceStateVariable;
import net.kodeninja.UPnP.description.ServiceStateVariableAllowedValueList;
import net.kodeninja.UPnP.identifiers.SSDPVersion;
import net.kodeninja.UPnP.identifiers.ServiceTypeURN;
import net.kodeninja.UPnP.internal.control.ControlException;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.UPnP.MediaServer;
import net.kodeninja.jem.server.UPnP.description.internal.BrowseResult;
import net.kodeninja.jem.server.UPnP.description.internal.MediaTree;
import net.kodeninja.jem.server.UPnP.description.internal.MediaTreeContainer;
import net.kodeninja.jem.server.UPnP.description.internal.MediaTreeItem;
import net.kodeninja.jem.server.UPnP.description.search.SearchExpression;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.MediaUpdateHook;
import net.kodeninja.jem.server.storage.Metadata;
import net.kodeninja.jem.server.www.WWWService;
import net.kodeninja.util.MimeType;

public class ContentDirectory1 extends Service implements MediaUpdateHook {

	protected int updateId = 0;

	protected static final String BrowseFlag_BrowseMetadata = "BrowseMetadata";
	protected static final String BrowseFlag_BrowseDirectChildren = "BrowseDirectChildren";

	protected ServiceAction GetSearchCapabilities, GetSortCapabilities, GetSystemUpdateID,
	Browse, Search;

	protected ServiceActionArgument GetSearchCapabilities_SearchCaps;
	protected ServiceActionArgument GetSortCapabilities_SortCaps;
	protected ServiceActionArgument GetSystemUpdateID_Id;
	protected ServiceActionArgument Browse_ObjectID, Browse_BrowseFlag, Browse_Filter,
	Browse_StartingIndex, Browse_RequestedCount, Browse_SortCriteria, Browse_Result,
	Browse_NumberReturned, Browse_TotalMatches, Browse_UpdateID,
	Search_ContainerID, Search_SearchCriteria;

	protected ServiceStateVariable A_ARG_TYPE_ObjectID, A_ARG_TYPE_Result, A_ARG_TYPE_BrowseFlag,
	A_ARG_TYPE_Filter, A_ARG_TYPE_SortCriteria, A_ARG_TYPE_Index, A_ARG_TYPE_Count,
	A_ARG_TYPE_UpdateID, SearchCapabilities, SortCapabilities, SystemUpdateID,
	A_ARG_TYPE_SearchCriteria;

	protected MediaTreeContainer Media_Root;

	protected MediaTreeContainer Media_Music, Media_Music_All, Media_Music_Genre, Media_Music_Artist,
	Media_Music_Album, Media_Music_Playlists, Media_Music_Folders, Media_Music_Contributing_Artists,
	Media_Music_Album_Artist, Media_Music_Composer, Media_Music_Rating, Media_Music_Rating_1,
	Media_Music_Rating_2, Media_Music_Rating_3, Media_Music_Rating_4, Media_Music_Rating_5;

	protected MediaTreeContainer Media_Video, Media_Video_All, Media_Video_Genre, Media_Video_Actor,
	Media_Video_Series, Media_Video_Playlists, Media_Video_Folders, Media_Video_Rating,
	Media_Video_Rating_1, Media_Video_Rating_2, Media_Video_Rating_3, Media_Video_Rating_4,
	Media_Video_Rating_5;

	protected MediaTreeContainer Media_Pictures, Media_Pictures_All, Media_Pictures_Date_Taken,
	Media_Pictures_Albums, Media_Pictures_Keyword, Media_Pictures_Playlists, Media_Pictures_Folders,
	Media_Pictures_Rating, Media_Pictures_Rating_1, Media_Pictures_Rating_2, Media_Pictures_Rating_3,
	Media_Pictures_Rating_4, Media_Pictures_Rating_5;

	protected MediaTreeContainer Media_Playlists, Media_Playlists_All, Media_Playlists_Folders;

	private WWWService www;
	public ContentDirectory1(MediaServer service, MediaServer1 owner) {
		super(owner, new ServiceTypeURN("ContentDirectory", new SSDPVersion(1)));

		www = service.getWWWService();

		ServiceStateVariableAllowedValueList browseFlagValueList = new ServiceStateVariableAllowedValueList();
		browseFlagValueList.addValue(BrowseFlag_BrowseMetadata);
		browseFlagValueList.addValue(BrowseFlag_BrowseDirectChildren);

		//Build state table
		addStateVar(A_ARG_TYPE_ObjectID = new ServiceStateVariable("A_ARG_TYPE_ObjectID", "string", false));
		addStateVar(A_ARG_TYPE_Result = new ServiceStateVariable("A_ARG_TYPE_Result", "string", false));
		addStateVar(A_ARG_TYPE_BrowseFlag = new ServiceStateVariable("A_ARG_TYPE_BrowseFlag", "string", browseFlagValueList, false));
		addStateVar(A_ARG_TYPE_Filter = new ServiceStateVariable("A_ARG_TYPE_Filter", "string", false));
		addStateVar(A_ARG_TYPE_SortCriteria = new ServiceStateVariable("A_ARG_TYPE_SortCriteria", "string", false));
		addStateVar(A_ARG_TYPE_Index = new ServiceStateVariable("A_ARG_TYPE_Index", "ui4", false));
		addStateVar(A_ARG_TYPE_Count = new ServiceStateVariable("A_ARG_TYPE_Count", "ui4", false));
		addStateVar(A_ARG_TYPE_UpdateID = new ServiceStateVariable("A_ARG_TYPE_UpdateID", "ui4", false));
		setStateVarValue(SearchCapabilities = new ServiceStateVariable("SearchCapabilities", "string", false), "*");
		addStateVar(SortCapabilities = new ServiceStateVariable("SortCapabilities", "string", false));
		setStateVarValue(SystemUpdateID = new ServiceStateVariable("SystemUpdateID", "ui4", true), 0);
		addStateVar(A_ARG_TYPE_SearchCriteria = new ServiceStateVariable("A_ARG_TYPE_SearchCriteria", "string", false));

		//Build action list
		actions.add(GetSearchCapabilities = new ServiceAction("GetSearchCapabilities"));
		GetSearchCapabilities.addArg(GetSearchCapabilities_SearchCaps = new ServiceActionArgument("SearchCaps", false, false, SearchCapabilities));

		actions.add(GetSortCapabilities = new ServiceAction("GetSortCapabilities"));
		GetSortCapabilities.addArg(GetSortCapabilities_SortCaps = new ServiceActionArgument("SortCaps", false, false, SortCapabilities));

		actions.add(GetSystemUpdateID = new ServiceAction("GetSystemUpdateID"));
		GetSystemUpdateID.addArg(GetSystemUpdateID_Id = new ServiceActionArgument("Id", false, false, SystemUpdateID));

		actions.add(Browse = new ServiceAction("Browse"));
		Browse.addArg(Browse_ObjectID = new ServiceActionArgument("ObjectID", true, false, A_ARG_TYPE_ObjectID));
		//The XBox 360 incorrectly sends the container ID instead of an object ID on a browse request
		Browse.addArg(Search_ContainerID = new ServiceActionArgument("ContainerID", true, false, A_ARG_TYPE_ObjectID));
		Browse.addArg(Browse_BrowseFlag = new ServiceActionArgument("BrowseFlag", true, false, A_ARG_TYPE_BrowseFlag));
		Browse.addArg(Browse_Filter = new ServiceActionArgument("Filter", true, false, A_ARG_TYPE_Filter));
		Browse.addArg(Browse_StartingIndex = new ServiceActionArgument("StartingIndex", true, false, A_ARG_TYPE_Index));
		Browse.addArg(Browse_RequestedCount = new ServiceActionArgument("RequestedCount", true, false, A_ARG_TYPE_Count));
		Browse.addArg(Browse_SortCriteria = new ServiceActionArgument("SortCriteria", true, false, A_ARG_TYPE_SortCriteria));
		Browse.addArg(Browse_Result = new ServiceActionArgument("Result", false, false, A_ARG_TYPE_Result));
		Browse.addArg(Browse_NumberReturned = new ServiceActionArgument("NumberReturned", false, false, A_ARG_TYPE_Count));
		Browse.addArg(Browse_TotalMatches = new ServiceActionArgument("TotalMatches", false, false, A_ARG_TYPE_Count));
		Browse.addArg(Browse_UpdateID = new ServiceActionArgument("UpdateID", false, false, A_ARG_TYPE_UpdateID));

		actions.add(Search = new ServiceAction("Search"));
		Search.addArg(Search_ContainerID);
		//Just in case a control point attempts to do the reverse of the XBox 360
		Search.addArg(Browse_ObjectID);
		Search.addArg(Search_SearchCriteria = new ServiceActionArgument("SearchCriteria", true, false, A_ARG_TYPE_SearchCriteria));
		Search.addArg(Browse_Filter);
		Search.addArg(Browse_StartingIndex);
		Search.addArg(Browse_RequestedCount);
		Search.addArg(Browse_SortCriteria);
		Search.addArg(Browse_Result);
		Search.addArg(Browse_NumberReturned);
		Search.addArg(Browse_TotalMatches);
		Search.addArg(Browse_UpdateID);

		Media_Root = new MediaTreeContainer("0", "Root", "object.container");

		Media_Music = new MediaTreeContainer("1", "Music", "object.container", Media_Root);
		Media_Music_All = new MediaTreeContainer("4", "All Music", "object.container", Media_Music);
		Media_Music_Genre = new MediaTreeContainer("5", "Genre", "object.container", Media_Music);
		Media_Music_Artist = new MediaTreeContainer("6", "Artist", "object.container", Media_Music);
		Media_Music_Album = new MediaTreeContainer("7", "Album", "object.container", Media_Music);
		Media_Music_Playlists = new MediaTreeContainer("F", "Playlists", "object.container", Media_Music);
		Media_Music_Folders = new MediaTreeContainer("14", "Folders", "object.container", Media_Music);
		Media_Music_Contributing_Artists = new MediaTreeContainer("100", "Contributing Artists", "object.container", Media_Music);
		Media_Music_Album_Artist = new MediaTreeContainer("107", "Album Artist", "object.container", Media_Music);
		Media_Music_Composer = new MediaTreeContainer("108", "Composer", "object.container", Media_Music);
		Media_Music_Rating = new MediaTreeContainer("101", "Rating", "object.container", Media_Music);
		Media_Music_Rating_1 = new MediaTreeContainer("102", "1+ stars", "object.container", Media_Music_Rating);
		Media_Music_Rating_2 = new MediaTreeContainer("103", "2+ stars", "object.container", Media_Music_Rating);
		Media_Music_Rating_3 = new MediaTreeContainer("104", "3+ stars", "object.container", Media_Music_Rating);
		Media_Music_Rating_4 = new MediaTreeContainer("105", "4+ stars", "object.container", Media_Music_Rating);
		Media_Music_Rating_5 = new MediaTreeContainer("106", "5+ stars", "object.container", Media_Music_Rating);

		Media_Video = new MediaTreeContainer("2", "Video", "object.container", Media_Root);
		Media_Video_All = new MediaTreeContainer("8", "All Video", "object.container", Media_Video);
		Media_Video_Genre = new MediaTreeContainer("9", "Genre", "object.container", Media_Video);
		Media_Video_Actor = new MediaTreeContainer("A", "Actor", "object.container", Media_Video);
		Media_Video_Series = new MediaTreeContainer("E", "Series", "object.container", Media_Video);
		Media_Video_Playlists = new MediaTreeContainer("10", "Playlists", "object.container", Media_Video);
		Media_Video_Folders = new MediaTreeContainer("15", "Folders", "object.container", Media_Video);
		Media_Video_Rating = new MediaTreeContainer("200", "Rating", "object.container", Media_Video);
		Media_Video_Rating_1 = new MediaTreeContainer("201", "1+ stars", "object.container", Media_Video_Rating);
		Media_Video_Rating_2 = new MediaTreeContainer("202", "2+ stars", "object.container", Media_Video_Rating);
		Media_Video_Rating_3 = new MediaTreeContainer("203", "3+ stars", "object.container", Media_Video_Rating);
		Media_Video_Rating_4 = new MediaTreeContainer("204", "4+ stars", "object.container", Media_Video_Rating);
		Media_Video_Rating_5 = new MediaTreeContainer("205", "5+ stars", "object.container", Media_Video_Rating);

		Media_Pictures = new MediaTreeContainer("3", "Pictures", "object.container", Media_Root);
		Media_Pictures_All = new MediaTreeContainer("B", "All Pictures", "object.container", Media_Pictures);
		Media_Pictures_Date_Taken = new MediaTreeContainer("C", "Date Taken", "object.container", Media_Pictures);
		Media_Pictures_Albums = new MediaTreeContainer("D", "Albums", "object.container", Media_Pictures);
		Media_Pictures_Keyword = new MediaTreeContainer("D2", "Keyword", "object.container", Media_Pictures);
		Media_Pictures_Playlists = new MediaTreeContainer("11", "Playlists", "object.container", Media_Pictures);
		Media_Pictures_Folders = new MediaTreeContainer("16", "Folders", "object.container", Media_Pictures);
		Media_Pictures_Rating = new MediaTreeContainer("300", "Rating", "object.container", Media_Pictures);
		Media_Pictures_Rating_1 = new MediaTreeContainer("301", "1+ stars", "object.container", Media_Pictures_Rating);
		Media_Pictures_Rating_2 = new MediaTreeContainer("302", "2+ stars", "object.container", Media_Pictures_Rating);
		Media_Pictures_Rating_3 = new MediaTreeContainer("303", "3+ stars", "object.container", Media_Pictures_Rating);
		Media_Pictures_Rating_4 = new MediaTreeContainer("304", "4+ stars", "object.container", Media_Pictures_Rating);
		Media_Pictures_Rating_5 = new MediaTreeContainer("305", "5+ stars", "object.container", Media_Pictures_Rating);

		Media_Playlists = new MediaTreeContainer("12", "Playlists", "object.container", Media_Root);
		Media_Playlists_All = new MediaTreeContainer("13", "All Playlists", "object.container", Media_Playlists);
		Media_Playlists_Folders = new MediaTreeContainer("17", "Folders", "object.container", Media_Playlists);
	}

	@Override
	public void processAction(ServiceAction action,
			Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs) throws ControlException {

		if (action == GetSearchCapabilities)
			getSearchCapabilitiesAction(inArgs, outArgs);
		else if (action == GetSortCapabilities)
			getSortCapabilitiesAction(inArgs, outArgs);
		else if (action == GetSystemUpdateID)
			getSystemUpdateIDAction(inArgs, outArgs);
		else if (action == Browse)
			browseAction(inArgs, outArgs, false);
		else if (action == Search)
			browseAction(inArgs, outArgs, true);
		else {
			System.err.println("ContentDirectory:1 - Unimplemented Action: " + action);
		}
	}

	protected void getSearchCapabilitiesAction(Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs) throws ControlException {
		outArgs.put(GetSearchCapabilities_SearchCaps, getStateVarValue(SearchCapabilities));
	}

	protected void getSortCapabilitiesAction(Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs) throws ControlException {
		outArgs.put(GetSortCapabilities_SortCaps, getStateVarValue(SortCapabilities));
	}

	protected void getSystemUpdateIDAction(Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs) throws ControlException {
		outArgs.put(GetSystemUpdateID_Id, getStateVarValue(SystemUpdateID));
	}

	protected int search(BrowseResult result, SearchExpression expr, MediaTree mt) {
		int matches = 0;
		Iterator<MediaTree> it = mt.getChildern();
		if (it != null) {
			while (it.hasNext())
				matches += search(result, expr, it.next());
		}
		
		if (expr.evaluate(mt)) {
			result.add(mt);
			matches++;
		}
		return matches;
	}

	protected void browseAction(Map<ServiceActionArgument, Object> inArgs,
			Map<ServiceActionArgument, Object> outArgs, boolean isSearch) throws ControlException {

		try {
			String id = ("" + (((inArgs.get(Browse_ObjectID) == null) || (inArgs.get(Browse_ObjectID).equals(""))) ? inArgs.get(Search_ContainerID) : inArgs.get(Browse_ObjectID))).trim();

			int start = Integer.parseInt("" + inArgs.get(Browse_StartingIndex));
			int count = Integer.parseInt("" + inArgs.get(Browse_RequestedCount));
			String filter = inArgs.get(Browse_Filter).toString();
			String sorting = inArgs.get(Browse_SortCriteria).toString();

			MediaTree container = Media_Root.getBranch(id);
			if (container == null)
				throw new ControlException(710, "No such container");

			int total = 0;
			BrowseResult result = new BrowseResult(start, count, filter, sorting);

			if (isSearch) {
				String terms = ("" + inArgs.get(Search_SearchCriteria)).trim();
				SearchExpression expr = new SearchExpression(terms);
				total = search(result, expr, container);
			}
			else {
				String flags = ("" + inArgs.get(Browse_BrowseFlag)).trim();
				if (flags.equals(BrowseFlag_BrowseDirectChildren)) {
					Iterator<MediaTree> it = container.getChildern();
					if (it != null) {
						int processed = 0;
						while (it.hasNext()) {
							result.add(it.next());
							processed++;
							if ((count > 0) && (processed > start + count))
								break;
						}
					}
					total = container.getChildernCount();
				}
				else if (flags.equals(BrowseFlag_BrowseMetadata)) {
					result.add(container);
					total = 1;
				}
			}
			
			outArgs.put(Browse_Result, result.toString());
			outArgs.put(Browse_NumberReturned, result.getCount());
			outArgs.put(Browse_TotalMatches, total);

			//FIXME This need a proper implementation
			//if (id.equals("0"))
			outArgs.put(Browse_UpdateID, getStateVarValue(SystemUpdateID));
			//	else
			//	outArgs.put(Browse_UpdateID, "");
		} catch (NumberFormatException e) {
			throw new ControlException(402, "Invalid args");
		}
	}

	private void addItemTo(MediaItem mi, MediaTreeContainer container) {
		new MediaTreeItem("i" + container.getId() + "_" + mi.hashCode(), container, mi, www.getURLBase() + www.getItemStreamURI(mi));
	}

	public void mediaChanged() {
		updateId++;
		if (updateId < 0) //Check for roll over
			updateId = 0;
		setStateVarValue(SystemUpdateID, updateId);

		Media_Music_All.clearChildern();
		Media_Music_Artist.clearChildern();
		Media_Music_Album.clearChildern();
		Media_Music_Genre.clearChildern();
		Set<MediaItem> music = JemServer.getMediaStorage().getMediaMatching(new MimeType("audio", "*"));
		Map<String, MediaTreeContainer> artists = new HashMap<String, MediaTreeContainer>();
		Map<String, MediaTreeContainer> albums = new HashMap<String, MediaTreeContainer>();
		Map<String, MediaTreeContainer> genres = new HashMap<String, MediaTreeContainer>();

		for (MediaItem mi: music) {
			addItemTo(mi, Media_Music_All);
			for (Metadata md: mi.getMetadataList()) {
				switch (md.getType())  {
				case Artist:
					MediaTreeContainer artist = artists.get(md.getValue().toLowerCase());
					if (artist == null) {
						artist = new MediaTreeContainer("artist_" + md.getValue().toLowerCase().replace(' ', '_'), md.getValue(), "object.container.person.musicArtist");
						Media_Music_Artist.addChild(artist);
						artists.put(md.getValue().toLowerCase(), artist);
					}
					addItemTo(mi, artist);
					break;
				case Set:
					MediaTreeContainer album = albums.get(md.getValue().toLowerCase());
					if (album == null) {
						album = new MediaTreeContainer("album_" + md.getValue().toLowerCase().replace(' ', '_'), md.getValue(), "object.container.album.musicAlbum");
						Media_Music_Album.addChild(album);
						albums.put(md.getValue().toLowerCase(), album);
					}
					addItemTo(mi, album);
					break;
				case Genre:
					MediaTreeContainer genre = genres.get(md.getValue().toLowerCase());
					if (genre == null) {
						genre = new MediaTreeContainer("genre_" + md.getValue().toLowerCase().replace(' ', '_'), md.getValue(), "object.container.genre.musicGenre");
						Media_Music_Genre.addChild(genre);
						genres.put(md.getValue().toLowerCase(), genre);
					}
					addItemTo(mi, genre);
					break;
				default:
					break;
				}
			}
		}

		/*
		Set<MediaItem> music = JemServer.getMediaStorage().getMediaMatching(new MimeType("audio", "*"));
		Set<String> artists = new HashSet<String>();

		Iterator<MediaTree> ait = Media_Music_All.getChildern();
		while (ait.hasNext()) {
			MediaTree mt = ait.next();
			if (mt instanceof MediaTreeItem) {
				MediaTreeItem mti = (MediaTreeItem)mt;
				if (music.contains(mti.getMediaItem()) == false) {
					changed.add(Media_Music_All);
					ait.remove();
				}
				else
					music.remove(mti.getMediaItem());
			}
		}
		for (MediaItem mi: music) {
			new MediaTreeItem("i" + Media_Music_All.getId() + "_" + mi.hashCode(), Media_Music_All, mi, www.getURLBase() + www.getItemStreamURI(mi));
			new MediaTreeItem("i" + Media_Music_Folders.getId() + "_" + mi.hashCode(), Media_Music_Folders, mi, www.getURLBase() + www.getItemStreamURI(mi));
		}

		Set<MediaItem> video = JemServer.getMediaStorage().getMediaMatching(new MimeType("video", "*"));

		Iterator<MediaTree> vit = Media_Video_All.getChildern();
		while (vit.hasNext()) {
			MediaTree mt = vit.next();
			if (mt instanceof MediaTreeItem) {
				MediaTreeItem mti = (MediaTreeItem)mt;
				if (video.contains(mti.getMediaItem()) == false) {
					changed.add(Media_Video_All);
					vit.remove();
				}
				else
					video.remove(mti.getMediaItem());
			}
		}
		for (MediaItem mi: video) {
			new MediaTreeItem("i" + Media_Video_All.getId() + "_" + mi.hashCode(), Media_Video_All, mi, www.getURLBase() + www.getItemStreamURI(mi));
			new MediaTreeItem("i" + Media_Video_Folders.getId() + "_" + mi.hashCode(), Media_Video_Folders, mi, www.getURLBase() + www.getItemStreamURI(mi));
		}

		Set<MediaItem> pictures = JemServer.getMediaStorage().getMediaMatching(new MimeType("image", "*"));

		Iterator<MediaTree> iit = Media_Pictures_All.getChildern();
		while (iit.hasNext()) {
			MediaTree mt = iit.next();
			if (mt instanceof MediaTreeItem) {
				MediaTreeItem mti = (MediaTreeItem)mt;
				if (pictures.contains(mti.getMediaItem()) == false) {
					changed.add(Media_Pictures_All);
					iit.remove();
				}
				else
					pictures.remove(mti.getMediaItem());
			}
		}
		for (MediaItem mi: pictures) {
			new MediaTreeItem("i" + Media_Pictures_All.getId() + "_" + mi.hashCode(), Media_Pictures_All, mi, www.getURLBase() + www.getItemStreamURI(mi));
			new MediaTreeItem("i" + Media_Pictures_Folders.getId() + "_" + mi.hashCode(), Media_Pictures_Folders, mi, www.getURLBase() + www.getItemStreamURI(mi));
		}
		 */

	}

}
