package fi.metropolia.mediaworks.juju.weighting;

import fi.metropolia.mediaworks.juju.extractor.Gram;
import fi.metropolia.mediaworks.juju.extractor.Grams;

public class FirstOccurenceWeighter extends ItemWeighter<Grams> {

	@Override
	double process(Weighter<Grams> weighter, Grams object, double weight) {
		double first = Double.MAX_VALUE;
		
		for (Gram g : object) {
			int index = g.get(0).getSentence().getIndex();
			int sentences = g.get(0).getSentence().getDocument().size();
			
			double sp = (double)index / (double)sentences;
			
			if (sp < first) {
				first = sp;
			}
		}
		
		return weight * (1 - first);
	}

}
