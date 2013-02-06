package fi.metropolia.mediaworks.juju.document;

public enum PartOfSpeech {
	SYMBOL("SY"),
	PRONOUN("PR"),
	ADJECTIVE("AD"),
	NUMERAL("NM"),
	VERB("VB"),
	BASE("BS"),
	CONNECTION("CN"),
	ACRONYM("AC"),
	FOREIGN_WORD("FW"),
	UNIDENTIFIED("XX");
	
	public final String tag;
	
	private PartOfSpeech(String tag) {
		this.tag = tag;
	}
}
