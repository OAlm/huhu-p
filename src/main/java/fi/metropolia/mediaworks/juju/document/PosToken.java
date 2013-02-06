package fi.metropolia.mediaworks.juju.document;

import java.util.List;

public class PosToken extends Token {
	private static final long serialVersionUID = 1L;
	
	private final String lemma;
	private final PartOfSpeech pos;
	private final GrammaticalCase grammaticalCase;
	private final GrammaticalNumber grammaticalNumber;
	
	public PosToken(String text, String lemma, PartOfSpeech pos) {
		this(text, lemma, pos, null, null, null);
	}

	public PosToken(String text, String lemma, PartOfSpeech pos, GrammaticalCase grammaticalCase, GrammaticalNumber grammaticalNumber, CaseChange caseChange) {
		super(text, caseChange);

		this.lemma = lemma;		
		this.pos = pos;
		this.grammaticalCase = grammaticalCase;
		this.grammaticalNumber = grammaticalNumber;
	}
	
	public final String getLemma() {
		if (lemma != null) {
			return lemma;
		}
		return getText();
	}
	
	public final PartOfSpeech getPartOfSpeech() {
		return pos;
	}
	
	public final GrammaticalCase getGrammaticalCase() {
		return grammaticalCase;
	}

	public final GrammaticalNumber getGrammaticalNumber() {
		return grammaticalNumber;
	}
	
	@Override
	protected List<String> getDebugStringParts() {
		List<String> parts = super.getDebugStringParts();
		
		if (lemma != null) {
			parts.add(lemma);
		}
		
		if (pos != null) {
			parts.add(pos.toString());
		}
		if (grammaticalCase != null) {
			parts.add(grammaticalCase.toString());
		}
		if (grammaticalNumber != null) {
			parts.add(grammaticalNumber.toString());
		}
		return parts;
	}
}
