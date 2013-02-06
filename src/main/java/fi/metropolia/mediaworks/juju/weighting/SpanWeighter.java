package fi.metropolia.mediaworks.juju.weighting;

import fi.metropolia.mediaworks.juju.extractor.Gram;
import fi.metropolia.mediaworks.juju.extractor.Grams;

public class SpanWeighter extends ItemWeighter<Grams> {

	@Override
	double process(Weighter<Grams> weighter, Grams object, double weight) {
		double start = Double.MAX_VALUE;
		double end = Double.MIN_VALUE;
		
		for (Gram g : object) {
			int index = g.get(0).getSentence().getIndex();
			int sentences = g.get(0).getSentence().getDocument().size();
			
			double sp = (double)index / (double)sentences;
			double ep = (double)index / (double)(sentences-1);
			
			if (sp < start) {
				start = sp;
			}
			if (ep > end) {
				end = ep;
			}
		}
		
		return weight * (end - start);
	}

}
