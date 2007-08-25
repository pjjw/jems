package net.kodeninja.util;

public class KNModuleInitException extends Exception {
	private static final long serialVersionUID = 8015414252916568611L;

	public KNModuleInitException() {
		super();
	}

	public KNModuleInitException(String ErrorText) {
		super(ErrorText);
	}
}
