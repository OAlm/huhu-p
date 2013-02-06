package fi.metropolia.mediaworks.juju.document;

import java.util.Iterator;

import static org.junit.Assert.*;

import org.junit.Test;

public class DocumentTest {
	
	@Test
	public void testTokenIterator() {
		Sentence s;
		
		Document d = new Document();
		s = new Sentence();
		d.add(s);
		Token t11 = new Token("This");
		s.add(t11);
		Token t12 = new Token("is");
		s.add(t12);
		Token t13 = new Token("test");
		s.add(t13);
		Token t14 = new Token(".");
		s.add(t14);
		
		s = new Sentence();
		d.add(s);
		Token t21 = new Token("This");
		s.add(t21);
		Token t22 = new Token("is");
		s.add(t22);
		Token t23 = new Token("a");
		s.add(t23);
		Token t24 = new Token("another");
		s.add(t24);
		Token t25 = new Token("sentence");
		s.add(t25);
		Token t26 = new Token("!");
		s.add(t26);
		
		Iterator<Token> itr = d.tokenIterator();
		assertEquals(t11, itr.next());
		assertTrue(itr.hasNext());
		assertEquals(t12, itr.next());
		assertTrue(itr.hasNext());
		assertEquals(t13, itr.next());
		assertTrue(itr.hasNext());
		assertEquals(t14, itr.next());
		assertTrue(itr.hasNext());
		
		assertEquals(t21, itr.next());
		assertTrue(itr.hasNext());
		assertEquals(t22, itr.next());
		assertTrue(itr.hasNext());
		assertEquals(t23, itr.next());
		assertTrue(itr.hasNext());
		assertEquals(t24, itr.next());
		assertTrue(itr.hasNext());
		assertEquals(t25, itr.next());
		assertTrue(itr.hasNext());
		assertEquals(t26, itr.next());
		assertFalse(itr.hasNext());
	}
}
