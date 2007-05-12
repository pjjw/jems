package net.kodeninja.util;

public abstract class TimedAction implements Runnable {
	public static final long DEFAULT_WAIT_TIME = 10000;
	public static final long DEFAULT_SLEEP_TIME = 100;
	protected volatile long waitTime;
	protected volatile long sleepTime;
	private volatile long timeWaited;
	protected boolean isStopped = false;
	private Thread t = null;

	public TimedAction() {
		this(DEFAULT_WAIT_TIME);
	}

	public TimedAction(long time) {
		this(time, DEFAULT_SLEEP_TIME);
	}

	public TimedAction(long timeToWait, long timeToSleep) {
		waitTime = timeToWait;
		sleepTime = timeToSleep;
	}

	public void run() {
		timeWaited = 0;
		isStopped = false;
		while ((isStopped == false) && (timeWaited < waitTime))
			synchronized (this) {
				try {
					timeWaited += sleepTime;
					wait(sleepTime);
				} catch (InterruptedException e) {
				}
			}

		synchronized (this) {
			t = null;
			if (isStopped == false)
				action();
		}
	}

	public synchronized void start() {
		stop();
		if ((t != null) && (t.isAlive()))
			try {
				t.join();
			} catch (InterruptedException e) {
			}
		t = new Thread(this);
		t.start();
	}

	public synchronized void reset() {
		if (isRunning()) {
			timeWaited = 0;
			notifyAll();
		} else
			start();
	}

	public boolean isRunning() {
		return (t != null);
	}

	public synchronized void stop() {
		isStopped = true;
	}

	public synchronized void changeWaitTime(long newTime) {
		waitTime = newTime;
	}

	abstract protected void action();

}
