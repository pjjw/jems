package net.kodeninja.http.packet.extra;

import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.util.MimeType;

public class HTTPErrorTextBody extends HTTPTextBody {
	public HTTPErrorTextBody(HTTPResponseCode ErrorCode) {
		super("<html>\n" + "<head><title>" + ErrorCode.getDescription()
				+ "</title></head>\n" + "<body>\n" + "<h2>"
				+ ErrorCode.getCode() + " - " + ErrorCode.getDescription()
				+ "</h2>\n" + "<hr>\n" + "</body>\n" + "</html>\n",
				new MimeType("text", "html"));

	}

}
