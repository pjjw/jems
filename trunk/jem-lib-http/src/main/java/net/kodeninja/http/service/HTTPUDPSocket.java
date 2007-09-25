package net.kodeninja.http.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.InvalidHeaderException;

public class HTTPUDPSocket implements HTTPSocket {

	protected DatagramSocket socket;
	protected InetAddress destAddr;
	protected int destPort;
	protected DatagramPacket udpPacket;
	protected String serverString;
	private long lastTick;
	private long timeout;

	public HTTPUDPSocket(DatagramSocket s, InetAddress dest, int port, String serverString, int childTimeout) {
		socket = s;
		udpPacket = null;
		destAddr = dest;
		destPort = port;
		this.serverString = serverString;
		lastTick = System.currentTimeMillis();
		timeout = childTimeout;
	}

	public HTTPUDPSocket(DatagramSocket s, DatagramPacket p, String serverString, int childTimeout) {
		this(s, p.getAddress(), p.getPort(), serverString, childTimeout);
		udpPacket = p;
	}

	public void close() {
		socket = null;
	}

	public boolean isOpen() {
		if (lastTick + timeout < System.currentTimeMillis())
			close();
		return (socket != null);
	}

	public int getTimeout() {
		if (socket != null) {
			try {
				return socket.getSoTimeout();
			} catch (SocketException e) {
			}
		}
		return -1;
	}

	public void setTimeout(int timeout) {
		if (socket != null) {
			try {
				socket.setSoTimeout(timeout);
			} catch (SocketException e) {
			}
		}
	}

	public String getRemoteHost() {
		return destAddr.getHostAddress();
	}

	public int getRemotePort() {
		return destPort;
	}

	public synchronized boolean sendPacket(HTTPPacket<? extends HTTPBody> Packet)
	throws IOException {
		if (socket == null)
			return false;

		int bufSize = 1024;
		if (Packet.getBody() != null)
			bufSize += Packet.getBody().getContentLength();

		ByteArrayOutputStream buffer = new ByteArrayOutputStream(bufSize);

		if (Packet.allowHeaderUpdate) {
			if (Packet.getHeader().getType() == HTTPHeader.HeaderType.REQUEST)
				Packet.getHeader().setParameter("Host", socket.getInetAddress() + ":" + socket.getPort());
			else
				Packet.getHeader().setParameter("Server", serverString);
		}
			

		Packet.writeToStream(buffer);
		DatagramPacket p = new DatagramPacket(buffer.toByteArray(), buffer.size(), destAddr, destPort);
		socket.send(p);
		lastTick = System.currentTimeMillis();

		return true;
	}

	public boolean getPacket(HTTPPacket<? extends HTTPBody> Packet)
			throws IOException {
		if ((udpPacket == null) || (socket == null))
			return false;

		ByteArrayInputStream buffer = new ByteArrayInputStream(udpPacket.getData());

		udpPacket = null;

		try {
			Packet.readFromStream(buffer);
		} catch (InvalidHeaderException e) {
			return false;
		} 
		lastTick = System.currentTimeMillis();
		return true;
	}

	public String getName() {
		return "HTTP UDP Socket";
	}

	public int getVersionMajor() {
		return 0;
	}

	public int getVersionMinor() {
		return 1;
	}

	public int getVersionRevision() {
		return 2;
	}

}
