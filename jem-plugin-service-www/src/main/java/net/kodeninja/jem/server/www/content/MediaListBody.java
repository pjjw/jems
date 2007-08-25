package net.kodeninja.jem.server.www.content;

import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.www.WWWService;
import net.kodeninja.util.MimeType;

public class MediaListBody extends HTTPTextBody {

	public MediaListBody(WWWService owner, Iterable<MediaItem> items) {
		super(HTTPTextBody.HTML_MIMETYPE);
		text.append("<html>\n");
		text.append("  <head>\n");
		text.append("    <title>WWW Media Interface - Media List</title>\n");
		text.append("  </head>\n");
		text.append("  <body>\n");
		text.append("    <table border=\"0\">\n");
		
		for (MediaItem mi: items) {
			MimeType mimetype = JemServer.getMediaStorage().getMimeType(mi);
			
			text.append("      <tr>");
			text.append("        <td>");
			text.append(JemServer.getMediaName(mi));
			text.append("</td>");
			text.append("        <td>[ <a href=\"/items/");
			text.append(mi.hashCode());
			text.append("/info/\">Info</a> ]</td>");
			text.append("        <td>[ <a href=\"");
			text.append(owner.getItemStreamURI(mi));
			text.append("\">");
			if (mimetype.getPrimaryType().equals("image"))
				text.append("View");
			else
				text.append("Stream");
			text.append("</a> ]</td>");

			if ((JemServer.getResource("res-www/") + mimetype.getPrimaryType() +  "player.html") != null) {						
				text.append("        <td>[ <a href=\"/items/");
				text.append(mi.hashCode());
				text.append("/play/\">Play</a> ]</td>");
			}
			text.append("      </tr>\n");
		}
		
		text.append("    </table>\n");
		text.append("  </body>\n");
		text.append("</html>\n");
	}

}
