package net.kodeninja.UPnP.identifiers;

public class SSDPMalformedIdentifierException extends Exception {
	private static final long serialVersionUID = -8375185237971310103L;

	public SSDPMalformedIdentifierException() {
		super();
	}
	
	public SSDPMalformedIdentifierException(String ErrorText) {
		super(ErrorText);
	}
	
}
