package net.kodeninja.scheduling;

public class MinimumTimeJob extends JobImpl {
	private long msec;
	private long last;

	public MinimumTimeJob(boolean repeat, Scheduler owner, long msec,
			boolean runNow) {
		super(repeat, owner);
		this.msec = msec;
		if (runNow)
			last = 0;
		else
			last = System.currentTimeMillis();
	}

	public void setTime(long msec) {
		this.msec = msec;
	}

	public long getTime() {
		return msec;
	}

	public void runNow() {
		last = System.currentTimeMillis() - (msec * 2); 
	}
	
	public void reset() {
		last = System.currentTimeMillis();
	}

	public boolean canRun() {
		return System.currentTimeMillis() > last + msec;
	}

	public boolean isUrgent() {
		return System.currentTimeMillis() > last + (msec * 2);
	}

	@Override
	public void run() {
		last = System.currentTimeMillis();
		super.run();
	}

}
