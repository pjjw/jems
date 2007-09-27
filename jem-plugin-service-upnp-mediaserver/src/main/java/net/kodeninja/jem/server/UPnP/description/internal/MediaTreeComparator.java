package net.kodeninja.jem.server.UPnP.description.internal;

import java.util.Comparator;

public class MediaTreeComparator implements Comparator<MediaTree> {

	private String[] sorting;
	
	public MediaTreeComparator(String sortString) {
		sorting = sortString.split(",");
		if (sorting.length == 0) {
			sorting = new String[1];
			sorting[0] = sortString;
		}
	}
	
	public int compareOn(MediaTree mt1, MediaTree mt2, String field) {
		String s1 = "";
		String s2 = "";
		if (field.equals("dc:title")) {
			s1 = mt1.getName();
			s2 = mt2.getName();
		}
		else {
			for (MediaTreeAttribute attr: mt1.getAttributes()) {
				if (attr.getName().equals(field)) {
					s1 = attr.getValue().toString();
					break;
				}
			}
			
			for (MediaTreeAttribute attr: mt2.getAttributes()) {
				if (attr.getName().equals(field)) {
					s2 = attr.getValue().toString();
					break;
				}
			}
		}
		return s1.compareTo(s2);
	}
	
	public int compare(MediaTree mt1, MediaTree mt2) {
		for (String sort: sorting) {
			boolean asc = sort.charAt(0) == '+';
			int result = compareOn(mt1, mt2, sort.substring(1));
			if (asc == false)
				result *= -1;
			if (result != 0)
				return result;
		}
		return 0;
	}

}
