package net.kodeninja.jem.server.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import net.kodeninja.jem.server.InterfaceHook;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.content.InvalidSearchTermException;
import net.kodeninja.jem.server.content.MediaItem;
import net.kodeninja.jem.server.content.RegExSearchRequest;
import net.kodeninja.jem.server.content.SearchRequest;
import net.kodeninja.jem.server.content.SimpleSearchRequest;
import net.kodeninja.scheduling.JobImpl;
import net.kodeninja.util.KNRunnableModule;
import net.kodeninja.util.logging.LoggerHook;

abstract public class ConsoleInterface extends JobImpl implements LoggerHook,
		InterfaceHook {
	protected Scanner input;
	protected PrintStream output;
	protected InputStream inStream;

	public ConsoleInterface(InputStream in, PrintStream out) {
		super(true, JemServer.getInstance().getScheduler());
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

				// Process the input
				// Help command
				if (line.equals("help")) {
					addLog(JemServer.getInstance().getName() + " "
							+ JemServer.getInstance().getVersionMajor() + "."
							+ JemServer.getInstance().getVersionMinor() + "."
							+ JemServer.getInstance().getVersionRevision()
							+ " - Command Help");
					addLog("  help			Shows this message");
					addLog("  status		Shows the current status of the server");
					addLog("  search		Searchs the entire media collection for the search term.");
					addLog("  search-r		Searchs the entire media collection for the regular expression search term.");
					addLog("  quit,exit		Exits the console");
					addLog("  shutdown		Halts the server");
				}
				// Status command
				else if (line.equals("status")) {
					Map<KNRunnableModule, Boolean> moduleStatus = JemServer
							.getInstance().Commands.getModuleStatus();
					Iterator<KNRunnableModule> it = moduleStatus.keySet()
							.iterator();
					KNRunnableModule tmpModule;

					String status = "Unknown";
					if (JemServer.getInstance().getStatus() == JemServer.Statuses.Starting)
						status = "Starting";
					else if (JemServer.getInstance().getStatus() == JemServer.Statuses.Running)
						status = "Running";
					addLog("Global Server Status: " + status);

					while (it.hasNext()) {
						tmpModule = it.next();
						addLog(tmpModule.getName()
								+ " Status: "
								+ ((moduleStatus.get(tmpModule) == true) ? "Running"
										: "Stopped"));
					}
				}
				// Search command
				else if (line.equals("search"))
					addLog("Please specify a search term.");
				else if ((line.startsWith("search "))
						|| (line.startsWith("search-r "))) {
					String searchTerm = line.substring(line.indexOf(" ") + 1);
					int resultCounter = 0;

					try {
						SearchRequest sr = null;

						if (line.startsWith("search "))
							sr = new SimpleSearchRequest(searchTerm);
						else if (line.startsWith("search-r "))
							sr = new RegExSearchRequest(searchTerm);

						Iterator<MediaItem> it = JemServer.getInstance().Commands
								.searchMedia(sr);
						addLog("Performing search for: " + searchTerm);

						while (it.hasNext()) {
							MediaItem tmpItem = it.next();
							addLog(" " + tmpItem.getMediaName());
							resultCounter++;
						}
						if (resultCounter == 0)
							addLog("No results found!");
						else
							addLog("Done - Results Returned: " + resultCounter);
					} catch (InvalidSearchTermException e) {
						addLog("Invalid search term: " + searchTerm);
					}
				}
				// Exit Command
				else if ((line.equals("exit") == true)
						|| (line.equals("quit") == true)) {
					stop();
					return;
				}
				// Shutdown command
				else if (line.equals("shutdown") == true) {
					JemServer.getInstance().addLog(
													"Shutdown requested by: "
															+ getUser());
					JemServer.getInstance().Commands.shutdown();
				}
				// Unknown command
				else if (line.equals("") == false)
					addLog("Unknown command: " + line);

			} catch (Exception e) {
				JemServer.getInstance().addLog(
												"Console exception caught: "
														+ e.toString());
				JemServer.getInstance().Commands.exception();
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
