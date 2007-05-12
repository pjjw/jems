package net.kodeninja.jem.server.content.transcoding;

import net.kodeninja.util.MimeType;

public class FFmpegMethod {
	private MimeType from, to;
	private String cmd;

	public FFmpegMethod(MimeType from, MimeType to, String cmd) {
		this.from = from;
		this.to = to;
		this.cmd = cmd;
	}

	public String getCommand() {
		return cmd;
	}

	public boolean matchs(MimeType f, MimeType t) {
		return f.matches(from) && t.matches(to);
	}
}
