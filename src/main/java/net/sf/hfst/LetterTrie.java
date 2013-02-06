package net.sf.hfst;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Simplified implementation 
 * http://sourceforge.net/p/hfst/code/2894/tree/trunk/hfst-optimized-lookup/hfst-optimized-lookup-java/src/net/sf/hfst/LetterTrie.java
 */
public class LetterTrie {
	private Map<String, Integer> map = Maps.newHashMap();

	public LetterTrie() {

	}

	public void addString(String str, int symbolNumber) {
		map.put(str, symbolNumber);
	}

	public List<Integer> findKeys(String string) {
		List<Integer> r = Lists.newArrayList();

		for (int i = 0; i < string.length(); i++) {
			r.add(findKey(Character.toString(string.charAt(i))));
		}

		return r;
	}

	public Integer findKey(String c) {
		if (map.containsKey(c)) {
			return map.get(c);
		} else {
			return HfstOptimizedLookup.NO_SYMBOL_NUMBER;
		}
	}
}
