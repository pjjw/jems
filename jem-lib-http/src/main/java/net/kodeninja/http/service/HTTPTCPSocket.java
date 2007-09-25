package net.kodeninja.http.service;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.net.Socket;
import java.net.SocketException;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.InvalidHeaderException;

public class HTTPTCPSocket implements HTTPSocket {
	protected Socket socket;
	protected String serverString;

	private final static long DEFAULT_TIMEOUT = 1000;
	private boolean timeingOut = false;
	private long timeout;

	public HTTPTCPSocket(Socket Connection, String serverString) {
		socket = Connection;
		this.serverString = serverString;
	}

	public boolean sendPacket(HTTPPacket<? extends HTTPBody> Packet)
	throws IOException {
		if (socket == null)
			return false;

		if (Packet.allowHeaderUpdate) {
			if (Packet.getHeader().getType() == HTTPHeader.HeaderType.REQUEST)
				Packet.getHeader().setParameter("Host", socket.getInetAddress() + ":" + socket.getPort());
			else
				Packet.getHeader().setParameter("Server", serverString);
		}

		socket.getOutputStream().flush();
		Packet.writeToStream(socket.getOutputStream());
		socket.getOutputStream().flush();
		return true;
	}

	public boolean getPacket(HTTPPacket<? extends HTTPBody> Packet)
	throws IOException {
		if (socket == null)
			return false;
		try {
			Packet.readFromStream(new BufferedInputStream(socket.getInputStream()));
		} catch (InvalidHeaderException e) {
			return false;
		}
		return true;
	}

	public String getRemoteHost() {
		return socket.getInetAddress().getHostAddress();
	}

	public int getRemotePort() {
		return socket.getPort();
	}

	public void close() {
		if (socket != null) {
			if ((timeingOut) && (System.currentTimeMillis() > timeout)) {
				Socket tmpSocket = socket;
				socket = null;
				try {
					tmpSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				timeingOut = true;
				timeout = System.currentTimeMillis() + DEFAULT_TIMEOUT;
			}
		}
	}

	public boolean isOpen() {
		if ((timeingOut) && (System.currentTimeMillis() > timeout))
			close();
		if (socket == null)
			return false;
		return socket.isConnected();
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

	public String getName() {
		return "HTTP TCP Socket";
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
}
