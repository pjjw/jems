package net.kodeninja.jem.server.www.content;

import net.kodeninja.http.packet.HTTPStreamBody;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.util.MimeType;

public class PlayerBody extends HTTPStreamBody {
	private static MimeType HTTP_MIMETYPE = new MimeType("text", "html");

	public PlayerBody() {
		super(JemServer.getResourceAsStream("res/player.html"), HTTP_MIMETYPE);
	}

}
