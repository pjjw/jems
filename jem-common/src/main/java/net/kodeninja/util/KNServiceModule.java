package net.kodeninja.util;

public interface KNServiceModule extends KNModule {
	public void init() throws KNModuleInitException;
	public void deinit() throws KNModuleInitException;
}
