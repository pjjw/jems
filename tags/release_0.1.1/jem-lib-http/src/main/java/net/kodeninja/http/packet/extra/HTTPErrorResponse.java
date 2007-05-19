package net.kodeninja.http.packet.extra;

import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPVersion;

public class HTTPErrorResponse extends
		HTTPPacket<HTTPHeader, HTTPErrorTextBody> {

	public HTTPErrorResponse(HTTPResponseCode ErrorCode, HTTPVersion Version) {
		super(new HTTPHeader(Version, ErrorCode), new HTTPErrorTextBody(
				ErrorCode));
		getHeader().setParameter("Connection", "close");
	}

}
