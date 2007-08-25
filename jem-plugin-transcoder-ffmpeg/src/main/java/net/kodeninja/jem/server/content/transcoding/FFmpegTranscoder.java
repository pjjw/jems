package net.kodeninja.jem.server.content.transcoding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Node;

import net.kodeninja.jem.server.JemServer;
import net.kodeninja.util.KNXMLModule;
import net.kodeninja.util.KNModuleInitException;
import net.kodeninja.util.MalformedMimeTypeException;
import net.kodeninja.util.MimeType;

public class FFmpegTranscoder implements Transcoder, KNXMLModule {
	private String transcoderName = "";
	private String path = "ffmpeg";
	private boolean debug = false;
	private List<FFmpegMethod> methods = new LinkedList<FFmpegMethod>();

	public void xmlInit(Node xmlNode) throws KNModuleInitException {
		transcoderName = xmlNode.getAttributes().getNamedItem("name")
				.getNodeValue();
		for (Node modNode = xmlNode.getFirstChild(); modNode != null; modNode = modNode
				.getNextSibling())
			if (modNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			else if (modNode.getNodeName().equals("path"))
				path = modNode.getTextContent();
			else if (modNode.getNodeName().equals("debug"))
				debug = true;
			else if (modNode.getNodeName().equals("transcoder"))
				try {
					FFmpegMethod m = new FFmpegMethod(new MimeType(modNode
							.getAttributes().getNamedItem("from")
							.getNodeValue()),
							new MimeType(modNode.getAttributes()
									.getNamedItem("to").getNodeValue()),
							modNode.getTextContent());
					methods.add(m);
				} catch (MalformedMimeTypeException e) {
					throw new KNModuleInitException(e.getMessage());
				}
		JemServer.getInstance().addLog(
										"[" + transcoderName + "] Added "
												+ methods.size()
												+ " transcoding methods.");
	}

	public InputStream transcode(MimeType from, MimeType to, InputStream src)
			throws IOException {
		FFmpegMethod method = null;

		for (FFmpegMethod m : methods)
			if (m.matchs(from, to)) {
				method = m;
				break;
			}

		if (method != null) {
			JemServer.getInstance().addLog("["+ getName() +"] Starting transcode. (" + from + " to " + to + ")");
			
			//Build command array
			Vector<String> cmdVector = new Vector<String>();
			cmdVector.add(path);
			String[] args = ("-i - " + method.getCommand() + " -").split(" ");
			for (String arg: args)
				cmdVector.add(arg);
			String[] cmd = new String[cmdVector.size()];
			cmdVector.toArray(cmd);
			
			Process pid = Runtime.getRuntime().exec(cmd);
			new FFmpegStreamBridge(pid, src, new BufferedOutputStream(pid.getOutputStream()), true).start();
			new FFmpegStreamBridge(pid, pid.getErrorStream(), ( debug ? System.out : null), false).start();
			return new BufferedInputStream(pid.getInputStream());
		} else
			return null;
	}

	public String getName() {
		return "FFmpeg Transcoder";
	}

	public int getVersionMajor() {
		return 0;
	}

	public int getVersionMinor() {
		return 1;
	}

	public int getVersionRevision() {
		return 0;
	}

}
