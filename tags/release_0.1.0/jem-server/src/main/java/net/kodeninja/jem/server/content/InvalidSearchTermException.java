package net.kodeninja.jem.server.content;

public class InvalidSearchTermException extends Exception {
	private static final long serialVersionUID = 3990295559280905913L;

	public InvalidSearchTermException() {
		super();
	}

	public InvalidSearchTermException(String ErrorText) {
		super(ErrorText);
	}
}
