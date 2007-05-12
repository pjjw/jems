package net.kodeninja.http.packet;

public class InvalidHeaderException extends Exception {

	public InvalidHeaderException() {
		super();
	}

	public InvalidHeaderException(String s) {
		super(s);
	}

	private static final long serialVersionUID = 1L;
}
