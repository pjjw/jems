package net.kodeninja.jem.server.www.content;

import java.io.FileNotFoundException;

import net.kodeninja.http.packet.HTTPStreamBody;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.util.MimeType;

public class PlayerBody extends HTTPStreamBody {
	private static MimeType HTTP_MIMETYPE = new MimeType("text", "html");

	public PlayerBody(String type) throws FileNotFoundException {
		super(JemServer.getResourceAsStream("res-www/" + type + "player.html"), HTTP_MIMETYPE);
	}

}
