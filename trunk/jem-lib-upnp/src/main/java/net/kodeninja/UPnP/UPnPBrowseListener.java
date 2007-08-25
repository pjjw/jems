package net.kodeninja.UPnP;

public interface UPnPBrowseListener extends UPnPBaseListener {
	
	/**
	 * Called when the service that was being sought is found.
	 * @param browser The operation that found the service.
	 */
	public void serviceFound(UPnPOperation browser, UPnPCache service);
	
	/**
	 * Called when the service that was being sought is lost.
	 * @param browser The operation that lost the service.
	 */
	public void serviceLost(UPnPOperation browser, UPnPCache service);

}
