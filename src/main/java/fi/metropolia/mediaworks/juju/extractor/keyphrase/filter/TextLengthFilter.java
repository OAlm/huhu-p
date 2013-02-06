package fi.metropolia.mediaworks.juju.extractor.keyphrase.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.Range;

import fi.metropolia.mediaworks.juju.extractor.Gram;

/**
 * Filter grams by their (total) String length:
 * Gram 'hello world', length (string): 11
 * 
 * e.g. 'range > 3' --> apply returns true if gram length over 3 chars. 
 * @author mertoniemi, alm
 *
 */
public class TextLengthFilter implements Predicate<Gram> {
	private Range<Integer> length;
	
	public TextLengthFilter(Range<Integer> length) {
		this.length = length;
	}
	
	@Override
	public boolean apply(Gram g) {
		return length.contains(g.toString().length());
	}
	
}
