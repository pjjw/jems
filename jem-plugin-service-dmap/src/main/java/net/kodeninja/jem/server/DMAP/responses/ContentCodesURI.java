package net.kodeninja.jem.server.DMAP.responses;

import net.kodeninja.DMAP.DataTypes.DMAPParameter;
import net.kodeninja.DMAP.parameters.dmap.mccr;
import net.kodeninja.DMAP.parameters.dmap.mcna;
import net.kodeninja.DMAP.parameters.dmap.mcnm;
import net.kodeninja.DMAP.parameters.dmap.mcty;
import net.kodeninja.DMAP.parameters.dmap.mdcl;
import net.kodeninja.jem.server.DMAP.DMAPService;

public class ContentCodesURI extends DMAPURI {

	public ContentCodesURI(DMAPService s) {
		super(s, "/content-codes");

		mccr root = new mccr();
		body.addParameter(root);

		// Status Tag
		root.addParameter(statusCode);

		// Temporary var
		mdcl dict = null;

		// DMAP Codes
		// Dictonary Entry for dmap.itemid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.itemid"));
		dict.addParameter(new mcnm("miid"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.itemname
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.itemname"));
		dict.addParameter(new mcnm("minm"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for dmap.itemkind
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.itemkind"));
		dict.addParameter(new mcnm("mikd"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.persistentid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.persistentid"));
		dict.addParameter(new mcnm("mper"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LONG));

		// Dictonary Entry for dmap.container
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.container"));
		dict.addParameter(new mcnm("mcon"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for dmap.containeritemid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.containeritemid"));
		dict.addParameter(new mcnm("mcti"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.parentcontainerid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.parentcontainerid"));
		dict.addParameter(new mcnm("mpco"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.status
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.status"));
		dict.addParameter(new mcnm("mstt"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.statusstring
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.statusstring"));
		dict.addParameter(new mcnm("msts"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for dmap.itemcount
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.itemcount"));
		dict.addParameter(new mcnm("mimc"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.containercount
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.containercount"));
		dict.addParameter(new mcnm("mctc"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.returnedcount
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.returnedcount"));
		dict.addParameter(new mcnm("mrco"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.specifiedtotalcount
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.specifiedtotalcount"));
		dict.addParameter(new mcnm("mtco"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.listing
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.listing"));
		dict.addParameter(new mcnm("mlcl"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for dmap.listingitem
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.listingitem"));
		dict.addParameter(new mcnm("mlit"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for dmap.bag
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.bag"));
		dict.addParameter(new mcnm("mbcl"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for dmap.dictionary
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.dictionary"));
		dict.addParameter(new mcnm("mdcl"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for dmap.serverinforesponse
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.serverinforesponse"));
		dict.addParameter(new mcnm("msrv"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for dmap.authenticationmethod
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.authenticationmethod"));
		dict.addParameter(new mcnm("msau"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.authenticationschemes
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.authenticationschemes"));
		dict.addParameter(new mcnm("msas"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.loginrequired
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.loginrequired"));
		dict.addParameter(new mcnm("mslr"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.protocolversion
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.protocolversion"));
		dict.addParameter(new mcnm("mpro"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_VERSION));

		// Dictonary Entry for dmap.supportsautologout
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.supportsautologout"));
		dict.addParameter(new mcnm("msal"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.supportsupdate
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.supportsupdate"));
		dict.addParameter(new mcnm("msup"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.supportspersistentids
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.supportspersistentids"));
		dict.addParameter(new mcnm("mspi"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.supportsextensions
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.supportsextensions"));
		dict.addParameter(new mcnm("msex"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.supportsbrowse
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.supportsbrowse"));
		dict.addParameter(new mcnm("msbr"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.supportsquery
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.supportsquery"));
		dict.addParameter(new mcnm("msqy"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.supportsindex
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.supportsindex"));
		dict.addParameter(new mcnm("msix"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.supportsresolve
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.supportsresolve"));
		dict.addParameter(new mcnm("msrs"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.timeoutinterval
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.timeoutinterval"));
		dict.addParameter(new mcnm("mstm"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.databasescount
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.databasescount"));
		dict.addParameter(new mcnm("msdc"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.utctime
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.utctime"));
		dict.addParameter(new mcnm("mstc"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_DATE));

		// Dictonary Entry for dmap.utcoffset
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.utcoffset"));
		dict.addParameter(new mcnm("msto"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_UINT));

		// Dictonary Entry for dmap.loginresponse
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.loginresponse"));
		dict.addParameter(new mcnm("mlog"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for dmap.sessionid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.sessionid"));
		dict.addParameter(new mcnm("mlid"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.updateresponse
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.updateresponse"));
		dict.addParameter(new mcnm("mupd"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for dmap.serverrevision
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.serverrevision"));
		dict.addParameter(new mcnm("musr"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.updatetype
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.updatetype"));
		dict.addParameter(new mcnm("muty"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for dmap.deletedidlisting
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.deletedidlisting"));
		dict.addParameter(new mcnm("mudl"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for dmap.contentcodesresponse
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.contentcodesresponse"));
		dict.addParameter(new mcnm("mccr"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for dmap.contentcodesnumber
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.contentcodesnumber"));
		dict.addParameter(new mcnm("mcnm"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dmap.contentcodesname
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.contentcodesname"));
		dict.addParameter(new mcnm("mcna"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for dmap.contentcodestype
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dmap.contentcodestype"));
		dict.addParameter(new mcnm("mcty"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_SHORT));

		// DAAP Codes
		// Dictonary Entry for daap.supportsextradata
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.supportsextradata"));
		dict.addParameter(new mcnm("ated"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_SHORT));

		// Dictonary Entry for daap.protocolversion
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.protocolversion"));
		dict.addParameter(new mcnm("apro"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_VERSION));

		// Dictonary Entry for daap.serverdatabases
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.serverdatabases"));
		dict.addParameter(new mcnm("avdb"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.databasebrowse
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.databasebrowse"));
		dict.addParameter(new mcnm("abro"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.browsealbumlisting
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.browsealbumlisting"));
		dict.addParameter(new mcnm("abal"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.browseartistlisting
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.browseartistlisting"));
		dict.addParameter(new mcnm("abar"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.browsecomposerlisting
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.browsecomposerlisting"));
		dict.addParameter(new mcnm("abcp"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.browsegenrelisting
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.browsegenrelisting"));
		dict.addParameter(new mcnm("abgn"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.databasesongs
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.databasesongs"));
		dict.addParameter(new mcnm("adbs"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.songalbum
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songalbum"));
		dict.addParameter(new mcnm("asal"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songgrouping
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songgrouping"));
		dict.addParameter(new mcnm("agrp"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songartist
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songartist"));
		dict.addParameter(new mcnm("asar"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songalbumartist
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songalbumartist"));
		dict.addParameter(new mcnm("asaa"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songbeatsperminute
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songbeatsperminute"));
		dict.addParameter(new mcnm("asbt"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_SHORT));

		// Dictonary Entry for daap.songbitrate
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songbitrate"));
		dict.addParameter(new mcnm("asbr"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_SHORT));

		// Dictonary Entry for daap.songcomment
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songcomment"));
		dict.addParameter(new mcnm("ascm"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songcodectype
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songcodectype"));
		dict.addParameter(new mcnm("ascd"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for daap.songcodecsubtype
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songcodecsubtype"));
		dict.addParameter(new mcnm("ascs"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for daap.songcompilation
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songcompilation"));
		dict.addParameter(new mcnm("asco"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for daap.songcomposer
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songcomposer"));
		dict.addParameter(new mcnm("ascp"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songdateadded
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songdateadded"));
		dict.addParameter(new mcnm("asda"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_DATE));

		// Dictonary Entry for daap.songdatemodified
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songdatemodified"));
		dict.addParameter(new mcnm("asdm"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_DATE));

		// Dictonary Entry for daap.songdisccount
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songdisccount"));
		dict.addParameter(new mcnm("asdc"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_SHORT));

		// Dictonary Entry for daap.songdiscnumber
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songdiscnumber"));
		dict.addParameter(new mcnm("asdn"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_SHORT));

		// Dictonary Entry for daap.songdisabled
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songdisabled"));
		dict.addParameter(new mcnm("asdb"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for daap.songeqpreset
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songeqpreset"));
		dict.addParameter(new mcnm("aseq"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songformat
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songformat"));
		dict.addParameter(new mcnm("asfm"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songgenre
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songgenre"));
		dict.addParameter(new mcnm("asgn"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songdescription
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songdescription"));
		dict.addParameter(new mcnm("asdt"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songrelativevolume
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songrelativevolume"));
		dict.addParameter(new mcnm("asrv"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_UBYTE));

		// Dictonary Entry for daap.songsamplerate
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songsamplerate"));
		dict.addParameter(new mcnm("assr"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for daap.songsize
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songsize"));
		dict.addParameter(new mcnm("assz"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for daap.songstarttime
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songstarttime"));
		dict.addParameter(new mcnm("asst"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for daap.songstoptime
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songstoptime"));
		dict.addParameter(new mcnm("assp"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for daap.songtime
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songtime"));
		dict.addParameter(new mcnm("astm"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for daap.songtrackcount
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songtrackcount"));
		dict.addParameter(new mcnm("astc"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_SHORT));

		// Dictonary Entry for daap.songtracknumber
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songtracknumber"));
		dict.addParameter(new mcnm("astn"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_SHORT));

		// Dictonary Entry for daap.songuserrating
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songuserrating"));
		dict.addParameter(new mcnm("asur"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for daap.songyear
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songyear"));
		dict.addParameter(new mcnm("asyr"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_SHORT));

		// Dictonary Entry for daap.songdatakind
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songdatakind"));
		dict.addParameter(new mcnm("asdk"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for daap.songdataurl
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songdataurl"));
		dict.addParameter(new mcnm("asul"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songcategory
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songcategory"));
		dict.addParameter(new mcnm("asct"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songcontentdescription
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songcontentdescription"));
		dict.addParameter(new mcnm("ascn"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songlongcontentdescription
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songlongcontentdescription"));
		dict.addParameter(new mcnm("aslc"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songkeywords
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songkeywords"));
		dict.addParameter(new mcnm("asky"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.songcontentrating
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songcontentrating"));
		dict.addParameter(new mcnm("ascr"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for daap.songgapless
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songgapless"));
		dict.addParameter(new mcnm("asgp"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for daap.songextradata
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songextradata"));
		dict.addParameter(new mcnm("ased"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_SHORT));

		// Dictonary Entry for daap.songdatereleased
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songdatereleased"));
		dict.addParameter(new mcnm("asdr"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_DATE));

		// Dictonary Entry for daap.songdatepurchased
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songdatepurchased"));
		dict.addParameter(new mcnm("asdp"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_DATE));

		// Dictonary Entry for daap.songhasbeenplayed
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songhasbeenplayed"));
		dict.addParameter(new mcnm("ashp"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for daap.sortname
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.sortname"));
		dict.addParameter(new mcnm("assn"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.sortartist
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.sortartist"));
		dict.addParameter(new mcnm("assa"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.sortalbumartist
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.sortalbumartist"));
		dict.addParameter(new mcnm("assl"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.sortalbum
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.sortalbum"));
		dict.addParameter(new mcnm("assu"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.sortcomposer
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.sortcomposer"));
		dict.addParameter(new mcnm("assc"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.sortseriesname
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.sortseriesname"));
		dict.addParameter(new mcnm("asss"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for daap.bookmarkable
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.bookmarkable"));
		dict.addParameter(new mcnm("asbk"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for daap.songbookmark
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.songbookmark"));
		dict.addParameter(new mcnm("asbo"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for daap.databaseplaylists
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.databaseplaylists"));
		dict.addParameter(new mcnm("aply"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.baseplaylist
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.baseplaylist"));
		dict.addParameter(new mcnm("abpl"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for daap.playlistsongs
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.playlistsongs"));
		dict.addParameter(new mcnm("apso"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.resolve
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.resolve"));
		dict.addParameter(new mcnm("arsv"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.resolveinfo
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.resolveinfo"));
		dict.addParameter(new mcnm("arif"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LIST));

		// Dictonary Entry for daap.playlistshufflemode
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.playlistshufflemode"));
		dict.addParameter(new mcnm("apsm"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for daap.playlistrepeatmode
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("daap.playlistrepeatmode"));
		dict.addParameter(new mcnm("aprm"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// DPAP Codes
		// Dictonary Entry for dpap.aspectratio
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dpap.aspectratio"));
		dict.addParameter(new mcnm("pasp"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for dpap.imageformat
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dpap.imageformat"));
		dict.addParameter(new mcnm("pfmt"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for dpap.imagepixelheight
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dpap.imagepixelheight"));
		dict.addParameter(new mcnm("phgt"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dpap.creationdate
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dpap.creationdate"));
		dict.addParameter(new mcnm("pcid"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_DATE));

		// Dictonary Entry for dpap.imagefilesize
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dpap.imagefilesize"));
		dict.addParameter(new mcnm("pifs"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dpap.imagefilename
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dpap.imagefilename"));
		dict.addParameter(new mcnm("pimf"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for dpap.imagelargefilesize
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dpap.imagelargefilesize"));
		dict.addParameter(new mcnm("plsz"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LONG));

		// Dictonary Entry for dpap.protocolversion
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dpap.protocolversion"));
		dict.addParameter(new mcnm("ppro"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_VERSION));

		// Dictonary Entry for dpap.imagerating
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dpap.imagerating"));
		dict.addParameter(new mcnm("prat"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for dpap.imagepixelwidth
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("dpap.imagepixelwidth"));
		dict.addParameter(new mcnm("pwth"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// APPLE EXTENSIONS
		// Dictonary Entry for com.apple.itunes.norm-volume
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.norm-volume"));
		dict.addParameter(new mcnm("aeNV"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.smart-playlist
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.smart-playlist"));
		dict.addParameter(new mcnm("aeSP"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for com.apple.itunes.itms-songid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.itms-songid"));
		dict.addParameter(new mcnm("aeSI"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.itms-artistid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.itms-artistid"));
		dict.addParameter(new mcnm("aeAI"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.itms-playlistid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.itms-playlistid"));
		dict.addParameter(new mcnm("aePI"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.itms-composerid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.itms-composerid"));
		dict.addParameter(new mcnm("aeCI"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.itms-genreid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.itms-genreid"));
		dict.addParameter(new mcnm("aeGI"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.itms-storefrontid
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.itms-storefrontid"));
		dict.addParameter(new mcnm("aeSF"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.music-sharing-version
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.music-sharing-version"));
		dict.addParameter(new mcnm("aeSV"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.is-podcast
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.is-podcast"));
		dict.addParameter(new mcnm("aePC"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for com.apple.itunes.is-podcast-playlist
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.is-podcast-playlist"));
		dict.addParameter(new mcnm("aePP"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for com.apple.itunes.has-video
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.has-video"));
		dict.addParameter(new mcnm("aeHV"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for com.apple.itunes.mediakind
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.mediakind"));
		dict.addParameter(new mcnm("aeMK"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for com.apple.itunes.series-name
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.series-name"));
		dict.addParameter(new mcnm("aeSN"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for com.apple.itunes.network-name
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.network-name"));
		dict.addParameter(new mcnm("aeNN"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for com.apple.itunes.episode-num-str
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.episode-num-str"));
		dict.addParameter(new mcnm("aeEN"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_STRING));

		// Dictonary Entry for com.apple.itunes.episode-sort
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.episode-sort"));
		dict.addParameter(new mcnm("aeES"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.season-num
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.season-num"));
		dict.addParameter(new mcnm("aeSU"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.gapless-heur
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.gapless-heur"));
		dict.addParameter(new mcnm("aeGH"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.gapless-enc-del
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.gapless-enc-del"));
		dict.addParameter(new mcnm("aeGE"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.gapless-enc-dr
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.gapless-enc-dr"));
		dict.addParameter(new mcnm("aeGD"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_INT));

		// Dictonary Entry for com.apple.itunes.gapless-dur
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.gapless-dur"));
		dict.addParameter(new mcnm("aeGU"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LONG));

		// Dictonary Entry for com.apple.itunes.gapless-resy
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.gapless-resy"));
		dict.addParameter(new mcnm("aeGR"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_LONG));

		// Dictonary Entry for com.apple.itunes.req-fplay
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.req-fplay"));
		dict.addParameter(new mcnm("aeFP"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));

		// Dictonary Entry for com.apple.itunes.special-playlist
		dict = new mdcl();
		root.addParameter(dict);
		dict.addParameter(new mcna("com.apple.itunes.special-playlist"));
		dict.addParameter(new mcnm("aePS"));
		dict.addParameter(new mcty(DMAPParameter.DATATYPE_BYTE));
	}

}
