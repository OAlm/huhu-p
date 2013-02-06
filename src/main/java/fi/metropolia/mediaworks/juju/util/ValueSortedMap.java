package fi.metropolia.mediaworks.juju.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class ValueSortedMap<K, V extends Comparable<V>> implements Map<K, V> {
	private boolean descending;	
	private Map<K, V> map = new TreeMap<K, V>();
		
	public ValueSortedMap(boolean descending) {
		this.descending = descending;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	private final Comparator<Entry<K,V>> entrySetComparator = new Comparator<Entry<K,V>>() {
		@Override
		public int compare(Entry<K, V> o1, Entry<K, V> o2) {
			int c = o1.getValue().compareTo(o2.getValue());
			if (descending) {
				c = -c;
			}
			return c;
		}
	};
	
	@Override
	public Set<Entry<K, V>> entrySet() {
		List<Entry<K,V>> list = new LinkedList<Entry<K,V>>(map.entrySet());
		Collections.sort(list, entrySetComparator);
		
		return new LinkedHashSet<Entry<K,V>>(list);
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		Set<K> keys = new LinkedHashSet<K>();
		for (Entry<K,V> e : entrySet()) {
			keys.add(e.getKey());
		}
		return keys;
	}

	@Override
	public V put(K key, V value) {
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
	}

	@Override
	public V remove(Object key) {
		return map.remove(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<V> values() {
		Set<V> values = new LinkedHashSet<V>();
		for (Entry<K,V> e : entrySet()) {
			values.add(e.getValue());
		}
		return values;
	}
	
	@Override
	public String toString() {
		ArrayList<String> parts = new ArrayList<String>();
		
		for (Map.Entry<K, V> e : entrySet()) {
			parts.add(String.format("%s: %s", e.getKey(), e.getValue()));
		}
		
		return StringUtils.join(parts, ", ");
	}
}
