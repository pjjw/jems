package net.kodeninja.jem.server.www.content;

import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.util.MimeType;

public class IndexBody extends HTTPTextBody {
	private static MimeType HTTP_MIMETYPE = new MimeType("text", "html");

	public IndexBody() {
		super(HTTP_MIMETYPE);
		text.append("<html>\n");
		text.append("  <head>\n");
		text.append("    <title>WWW Media Interface - Home</title>\n");
		text.append("  </head>\n");
		text.append("  <body>\n");
		text.append("  <a href=\"/items/\">Show All</a><br>\n");
		text.append("  <a href=\"/collections/\">By Collection</a><br>\n");
		text.append("  </body>\n");
		text.append("</html>\n");
	}
}
