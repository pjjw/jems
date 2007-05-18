package net.kodeninja.jem.server.www.content;

import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.util.MimeType;

public class IndexBody extends HTTPTextBody {
	private static MimeType HTTP_MIMETYPE = new MimeType("text", "html");

	public IndexBody() {
		super(HTTP_MIMETYPE);
		text += "<html>\n";
		text += "  <head>\n";
		text += "    <title>WWW Media Interface - Home</title>\n";
		text += "  </head>\n";
		text += "  <body>\n";
		text += "  <a href=\"/items/\">Show All</a><br>\n";
		text += "  <a href=\"/collections/\">By Collection</a><br>\n";
		text += "  </body>\n";
		text += "</html>\n";
	}
}
