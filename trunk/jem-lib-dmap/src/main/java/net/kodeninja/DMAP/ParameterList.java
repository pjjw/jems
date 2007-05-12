package net.kodeninja.DMAP;

import java.util.Iterator;

import net.kodeninja.DMAP.DataTypes.DMAPParameter;

/**
 * Generic interface to access a list of DMAP parameters.
 * @author Charles Ikeson
 *
 */
public interface ParameterList {
	/**
	 * Adds a parameter to the list.
	 * @param param The parameter to add.
	 */
	public void addParameter(DMAPParameter param);

	/**
	 * Removes a parameter from the list.
	 * @param param The parameter to remove.
	 */
	public void removeParameter(DMAPParameter param);

	/**
	 * Returns an iterator of the parameters.
	 * @return An iterator consisting of all the parameters in the list.
	 */
	public Iterator<DMAPParameter> getParameters();

	/**
	 * Searches for the specified tag, optionally recursively, in the list.
	 * @param tag The tag to search for.
	 * @param recurse If we encounter another list, descend into it?
	 * @return The parameter we found, or null if nothing was found.
	 */
	public DMAPParameter findParamByTag(String tag, boolean recurse);

	/**
	 * Empties the list.
	 */
	public void clear();
}
