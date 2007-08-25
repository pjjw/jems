package net.kodeninja.http.packet;

import java.io.File;
import java.io.FilenameFilter;

public class HTTPDirectoryListBody extends HTTPTextBody {
	public HTTPDirectoryListBody(File directory) {
		super(HTTPTextBody.HTML_MIMETYPE);
		generateText(directory, null);
	}

	protected void generateText(File directory, FilenameFilter filter) {
		text.delete(0, text.length());
		text.append("<html>\n<head><title>Directory Index</title></head>\n<body>\n");

		File[] listing = directory.listFiles();
		for (File relPath : listing) {
			if (relPath.isDirectory()) {
				text.append("<a href=\"");
				text.append(relPath.getName());
				text.append("/\">");
				text.append(relPath.getName());
				text.append("/</a><br>\n");
			}
			else {
				text.append("<a href=\"");
				text.append(relPath.getName());
				text.append("\">");
				text.append(relPath.getName());
				text.append("</a><br>\n");
			}
		}
		text.append("</body>\n</html>\n");
	}
}
