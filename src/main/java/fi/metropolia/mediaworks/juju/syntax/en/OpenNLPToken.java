package fi.metropolia.mediaworks.juju.syntax.en;

import java.util.List;

import fi.metropolia.mediaworks.juju.document.PosToken;

public class OpenNLPToken extends PosToken {
	private static final long serialVersionUID = 1L;
	
	private final OpenNLPPartOfSpeech pos;
	
	public OpenNLPToken(String text, OpenNLPPartOfSpeech pos) {
		super(text, null, pos.pos);
		this.pos = pos;
	}
	
	@Override
	protected List<String> getDebugStringParts() {
		List<String> parts = super.getDebugStringParts();
		parts.add(pos.toString());
		return parts;
	}
}
