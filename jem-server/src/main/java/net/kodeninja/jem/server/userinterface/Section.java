package net.kodeninja.jem.server.userinterface;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Section implements UIBase {

	private String title = null;
	private String desc = null;
	private Set<Option> options = new LinkedHashSet<Option>();
	private Set<Command> commands = new LinkedHashSet<Command>();
	
	public Section(String title, String desc) {
		this.title = title;
		this.desc = desc;
	}
	
	public void addOption(Option op) {
		options.add(op);
	}
	
	public void removeOption(Option op) {
		options.remove(op);
	}

	public Iterator<Option> getOptions() {
		return options.iterator();
	}
	
	public void addCommand(Command cmd) {
		commands.add(cmd);
	}
	
	public void removeCommand(Command cmd) {
		commands.remove(cmd);
	}

	public Iterator<Command> getCommands() {
		return commands.iterator();
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return desc;
	}
	
}
