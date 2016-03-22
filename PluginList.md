# Introduction #

There are many plugins that exist for the JEMS platform, and are in essence, what really makes JEMS useful.

# Plugin List #

## Media Metadata Plugins ##
These plugins deal directly with the media. Common uses include collecting media and its metadata, and storing all the metadata in between server launches.

  * [XML Storage](jem_plugin_source_xml.md) - This plugin is used to store metadata of collected media files while the server is off line.
  * [Filesystem Scanner](jem_plugin_source_fs.md) - This plugin scourers specified directories for any media and adds it to the JEMS system.

## Media Announcement Plugins ##
These plugins do the heavy lifting to provide your discovered media to all your devices. See also [Supported Devices](SupportedDevices.md).

  * [DMAP Server](jem_plugin_service_dmap.md) - This plugin uses DMAP to advertise media to DAAP (Canola, iTunes, FrontRow) and DPAP (Canola, iPhoto, FrontRow) capable clients.
  * [UPnP MediaServer 1.0](jem_plugin_service_upnp_mediaserver.md) - This plugin uses UPnP to provide data to UPnP aware clients, such as the XBox 360.
  * [WWW Server](jem_plugin_service_www.md) - This plugin provides a basic web interface for interacting with the stored media.

## Interface Plugins ##
These plugins give users an alternate method for interacting with the JEMS interface.

  * [Telnet Interface](jem_plugin_interface_telnet.md) - This plugin allows you to easily access the command line interface via the telnet protocol.

## Transcoder Plugins ##
These plugins allow for converting media on-the-fly to deliver to various clients that may not support the original format.

  * [FFmpeg Transcoder](jem_plugin_transcoder_ffmpeg.md) - Uses FFmpeg as a back end to enable transcoding of media.