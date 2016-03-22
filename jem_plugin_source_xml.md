# Introduction #

This plugin is used to maintain a persistent media list over runs of the server. Without this plugin, or one like it, the media list is lost and must be rescanned.

# Configuration #

This plugin is set up in the modules section of the JemsConfig.xml file.

```
<?xml version='1.0' encoding='utf-8'?> 
<jems>
  <modules>
    ...
    <module name="XML Media List" class="net.kodeninja.jem.server.content.xml.XMLMediaSource2">
      <path>medialist.xml</path>
    </module>
    ...
  </modules>
  ...
</jems>
```
Figure 1.1 - _Sample module definition_

**Legend:**
  * **path** (_Optional_) - Specifies what file to store the saved metadata in. If left unspecified, a file named 'JemsMediaList.xml' is created and used in the local directory.

**Note:** The module class used to be `net.kodeninja.jem.server.content.xml.XMLMediaSource` this is now deprecated, and you should use the newer, faster `net.kodeninja.jem.server.content.xml.XMLMediaSource2`.