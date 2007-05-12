package net.kodeninja.http.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import net.kodeninja.http.service.handlers.PacketHandler;
import net.kodeninja.util.KNModule;

public class HTTPTCPServerSocket implements HTTPServerSocket, KNModule {
	protected ServerSocket socket = null;
	protected int requestTimeout = 100;
	protected int childTimeout = 10000;
	protected Set<PacketHandler> handlers = Collections
			.synchronizedSet(new LinkedHashSet<PacketHandler>());

	public synchronized HTTPChildService checkRequests(
			HTTPService<? extends HTTPServerSocket> owner) {
		if (socket == null)
			return null;

		Socket child;

		try {
			child = socket.accept();
			if (child != null) {
				child.setSoTimeout(childTimeout);
				return new HTTPChildService(owner.getScheduler(), this,
						new HTTPTCPSocket(child));
			}
		} catch (SocketTimeoutException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	public synchronized void closeSocket() {
		if (socket == null)
			return;

		ServerSocket tmpSocket = socket;
		socket = null;
		try {
			tmpSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean openSocket(int Port) {
		if (socket != null)
			return false;

		try {
			if (Port == -1)
				socket = new ServerSocket();
			else
				socket = new ServerSocket(Port);
			socket.setSoTimeout(requestTimeout);
		} catch (SocketException e) {
			e.printStackTrace();
			socket = null;
			return false;
		} catch (IOException e) {
			socket = null;
			return false;
		}

		return true;
	}

	public boolean isOpen() {
		return socket.isBound() && !socket.isClosed();
	}

	public String getName() {
		return "HTTP(TCP) Service";
	}

	public int getVersionMajor() {
		return 0;
	}

	public int getVersionMinor() {
		return 1;
	}

	public int getVersionRevision() {
		return 0;
	}

	public void addHandler(PacketHandler Handler) {
		handlers.add(Handler);
	}

	public void removeHandler(PacketHandler Handler) {
		handlers.remove(Handler);
	}

	public Iterator<PacketHandler> getHandlers() {
		return handlers.iterator();
	}

	public void setTimeout(int msec) {
		childTimeout = msec;
	}

	public int getTimeout() {
		return childTimeout;
	}

}
