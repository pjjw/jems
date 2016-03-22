# Introduction #

JEMS configuration is done through customizing the JemsConfig.xml file which resides in the working directory of the program. Here we will explore the format and options it provides to get it running and sharing media.

## Using the provided packages ##

### Downloading ###

The first thing you need to know is that the server comes in two flavors: **Full** and **Minimal**. Full comes with all the official developed plug ins which drive the server, while minimal is the base server files. Since almost all functionality is provided by the plug ins, first time installations are suggested to get the full package.

You will also notice that the full and minimal packages each have 3 separate downloads. The contents of each are all the same, but they are provided to that people may select the format they are most comfortable with. Windows users will likely want the ZIP distribution.

### Installation ###

Once the package has downloaded, unzip it to a directory you wish to install it to. If you downloaded the full package this should create a directory named "plugins." This directory contains plug ins and any supporting JARs (Java library files.)

You can skip down to the configuration section.

## Compiling your own ##

Check back later!

# Configuration #

Open your favorite text editor (or favorite XML editor should you have one) and create a file named "JemsConfig.xml", without the quotes of course. This is the configuration file that drives the JEMS framework. It is used to specify which plug ins to use, as well as the options for each plug in.

The basic format of the configuration file is shown below:
```
<?xml version='1.0' encoding='utf-8'?> 
<jems>
  <modules>
  </modules>
  <services>
  </services>
  <loggers>
  </loggers>
  <interfaces>
  </interfaces>
  <transcoders>
  </transcoders>
  <media>
  </media>
</jems>

```
Figure 1.1 - _Basic JemsConfig.xml file_

The "**modules**" section is used to tell JEMS to load a specified plug in.
The "**services**" is used when a module provides a running background service, suck as a DAAP server (for sharing music and video iTunes, FrontRow, Amarok and other DAAP capable clients)
The "**loggers**" section is used when a module provides functionality for saving or displaying server messages from loaded modules.
The "**interfaces**" section is used when a module provides some sort of interface to the server, for example over a telnet connection.
The "**transcoders**" section provides the server with transcoding modules so when a requests media in a different format then the original source, the server knows what module it can ask to provide it.
The "**media**" section is used for setting up media specific options. For example, smart play list modules may be specified here.

The specific configuration used for each section is dependent on the plug in, so please refer to that plug in's documentation for more instructions.