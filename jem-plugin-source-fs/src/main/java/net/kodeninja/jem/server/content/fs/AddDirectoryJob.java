package net.kodeninja.jem.server.content.fs;

import java.io.File;
import java.net.URI;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.storage.MediaItem;
import net.kodeninja.jem.server.storage.MetadataType;
import net.kodeninja.scheduling.JobImpl;
import net.kodeninja.util.MimeType;

public class AddDirectoryJob extends JobImpl {
	private static final int MAX_RUNNING = 1;
	private static int numWaiting = 0;
	private static int numRunning = 0;
	private File path;
	private DirectoryMediaSource owner;
	private boolean ignoreUnknown;

	private static synchronized void addWaiting() {
		numWaiting++;
	}

	private static synchronized void addRunning() {
		numRunning++;
	}

	private static synchronized void subRunning() {
		numRunning--;
		numWaiting--;
	}

	private static synchronized int getRunning() {
		return numRunning;
	}

	public AddDirectoryJob(DirectoryMediaSource owner, File path, boolean ignoreUnknown) {
		super(false, JemServer.getScheduler());
		this.path = path;
		this.owner = owner;
		this.ignoreUnknown = ignoreUnknown;
		this.owner.jobs.put(this, DEFAULT_RUN_ID);
		addWaiting();
	}

	@Override
	public void run() {
		if (path.isDirectory() == false) {
			super.run();
			return;
		}

		addRunning();
		for (File f : path.listFiles())
			if (f.getName().startsWith(".") == false) {
				if (f.isFile()) {
					MimeType fileType = owner.getMimeTypeFactory(f);
					if ((ignoreUnknown == false) || (fileType.equals(MimeType.WILDCARD) == false)) {
						MediaItem mi;

						URI uri = f.toURI();
						if ((JemServer.getMediaStorage().mediaExists(uri) == false) && 
								((mi = JemServer.getMediaStorage().addNewMedia(uri, fileType)) != null)) {
							JemServer.getMediaStorage().addMediaMetadata(mi, MetadataType.FileSize, "" + f.length());

							owner.incMediaCount();
							try {
								owner.getMetadataFactory(fileType).addMetadata(mi);
							} catch (Throwable t) {
								System.err.println(f);
								t.printStackTrace();
							}
						}
					}
				} else if (f.isDirectory()) {
					// Ignore the same path as we are currently searching
					if (path.getAbsolutePath().equals(f.getAbsolutePath()))
						continue;
					// Ignore a path leading to a super directory
					if (path.getAbsolutePath().startsWith(f.getAbsolutePath()))
						continue;
					(new AddDirectoryJob(owner, f, ignoreUnknown)).start();
				}
			}
		subRunning();
		super.run();
	}

	public boolean canRun() {
		return (getRunning() < MAX_RUNNING);
	}

	public boolean isUrgent() {
		return false;
	}

}
