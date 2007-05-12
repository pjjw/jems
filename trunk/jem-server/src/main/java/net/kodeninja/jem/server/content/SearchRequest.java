package net.kodeninja.jem.server.content;

import java.util.HashMap;
import java.util.Map;

abstract public class SearchRequest {
	protected Map<String, String> params = new HashMap<String, String>();

	public SearchRequest(String terms) throws InvalidSearchTermException {
		int lastBracket = -1;
		int paramSpace = -1;
		String searchTerm = "";

		for (int i = 0; i < terms.length(); i++)
			if (terms.charAt(i) == '[') {
				if (lastBracket != -1)
					throw new InvalidSearchTermException();
				else
					lastBracket = i;
			} else if (terms.charAt(i) == ']') {
				if (lastBracket == -1)
					throw new InvalidSearchTermException();
				else {
					String paramName = "";
					String paramArg = "";
					if (paramSpace != -1) {
						paramName = terms
								.substring(lastBracket + 1, paramSpace);
						paramArg = terms.substring(paramSpace + 1, i);
					} else
						paramName = terms.substring(lastBracket + 1, i);

					params.put(paramName.trim().toLowerCase(), paramArg);

					lastBracket = -1;
					paramSpace = -1;
				}
			} else if ((lastBracket != -1) && (terms.charAt(i) == ' ')
					&& (paramSpace == -1))
				paramSpace = i;
			else if (lastBracket == -1)
				searchTerm += terms.charAt(i);
		if (createdPattern(searchTerm) == false)
			throw new InvalidSearchTermException();
	}

	public String getParam(String paramName) {
		return params.get(paramName.trim().toLowerCase());
	}

	public int paramCount() {
		return params.size();
	}

	abstract public boolean matches(String s);

	abstract protected boolean createdPattern(String pattern);
}
