package net.kodeninja.DMAP;

import java.util.Iterator;
import java.util.LinkedList;

import net.kodeninja.DMAP.DataTypes.DMAPParameter;

/**
 * Implements the parameter list interface with a linked list back end.
 * @author Charles Ikeson
 *
 */
public class ParameterLinkedList implements ParameterList {
	/**
	 * The parameter list back end.
	 */
	protected LinkedList<DMAPParameter> params = new LinkedList<DMAPParameter>();

	/**
	 * Adds a parameter to the list.
	 * @param param The parameter to add.
	 */
	public void addParameter(DMAPParameter param) {
		params.add(param);
	}

	/**
	 * Removes a parameter from the list.
	 * @param param The parameter to remove.
	 */
	public void removeParameter(DMAPParameter param) {
		params.remove(param);
	}

	/**
	 * Returns an iterator of the parameters.
	 * @return An iterator consisting of all the parameters in the list.
	 */
	public Iterator<DMAPParameter> getParameters() {
		return params.iterator();
	}

	/**
	 * Searches for the specified tag, optionally recursively, in the list.
	 * @param tag The tag to search for.
	 * @param recurse If we encounter another list, descend into it?
	 * @return The parameter we found, or null if nothing was found.
	 */
	public DMAPParameter findParamByTag(String tag, boolean recurse) {
		tag = tag.toLowerCase();
		for (DMAPParameter tmpParam : params)
			if (tmpParam.getTag().toLowerCase().equals(tag))
				return tmpParam;
			else if ((recurse == true) && (tmpParam instanceof ParameterList))
				if ((tmpParam = ((ParameterList) tmpParam)
						.findParamByTag(tag, true)) != null)
					return tmpParam;

		return null;
	}

	/**
	 * Empties the list.
	 */
	public void clear() {
		params.clear();
	}
}
