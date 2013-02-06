package fi.metropolia.mediaworks.juju.document;

import java.util.List;

import fi.metropolia.mediaworks.juju.syntax.fi.omorfi.OmorfiPartOfSpeech;


public class OmorfiToken extends PosToken {	
	private static final long serialVersionUID = 1L;
	
	private final OmorfiPartOfSpeech partOfSpeech;
	
	public OmorfiToken(String text, String lemma, OmorfiPartOfSpeech partOfSpeech, GrammaticalCase grammaticalCase, GrammaticalNumber grammaticalNumber, CaseChange caseChange) {
		super(text, lemma, partOfSpeech.pos, grammaticalCase, grammaticalNumber, caseChange);
		this.partOfSpeech = partOfSpeech;
	}
		
	public OmorfiPartOfSpeech getOmorfiPartOfSpeech() {
		return partOfSpeech;
	}
	
	@Override
	protected List<String> getDebugStringParts() {
		List<String> parts = super.getDebugStringParts();
		parts.add(partOfSpeech.toString());
		return parts;
	}
}
