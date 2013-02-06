package fi.metropolia.mediaworks.juju.extractor.keyphrase.filter;

import com.google.common.base.Predicate;

import fi.metropolia.mediaworks.juju.document.PosToken;
import fi.metropolia.mediaworks.juju.document.PartOfSpeech;
import fi.metropolia.mediaworks.juju.document.Token;
import fi.metropolia.mediaworks.juju.extractor.Gram;

public class SmartFilter implements Predicate<Gram> {
	private static final String CHECK = "\\p{L}{1,}(-\\p{L}{1,})*";

	private boolean testToken(PosToken token) {
		return token.getPartOfSpeech() == PartOfSpeech.BASE || (token.getPartOfSpeech() == PartOfSpeech.UNIDENTIFIED && token.getLemma().matches(CHECK));
	}

	@Override
	public boolean apply(Gram gram) {
		Token first = gram.firstToken();
		Token last = gram.lastToken();

		if (first instanceof PosToken && last instanceof PosToken) {
			PosToken cFirst = (PosToken) first;
			PosToken cLast = (PosToken) last;

			if (testToken(cFirst) && testToken(cLast)) {
				int n = gram.size();
				if (n > 2) {
					for (int i = 1; i < n - 1; i++) {
						Token t = gram.get(i);
						
						if (t instanceof PosToken) {
							PosToken ct = (PosToken)t;
							
							if (ct.getPartOfSpeech() == PartOfSpeech.NUMERAL || ct.getPartOfSpeech() == PartOfSpeech.VERB) {
								return false;
							}

							if (!t.getText().matches(CHECK)) {
								return false;
							}	
						}
					}
				}
				return true;
			}
		}
		return false;
	}

}
