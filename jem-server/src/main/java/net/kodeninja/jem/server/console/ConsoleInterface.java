package net.kodeninja.jem.server.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import net.kodeninja.jem.server.InterfaceHook;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.userinterface.Command;
import net.kodeninja.jem.server.userinterface.Group;
import net.kodeninja.jem.server.userinterface.Option;
import net.kodeninja.jem.server.userinterface.Section;
import net.kodeninja.scheduling.JobImpl;
import net.kodeninja.util.logging.LoggerHook;

abstract public class ConsoleInterface extends JobImpl implements LoggerHook,
InterfaceHook {
	protected Scanner input;
	protected PrintStream output;
	protected InputStream inStream;
	protected Group group = null;
	protected Section section = null;

	public ConsoleInterface(InputStream in, PrintStream out) {
		super(true, JemServer.getScheduler());
		inStream = in;
		input = new Scanner(inStream);
		output = out;
	}

	public boolean canRun() {
		return inputWaiting();
	}

	public boolean isUrgent() {
		return false;
	}

	@Override
	public void run() {
		if (inputWaiting())
			try {
				// Get the input
				String line = getInput();

				if (line == null) {
					stop();
					return;
				}

				line = line.trim().toLowerCase();

				if (line.equals("") == false) { 
					if (line.equals("shutdown")) {
						JemServer.command().shutdown();
					}
					else if (line.equals("quit") || line.equals("exit")) {
						stop();
					}
					else if (group == null) {
						Set<Group> groups = JemServer.getInstance().getUIGroups();
						if (line.equals("list")) {
							addLog("Groups:");
							for (Group g: groups)
								if (g.sectionCount() > 0)
									addLog("  " + g.getTitle() + " - " + g.getDescription());
						}
						else {
							for (Group g: groups)
								if ((g.sectionCount() > 0) && (g.getTitle().equalsIgnoreCase(line))) {
									group = g;
									break;
								}
							if (group == null)
								addLog("Unknown group: " + line);
							else {
								addLog("Now viewing group: " + group.getTitle());
								if (group.sectionCount() == 1)
									section = group.iterator().next();
							}
						}
					}
					else if (section == null) {
						if (line.equals("list")) {
							addLog("Sections:");
							for (Section s: group)
								addLog("  " + s.getTitle() + " - " + s.getDescription());
						}
						else if (line.equals("back")) {
							group = null;
							addLog("Now viewing: Root Menu");
						}
						else {
							for (Section s: group)
								if (s.getTitle().equalsIgnoreCase(line)) {
									section = s;
									addLog("Now viewing section: " + section.getTitle());
									break;
								}
							if (section == null)
								addLog("Unknown section: " + line);
						}
					}
					else {
						if (line.equals("list")) {
							addLog("Commands:");
							Iterator<Command> cit = section.getCommands();
							while (cit.hasNext()) {
								Command c = cit.next();
								addLog("  " + c.getTitle() + " - " + c.getDescription());
							}
							addLog("");
							addLog("Options: (Change with set)");
							Iterator<Option> oit = section.getOptions();
							while (oit.hasNext()) {
								Option o = oit.next();
								addLog("  " + o.getTitle() + " - " + o.getDescription());
							}
							addLog("");
						}
						else if (line.equals("back")) {
							section = null;
							if (group.sectionCount() == 1) {
								group = null;
								addLog("Now viewing: Root Menu");
							}
							else
								addLog("Now viewing group: " + group.getTitle());
						}
						else if (line.startsWith("set")) {
							int pos1 = line.indexOf(' ');
							int pos2 = line.indexOf(pos1 + 1, ' ');
							if (pos1 > -1) {
								String opStr = line.substring(pos1 + 1, (pos2 == -1 ? line.length() : pos2) - pos1);
								String value = "";
								if (pos2 > -1)
									value = line.substring(pos2 + 1);

								boolean found = false;
								Iterator<Option> oit = section.getOptions();
								while (oit.hasNext()) {
									Option o = oit.next();
									if (opStr.equals(o.getTitle())) {
										addLog("Changed " + opStr + " from " + o.getValue() + " to " + value);
										found = true;
										break;
									}
								}

								if (found == false)
									addLog("Unknown option: " + opStr);
							}
							else {
								addLog("Usage: set OPTION [VALUE]");
							}
						}
						else {
							boolean found = false;
							Iterator<Command> cit = section.getCommands();
							while (cit.hasNext()) {
								Command c = cit.next();
								if (line.startsWith(c.getTitle().toLowerCase())) {
									String[] args = null;
									if (line.length() > c.getTitle().length())
										args = line.substring(c.getTitle().length() + 1).split(" ");
									else
										args = new String[0];
									c.activate(this, args);
									found = true;
									break;
								}
							}

							if (found == false) {
								Iterator<Option> oit = section.getOptions();
								while (oit.hasNext()) {
									Option o = oit.next();
									if (line.equals(o.getTitle())) {
										addLog(o.getTitle() + " = " + o.getValue());
										found = true;
										break;
									}
								}	
							}

							if (found == false)
								addLog("Unknown command or option: " + line);
						}
					}
				}
			} catch (Exception e) {
				JemServer.getInstance().addLog("Console exception caught: " + e.toString());
				JemServer.command().exception();
			}
			super.run();
	}

	@Override
	public void start() {
		if (isStarted() == false)
			JemServer.getInstance().addLog(getUser() + " logged in.");
		super.start();
	}

	@Override
	public void stop() {
		if (isStarted())
			JemServer.getInstance().addLog(getUser() + " logged out.");
		super.stop();
	}

	/***************************************************************************
	 * This function is used to see if there will be any more input.
	 *
	 * @return Returns true, if the session is still open.
	 */
	protected boolean inputWaiting() {
		try {
			int avail = inStream.available();
			return (avail > 0);
		} catch (IOException e) {
			return false;
		}
	}

	/***************************************************************************
	 * This function is used to actually get the next command from the user.
	 *
	 * @return Returns the next command requested by the user.
	 */
	protected String getInput() {
		return input.nextLine();
	}

	public void addLog(String LogText) {
		output.println(LogText);
	}

	abstract public String getUser();
}
