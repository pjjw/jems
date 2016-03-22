# Introduction #

This plugin servers out audio and video to DAAP capable clients (ie. iTunes, FrontRow) and pictures out to DPAP capable clients (ie. iPhoto, FrontRow)

# Configuration #

This plugin is set up in the modules section of the JemsConfig.xml file.

```
<?xml version='1.0' encoding='utf-8'?> 
<jems>
  <modules>
    ...
    <module name="DMAP Server" class="net.kodeninja.jem.server.DMAP.DMAPService">
      <servicename>JemServer</servicename>
      <port>3689</port>
      <transcode>video/x-ms-wmv</transcode>
      <transcode>video/avi</transcode>
      <transcode>video/mpeg</transcode>
    </module>
    ...
  </modules>
  <services>
    ...
    <service module="DMAP Server"/>
    ...
  </services>
  ...
</jems>
```
Figure 1.1 - _Sample module definition_

**Note:** This service requires an entry in the services section.

**Legend:**
  * **servicename** - Specifies the name the service should be advertised under.
  * **port** (_Optional_) - Specifies what port the service should use. The iTunes default (3689) is used if this is not specified.
  * **transcode** (_Optional_) - These tags specify what mimetypes the service should attempt to transcode into the Quicktime format that iTunes/FrontRow requires. _Currently there is **no** transcoder plugin that will do this as FFmpeg doesn't support transcoding to/from quicktime files via stdin/stdout._ Any mimetypes not specified will not be touched by the transcoder and will be sent as is.