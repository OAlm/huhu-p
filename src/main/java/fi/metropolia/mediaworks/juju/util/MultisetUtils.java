package fi.metropolia.mediaworks.juju.util;

import java.util.HashMap;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Multisets;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class MultisetUtils {
	public static <A> Multiset.Entry<A> getItem(Multiset<A> multiset, int index) {
		return Iterables.get(multiset.entrySet(), index);
	}
	
	public static <K> HashMap<K, Integer> multisetToHashMap(Multiset<K> multiset) {
		HashMap<K, Integer> out = new HashMap<K, Integer>();
		
		for (Multiset.Entry<K> e : multiset.entrySet()) {
			out.put(e.getElement(), e.getCount());
		}
		
		return out;
	}
	
	public static <A extends Comparable<A>>Range<A> getRange(Multiset<A> set) {
		A minObj = null;
		A maxObj = null;
		
		for (Entry<A> e : set.entrySet()) {
			A obj = e.getElement();
			if (minObj == null || (obj.compareTo(minObj)<0)) {
				minObj = obj;
			}
			if (maxObj == null || (obj.compareTo(maxObj)>0)) {
				maxObj = obj;
			}
		}
		
		return Ranges.closed(minObj, maxObj);
	}
	
	public static <A> Multiset<A> filterByCount(Multiset<A> multiset, Range<Integer> range) {
		ImmutableMultiset<A> c = Multisets.copyHighestCountFirst(multiset);
		
		LinkedHashMultiset<A> o = LinkedHashMultiset.create();
		
		for (Multiset.Entry<A> e : c.entrySet()) {
			if (range.contains(e.getCount())) {
				o.add(e.getElement(), e.getCount());
			}
		}
		
		return Multisets.unmodifiableMultiset(o);
	}
}
