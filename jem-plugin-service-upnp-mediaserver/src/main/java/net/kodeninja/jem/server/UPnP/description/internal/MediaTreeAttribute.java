package net.kodeninja.jem.server.UPnP.description.internal;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MediaTreeAttribute {

	private Map<String, Object> attrs = new LinkedHashMap<String, Object>();
	private String name;
	private Object value;
	
	public MediaTreeAttribute(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void addAttribute(String attrName, Object attrValue) {
		attrs.put(attrName, attrValue);
	}
	
	public Iterator<String> getAttributeIterator() {
		return attrs.keySet().iterator();
	}
	
	public Object getAttributeValue(String name) {
		return attrs.get(name);
	}
	
}
