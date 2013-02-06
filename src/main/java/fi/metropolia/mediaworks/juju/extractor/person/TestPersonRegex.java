package fi.metropolia.mediaworks.juju.extractor.person;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fi.metropolia.mediaworks.juju.extractor.person.PersonRegex;

public class TestPersonRegex {

	private PersonRegex r;
	
	@Before
	public void setUp() {
		r = new PersonRegex();
	}
	
	@Test
	public void testApply() {
		
		assertEquals(r.applyOne("Koira"), null);
		assertEquals(r.applyOne("Adolf Hitler"), "Adolf Hitler");
		assertEquals(r.applyOne("Ninki Nonk"), null);
		assertEquals(r.applyOne("Toni Spännäristä"), "Toni Spännäri");
		assertEquals(r.applyOne("Olli Almilta"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almille"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almilla"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almista"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almiin"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almilta"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almissa"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almiksi"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almina"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almia"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almiin"), "Olli Alm");
		assertEquals(r.applyOne("Olli Almin"), "Olli Alm");

	}
	
}
