package net.kodeninja.jem.server.userinterface;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Group implements UIBase, Iterable<Section> {

	private String title = null;
	private String desc = null;
	private Set<Section> sections = new LinkedHashSet<Section>();
	
	public Group(String title, String desc) {
		this.title = title;
		this.desc = desc;
	}
	
	public void addSection(Section section) {
		sections.add(section);
	}
	
	public void removeSection(Section section) {
		sections.remove(section);
	}
	
	public int sectionCount() {
		return sections.size();
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return desc;
	}
	
	public Iterator<Section> iterator() {
		return sections.iterator();
	}
	
}
