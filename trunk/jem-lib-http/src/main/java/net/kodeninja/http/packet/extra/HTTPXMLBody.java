package net.kodeninja.http.packet.extra;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.kodeninja.http.packet.HTTPBody;
import net.kodeninja.io.LimitedSizeInputStream;
import net.kodeninja.util.MimeType;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class HTTPXMLBody implements HTTPBody {
	public static final MimeType XML_MIMETYPE = new MimeType("text", "xml");
	protected Document doc = null;
	protected MimeType mime;

	public HTTPXMLBody() {
		this(null, XML_MIMETYPE);
	}

	public HTTPXMLBody(Document d) {
		this(d, XML_MIMETYPE);
	}

	public HTTPXMLBody(Document d, MimeType m) {
		mime = m;
		doc = d;
	}

	public boolean forceCompression() {
		return false;
	}

	public long getContentLength() {
		try {
			int result;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			StreamResult wr = new StreamResult(out);
			DOMSource src = new DOMSource(doc);
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.transform(src, wr);
			result = out.size();
			out.close();
			return result;
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public MimeType getMimeType() {
		return mime;
	}

	public String getContentType() {
		return getMimeType().toString() + "; charset=\"utf-8\"";
	}
	
	public void readFromStream(InputStream in, int ContentLength) throws IOException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			if (ContentLength >= 0)
				in = new LimitedSizeInputStream(in , ContentLength, true);
			doc = db.parse(in);
		}
		catch (ParserConfigurationException e) {
			throw new IOException(e);
		}
		catch (SAXException e) {
			throw new IOException(e);
		}
	}

	public void writeToStream(OutputStream out) throws IOException {
		try {
			StreamResult wr = new StreamResult(out);
			DOMSource src = new DOMSource(doc);
			Transformer serializer = TransformerFactory.newInstance().newTransformer();
			serializer.transform(src, wr);
		}
		catch (TransformerConfigurationException e) {
			throw new IOException(e);
		}
		catch (TransformerException e) {
			throw new IOException(e);
		}
	}
	
	public Document getDocument() {
		return doc;
	}
	
	public boolean forceChunked() {
		return false;
	}
}
