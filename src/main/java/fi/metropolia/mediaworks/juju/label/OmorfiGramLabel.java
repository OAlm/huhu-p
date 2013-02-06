package fi.metropolia.mediaworks.juju.label;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import fi.metropolia.mediaworks.juju.document.OmorfiToken;
import fi.metropolia.mediaworks.juju.document.Token;
import fi.metropolia.mediaworks.juju.extractor.Gram;

public class OmorfiGramLabel extends GramLabel {
	@Override
	public int getPriority() {
		return super.getPriority() + 1;
	}
	
	@Override
	public boolean canHandle(Object object) {
		if (super.canHandle(object)) {
			for (Token t : (Gram)object) {
				if (!(t instanceof OmorfiToken)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String createLabel(Gram gram) {
		List<String> words = Lists.newArrayListWithCapacity(gram.size());
		
		for (Token t : gram) {
			if (t instanceof OmorfiToken) {
				OmorfiToken ot = (OmorfiToken)t;
				words.add(ot.getLemma());
			}
		}
		
		return StringUtils.join(words, " ").trim();
	}
}
