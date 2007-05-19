package net.kodeninja.http.service;

import java.io.IOException;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.util.KNModule;

public interface HTTPSocket extends KNModule {
	public boolean sendPacket(
			HTTPPacket<? extends HTTPHeader, ? extends HTTPBody> Packet);

	public boolean getPacket(
			HTTPPacket<? extends HTTPHeader, ? extends HTTPBody> Packet)
			throws IOException;

	public void close();

	public boolean isOpen();
}
