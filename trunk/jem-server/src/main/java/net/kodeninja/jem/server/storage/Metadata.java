package net.kodeninja.jem.server.storage;

import net.kodeninja.util.Pair;

public class Metadata extends Pair<MetadataType, String> {

	public Metadata(MetadataType a, String b) {
		super(a, b);
	}
	
	public MetadataType getType() {
		return getA();
	}
	
	public String getValue() {
		return getB();
	}
	
	public String toString() {
		return getType() + "=" + getValue();
	}
}
