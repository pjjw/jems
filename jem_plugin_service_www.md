# Introduction #

This plugin provides a basic (_read: very basic_) web interface for streaming and viewing media.

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

**Note:** This service requires an entry in the services section.

**Legend:**
  * **port** (_Optional_) - Specifies which port the http server should run on. Defaults to 80 (the standard HTTP port.)