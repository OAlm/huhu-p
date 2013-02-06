package fi.metropolia.mediaworks.juju.document;

public enum GrammaticalNumber {
	SINGULAR("SG"),
	PLURAL("PL");
	
	public final String tag;
	
	private GrammaticalNumber(String tag) {
		this.tag = tag;
	}
	
	public static GrammaticalNumber getByTag(String tag) {
		for (GrammaticalNumber n : values()) {
			if (n.tag.equals(tag)) {
				return n;
			}
		}
		System.err.println("Missing number: " + tag);
		return null;
	}
}
