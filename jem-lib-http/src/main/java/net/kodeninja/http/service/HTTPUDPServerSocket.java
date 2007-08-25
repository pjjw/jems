package net.kodeninja.http.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.service.handlers.PacketHandler;
import net.kodeninja.util.KNModule;

public class HTTPUDPServerSocket implements HTTPServerSocket, KNModule {

	protected Set<InetAddress> multicastGroups = new HashSet<InetAddress>();
	protected MulticastSocket socket = null;
	protected int requestTimeout = 100;
	protected int childTimeout = 10000;
	protected Set<PacketHandler> handlers = Collections.synchronizedSet(new LinkedHashSet<PacketHandler>());
	protected String serverString = null; 

	public HTTPChildService checkRequests(
			HTTPService<? extends HTTPServerSocket> owner) {
		if (socket == null)
			return null;

		try {
			DatagramPacket packet = new DatagramPacket(new byte[socket.getReceiveBufferSize()], socket.getReceiveBufferSize());

			socket.receive(packet);

			return new HTTPChildService(owner.getScheduler(), this,
					new HTTPUDPSocket(socket, packet, serverString));
		} catch (SocketTimeoutException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean openSocket(int Port) {
		try {
			if (Port == -1)
				socket = new MulticastSocket();
			else
				socket = new MulticastSocket(Port);
			socket.setSoTimeout(requestTimeout);

			Iterator<InetAddress> it = multicastGroups.iterator();
			while (it.hasNext())
				socket.joinGroup(it.next());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void closeSocket() {
		if (socket == null)
			return;

		socket.close();
		socket = null;
	}

	public String getLocalHost() {
		if (isOpen())
			return socket.getInetAddress().getHostName();
		else
			return "";
	}

	public int getPort() {
		if (isOpen())
			return socket.getLocalPort();
		else
			return -1;
	}

	public boolean isOpen() {
		return socket.isBound() && !socket.isClosed();
	}

	public void joinMulticastGroup(InetAddress group) {
		multicastGroups.add(group);

		try {
			if (socket != null)
				socket.joinGroup(group);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void leaveMulticastGroup(InetAddress group) {
		multicastGroups.remove(group);

		try {
			if (socket != null)
				socket.leaveGroup(group);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTTL(int ttl) {
		try {
			if (socket != null)
				socket.setTimeToLive(ttl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean sendPacket(InetAddress addr, int port,
			HTTPPacket<? extends HTTPBody> Packet) throws IOException {
		if (socket == null)
			return false;
		return (new HTTPUDPSocket(socket, addr, port, serverString)).sendPacket(Packet);
	}

	public String getName() {
		return "HTTP(UDP) Service";
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
	
	public void setServerString(String s) {
		serverString = s;
	}

	public String getServerString() {
		return serverString;
	}

}
