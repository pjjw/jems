package net.kodeninja.jem.server.www.content;

import java.util.Iterator;

import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.util.MimeType;

public class MediaListBody extends HTTPTextBody {
	private static MimeType HTTP_MIMETYPE = new MimeType("text", "html");

	public MediaListBody(Iterator<MediaItem> it) {
		super(HTTP_MIMETYPE);
		text += "<html>\n";
		text += "  <head>\n";
		text += "    <title>WWW Media Interface - Media List</title>\n";
		text += "  </head>\n";
		text += "  <body>\n";
		text += "    <table border=\"0\">\n";
		
		while (it.hasNext()) {
			MediaItem mi = it.next();
			text += "      <tr>";
			text += "        <td>" + mi.getMediaName() + "</td>";
			text += "        <td>[ <a href=\"/items/" + mi.hashCode() + "/info/\">Info</a> ]</td>";
			text += "        <td>[ <a href=\"/items/" + mi.hashCode() + "/stream/\">Stream</a> ]</td>";
			if (mi.getMediaMimeType().getPrimaryType().equals("video"))						
				text += "        <td>[ <a href=\"/items/" + mi.hashCode() + "/play/\">Play</a> ]</td>";
			text += "      </tr>\n";
		}
		
		text += "    </table>\n";
		text += "  </body>\n";
		text += "</html>\n";
	}

}
