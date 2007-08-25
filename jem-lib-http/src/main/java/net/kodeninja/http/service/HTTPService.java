package net.kodeninja.http.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.kodeninja.scheduling.FIFOScheduler;
import net.kodeninja.scheduling.JobImpl;
import net.kodeninja.scheduling.Scheduler;
import net.kodeninja.util.KNModule;
import net.kodeninja.util.logging.LoggerHook;

public class HTTPService<T extends HTTPServerSocket> extends JobImpl implements
		KNModule, LoggerHook {
	private final static int MAX_CONNECTIONS = 10;
	private T transport;
	private int port;
	private Set<HTTPChildService> requests = new HashSet<HTTPChildService>();
	private int requestCounter = 0;
	private Scheduler ownerScheduler;

	public HTTPService(T HostSocket, int Port) {
		this(HostSocket, Port, MAX_CONNECTIONS);
	}

	public HTTPService(T HostSocket, int Port, int MaxConnections) {
		this(new FIFOScheduler(MaxConnections), HostSocket, Port);
		ownerScheduler.start(false);
	}

	public HTTPService(Scheduler owner, T HostSocket, int Port) {
		super(true, owner);
		ownerScheduler = owner;
		transport = HostSocket;
		port = Port;
	}

	public Scheduler getScheduler() {
		return ownerScheduler;
	}

	public String getName() {
		return "HTTP Service";
	}

	public int getVersionMajor() {
		return 0;
	}

	public int getVersionMinor() {
		return 1;
	}

	public int getVersionRevision() {
		return 1;
	}

	public String getURLBase() {
		try {
			return "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + getBoundPort();
		}
		catch (UnknownHostException e) {
			return "http://" + transport.getLocalHost() + ":" + getBoundPort();
		}
	}
	
	@Override
	public void run() {
		Iterator<HTTPChildService> it = requests.iterator();
		while (it.hasNext())
			if (it.next().isStarted() == false)
				it.remove();

		HTTPChildService request = transport.checkRequests(this);
		if (request != null) {
			request.start();
			requests.add(request);
			requestCounter++;
		}

		super.run();
	}

	@Override
	public void start() {
		transport.openSocket(port);
		super.start();
	}

	@Override
	public void stop() {
		Iterator<HTTPChildService> childIt = requests.iterator();
		while (childIt.hasNext())
			childIt.next().stop();

		transport.closeSocket();
		super.stop();
	}

	public T getTransport() {
		return transport;
	}

	public int getPort() {
		return port;
	}
	
	public int getBoundPort() {
		int result = transport.getPort();
		if (result < 1)
			result = getPort();
		return result;
	}

	protected void setPort(int newPort) {
		port = newPort;
	}

	/* Copied from LoggerCollection */
	protected Set<LoggerHook> Loggers = Collections
			.synchronizedSet(new HashSet<LoggerHook>());

	public void addLog(String LogText) {
		Iterator<LoggerHook> it = Loggers.iterator();
		while (it.hasNext())
			it.next().addLog(LogText);
	}

	public void addLogger(LoggerHook logger) {
		Loggers.add(logger);
	}

	public void removeLogger(LoggerHook logger) {
		Loggers.remove(logger);
	}

	/* Job interface stuff */
	public boolean canRun() {
		return true;
	}

	public boolean isUrgent() {
		return false;
	}

}
