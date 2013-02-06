package fi.metropolia.mediaworks.juju.extractor.keyphrase.outputfilter;

import java.util.Map;
import java.util.Map.Entry;

import fi.metropolia.mediaworks.juju.extractor.Grams;
import fi.metropolia.mediaworks.juju.util.ValueSortedMap;

public class CapCountOutputFilter implements IOutputFilter {
	private final int limit;
	
	public CapCountOutputFilter(int limit) {
		this.limit = limit;
	}
	
	@Override
	public Map<Grams, Double> filter(Map<Grams, Double> input) {
		Map<Grams, Double> result = new ValueSortedMap<Grams, Double>(true);
		
		int counter = 0;
		for (Entry<Grams, Double> e : input.entrySet()) {
			counter++;
			
			if (counter > limit) {
				break;
			}
			
			result.put(e.getKey(), e.getValue());
		}

		return result;
	}

}
