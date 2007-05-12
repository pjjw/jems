package net.kodeninja.util;

public class MalformedMimeTypeException extends Exception {
	private static final long serialVersionUID = -650444159149855098L;

	public MalformedMimeTypeException() {
		super();
	}

	public MalformedMimeTypeException(String ErrorText) {
		super(ErrorText);
	}
}
