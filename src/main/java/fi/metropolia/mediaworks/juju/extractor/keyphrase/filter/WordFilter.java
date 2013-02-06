package fi.metropolia.mediaworks.juju.extractor.keyphrase.filter;

import com.google.common.base.Predicate;

import fi.metropolia.mediaworks.juju.document.Token;
import fi.metropolia.mediaworks.juju.extractor.Gram;

public class WordFilter implements Predicate<Gram> {
	private static final String CHECK = "\\p{L}{1,}(-\\p{L}{1,})*"; 
	
	@Override
	public boolean apply(Gram input) {
		boolean ok = true;
		for (Token t : input) {
			if (!t.getText().matches(CHECK)) {
				ok = false;
				break;
			}
		}
		return ok;
	}
}
