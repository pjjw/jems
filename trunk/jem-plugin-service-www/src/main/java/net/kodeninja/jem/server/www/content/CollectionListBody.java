package net.kodeninja.jem.server.www.content;

import java.util.Set;

import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.jem.server.storage.MediaCollection;
import net.kodeninja.util.MimeType;

public class CollectionListBody extends HTTPTextBody {
	private static MimeType HTTP_MIMETYPE = new MimeType("text", "html");

	public CollectionListBody(Set<MediaCollection> collections) {
		super(HTTP_MIMETYPE);
		text.append("<html>\n");
		text.append("  <head>\n");
		text.append("    <title>WWW Media Interface - Media By Collection</title>\n");
		text.append("  </head>\n");
		text.append("  <body>\n");
		for (MediaCollection col: collections) {
			text.append("    <a href=\"/collections/");
			text.append(col.hashCode());
			text.append("/\">");
			text.append(col.getCollectionName());
			text.append("</a><br>\n");
		}

		text.append("  </body>\n");
		text.append("</html>\n");
	}
}
