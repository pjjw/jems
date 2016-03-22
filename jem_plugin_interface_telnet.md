# Introduction #

This plugin provides a basic telnet interface for administering the server.

# Configuration #

This plugin is set up in the modules section of the JemsConfig.xml file.

```
<?xml version='1.0' encoding='utf-8'?> 
<jems>
  <modules>
    ...
    <module name="Telnet" class="net.kodeninja.jem.server.console.telnet.TelnetConsole">
      <port>23</port>
    </module>
    ...
  </modules>
  <services>
    ...
    <service module="Telnet"/>
    ...
  </services>
  ...
</jems>
```
Figure 1.1 - _Sample module definition_

**Note:** This service requires an entry in the services section.

**Legend:**
  * **port** (_Optional_) - Specifies which port the telnet server should run on. Defaults to 23 (the standard Telnet port.)