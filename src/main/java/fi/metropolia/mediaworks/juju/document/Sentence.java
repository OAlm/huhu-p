package fi.metropolia.mediaworks.juju.document;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class Sentence extends AbstractList<Token> implements Serializable {
	private static final long serialVersionUID = 1L;

	private Document document;
	
	private final List<Token> tokens = Lists.newArrayList();
	
	@Override
	public void add(int index, Token element) {
		if (element != null) {
			element.setSentence(this);
		}
		tokens.add(index, element);
	}
	
	@Override
	public Token get(int index) {
		return tokens.get(index);
	}
	
	public Document getDocument() {
		return document;
	}

	@Override
	public Token remove(int index) {
		Token t = tokens.remove(index);
		if (t != null && t.getSentence() == this) {
			t.setSentence(null);
		}
		return t;
	}

	@Override
	public Token set(int index, Token element) {
		element.setSentence(this);
		Token t = tokens.set(index, element);
		if (t != null && t.getSentence() == this) {
			t.setSentence(null);
		}
		return t;
	}
	
	void setDocument(Document document) {
		this.document = document;
	}
	
	@Override
	public int size() {
		return tokens.size();
	}
	
	@Override
	public String toString() {
		return StringUtils.join(tokens, " ");
	}
	
	public int getIndex() {
		if (document != null) {
			return document.indexOf(this);
		} else {
			return 0;
		}
	}
	
	public void print() {
		List<String> words = Lists.newArrayList();
		for (Token t : this) {
			words.add(t.toDebugString());
		}
		System.out.println(StringUtils.join(words, " "));
	}
}
