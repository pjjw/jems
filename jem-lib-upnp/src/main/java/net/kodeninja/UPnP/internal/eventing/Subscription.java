package net.kodeninja.UPnP.internal.eventing;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.kodeninja.UPnP.UPnP;
import net.kodeninja.UPnP.description.Service;
import net.kodeninja.UPnP.description.ServiceStateVariable;
import net.kodeninja.UPnP.identifiers.SSDPUUID;
import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.http.packet.HTTPHeader;
import net.kodeninja.http.packet.HTTPPacket;
import net.kodeninja.http.packet.HTTPTextBody;
import net.kodeninja.http.packet.HTTPVersion;
import net.kodeninja.http.packet.extra.HTTPXMLBody;
import net.kodeninja.http.service.HTTPTCPPersistentConnection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Subscription {
	private static final long SEQ_ROLLOVER = 4294967296l;

	protected HTTPTCPPersistentConnection connection;
	private Service service;
	private SSDPUUID sid;
	private URL callback;
	private long timeout;
	private long lastSubscribe;
	private long seq = 0;
	private boolean forceExpire = false;

	public Subscription(Service service, SSDPUUID sid, URL callback, long timeout) {
		this.service = service;
		this.sid = sid;
		this.callback = callback;
		this.timeout = timeout;
		this.lastSubscribe = System.currentTimeMillis() / 1000;

		try {
			connection = new HTTPTCPPersistentConnection(callback.getHost(), callback.getPort(), UPnP.getServerString());
			connection.setTimeout(30000);
		}
		catch (IOException e) {
			connection = null;
		}
	}

	public SSDPUUID getSID() {
		return sid;
	}

	public URL getCallbackURL() {
		return callback;
	}

	public boolean expired() {
		return (forceExpire || (timeout == -1 ? true : (System.currentTimeMillis() / 1000 > lastSubscribe + timeout)));
	}

	public void renew(long timeout) {
		this.timeout = timeout;
		this.lastSubscribe = System.currentTimeMillis() / 1000;		
	}

	protected long incSeq() {
		long result = seq;
		seq++;

		//Roll over as per the UPnP spec
		if (seq == SEQ_ROLLOVER)
			seq = 0;

		return result;
	}

	public boolean announceStateChange(Map<ServiceStateVariable, Object> vals) {
		if (connection == null)
			return false;

		try {
			//Build XML data
			Document outDoc = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();
			outDoc = builder.newDocument();

			Element root = outDoc.createElement("e:propertyset");
			outDoc.appendChild(root);

			root.setAttribute("xmlns:e", "urn:schemas-upnp-org:event-1-0");
			root.setAttribute("xmlns:s", service.getType().toString());

			for (ServiceStateVariable var: vals.keySet()) {
				Object val = vals.get(var);
				Element prop = outDoc.createElement("e:property");
				root.appendChild(prop);

				Element varTag = outDoc.createElement("s:" + var.getName());
				prop.appendChild(varTag);
				if (val != null)
					varTag.setTextContent(val.toString());

				varTag.setAttribute("xmlns:dt", "urn:schemas-microsoft-com:datatypes");
				varTag.setAttribute("dt:dt", var.getDataType());
			}

			//Create body
			HTTPBody body = new HTTPXMLBody(outDoc);
			
			//body = new HTTPTextBody("<?xml version=\"1.0\"?>\r\n<e:propertyset xmlns:e=\"urn:schemas-upnp-org:event-1-0\" xmlns:s=\"urn:schemas-upnp-org:service:ConnectionManager:1\"><e:property><s:SourceProtocolInfo xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"string\">rtsp-rtp-udp:*:audio/L16;rate=44100;channels=2:DLNA.ORG_PN=LPCM,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVMED_PRO,http-get:*:video/x-ms-asf:DLNA.ORG_PN=MPEG4_P2_ASF_SP_G726,rtsp-rtp-udp:*:audio/mpeg:DLNA.ORG_PN=MP3X,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVMED_FULL,http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_MED,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVSPLL_BASE,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVMED_BASE,http-get:*:image/png:DLNA.ORG_PN=PNG_LRG,http-get:*:audio/L16;rate=44100;channels=1:DLNA.ORG_PN=LPCM,rtsp-rtp-udp:*:audio/x-ms-wma:DLNA.ORG_PN=WMDRM_WMAPRO,http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_PS_PAL,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVHIGH_PRO,http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_PS_NTSC,http-get:*:audio/L16;rate=44100;channels=2:DLNA.ORG_PN=LPCM,http-get:*:audio/x-ms-wma:DLNA.ORG_PN=WMDRM_WMAFULL,rtsp-rtp-udp:*:audio/x-ms-wma:DLNA.ORG_PN=WMAPRO,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVSPML_MP3,http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_SM,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVHIGH_PRO,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVSPML_BASE,http-get:*:audio/x-ms-wma:DLNA.ORG_PN=WMDRM_WMABASE,rtsp-rtp-udp:*:video/x-ms-asf:DLNA.ORG_PN=MPEG4_P2_ASF_ASP_L5_SO_G726,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVHIGH_FULL,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVMED_FULL,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVHIGH_FULL,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMVMED_PRO,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVSPML_MP3,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVMED_BASE,http-get:*:audio/x-ms-wma:DLNA.ORG_PN=WMAFULL,rtsp-rtp-udp:*:audio/x-ms-wma:DLNA.ORG_PN=WMAFULL,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVMED_FULL,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMVSPML_MP3,http-get:*:audio/x-ms-wma:DLNA.ORG_PN=WMABASE,rtsp-rtp-udp:*:video/x-ms-asf:DLNA.ORG_PN=MPEG4_P2_ASF_ASP_L4_SO_G726,rtsp-rtp-udp:*:audio/L16;rate=48000;channels=2:DLNA.ORG_PN=LPCM,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVSPLL_BASE,rtsp-rtp-udp:*:audio/x-ms-wma:DLNA.ORG_PN=WMABASE,http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_PS_NTSC_XAC3,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVMED_BASE,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVSPLL_BASE,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVHIGH_PRO,rtsp-rtp-udp:*:video/x-ms-asf:DLNA.ORG_PN=MPEG4_P2_ASF_SP_G726,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVMED_PRO,rtsp-rtp-udp:*:audio/mpeg:DLNA.ORG_PN=MP3,rtsp-rtp-udp:*:audio/L16;rate=48000;channels=1:DLNA.ORG_PN=LPCM,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMVHIGH_FULL,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVSPML_BASE,http-get:*:video/x-ms-asf:DLNA.ORG_PN=MPEG4_P2_ASF_ASP_L5_SO_G726,http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_LRG,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVSPML_BASE,http-get:*:audio/mpeg:DLNA.ORG_PN=MP3,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMVHIGH_PRO,http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_PS_PAL_XAC3,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMVMED_FULL,rtsp-rtp-udp:*:audio/x-ms-wma:DLNA.ORG_PN=WMDRM_WMAFULL,http-get:*:audio/x-ms-wma:DLNA.ORG_PN=WMDRM_WMAPRO,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMVMED_BASE,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMVSPLL_BASE,http-get:*:audio/L16;rate=48000;channels=1:DLNA.ORG_PN=LPCM,http-get:*:audio/x-ms-wma:DLNA.ORG_PN=WMAPRO,rtsp-rtp-udp:*:audio/x-ms-wma:DLNA.ORG_PN=WMDRM_WMABASE,http-get:*:video/mpeg:DLNA.ORG_PN=MPEG1,http-get:*:video/x-ms-asf:DLNA.ORG_PN=MPEG4_P2_ASF_ASP_L4_SO_G726,http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_TN,http-get:*:audio/L16;rate=48000;channels=2:DLNA.ORG_PN=LPCM,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVMED_PRO,http-get:*:audio/mpeg:DLNA.ORG_PN=MP3X,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVHIGH_FULL,rtsp-rtp-udp:*:audio/L16;rate=44100;channels=1:DLNA.ORG_PN=LPCM,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVSPML_MP3,rtsp-rtp-udp:*:video/x-ms-wmv:DLNA.ORG_PN=WMVSPML_BASE,http-get:*:audio/L16;rate=22000;channels=1:*,rtsp-rtp-udp:*:audio/L16;rate=11000;channels=1:*,rtsp-rtp-udp:*:video/x-ms-asf:*,http-get:*:image/vnd.ms-photo:*,rtsp-rtp-udp:*:audio/x-ms-wma:*,http-get:*:video/x-ms-wmv:*,http-get:*:audio/L16;rate=44100;channels=4:*,rtsp-rtp-udp:*:audio/L16;rate=11025;channels=1:*,http-get:*:audio/L8:*,http-get:*:image/x-icon:*,rtsp-rtp-udp:*:audio/L16;rate=22050;channels=1:*,http-get:*:audio/basic:*,http-get:*:audio/L8;rate=11025;channels=2:*,http-get:*:video/x-ms-wmx:*,http-get:*:audio/L8;rate=22050;channels=2:*,http-get:*:audio/L16;rate=44100;channels=6:*,http-get:*:application/x-wavpack:*,http-get:*:audio/L16;rate=11000;channels=2:*,http-get:*:audio/L8;rate=8000;channels=1:*,http-get:*:audio/aiff:*,http-get:*:audio/x-ms-wax:*,http-get:*:audio/L16;rate=44100;channels=8:*,http-get:*:audio/mid:*,http-get:*:audio/L16;rate=11025;channels=2:*,http-get:*:audio/L8;rate=11000;channels=2:*,http-get:*:audio/L16;rate=22050;channels=2:*,http-get:*:audio/L8;rate=44100;channels=1:*,http-get:*:video/x-ms-wm:*,http-get:*:application/vnd.ms-wpl:*,rtsp-rtp-udp:*:audio/L16;rate=8000;channels=2:*,http-get:*:video/x-ms-asf:*,http-get:*:image/gif:*,http-get:*:audio/wav:*,http-get:*:audio/L16;rate=8000;channels=2:*,http-get:*:audio/x-ms-wma:*,http-get:*:video/mp4:*,http-get:*:audio/L16:*,http-get:*:audio/L8;rate=48000;channels=1:*,rtsp-rtp-udp:*:audio/L16;rate=22000;channels=1:*,http-get:*:image/tiff:*,http-get:*:audio/L8;rate=22000;channels=2:*,rtsp-rtp-udp:*:audio/L16;rate=44100;channels=4:*,http-get:*:image/x-ycbcr-yuv420:*,http-get:*:application/x-shockwave-flash:*,http-get:*:audio/mpeg:*,rtsp-rtp-udp:*:audio/L16;rate=44100;channels=6:*,rtsp-rtp-udp:*:video/x-ms-wmv:*,http-get:*:audio/L16;rate=22000;channels=2:*,http-get:*:image/jpeg:*,rtsp-rtp-udp:*:audio/L16;rate=11000;channels=2:*,http-get:*:application/x-ms-wmd:*,http-get:*:audio/L8;rate=11025;channels=1:*,http-get:*:audio/L8;rate=22050;channels=1:*,http-get:*:application/x-ms-wmz:*,rtsp-rtp-udp:*:audio/L16;rate=44100;channels=8:*,http-get:*:video/x-ms-wvx:*,rtsp-rtp-udp:*:audio/L16;rate=11025;channels=2:*,http-get:*:audio/L16;rate=11000;channels=1:*,http-get:*:video/x-ms-dvr:*,rtsp-rtp-udp:*:audio/L16;rate=22050;channels=2:*,http-get:*:image/bmp:*,rtsp-rtp-udp:*:audio/mpeg:*,http-get:*:audio/L16;rate=11025;channels=1:*,http-get:*:audio/L8;rate=11000;channels=1:*,http-get:*:audio/L16;rate=22050;channels=1:*,rtsp-rtp-udp:*:audio/L16;rate=8000;channels=1:*,http-get:*:video/avi:*,http-get:*:audio/L16;rate=8000;channels=1:*,http-get:*:audio/L8;rate=8000;channels=2:*,http-get:*:application/vnd.ms-search:*,http-get:*:audio/L8;rate=44100;channels=2:*,http-get:*:video/mpeg:*,http-get:*:audio/L8;rate=22000;channels=1:*,http-get:*:audio/L8;rate=48000;channels=2:*,rtsp-rtp-udp:*:audio/L16;rate=22000;channels=2:*,http-get:*:audio/x-mpegurl:*,http-get:*:image/png:*</s:SourceProtocolInfo></e:property><e:property><s:SinkProtocolInfo xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"string\"></s:SinkProtocolInfo></e:property><e:property><s:CurrentConnectionIDs xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" dt:dt=\"string\">0</s:CurrentConnectionIDs></e:property></e:propertyset>\r\n");
			
			//Set up header
			HTTPHeader header = new HTTPHeader("NOTIFY", callback.toURI(), HTTPVersion.HTTP1_1);
			header.setParameter("NT", "upnp:event");
			header.setParameter("NTS", "upnp:propchange");
			header.setParameter("SID", sid.toString());
			header.setParameter("SEQ", "" + incSeq());
			header.setParameter("Content-Type", "text/xml; charset=\"utf-8\"");
			header.setParameter("User-Agent", "Mozilla/4.0 (compatible; UPnP/1.0; Windows NT/5.1)");// UPnP.getUserAgentString());
			header.setParameter("Host", connection.getRemoteHost() + ":" + connection.getRemotePort());
			header.setParameter("Content-Length", "" + body.getContentLength());
			header.setParameter("Connection", "Close");
			header.setParameter("Cache-Control", "no-cache");
			header.setParameter("Pragma", "no-cache");


			HTTPPacket<HTTPBody> request = new HTTPPacket<HTTPBody>(header, body);
			request.allowHeaderUpdate = false;
			
			try {
				HTTPPacket<HTTPBody> response = connection.sendRequest(request);

				if ((response != null) && (response.getHeader().getResponse().getCode() == 200))
					return true;
			
				if (response == null)
					return true;
				
				HTTPTextBody error = new HTTPTextBody();
				response.readBody(error);
			}
			catch (IOException e) {
				//Do nothing as the socket likely timed out
			}
			
			return false;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			//Do nothing, force expire gets set after anyway
			//This likely shouldn't happen either...
		} catch (ParserConfigurationException e) {
			//This shouldn't happen!
			e.printStackTrace();
		}

		//Failed to make callback, expire subscription
		forceExpire = true;
		return false;
	}

}
