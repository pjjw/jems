package net.kodeninja.UPnP;

public interface UPnPBaseListener {

	/**
	 * Called when an error occurs with the requested operation.
	 * @param operation The operation the error occured in. 
	 * @param error The error that occured.
	 */
	public void operationFailed(UPnPOperation operation, UPnPException e);

}
