package net.kodeninja.jem.server.www;

import org.w3c.dom.Node;

import net.kodeninja.http.service.HTTPTCPService;
import net.kodeninja.http.service.handlers.GetHandler;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.jem.server.www.handlers.MediaURI;
import net.kodeninja.jem.server.www.handlers.ResourceURI;
import net.kodeninja.util.KNRunnableModule;
import net.kodeninja.util.KNXMLModule;
import net.kodeninja.util.KNXMLModuleInitException;

public class WWWService extends HTTPTCPService implements KNRunnableModule,
		KNXMLModule {
	private String serviceName = "WWW Service";
	public final static int WWW_PORT = 80;

	public WWWService() {
		super(JemServer.getInstance().getScheduler(), WWW_PORT);
	}

	public void xmlInit(Node xmlNode) throws KNXMLModuleInitException {
		serviceName = xmlNode.getAttributes().getNamedItem("name")
				.getNodeValue();
		for (Node modNode = xmlNode.getFirstChild(); modNode != null; modNode = modNode
				.getNextSibling()) {
			if (modNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (modNode.getNodeName().equals("port")) {
				String portString = modNode.getTextContent().trim();
				if (portString.length() > 0)
					try {
						setPort(Integer.parseInt(portString));
					} catch (NumberFormatException e) {
						throw new KNXMLModuleInitException("Invalid port.");
					}
				else
					throw new KNXMLModuleInitException("Invalid port.");
			}
		}
	}

	@Override
	public void start() {
		if (isStarted() == false) {
			// Setup logger for server
			addLogger(JemServer.getInstance());

			// The main get handler
			GetHandler getter = new GetHandler();

			// Add handlers to server
			getTransport().addHandler(getter);

			// Add handlers
			getter.addURIHandler(new MediaURI());
			getter.addURIHandler(new ResourceURI());

			// Start service
			super.start();
			JemServer.getInstance().addLog(
											getName() + " [" + serviceName
													+ "] started. (Port: "
													+ getPort() + ")");
		}

	}

	@Override
	public String getName() {
		return "JEM WWW Plugin";
	}

	@Override
	public int getVersionMajor() {
		return 0;
	}

	@Override
	public int getVersionMinor() {
		return 1;
	}

	@Override
	public int getVersionRevision() {
		return 0;
	}

}
