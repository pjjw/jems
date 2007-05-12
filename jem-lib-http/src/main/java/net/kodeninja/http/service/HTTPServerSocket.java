package net.kodeninja.http.service;

import java.util.Iterator;

import net.kodeninja.http.service.handlers.PacketHandler;
import net.kodeninja.util.KNModule;

public interface HTTPServerSocket extends KNModule {
	/**
	 * Opens the socket on the specified port.
	 *
	 * @param Port
	 *            The port to open to socket on.
	 * @return Success of the operation.
	 */
	public boolean openSocket(int Port);

	/**
	 * Closes the currently open socket.
	 */
	public void closeSocket();

	/**
	 * Returns the status of the socket.
	 *
	 * @return True if the socket has been opened.
	 */
	public boolean isOpen();

	/**
	 * Checks to see if any pending requests are waiting.
	 *
	 * @param owner The requesting service object.
	 * @return Null if no requests have yet to be processed.
	 */
	public HTTPChildService checkRequests(
			HTTPService<? extends HTTPServerSocket> owner);

	/**
	 * Adds a request handler for the service's use to process packets.
	 *
	 * @param Handler
	 *            The handler to add.
	 */
	public void addHandler(PacketHandler Handler);

	/**
	 * Removes a request handler for the service's use to process packets.
	 *
	 * @param Handler
	 *            The handler to add.
	 */
	public void removeHandler(PacketHandler Handler);

	/**
	 * Returns an iterator that traverses through the handlers.
	 *
	 * @return An iterator pointing at the internal list of handlers.
	 */
	public Iterator<PacketHandler> getHandlers();
}
