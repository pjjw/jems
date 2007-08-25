package net.kodeninja.http.service.handlers;

import java.io.File;
import java.io.FilenameFilter;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPDirectoryListBody;
import net.kodeninja.http.packet.HTTPFileBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.service.HTTPSocket;
import net.kodeninja.util.MimeTypeFactory;

public class DirectoryURIHandler implements URIHandler {
	protected File baseDirectory;
	protected String baseURI = "/";
	protected FilenameFilter filter = null;
	protected MimeTypeFactory mtFactory;

	public DirectoryURIHandler(File BaseDirectory, MimeTypeFactory TypeFactory) {
		baseDirectory = BaseDirectory;
		mtFactory = TypeFactory;
	}

	public DirectoryURIHandler(File BaseDirectory, MimeTypeFactory TypeFactory,
			String BaseURI) {
		baseDirectory = BaseDirectory;
		baseURI = BaseURI;
		mtFactory = TypeFactory;
	}

	public DirectoryURIHandler(File BaseDirectory, MimeTypeFactory TypeFactory,
			String BaseURI, FilenameFilter Filter) {
		baseDirectory = BaseDirectory;
		baseURI = BaseURI;
		filter = Filter;
		mtFactory = TypeFactory;
	}

	public  HTTPPacket<? extends HTTPBody> process(HTTPSocket Socket,
			HTTPPacket<? extends HTTPBody> Packet) {

		String fragment = Packet.getHeader().getLocation().getPath();
		if (fragment.startsWith(baseURI) == false)
			return null;

		File request = new File(baseDirectory.getAbsolutePath() + fragment);
		if (request.exists() == false)
			return null;

		HTTPBody body = null;

		if (request.isDirectory()) {
			File indexPage;
			indexPage = new File(request.getPath() + File.separatorChar
					+ "index.htm");
			if (indexPage.exists())
				request = indexPage;
			else {
				indexPage = new File(request.getPath() + File.separatorChar
						+ "index.html");
				if (indexPage.exists())
					request = indexPage;
			}
		}

		if (request.isDirectory())
			body = new HTTPDirectoryListBody(request);
		else if (request.isFile())
			body = new HTTPFileBody(request, mtFactory);

		return new HTTPPacket<HTTPBody>(new HTTPHeader(Packet
				.getHeader().getVersion(), HTTPResponseCode.HTTP_200_OK), body);
	}

}
