package fi.metropolia.mediaworks.juju.extractor.keyphrase.outputfilter;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Range;

import fi.metropolia.mediaworks.juju.extractor.Grams;
import fi.metropolia.mediaworks.juju.util.ValueSortedMap;

public class ThresholdOutputFilter implements IOutputFilter {
	private Range<Double> range;
	
	public ThresholdOutputFilter(Range<Double> range) {
		this.range = range;
	}
	
	public void setRange(Range<Double> range) {
		this.range = range;
	}
	
	@Override
	public Map<Grams, Double> filter(Map<Grams, Double> input) {
		Map<Grams, Double> result = new ValueSortedMap<Grams, Double>(true);
		
		for (Entry<Grams, Double> e : input.entrySet()) {
			if (range.contains(e.getValue())) {
				result.put(e.getKey(), e.getValue());
			}
		}
		
		return result;
	}
	
}
