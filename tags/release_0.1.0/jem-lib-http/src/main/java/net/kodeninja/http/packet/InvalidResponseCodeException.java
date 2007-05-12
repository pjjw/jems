package net.kodeninja.http.packet;

public class InvalidResponseCodeException extends Exception {

	private static final long serialVersionUID = -8322058461099829707L;

	public InvalidResponseCodeException() {
		super();
	}

	public InvalidResponseCodeException(String Reason) {
		super(Reason);
	}

}
