package net.kodeninja.jem.server.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.kodeninja.util.MimeType;

public class MimeTypeTree<T> {
	protected Map<String, Map<String, Set<T>>> tree = new HashMap<String, Map<String, Set<T>>>();

	public synchronized void add(T item, MimeType type) {
		Map<String, Set<T>> firstLevel = tree.get(type.getPrimaryType());
		if (firstLevel == null) {
			firstLevel = new HashMap<String, Set<T>>();
			tree.put(type.getPrimaryType(), firstLevel);
		}

		Set<T> secondLevel = firstLevel.get(type.getSubType());
		if (secondLevel == null) {
			secondLevel = new HashSet<T>();
			firstLevel.put(type.getSubType(), secondLevel);
		}

		secondLevel.add(item);
	}

	public synchronized boolean remove(T item) {
		for (String primaryType: tree.keySet()) {
			Map<String, Set<T>> firstLevel = tree.get(primaryType);
			for (String subType: firstLevel.keySet()) {
				Set<T> itemList = firstLevel.get(subType);	 
				if (itemList.remove(item))
					return true;
			}
		}
		return false;
	}

	public synchronized MimeType getMimeType(T item) {
		for (String primaryType: tree.keySet()) {
			Map<String, Set<T>> firstLevel = tree.get(primaryType);
			for (String subType: firstLevel.keySet()) {
				Set<T> itemList = firstLevel.get(subType);	 
				if (itemList.contains(item))
					return new MimeType(primaryType, subType);
			}
		}
		return null;
	}

	public synchronized Set<T> getMatching(MimeType type) {
		Set<T> result = new HashSet<T>();

		for (String primary: tree.keySet())
			if (type.getPrimaryType().equals("*") || (type.getPrimaryType().equalsIgnoreCase(primary))) {
				Map<String, Set<T>> secondaryMap =  tree.get(primary);
				for (String secondary: secondaryMap.keySet())
					if (type.getSubType().equals("*") || (type.getSubType().equalsIgnoreCase(secondary)))
						result.addAll(secondaryMap.get(secondary));
			}

		return result;
	}
}
