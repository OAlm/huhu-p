package fi.metropolia.mediaworks.juju.document;

public enum GrammaticalCase {
	NOMINATIVE("NOM"),
	TRANSLATIVE("TRA"),
	ALLATIVE("ALL"),
	PARTITIVE("PAR"),
	ELATIVE("ELA"),
	GENITIVE("GEN"),
	INSTRUCTIVE("INS"),
	ILLATIVE("ILL"),
	ABLATIVE("ABL"),
	ESSIVE("ESS"),
	INESSIVE("INE"),
	ADESSIVE("ADE"),
	ACCUSATIVE("ACC"),
	ABESSIVE("ABE"),
	COMITATIVE("CMT"),
	LATIVE("LAT"),
	DISTRIBUTIVE("DIS"),
	PROLATIVE("PRL"),
	TEMPORAL("TMP");
	
	public final String tag;
	
	private GrammaticalCase(String tag) {
		this.tag = tag;
	}
	
	public static GrammaticalCase getWithTag(String tag) {
		for (GrammaticalCase c : values()) {
			if (c.tag.equals(tag)) {
				return c;
			}
		}
		System.err.println("Missing case: " + tag);
		return null;
	}
}
