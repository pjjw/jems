package net.kodeninja.http.service;

import java.io.IOException;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.util.KNModule;

public interface HTTPSocket extends KNModule {
	public boolean sendPacket(HTTPPacket<? extends HTTPBody> Packet)
			throws IOException;

	public boolean getPacket(HTTPPacket<? extends HTTPBody> Packet)
			throws IOException;

	public String getRemoteHost();
	
	public int getRemotePort();
	
	public void close();

	public boolean isOpen();
	
	public int getTimeout();

	public void setTimeout(int timeout);
}
