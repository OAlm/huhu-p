package fi.metropolia.mediaworks.juju.label;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import fi.metropolia.mediaworks.juju.document.GrammaticalCase;
import fi.metropolia.mediaworks.juju.document.OmorfiToken;
import fi.metropolia.mediaworks.juju.document.Token;
import fi.metropolia.mediaworks.juju.extractor.Gram;

public class NormalizedOmorfiGramLabel extends OmorfiGramLabel {
	@Override
	public boolean canHandle(Object object) {
		if (super.canHandle(object)) {
			Gram gram = (Gram)object;
			if (gram.size() > 1) {
				if (gram.size() == 3 && gram.get(1).getText().equalsIgnoreCase("ja")) {
					return false;
				}
				for (Token t : gram) {
					OmorfiToken ot = (OmorfiToken)t;
					if (isGenitive(ot)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public int getPriority() {
		return super.getPriority() + 1;
	}
	
	private boolean isGenitive(OmorfiToken t) {
		if (t.getGrammaticalCase() == GrammaticalCase.GENITIVE || t.getText().endsWith(":n")) {
			return true;
		}
		return false;
	}

	@Override
	public String createLabel(Gram gram) {
		ArrayList<String> words = Lists.newArrayListWithCapacity(gram.size());

		Iterator<Token> itr = gram.iterator();
		
		while(itr.hasNext()) {
			OmorfiToken ot = (OmorfiToken)itr.next();
			if (itr.hasNext() && isGenitive(ot)) {
				words.add(ot.getText());
			} else {
				words.add(ot.getLemma());
			}
		}
		
		return StringUtils.join(words, " ").trim();
	}
}
