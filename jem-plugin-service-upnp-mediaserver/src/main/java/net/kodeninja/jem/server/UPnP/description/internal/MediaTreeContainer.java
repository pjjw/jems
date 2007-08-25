package net.kodeninja.jem.server.UPnP.description.internal;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class MediaTreeContainer implements MediaTree {
	private Set<MediaTree> childern = new LinkedHashSet<MediaTree>();
	private Set<MediaTreeAttribute> attributes = new LinkedHashSet<MediaTreeAttribute>();
	private MediaTree parent = null;
	private String id;
	private String name;
	
	public MediaTreeContainer(String id, String name, String itemClass) {
		this.id = id;
		this.name = name;
		//FIXME This needs fixing...
		itemClass = "object.container";
		
		attributes.add(new MediaTreeAttribute("upnp:class", itemClass));
	}
	
	public MediaTreeContainer(String id, String name, String itemClass, MediaTree parent) {
		this(id, name, itemClass);
		this.parent = parent;
		parent.addChild(this, false);
	}
	
	public void addChild(MediaTree child) {
		addChild(child, true);
	}
	
	public void addChild(MediaTree child, boolean setParent) {
		childern.add(child);
		if (setParent)
			child.setParent(this, false);
	}
	
	public void removeChild(MediaTree child) {
		childern.remove(child);
	}
	
	public void setParent(MediaTree parent) {
		setParent(parent, true);
	}
	
	public void setParent(MediaTree parent, boolean addAsChild) {
		this.parent = parent;
		if (addAsChild)
			parent.addChild(this, false);
	}

	public MediaTree getParent() {
		return parent;
	}

	public int getChildernCount() {
		return childern.size();
	}
	
	public Iterator<MediaTree> getChildern() {
		return childern.iterator();
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o instanceof MediaTree)
			return ((MediaTree)o).getId().equals(getId());
		return false;
	}
	
	public int hashCode() {
		return getId().hashCode();
	}
	
	public MediaTree getBranch(String id) {
		if (id.equals(getId()))
			return this;
		
		MediaTree result;
		for (MediaTree child: childern)
			if ((result = child.getBranch(id)) != null)
				return result;
		
		return null;
	}

	public void addAttribute(MediaTreeAttribute tree) {
		attributes.add(tree);
	}

	public Set<MediaTreeAttribute> getAttributes() {
		return attributes;
	}
	
}
