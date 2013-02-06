package fi.metropolia.mediaworks.juju.extractor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import fi.metropolia.mediaworks.juju.document.PosToken;
import fi.metropolia.mediaworks.juju.document.Token;

public class Gram extends ArrayList<Token> {
	private static final long serialVersionUID = 4715001846044015112L;

	private String matchString; // TODO: matchString

	public Gram(List<Token> tokens) {
		super(tokens);
		
		List<String> words = Lists.newArrayList();
		for (Token t : tokens) {
			if (t instanceof PosToken) {
				words.add(((PosToken)t).getLemma());
			} else {
				words.add(t.getText());	
			}
		}
		matchString = StringUtils.join(words, " ");
	}

	public Token firstToken() {
		return get(0);
	}

	public Token lastToken() {
		return get(size() - 1);
	}

	@Override
	public String toString() {
		return StringUtils.join(this, " ");
	}

	public String getMatchString() {
		return matchString;
	}
}
