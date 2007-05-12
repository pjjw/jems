package net.kodeninja.http.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.InvalidHeaderException;

public class HTTPUDPSocket implements HTTPSocket {

	protected DatagramSocket socket;
	protected InetAddress destAddr;
	protected int destPort;
	protected DatagramPacket udpPacket;

	public HTTPUDPSocket(DatagramSocket s, InetAddress dest, int port) {
		socket = s;
		udpPacket = null;
		destAddr = dest;
		destPort = port;
	}

	public HTTPUDPSocket(DatagramSocket s, DatagramPacket p) {
		socket = s;
		udpPacket = p;
		destAddr = p.getAddress();
		destPort = p.getPort();
	}

	public void close() {
		socket = null;
	}

	public boolean isOpen() {
		return (socket != null);
	}

	public boolean sendPacket(
			HTTPPacket<? extends HTTPHeader, ? extends HTTPBody> Packet) {
		if (socket == null)
			return false;

		int bufSize = 1024;
		if (Packet.getBody() != null)
			bufSize += Packet.getBody().getContentLength();

		ByteArrayOutputStream buffer = new ByteArrayOutputStream(bufSize);

		try {
			Packet.writeToStream(buffer);
			DatagramPacket p = new DatagramPacket(buffer.toByteArray(), buffer
					.size(), destAddr, destPort);
			socket.send(p);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean getPacket(
			HTTPPacket<? extends HTTPHeader, ? extends HTTPBody> Packet) {
		if ((udpPacket == null) || (socket == null))
			return false;

		ByteArrayInputStream buffer = new ByteArrayInputStream(udpPacket
				.getData());

		udpPacket = null;

		try {
			Packet.readFromStream(buffer);
		} catch (InvalidHeaderException e) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

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
		return 0;
	}

}
