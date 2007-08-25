package net.kodeninja.http.packet.extra;

import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPTextBody;

public class HTTPErrorTextBody extends HTTPTextBody {
	public HTTPErrorTextBody(HTTPResponseCode ErrorCode) {
		super(HTTPTextBody.HTML_MIMETYPE);
		text.append("<html>\n");
		text.append("<head><title>");
		text.append(ErrorCode.getDescription());
		text.append("</title></head>\n");
		text.append("<body>\n");
		text.append("<h2>");
		text.append(ErrorCode.getCode());
		text.append(" - ");
		text.append(ErrorCode.getDescription());
		text.append("</h2>\n");
		text.append("<hr>\n");
		text.append("</body>\n");
		text.append("</html>\n");

	}

}
