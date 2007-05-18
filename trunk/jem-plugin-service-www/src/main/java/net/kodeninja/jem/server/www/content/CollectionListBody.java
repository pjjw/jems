package net.kodeninja.jem.server.www.content;

import java.util.Iterator;

import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.jem.server.content.MediaCollection;
import net.kodeninja.util.MimeType;

public class CollectionListBody extends HTTPTextBody {
	private static MimeType HTTP_MIMETYPE = new MimeType("text", "html");

	public CollectionListBody(Iterator<MediaCollection> it) {
		super("", HTTP_MIMETYPE);
		text += "<html>\n";
		text += "  <head>\n";
		text += "    <title>WWW Media Interface - Media By Collection</title>\n";
		text += "  </head>\n";
		text += "  <body>\n";
		while (it.hasNext()) {
			MediaCollection col = it.next();
			text += "    <a href=\"/collections/" + col.hashCode() + "/\">"
					+ col.getCollectionName() + "</a><br>\n";
		}

		text += "  </body>\n";
		text += "</html>\n";
	}
}
