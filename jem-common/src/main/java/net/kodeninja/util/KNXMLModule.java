package net.kodeninja.util;

import org.w3c.dom.Node;

public interface KNXMLModule extends KNModule {
	public void xmlInit(Node xmlNode) throws KNXMLModuleInitException;
}
