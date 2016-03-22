# Introduction #

The XBox 360 is Microsoft's latest incarnation for console gaming. Other then playing games, and providing downloadable content, the XBox 360 uses UPnP to find other Microsoft UPnP Media Servers on the network. Because of this, it uses its own superset of the UPnP specification for UPnP Media Servers, and adds several other undocumented requirements, as well as not completely following the UPnP standard.

This page is dedicated to the gotchas when trying to have a conversation with the XBox 360 using the UPnP Media Server 1.0 specification.

# Discovery #
  * The XBox is mildly picky about the discovery phase.
  * The date field must be properly formatted, and in GMT Time.
  * The XBox will search for both MediaServer and MS-MediaServer services. You only need to reply to one.
  * The XBox also honors NOTIFY events, so a server can be started after the initial search.

**Example NOTIFY Packet from JEMS**
```
NOTIFY * HTTP/1.1
HOST: 239.255.255.250:1900
CACHE-CONTROL: max-age=1800
LOCATION: http://192.168.0.196:49226/upnp/device/24b1a1e6-4107-407e-b1c7-8945fb9181a6/description.xml
NT: upnp:rootdevice
NTS: ssdp:alive
SERVER: Windows-Vista/6.0 UPnP/1.0 KodeNinja-UPnP-Stack/0.1
USN: uuid:24b1a1e6-4107-407e-b1c7-8945fb9181a6::upnp:rootdevice
```

**Example M-SEARCH Response Packet from JEMS**
```
HTTP/1.1 200 OK
CACHE-CONTROL: max-age=1800
DATE: Wed, 12 Sep 2007 17:32:19 GMT
EXT: 
LOCATION: http://192.168.0.196:49226/upnp/device/24b1a1e6-4107-407e-b1c7-8945fb9181a6/description.xml
SERVER: Windows-Vista/6.0 UPnP/1.0 KodeNinja-UPnP-Stack/0.1
ST: urn:schemas-upnp-org:device:MediaServer:1
USN: uuid:24b1a1e6-4107-407e-b1c7-8945fb9181a6::urn:schemas-upnp-org:device:MediaServer:1
```

# Description #

The description phase really seems to be the gate keeper to see whether or not your device will show up in the list recognized devices when the XBox is searching.

  * It is very picky about the friendlyName and modelName elements. See below.
  * Requires the undocumented [X\_MS\_MediaReceiverRegistrar](X_MS_MediaReceiverRegistrar.md) service. See below.
  * Does not require/make use of the iconList element.
  * Does not require the DNLA element.

## friendlyName and modelName Elements ##

The friendly name must be in the format of one of the following:
  * <computer name> : 

&lt;description&gt;

 : <model name>
  * <computer name> : 

&lt;description&gt;


Any other format and it refuses to list the device. While no specific model number seems to be enforced, it does seem that how the friendlyName element is interpreted does depend on the modelNumber element.
Please note the computer name is not verified by the XBox so any value can be used as long as it would be a valid NetBios computer name. Ie. single words such as "JEMS".

The model name must be a Microsoft one. Such as:
  * Windows Media Connect
  * Windows Media Player
It is unsure at this time whether or not the XBox make assumptions depending on the value of this string.

## [X\_MS\_MediaReceiverRegistrar](X_MS_MediaReceiverRegistrar.md) Service ##

This service is required to a certain extent. It must be listed, however all requests to the service can be safely denied and the description can be empty or even blank. The XBox will periodically make queries to the non-existent service, but never actually complain about it not working.
If the service is not present in the device description, the device will still show up, however upon connection, it will timeout and complain about network connectivity problems, or something equally generic.

**Example [X\_MS\_MediaReceiverRegistrar](X_MS_MediaReceiverRegistrar.md) Service Definition**
```
<service>
  <serviceType>urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:1</serviceType>
  <serviceId>urn:microsoft.com:serviceId:X_MS_MediaReceiverRegistrar</serviceId>
  <SCPDURL>URL OMITTED FOR READABILITY</SCPDURL>
  <controlURL>URL OMITTED FOR READABILITY</eventSubURL>
</service>
```

See also:
  * [Building\_a\_Network\_Device\_Compatible\_with\_WMC.doc](http://download.microsoft.com/download/6/5/d/65d7bd40-8b73-4b7c-a3ca-da04f2535421/Building_a_Network_Device_Compatible_with_WMC.doc) - Released by Microsoft, this service is described in Appendex B.
  * [Device\_UI\_Design\_Recommendations.doc](http://download.microsoft.com/download/6/5/d/65d7bd40-8b73-4b7c-a3ca-da04f2535421/Device_UI_Design_Recommendations.doc) - This server is talked about in the first couple of sections.
# Browsing and Searching #

For the most part, the XBox uses typical browsing and searching as described by the official UPnP Content Directory documentation except for a few minor notes.

  * It seems to expect the content directory to be laid out as WMC or WMP does, at least as far as container IDs are concerned. This layout is described by this document: [NetCompat\_WMP11.doc](http://download.microsoft.com/download/0/0/b/00bba048-35e6-4e5b-a3dc-36da83cbb0d1/NetCompat_WMP11.doc)
  * During browse operations it specifies a ContainerID instead of an ObjectID.

## XBox 360 Hierarchy ##

### Music ###
#### Albums ####
  * Action: Search
  * ContainerID: 7 (_'Music/Album' according to [NetCompat\_WMP11.doc](http://download.microsoft.com/download/0/0/b/00bba048-35e6-4e5b-a3dc-36da83cbb0d1/NetCompat_WMP11.doc)_)
  * Query: (upnp:class = "object.container.album.musicAlbum")

#### Artists ####
  * Action: Search
  * ContainerID: 6 (_'Music/Artist' according to [NetCompat\_WMP11.doc](http://download.microsoft.com/download/0/0/b/00bba048-35e6-4e5b-a3dc-36da83cbb0d1/NetCompat_WMP11.doc)_)
  * Query: (upnp:class = "object.container.person.musicArtist")

#### Saved Playlists ####
  * Action: Search
  * ContainerID: F (_'Music/Playlists' according to [NetCompat\_WMP11.doc](http://download.microsoft.com/download/0/0/b/00bba048-35e6-4e5b-a3dc-36da83cbb0d1/NetCompat_WMP11.doc)_)
  * Query: (upnp:class = "object.container.playlistContainer")

#### Songs ####
  * Action: Search
  * ContainerID: 4 (_'Music/All Music' according to [NetCompat\_WMP11.doc](http://download.microsoft.com/download/0/0/b/00bba048-35e6-4e5b-a3dc-36da83cbb0d1/NetCompat_WMP11.doc)_)
  * Query: (upnp:class derivedfrom "object.item.audioItem")

#### Genres ####
  * Action: Search
  * ContainerID: 5 (_'Music/Genre' according to [NetCompat\_WMP11.doc](http://download.microsoft.com/download/0/0/b/00bba048-35e6-4e5b-a3dc-36da83cbb0d1/NetCompat_WMP11.doc)_)
  * Query: (upnp:class = "object.container.genre.musicGenre")

### Pictures ###
  * Action: BrowseDirectChildern
  * ContainerID: 16 (_'Pictures/Folders' according to [NetCompat\_WMP11.doc](http://download.microsoft.com/download/0/0/b/00bba048-35e6-4e5b-a3dc-36da83cbb0d1/NetCompat_WMP11.doc)_)

### Video ###
  * Action: BrowseDirectChildern
  * ContainerID: 15 (_'Video/Folders' according to [NetCompat\_WMP11.doc](http://download.microsoft.com/download/0/0/b/00bba048-35e6-4e5b-a3dc-36da83cbb0d1/NetCompat_WMP11.doc)_)





