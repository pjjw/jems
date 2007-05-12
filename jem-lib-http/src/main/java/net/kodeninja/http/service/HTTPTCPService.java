package net.kodeninja.http.service;

import net.kodeninja.scheduling.Scheduler;

public class HTTPTCPService extends HTTPService<HTTPTCPServerSocket> {
	public final static int DEFAULT_HTTP_PORT = 80;
	public final static int ALTERNATIVE_HTTP_PORT = 8080;

	public HTTPTCPService() {
		this(DEFAULT_HTTP_PORT);
	}

	public HTTPTCPService(int port) {
		super(new HTTPTCPServerSocket(), port);
	}

	public HTTPTCPService(int Port, int MaxConnections) {
		super(new HTTPTCPServerSocket(), Port, MaxConnections);
	}

	public HTTPTCPService(Scheduler owner, int Port) {
		super(owner, new HTTPTCPServerSocket(), Port);
	}
}
