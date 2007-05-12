package net.kodeninja.http.packet;

public final class HTTPResponseCode {
	/*
	 * For descriptions of each, please see
	 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
	 */
	public final static HTTPResponseCode HTTP_100_CONTINUE = new HTTPResponseCode(
			100, "Continue");
	public final static HTTPResponseCode HTTP_101_SWITCHING_PROTOCOLS = new HTTPResponseCode(
			101, "Switching Protocols");
	public final static HTTPResponseCode HTTP_200_OK = new HTTPResponseCode(
			200, "OK");
	public final static HTTPResponseCode HTTP_201_CREATED = new HTTPResponseCode(
			201, "Created");
	public final static HTTPResponseCode HTTP_202_ACCEPTED = new HTTPResponseCode(
			202, "Accepted");
	public final static HTTPResponseCode HTTP_203_NONAUTHORITATIVE_INFORMATION = new HTTPResponseCode(
			203, "Non-Authoritative Information");
	public final static HTTPResponseCode HTTP_204_NO_CONTENT = new HTTPResponseCode(
			204, "No Content");
	public final static HTTPResponseCode HTTP_205_RESET_CONTENT = new HTTPResponseCode(
			205, "Reset Content");
	public final static HTTPResponseCode HTTP_206_PARTIAL_CONTENT = new HTTPResponseCode(
			206, "Partial Content");
	public final static HTTPResponseCode HTTP_300_MULTIPLE_CHOICES = new HTTPResponseCode(
			300, "Multiple Choices");
	public final static HTTPResponseCode HTTP_301_MOVED_PERMANENTLY = new HTTPResponseCode(
			301, "Moved Permanently");
	public final static HTTPResponseCode HTTP_302_FOUND = new HTTPResponseCode(
			302, "Found");
	public final static HTTPResponseCode HTTP_303_SEE_OTHER = new HTTPResponseCode(
			303, "See Other");
	public final static HTTPResponseCode HTTP_304_NOT_MODIFIED = new HTTPResponseCode(
			304, "Not Modified");
	public final static HTTPResponseCode HTTP_305_USE_PROXY = new HTTPResponseCode(
			305, "Use Proxy");
	public final static HTTPResponseCode HTTP_307_TEMPORARY_REDIRECT = new HTTPResponseCode(
			307, "Temporary Redirect");
	public final static HTTPResponseCode HTTP_400_BAD_REQUEST = new HTTPResponseCode(
			400, "Bad Request");
	public final static HTTPResponseCode HTTP_401_UNAUTHORIZED = new HTTPResponseCode(
			401, "Unauthorized");
	public final static HTTPResponseCode HTTP_403_FORBIDDEN = new HTTPResponseCode(
			403, "Forbidden");
	public final static HTTPResponseCode HTTP_404_NOT_FOUND = new HTTPResponseCode(
			404, "Not Found");
	public final static HTTPResponseCode HTTP_405_METHOD_NOT_ALLOWED = new HTTPResponseCode(
			405, "Method Not Allowed");
	public final static HTTPResponseCode HTTP_406_NOT_ACCEPTABLE = new HTTPResponseCode(
			406, "Not Acceptable");
	public final static HTTPResponseCode HTTP_407_PROXY_AUTHENTICATION_REQUIRED = new HTTPResponseCode(
			407, "Proxy Authentication Required");
	public final static HTTPResponseCode HTTP_408_REQUEST_TIMEOUT = new HTTPResponseCode(
			408, "Request Timeout");
	public final static HTTPResponseCode HTTP_409_CONFLICT = new HTTPResponseCode(
			409, "Conflict");
	public final static HTTPResponseCode HTTP_410_GONE = new HTTPResponseCode(
			410, "Gone");
	public final static HTTPResponseCode HTTP_411_LENGTH_REQUIRED = new HTTPResponseCode(
			411, "Length Required");
	public final static HTTPResponseCode HTTP_412_PRECONDITON_FAILED = new HTTPResponseCode(
			412, "Precondition Failed");
	public final static HTTPResponseCode HTTP_413_REQUEST_ENTITY_TOO_LARGE = new HTTPResponseCode(
			413, "Request Entity Too Large");
	public final static HTTPResponseCode HTTP_414_REQUEST_URI_TOO_LONG = new HTTPResponseCode(
			414, "Request-URI Too Long");
	public final static HTTPResponseCode HTTP_415_UNSUPPORTED_MEDIA_TYPE = new HTTPResponseCode(
			415, "Unsupported Media Type");
	public final static HTTPResponseCode HTTP_416_REQUEST_RANGE_NOT_SATISFIABLE = new HTTPResponseCode(
			416, "Requested Range Not Satisfiable");
	public final static HTTPResponseCode HTTP_417_EXPECTATION_FAILED = new HTTPResponseCode(
			417, "Expectation Failed");
	public final static HTTPResponseCode HTTP_500_INTERNAL_SERVER_ERROR = new HTTPResponseCode(
			500, "Internal Server Error");
	public final static HTTPResponseCode HTTP_501_NOT_IMPLEMENTED = new HTTPResponseCode(
			501, "Not Implemented");
	public final static HTTPResponseCode HTTP_502_BAD_GATEWAY = new HTTPResponseCode(
			502, "Bad Gateway");
	public final static HTTPResponseCode HTTP_503_SERVICE_UNAVAILABLE = new HTTPResponseCode(
			503, "Service Unavailable");
	public final static HTTPResponseCode HTTP_504_GATEWAY_TIMEOUT = new HTTPResponseCode(
			504, "Gateway Timeout");
	public final static HTTPResponseCode HTTP_505_HTTP_VERSION_NOT_SUPPORTED = new HTTPResponseCode(
			505, "HTTP Version Not Supported");

	private int code;
	private String description;

	public HTTPResponseCode(int Code, String Description) {
		code = Code;
		description = Description;
	}

	public HTTPResponseCode(String ResponseLine)
			throws InvalidResponseCodeException {
		String line = ResponseLine;
		if (line.startsWith("HTTP"))
			line = line.substring(line.indexOf(" ") + 1);

		int seperator = line.indexOf(" ");
		try {
			code = Integer.parseInt(line.substring(0, seperator));
			if ((description = line.substring(seperator + 1)).length() == 0)
				throw new InvalidResponseCodeException(
						"No error reason provided.");
		} catch (NumberFormatException e) {
			throw new InvalidResponseCodeException("No error code provided.");
		}

	}

	@Override
	public String toString() {
		return code + " " + description;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o instanceof HTTPResponseCode)
			return ((HTTPResponseCode) o).code == code;
		return false;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
