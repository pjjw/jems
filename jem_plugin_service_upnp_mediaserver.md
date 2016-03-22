# Introduction #

This plugin exposes the music, video and pictures to clients that are compatible with the UPnP MediaServer 1.0 specification. Extra care has been taken to extend the server so that it is recognizable by the XBox 360 aswell. (For more information, see the [the notes](XBox360Notes.md) on this.)

# Configuration #

This plugin is set up in the modules section of the JemsConfig.xml file.

```
<?xml version='1.0' encoding='utf-8'?> 
<jems>
  <modules>
    ...
    <module name="WWW Server" class="net.kodeninja.jem.server.www.WWWService">
      <port>80</port>
    </module>
    ...
    <module name="UPnP Media Server" class="net.kodeninja.jem.server.UPnP.MediaServer">
      <friendlyname>Jems Media Center</friendlyname>
      <www-module>WWW Server</www-module>
      <uuid>24b1a1e6-4107-407e-b1c7-8945fb9181a6</uuid>
    </module>
    ...
  </modules>
  <services>
    ...
    <service module="WWW Server"/>
    ...
  </services>
  ...
</jems>
```
Figure 1.1 - _Sample module definition_

**Note:** The UPnP server currently requires the WWW server to be previously defined in order to work.

**Notes:** While the UPnP server does **not** require an entry in the services area, the WWW server still does.

**Legend:**
  * **friendlyname**  - Specifies the name the server advertises itself under.
  * **www-module** - The module name of the WWW server.
  * **uuid** - A unique identifier for the server. If none is specified, one is generated and logged upon start up. It is recommended that you set this field to the generated id so that media renders and control points will recognize the media center as the same one on each boot.