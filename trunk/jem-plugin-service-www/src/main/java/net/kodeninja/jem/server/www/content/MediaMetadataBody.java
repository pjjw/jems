package net.kodeninja.jem.server.www.content;

import java.util.Date;

import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.Metadata;
import net.kodeninja.jem.server.storage.MetadataType;

public class MediaMetadataBody extends HTTPTextBody {

	public MediaMetadataBody(MediaItem mi) {
		super(HTTPTextBody.HTML_MIMETYPE);
		text.append("<html>\n");
		text.append("  <head>\n");
		text.append("    <title>WWW Media Interface - Metadata List</title>\n");
		text.append("  </head>\n");
		text.append("  <body>\n");
		text.append("    <h2>");
		text.append(JemServer.getMediaName(mi));
		text.append("</h2>\n");
		text.append("    <table border=\"0\">\n");
		text.append("      <tr><td align=\"right\"><b>Mime Type:</b></td><td>" + JemServer.getMediaStorage().getMimeType(mi) + "</td></tr>\n");

		for (Metadata metadata: mi.getMetadataList()) {
			if (metadata.getType() == MetadataType.DateAdded) {
				text.append("      <tr><td align=\"right\"><b>Date Added:</b></td><td>");
				text.append(new Date(Long.parseLong(metadata.getValue())));
				text.append("</td></tr>\n");
			}
			else if ((metadata.getType() != MetadataType.Title) || (metadata.getType() != MetadataType.Artist)) { 
				text.append("      <tr><td align=\"right\"><b>");
				text.append(metadata.getType().name());
				text.append(":</b></td><td>");
				text.append(metadata.getValue());
				text.append("</td></tr>\n");
			}
		}
		text.append("    </table>\n");
		text.append("  </body>\n");
		text.append("</html>\n");
	}

}
