package net.kodeninja.jem.server.UPnP.description.internal;

import java.util.Iterator;
import java.util.Set;

public interface MediaTree {

	public String getName();
	
	public String getId();

	public MediaTree getParent();
	
	public void setParent(MediaTree parent);
	
	public void setParent(MediaTree parent, boolean addAsChild);
	
	public void addChild(MediaTree child);
	
	public void addChild(MediaTree child, boolean setParent);
	
	public void removeChild(MediaTree child);
	
	public int getChildernCount();
	
	public Iterator<MediaTree> getChildern();
	
	public void addAttribute(MediaTreeAttribute tree);
	
	public Set<MediaTreeAttribute> getAttributes();
	
	public MediaTree getBranch(String id);
	
}
