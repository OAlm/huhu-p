package fi.metropolia.mediaworks.juju.weighting;

import fi.metropolia.mediaworks.juju.corpus.Corpus;
import fi.metropolia.mediaworks.juju.extractor.Grams;

public class CorpusWeighter extends ItemWeighter<Grams> {
	private Corpus corpus;
	
	public Corpus getCorpus() {
		return corpus;
	}

	public CorpusWeighter(Corpus corpus) {
		this.corpus = corpus;
	}
	
	@Override
	public double process(Weighter<Grams> weighter, Grams object, double weight) {
		return corpus.getInverseTermDocFrequency(object.getMatchString()) * weight;
	}
}
