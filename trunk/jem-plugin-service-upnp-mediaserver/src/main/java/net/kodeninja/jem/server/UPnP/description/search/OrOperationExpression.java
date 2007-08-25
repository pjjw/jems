package net.kodeninja.jem.server.UPnP.description.search;

import net.kodeninja.jem.server.UPnP.description.internal.MediaTree;

public class OrOperationExpression implements Terminal {
	
	private Terminal t1;
	private Terminal t2;

	public OrOperationExpression(Terminal t1, Terminal t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	
	public boolean evaluate(MediaTree mt) {
		return t1.evaluate(mt) || t2.evaluate(mt);
	}
	
	public String toString() {
		return "(" + t1 + " OR " + t2 + ")";
	}

}
