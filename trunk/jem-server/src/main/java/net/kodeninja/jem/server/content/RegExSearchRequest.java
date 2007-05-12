package net.kodeninja.jem.server.content;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegExSearchRequest extends SearchRequest {
	protected Pattern searchPattern;

	public RegExSearchRequest(String terms) throws InvalidSearchTermException {
		super(terms);
	}

	@Override
	public boolean matches(String s) {
		if (searchPattern == null)
			return false;
		return searchPattern.matcher(s).matches();
	}

	@Override
	protected boolean createdPattern(String pattern) {
		try {
			searchPattern = Pattern.compile(pattern.trim(),
											Pattern.CASE_INSENSITIVE
													| Pattern.CANON_EQ);
		} catch (PatternSyntaxException e) {
			return false;
		}
		return true;
	}
}
