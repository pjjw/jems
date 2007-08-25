package net.kodeninja.jem.server.DMAP.content;

import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

import net.kodeninja.jem.server.storage.MediaItem;

public class DMAPQueryResultCollection extends DMAPMediaCollection {

	public static final String DEFAULT_NAME = "DAAP Media Collection";
	protected Set<MediaItem> mediaSet = new HashSet<MediaItem>();
	protected String collectionQuery;

	public DMAPQueryResultCollection(DMAPMediaCollection base, String query) {
		super(base.getName(), (DMAPMediaCollection) null, base.getType());
		collectionQuery = query.substring(1, query.length() - 1);
	}

	// 'dmap.itemid:1100420977','dmap.itemid:1101344498'
	@Override
	public boolean acceptMedia(MediaItem media) {
		if (super.acceptMedia(media)) {
			Scanner s = (new Scanner(collectionQuery)).useDelimiter("\\s*,\\s*");
			String itemId = "dmap.itemid:" + media.hashCode();
			while (s.hasNext()) {
				String token = s.next();
				token = token.toLowerCase().substring(1, token.length() - 1);
				if (token.equals(itemId))
					return true;
			}
		}

		return false;
	}

}
