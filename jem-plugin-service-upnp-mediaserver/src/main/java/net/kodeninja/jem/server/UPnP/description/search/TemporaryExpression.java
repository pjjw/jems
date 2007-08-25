package net.kodeninja.jem.server.UPnP.description.search;

import net.kodeninja.jem.server.UPnP.description.internal.MediaTree;

public class TemporaryExpression implements Terminal {

	private String v;
	
	public TemporaryExpression(String val) {
		v = val;
	}
	
	public String getValue() {
		return v;
	}

	public boolean evaluate(MediaTree mt) {
		return false;
	}
	
	public String toString() {
		return "[" + v + "]";
	}
	
}
