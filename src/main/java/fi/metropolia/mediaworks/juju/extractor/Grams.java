package fi.metropolia.mediaworks.juju.extractor;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.ImmutableSet;

import fi.metropolia.mediaworks.juju.label.LabelMaker;

public class Grams extends HashSet<Gram> implements Comparable<Grams>  {
	private static final long serialVersionUID = -1674999660582160406L;
	private String matchString;
	private String displayString;
	
	public Grams(Collection<Gram> grams) {
		super(ImmutableSet.copyOf(grams));
		
		Gram firstGram = iterator().next();
		
		matchString = firstGram.getMatchString();
		displayString = LabelMaker.getLabel(firstGram);
	}
	
	@Override
	public int compareTo(Grams o) {
		return matchString.compareTo(o.matchString);
	}
	
	public String getMatchString() {
		return matchString;
	}
	
	@Override
	public String toString() {
		return displayString;
	}
}
