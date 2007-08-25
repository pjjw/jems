package net.kodeninja.http.service;

import java.io.IOException;
import java.util.Iterator;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPResponseCode;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.packet.extra.HTTPErrorResponse;
import net.kodeninja.http.service.handlers.PacketHandler;
import net.kodeninja.scheduling.JobImpl;
import net.kodeninja.scheduling.Scheduler;
import net.kodeninja.util.KNModule;

public class HTTPChildService extends JobImpl implements KNModule {

	protected HTTPSocket socket;
	protected boolean stopProcessing;
	protected HTTPServerSocket owner;

	public HTTPChildService(Scheduler scheduler, HTTPServerSocket Owner,
			HTTPSocket Connection) {
		super(true, scheduler);
		owner = Owner;
		socket = Connection;
	}

	public String getName() {
		return owner.getName() + " Child";
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

	public boolean isPublic() {
		return true;
	}

	@Override
	public void stop() {
		if ((socket != null) && (socket.isOpen()))
			socket.close();
		super.stop();
	}

	@Override
	public void run() {
		try {
			if ((socket == null) || (socket.isOpen() == false)) {
				throw new IOException("Child socket no longer open.");
			}


			boolean wasProcessed = false;
			HTTPPacket<HTTPBody> recievedPacket = new HTTPPacket<HTTPBody>(new HTTPHeader());

			try {
				if (socket.getPacket(recievedPacket) == false) {
					super.run();
					return;
				}
			} catch (IOException e) {
				stop();
				return ;
			}

			Iterator<PacketHandler> it = owner.getHandlers();
			while (it.hasNext() && (!wasProcessed)) {
				PacketHandler handler = it.next();
				wasProcessed = handler.process(socket, recievedPacket);
			}
			if (wasProcessed == false) {
				socket.sendPacket(new HTTPErrorResponse(HTTPResponseCode.HTTP_501_NOT_IMPLEMENTED, recievedPacket.getHeader().getVersion()));
				stop();
			}
			if (recievedPacket.getHeader().getVersion().equals(HTTPVersion.HTTP1_0))
				stop();
			else {
				String conn = recievedPacket.getHeader().getParameter("connection");
				if ((conn != null) && (conn.equals("close")))
					socket.close();
			}
		}
		catch (Throwable e) {
			stop();
		}

		super.run();
	}

	public boolean canRun() {
		return true;
	}

	public boolean isUrgent() {
		return false;
	}

}

