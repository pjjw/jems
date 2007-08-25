package net.kodeninja.http.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.kodeninja.io.StreamLineReader;

public class HTTPHeader {
	public enum HeaderType {
		UNDETERMINED, REQUEST, RESPONSE
	};

	protected Map<String, String> params = new LinkedHashMap<String, String>();
	protected String firstLine = "";

	protected HTTPVersion version = null;
	protected String method = null;
	protected URI location = null;
	protected HTTPResponseCode response = null;

	protected HeaderType type = HeaderType.UNDETERMINED;

	public HTTPHeader() {
	}

	public HTTPHeader(String Method, URI Location, HTTPVersion Version) {
		firstLine = Method + " " + Location.getPath().toString() + " " + Version;
		version = Version;
		method = Method;
		location = Location;
		type = HeaderType.REQUEST;
	}

	public HTTPHeader(HTTPVersion Version, HTTPResponseCode Response) {
		firstLine = Version + " " + Response.toString();
		version = Version;
		response = Response;
		type = HeaderType.RESPONSE;
	}

	public String getParameter(String ParamName) {
		for (String name: params.keySet())
			if (name.equalsIgnoreCase(ParamName))
				return params.get(name);
		
		return null;
	}

	public void setParameter(String ParamName, String ParamValue) {
		params.put(ParamName.trim(), ParamValue);
	}
	
	public void removeParameter(String ParamName) {
		for (String name: params.keySet())
			if (name.equalsIgnoreCase(ParamName))
				params.remove(name);
	}

	protected String getParameters() {
		Iterator<String> it = params.keySet().iterator();
		String retVal = "";
		while (it.hasNext()) {
			String paramName = it.next();
			retVal += paramName + ": " + params.get(paramName) + "\r\n";
		}

		return retVal;
	}

	@Override
	public String toString() {
		return firstLine + "\r\n" + getParameters() + "\r\n";
	}

	public void writeToStream(OutputStream out) throws IOException {
		PrintStream ps = new PrintStream(out);
		ps.print(toString());
	}

	public void readFromStream(InputStream in) throws InvalidHeaderException,
			IOException {
		type = HeaderType.UNDETERMINED;
		try {
			StreamLineReader br = new StreamLineReader(in);
			firstLine = br.readLine().trim();
			if (firstLine == "")
				firstLine = br.readLine().trim();
			int seperator1 = firstLine.indexOf(" ");
			int seperator2 = firstLine.lastIndexOf(" ");
			if ((seperator1 < 0) || (seperator2 < 0))
				throw new InvalidHeaderException("Empty or bad request line.");

			String firstToken = firstLine.substring(0, seperator1);

			try {
				version = new HTTPVersion(firstToken);
				try {
					response = new HTTPResponseCode(firstLine.substring(seperator1 + 1));
				} catch (InvalidResponseCodeException e) {
					throw new InvalidHeaderException("Empty or bad response line.");
				}
				type = HeaderType.RESPONSE;
			} catch (InvalidHTTPVersionException e1) {
				if (seperator1 == seperator2)
					throw new InvalidHeaderException("Empty or bad request line.");

				method = firstToken;
				StringBuffer tmpLoc = new StringBuffer(firstLine.substring(seperator1 + 1, seperator2).trim());
				// Trim all leading / except 1
				while ((tmpLoc.length() > 1) && (tmpLoc.charAt(0) == '/') && (tmpLoc.charAt(1) == '/'))
					tmpLoc.deleteCharAt(0);
				location = new URI(tmpLoc.toString());
				try {
					version = new HTTPVersion(firstLine.substring(seperator2 + 1));
				} catch (InvalidHTTPVersionException e2) {
					throw new InvalidHeaderException("Empty or bad request line.");
				}
				type = HeaderType.REQUEST;
			}

			readParamsFromStream(in);
		} catch (URISyntaxException e) {
			throw new InvalidHeaderException(e.getReason());
		}

	}

	protected void readParamsFromStream(InputStream in) throws IOException {
		StreamLineReader br = new StreamLineReader(in);
		while (true) {
			String line = br.readLine();
			if ((line == null) || (line == ""))
				break;
			int seperator = line.indexOf(":");
			setParameter(line.substring(0, seperator), line.substring(seperator + 1).trim());
		}
	}

	public String getFirstLine() {
		return firstLine;
	}
	
	public HeaderType getType() {
		return type;
	}

	public HTTPVersion getVersion() {
		return version;
	}

	public URI getLocation() {
		return location;
	}

	public String getMethod() {
		return method;
	}

	public HTTPResponseCode getResponse() {
		return response;
	}

	public void setResponseCode(HTTPVersion Version, HTTPResponseCode Response) {
		firstLine = Version + " " + Response.toString();
		version = Version;
		response = Response;
		type = HeaderType.RESPONSE;
	}
}
