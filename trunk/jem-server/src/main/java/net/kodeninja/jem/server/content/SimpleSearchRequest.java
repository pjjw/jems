package net.kodeninja.jem.server.content;

public class SimpleSearchRequest extends SearchRequest {
	protected String Pattern;

	public SimpleSearchRequest(String terms) throws InvalidSearchTermException {
		super(terms);
	}

	@Override
	protected boolean createdPattern(String pattern) {
		Pattern = pattern.toLowerCase();
		return true;
	}

	@Override
	public boolean matches(String s) {
		if (Pattern == null)
			return false;
		return s.toLowerCase().contains(Pattern);
	}

}
