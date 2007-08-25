package net.kodeninja.http.service;

import java.net.Socket;
import java.io.IOException;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;

public class HTTPTCPPersistentConnection extends HTTPTCPSocket {

	public static final int DEFAULT_HTTP_PORT = 80;
	
	protected String host;
	protected int port;
	
	public HTTPTCPPersistentConnection(String host, int port, String serverString) throws IOException {
		super(new Socket(host, port), serverString);
		this.host = host;
		this.port = port;
	}

	public HTTPPacket<HTTPBody> sendRequest(HTTPPacket<? extends HTTPBody> Packet) throws IOException {
		if (socket.isConnected() == false)
			socket = new Socket(host, port);
		
		if (sendPacket(Packet)) {
			if (socket.isConnected() == false)
				return null;
			HTTPPacket<HTTPBody> recievedPacket = new HTTPPacket<HTTPBody>(new HTTPHeader());
			if (getPacket(recievedPacket))
				return recievedPacket;
		}
		
		return null;
	}
	
}
