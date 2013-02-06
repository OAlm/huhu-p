package fi.metropolia.mediaworks.juju.rle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Set;

import fi.metropolia.mediaworks.juju.util.NumberStringComparator;

public class LabelCollector {
	private TreeMap<String, TreeMap<String, List<String>>> map = new TreeMap<String, TreeMap<String,List<String>>>();
	private static final Comparator<String> comparator = new NumberStringComparator();
	
	private TreeMap<String, List<String>> getLanguage(String language, boolean create) {
		TreeMap<String, List<String>> l = map.get(language);
		
		if (create && l == null) {
			l = new TreeMap<String, List<String>>(comparator);
			map.put(language, l);
		}
		
		return l;
	}
	
	private List<String> getConcept(String language, String concept, boolean create) {
		List<String> s = null;
		TreeMap<String, List<String>> l = getLanguage(language, create);
		
		if (l != null) {
			s = l.get(concept);
			if (create && s == null) {
				s = new ArrayList<String>();
				l.put(concept, s);
			}
		}
		
		return s;
	}
	
	public void addLabel(String concept, String language, String label) {
		label = label.trim();
		
		if (label.length() > 0 && language.length() > 0) {
			getConcept(language, concept, true).add(label);
		}
	}
	
	public Set<String> getLanguages() {
		return map.keySet();
	}
	
	public Set<Entry<String, List<String>>> getConcepts(String language) {
		TreeMap<String, List<String>> l = getLanguage(language, false);
		if (l != null) {
			return l.entrySet();
		}
		return null;
	}
}