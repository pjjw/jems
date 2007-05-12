package net.kodeninja.jem.server.console.telnet;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TelnetFilterInputStream extends FilterInputStream {

	public static final int SE = 240;
	public static final int NOP = 241;
	public static final int DM = 242;
	public static final int BRK = 243;
	public static final int IP = 244;
	public static final int AO = 245;
	public static final int AYT = 246;
	public static final int EC = 247;
	public static final int EL = 248;
	public static final int GA = 249;
	public static final int SB = 250;
	public static final int WILL = 251;
	public static final int WONT = 252;
	public static final int DO = 253;
	public static final int DONT = 254;
	public static final int IAC = 255;

	public static final byte OPT_ECHO = 1;
	public static final byte OPT_SUPPRESS_GO_AHEAD = 3;
	public static final byte OPT_STATUS = 5;
	public static final byte OPT_TIMING_MARK = 6;
	public static final byte OPT_TERMINAL_TYPE = 24;
	public static final byte OPT_WINDOW_SIZE = 31;
	public static final byte OPT_TERMINAL_SPEED = 32;
	public static final byte OPT_REMOTE_FLOW_CONTROL = 33;
	public static final byte OPT_LINEMODE = 34;
	public static final byte OPT_ENVIRONMENT_VARIABLES = 36;

	protected OutputStream out;
	protected boolean isStopped = false;
	protected StringBuffer buffer = new StringBuffer();

	public TelnetFilterInputStream(InputStream in, OutputStream out) {
		super(in);
		this.out = out;
	}

	@Override
	public int read() {
		int retVal = -2;
		if (buffer.length() > 0) {
			retVal = buffer.charAt(0);
			buffer.deleteCharAt(0);
		}

		return retVal;
	}

	@Override
	public int available() throws IOException {

		try {
			int tmpChar = super.read();

			while (tmpChar == IAC) {
				int command = super.read();
				if (command != SB) {
					byte arg = (byte) super.read();
					byte[] response = new byte[3];
					response[0] = (byte) IAC;
					if (command == WILL)
						response[1] = (byte) DONT;
					else
						response[1] = (byte) WONT;
					response[2] = arg;
					out.write(response);
				} else
					throw new IOException("Opps!");
				tmpChar = super.read();
			}

			if ((tmpChar >= 32) || (tmpChar == 13) || (tmpChar == 10))
				buffer.append((char) tmpChar);
			else if (tmpChar == 8)
				buffer.deleteCharAt(buffer.length() - 1);
		} catch (java.net.SocketTimeoutException e) {

		}

		int size = buffer.indexOf("\n") + 1;

		return size;
	}

	/*
	 * public int read() { try { int retVal = super.read(); while (retVal ==
	 * IAC) { int command = super.read(); if (command != SB) { byte arg =
	 * (byte)super.read(); byte[] response = new byte[3]; response[0] =
	 * (byte)IAC; if (command == WILL) response[1] = (byte)DONT; else
	 * response[1] = (byte)WONT; response[2] = arg; out.write(response); } else {
	 * throw new IOException("Opps!"); } retVal = super.read(); } return retVal; }
	 * catch (java.net.SocketTimeoutException e) { return -2; } catch
	 * (IOException e) { return -1; } }
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (isStopped)
			return -1;
		int i = off;
		for (; i < off + len; i++) {
			int tmp = read();
			if (tmp > -1)
				b[i] = (byte) tmp;
			else if (tmp == -2) {
				b[i] = 13;
				i++;
				b[i] = 10;
				i++;
				break;
			} else
				return -1;
		}

		return (i - off);
	}

	public void stop() {
		isStopped = true;
	}

}
