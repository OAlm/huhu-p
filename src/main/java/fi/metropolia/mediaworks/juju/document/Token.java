package fi.metropolia.mediaworks.juju.document;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class Token implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String text;
	private final CaseChange caseChange;
	private Sentence sentence;

	public Token(String text) {
		this(text, null);
	}

	public Token(String text, CaseChange caseChange) {
		this.text = text;
		
		if (caseChange == null) {
			if (StringUtils.isAllUpperCase(text)) {
				this.caseChange = CaseChange.UP_ALL;
			} else if (text.length() > 0 && Character.isUpperCase(text.charAt(0))) {
				this.caseChange = CaseChange.UP_FIRST;
			} else {
				this.caseChange = CaseChange.NONE;
			}
		} else {
			this.caseChange = caseChange;
		}
	}

	public final String getText() {
		return text;
	}

	public final Sentence getSentence() {
		return sentence;
	}

	final void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	@Override
	public String toString() {
		return text;
	}

	public final int getIndex() {
		if (sentence != null) {
			return sentence.indexOf(this);
		}
		return 0;
	}

	public CaseChange getCaseChange() {
		return caseChange;
	}
	
	public final String toDebugString() {
		return String.format("%s (%s)", text, StringUtils.join(getDebugStringParts(), ", "));
	}
	
	protected List<String> getDebugStringParts() {
		return Lists.newArrayList(caseChange.toString());
	}
}
