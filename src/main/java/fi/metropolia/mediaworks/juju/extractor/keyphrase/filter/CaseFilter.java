package fi.metropolia.mediaworks.juju.extractor.keyphrase.filter;

import com.google.common.base.Predicate;

import fi.metropolia.mediaworks.juju.document.Token;
import fi.metropolia.mediaworks.juju.extractor.Gram;

public class CaseFilter implements Predicate<Gram> {
	private boolean uppercase;
	
	public CaseFilter(boolean uppercase) {
		this.uppercase = uppercase;
	}
	
	@Override
	public boolean apply(Gram input) {
		for (Token t : input) {
			if (t.getCaseChange().upperCase != uppercase) {
				return false;
			}
		}
		return true;
	}

}
