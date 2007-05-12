package net.kodeninja.scheduling;

import java.util.Map;

public interface Scheduler extends Runnable {

	/**
	 * Adds a job to the scheduler's queue.
	 *
	 * @param job
	 *            The job to add to the scheduler's queue.
	 * @return True if the job was not allready added.
	 */
	public boolean addJob(Job job);

	/**
	 * Removes a job from the scheduler's queue.
	 *
	 * @param job
	 *            The job to remove from the queue.
	 * @return True if the job was found and therefore removed.
	 */
	public boolean removeJob(Job job);

	/**
	 * Starts the scheduler's process. Blocks until exits.
	 */
	public void start();

	/**
	 * Starts the scheduler's process. Blocks until exits if block is set to
	 * true.
	 *
	 * @param block
	 *            If set to true, it includes the calling thread in pool and
	 *            doesn't return until a stop has been called.
	 */
	public void start(boolean block);

	/**
	 * Stops the scheduler, and returns from the block state.
	 */
	public void stop();

	/**
	 * The run procedure for the main and helper threads. Do not call this
	 * procedure directly.
	 */
	public void run();

	/**
	 * Runs other jobs while waiting for the requested job to finish.
	 *
	 * @param job
	 *            The job to wait for.
	 */
	public void waitFor(Job job);

	/**
	 * Runs other jobs while waiting for the requested jobs to finish.
	 *
	 * @param jobs
	 *            The jobs to wait for.
	 */
	public void waitFor(Map<? extends Job, Integer> jobs);
}
