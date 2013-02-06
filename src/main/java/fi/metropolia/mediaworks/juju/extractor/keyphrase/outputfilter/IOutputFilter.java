package fi.metropolia.mediaworks.juju.extractor.keyphrase.outputfilter;

import java.util.Map;

import fi.metropolia.mediaworks.juju.extractor.Grams;

public interface IOutputFilter {
	public Map<Grams, Double> filter(Map<Grams, Double> input);
}
