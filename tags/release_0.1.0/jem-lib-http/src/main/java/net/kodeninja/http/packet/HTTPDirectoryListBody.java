package net.kodeninja.http.packet;

import java.io.File;
import java.io.FilenameFilter;

import net.kodeninja.util.MimeType;

public class HTTPDirectoryListBody extends HTTPTextBody {
	public HTTPDirectoryListBody(File directory) {
		super("", new MimeType("text", "html"));
		generateText(directory, null);
	}

	protected void generateText(File directory, FilenameFilter filter) {
		text = "<html>\n" + "<head><title>Directory Index</title></head>\n"
				+ "<body>\n";

		File[] listing = directory.listFiles();
		for (File relPath : listing) {
			if (relPath.isDirectory())
				text += "<a href=\"" + relPath.getName() + "/\">"
						+ relPath.getName() + "/</a><br>\n";
			else
				text += "<a href=\"" + relPath.getName() + "\">"
						+ relPath.getName() + "</a><br>\n";
		}

		text += "</body>\n" + "</html>\n";
	}
}
