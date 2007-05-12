package net.kodeninja.scheduling;

public abstract class JobImpl implements Job {
	protected static final int DEFAULT_RUN_ID = Integer.MIN_VALUE + 1;
	private int runNum = DEFAULT_RUN_ID;
	private boolean repeat = false;
	private boolean started = false;
	private Scheduler owner;

	public JobImpl(boolean repeat, Scheduler owner) {
		this.repeat = repeat;
		this.owner = owner;
	}

	public int runId() {
		return runNum;
	}

	public void run() {
		// Increment the run number
		runNum++;
		if (runNum == Integer.MAX_VALUE - 1)
			runNum = Integer.MIN_VALUE + 1;

		if ((repeat) && (started))
			owner.addJob(this);
	}

	public boolean isStarted() {
		return started;
	}

	public void start() {
		started = true;
		owner.addJob(this);
	}

	public void stop() {
		started = false;
	}

}
