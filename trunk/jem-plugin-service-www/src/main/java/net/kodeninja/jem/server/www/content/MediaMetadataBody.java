package net.kodeninja.jem.server.www.content;

import java.util.Date;

import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.MetadataTypes;

public class MediaMetadataBody extends HTTPTextBody {

	public MediaMetadataBody(MediaItem mi) {
		super(HTTP_MIMETYPE);
		text += "<html>\n";
		text += "  <head>\n";
		text += "    <title>WWW Media Interface - Metadata List</title>\n";
		text += "  </head>\n";
		text += "  <body>\n";
		text += "    <h2>" + mi.getMediaName() + "</h2>\n";
		text += "    <table border=\"0\">\n";
		for (MetadataTypes mdt: MetadataTypes.values())
			if (mi.getMetadata(mdt) != null) {
				if (mdt == MetadataTypes.DateAdded)
					text += "      <tr><td>Date Added:</td><td>" + new Date(Long.parseLong(mi.getMetadata(mdt))) + "</td></tr>\n";
				else if ((mdt != MetadataTypes.Title) || (mdt != MetadataTypes.Artist)) 
					text += "      <tr><td>" + mdt.name() + ":</td><td>" + mi.getMetadata(mdt) + "</td></tr>\n";
			}
		text += "    </table>\n";
		text += "  </body>\n";
		text += "</html>\n";
	}

}
