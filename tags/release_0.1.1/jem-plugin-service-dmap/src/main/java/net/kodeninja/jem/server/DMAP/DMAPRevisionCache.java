package net.kodeninja.jem.server.DMAP;

import java.util.LinkedHashMap;
import java.util.Map;

import net.kodeninja.jem.server.DMAP.content.DMAPMediaCollection;

public class DMAPRevisionCache extends
		LinkedHashMap<Integer, DMAPMediaCollection> {

	private static final long serialVersionUID = 5641648655125503107L;
	private static final int MAX_ENTRIES = 3;

	@Override
	protected boolean removeEldestEntry(
			Map.Entry<Integer, DMAPMediaCollection> eldest) {
		return size() > MAX_ENTRIES;
	}
}
