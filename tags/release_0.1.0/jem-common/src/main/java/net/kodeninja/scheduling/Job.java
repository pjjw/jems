package net.kodeninja.scheduling;

/**
 * This interface is to be inherited by objects that wish to place themselves on
 * the scheduler. The jobs should take a very short time to execute and if
 * possible, should not include loops. For example, a server thread that would
 * normally loop looking for requests to answer, would only check for one
 * request, then add itself back to the scheduler for another check.
 *
 * @author Chuck
 */
public interface Job extends Runnable {

	/**
	 * Polls the job to see if it needs to be run ASAP.
	 *
	 * @return True if the job needs to be run ASAP.
	 */
	public boolean isUrgent();

	/**
	 * Checks to see if the job can run on the helper threads aswell as main
	 * thread.
	 *
	 * @return True if it can only be run on the main thread.
	 */
	public boolean canRun();

	/**
	 * Returns the id of the next run. This should be different for each
	 * subsequent run.
	 *
	 * @return The id of the next run.
	 */
	public int runId();

	/**
	 * Starts/Continues the current job.
	 */
	public void start();

	/**
	 * Stops the current job.
	 */
	public void stop();

	/**
	 * Checks to see whether the job is set to run or not.
	 *
	 * @return True if start has been called since creation or the last stop
	 *         call.
	 */
	public boolean isStarted();
}
