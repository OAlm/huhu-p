package fi.metropolia.mediaworks.juju.extractor.keyphrase.filter;

import java.util.HashSet;
import java.util.Scanner;

import com.google.common.base.Predicate;

import fi.metropolia.mediaworks.juju.document.PosToken;
import fi.metropolia.mediaworks.juju.document.Token;
import fi.metropolia.mediaworks.juju.extractor.Gram;

public class StopwordFilter implements Predicate<Gram> {
	HashSet<String> stopwords;

	public StopwordFilter() {
		init();
	}

	private void init() {
		stopwords = new HashSet<String>();

		Scanner s;

		s = new Scanner(getClass().getResourceAsStream("/omorfi/stopwords_fi.txt"));
		while (s.hasNextLine()) {
			stopwords.add(s.nextLine());
		}
		s.close();
	}

	@Override
	public boolean apply(Gram gram) {
		for (Token t : gram) {
			if (isStopword(t)) {
				return false;
			}
		}
		return true;
	}

	public boolean isStopword(Token token) {
		String word;
		if (token instanceof PosToken) {
			word = ((PosToken) token).getLemma();
		} else {
			word = token.getText();
		}
		return stopwords.contains(word);
	}
}
