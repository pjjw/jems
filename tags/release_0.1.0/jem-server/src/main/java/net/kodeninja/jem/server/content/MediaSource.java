package net.kodeninja.jem.server.content;

import net.kodeninja.util.KNXMLModule;

public interface MediaSource extends KNXMLModule {

	/**
	 * Returns a descriptive name of the collection.
	 *
	 * @return A descriptive name of the collection.
	 */
	public String getSourceName();

}
