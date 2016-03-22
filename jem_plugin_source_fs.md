# Introduction #
This plug in's main purpose is to scan local directories for media and add it to the the internal media list. In addition to adding media, if media that is found on the harddrive is missing/removed, it is automatically removed from the media list.

**Note:** Any media found is only stored for that session and is lost when the server shuts down. For a persistent media list, please see the XML Media Source plugin (see [jem\_plugin\_source\_xml](jem_plugin_source_xml.md)).

# Configuration #

This plugin is set up in the modules section of the JemsConfig.xml file.

```
<?xml version='1.0' encoding='utf-8'?> 
<jems>
  <modules>
    ...
    <module name="FS Media Plugin" class="net.kodeninja.jem.server.content.fs.DirectoryMediaSource">
      <refresh>180000</refresh>
      <ignoreUnknown>true</ignoreUnknown>
      <directory>C:/shared/</directory>
      <directory>D:/shared/</directory>
      <mimetype type="audio/mpeg" class="net.kodeninja.jem.server.content.mimetypes.MPEGAudioFile">
        <extension>mp3</extension>
      </mimetype>
      <mimetype type="video/quicktime" class="net.kodeninja.jem.server.content.mimetypes.GenericFile">
        <extension>mov</extension>
        <extension>m4v</extension>
      </mimetype>
      <mimetype type="image/jpeg" class="net.kodeninja.jem.server.content.mimetypes.ImageFile">
        <extension>jpeg</extension>
        <extension>jpg</extension>
      </mimetype>
    </module>
    ...
  </modules>
  <services>
  ...
    <service module="FS Media Plugin"/>
  ...
  </services>
  ...
</jems>
```
Figure 1.1 - _Sample module definition_

The configuration for this module is split up in to 3 sections: Module Options, Directories, and Mimetype/Metadata Module mappings.

**Note:** This plugin requires an entry in the services section.

## Module Options ##
There is currently two options for the module:
  * **refresh** (_Optional_) - Use this tag to specify how long the file system scanner should wait to rescan the directory for changes. It is specified in seconds (ie. 30000 is 5 minutes). The default value if it is not specified is 10 minutes, or a value of 60000. You may only specify this tag once.
  * **ignoreUnknown** (_Optional_) - Set this to false to enumerate files that have no mimetype defined for them in the Mimetype/Metadata mapping.
## Directories ##
The `<directory>` tag is used specify a directory to scan for media. You may specify as many directories as you wish, by repeating the tag. You are advised only to only select directories that contain media. (For example, don't specify an entire harddrive, if most of it isn't media you wished shared)

## Mimetype/Metadata Module mappings ##
By default, the directory scanner knows nothing about the files its finding. To remedy this, we create mimetype/metadata module mappings based on file extensions. What does this mean? It means we are telling the scanning when it encounters a certain file extension (for example, .mp3) that this file is a music file and it should use the correct module to parse the ID3 tags and song length from the file.

The `type` attribute is used to specify the file's mimetype. These are used to specify what is actually stored within the file as there can be multiple extensions for files of the same type.

The `class` attribute specifies which java module to load parse the file for metadata. The current available ones are listed later.

The `extension` sub tags are used to specify which file extensions to use to match a file to it's parent's metadata parser and mimetype. These tags can be specified multiple times, once for each extension to match on. For example, for matching 'jpg' and 'jpeg' as the same mimetype, would require 2 `extension` sub tags.

|**Media**|**Mimetype**|**Class**|**Extensions**|Notes|
|:--------|:-----------|:--------|:-------------|:----|
|Pictures |image/_(dependent on the extension)_|`net.kodeninja.jem.server.content.mimetypes.ImageFile`|.jpg, .jpeg, .gif, .bmp, etc...|This module uses the built in java image libraries for parsing the images. The formats supported are the same that java can open natively.|
|MP3s (Music)|audio/mpeg  |`net.kodeninja.jem.server.content.mimetypes.MPEGAudioFile`|.mp1, .mp2, mp3|Failing to gather any metadata from the file, it falls back to the AudioFile class|
|MP4 Audio|audio/mp4   |`net.kodeninja.jem.server.content.mimetypes.MPEG4File`|.m4a, .mp4    |Note that a mp4 file can be a video file or an audio only file. This plugin can not tell the difference yet.|
|Other Music|audio/_(dependent on the extension)_|`net.kodeninja.jem.server.content.mimetypes.AudioFile`|.ogg, .wav, .flac|This module attempts to gather metadata from the filename.|
|AVIs     |video/avi   |`net.kodeninja.jem.server.content.mimetypes.AVIFile`|.avi, .divx   |     |
|MP4 Video|video/mp4   |`net.kodeninja.jem.server.content.mimetypes.MPEG4File`|.mp4, .m4v, mp4v|Note that a mp4 file can be a video file or an audio only file. This plugin can not tell the difference yet.|
|Any file not listed|_(dependent on the extension)_|`net.kodeninja.jem.server.content.mimetypes.GenericFile`|_Any_         |If a module does not yet exist for a file use this one to grab as much as it can from a file.|

It is suggested that even though a file may not have a parser yet, that an entry is created for it with the correct mimetype as some plug ins may only use that file if the file has an associated mime type.