package fi.metropolia.mediaworks.juju.rle;

import org.apache.commons.lang3.ArrayUtils;

public enum RDFType {
	XML("RDF/XML", new String[] {"rdf", "owl"}), TURTLE("TURTLE", new String[] {"ttl"});
	
	private String type;
	private String[] extensions;
	
	RDFType(String type, String[] extensions) {
		this.type = type;
		this.extensions = extensions;
	}
	
	public String getType() {
		return type;
	}
	
	public String[] getExtensions() {
		return extensions;
	}
	
	public static RDFType getRDFType(String extension) {
		for (RDFType t : RDFType.values()) {
			if (ArrayUtils.contains(t.extensions, extension)) {
				return t;
			}
		}
		return XML;
	}
}
