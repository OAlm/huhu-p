package fi.metropolia.mediaworks.juju.syntax.fi.omorfi;

import org.junit.Test;

import fi.metropolia.mediaworks.juju.document.Document;

public class OmorfiParserTest {
	// TODO: tests :)
	
	@Test
	public void testCases() {
		OmorfiParser p = new OmorfiParser();
		Document d = p.parseDocument("vilkas");
		System.out.println(d);
	}
}
