package net.kodeninja.http.service;

import java.io.IOException;
import java.net.Socket;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.InvalidHeaderException;

public class HTTPTCPSocket implements HTTPSocket {
	protected Socket socket;

	public HTTPTCPSocket(Socket Connection) {
		socket = Connection;
	}

	public boolean sendPacket(
			HTTPPacket<? extends HTTPHeader, ? extends HTTPBody> Packet) {
		if (socket == null)
			return false;
		try {
			socket.getOutputStream().flush();
			Packet.writeToStream(socket.getOutputStream());
			socket.getOutputStream().flush();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean getPacket(
			HTTPPacket<? extends HTTPHeader, ? extends HTTPBody> Packet) throws IOException {
		if (socket == null)
			return false;
		try {
			Packet.readFromStream(socket.getInputStream());
		} catch (InvalidHeaderException e) {
			return false;
		}
		return true;
	}

	public void close() {
		if (socket != null) {
			Socket tmpSocket = socket;
			socket = null;
			try {
				tmpSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isOpen() {
		if (socket == null)
			return false;
		return socket.isConnected();
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
		return 0;
	}
}
