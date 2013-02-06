package fi.metropolia.mediaworks.juju.util;

import static junit.framework.Assert.*;

import org.junit.Test;

public class LRUCacheTest {
	@Test
	public void testSimplePut() {
		LRUCache<String, String> cache = new LRUCache<String, String>(3);
		assertNull(cache.get("kissa"));
		cache.put("kissa", "koira");
		assertEquals("koira", cache.get("kissa"));
	}
	
	@Test
	public void testSizeLimit() {
		LRUCache<String, String> cache = new LRUCache<String, String>(3);
		cache.put("kissa", "jee");
		cache.put("koira", "jee");
		cache.put("mato", "jee");
		
		assertEquals("jee", cache.get("kissa"));
		cache.put("sika", "jee");
		assertNull(cache.get("koira"));
	}
}
