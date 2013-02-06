package fi.metropolia.mediaworks.juju.extractor.keyphrase.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.Range;

import fi.metropolia.mediaworks.juju.extractor.Gram;

public class LengthFilter implements Predicate<Gram> {
	private Range<Integer> length;
	
	public LengthFilter(Range<Integer> length) {
		this.length = length;
	}
	
	@Override
	public boolean apply(Gram gram) {
		return length.contains(gram.size());
	}

}
