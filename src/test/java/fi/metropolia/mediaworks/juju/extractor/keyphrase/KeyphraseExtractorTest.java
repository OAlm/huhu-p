package fi.metropolia.mediaworks.juju.extractor.keyphrase;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import fi.metropolia.mediaworks.juju.document.Document;
import fi.metropolia.mediaworks.juju.syntax.parser.DocumentBuilder;

public class KeyphraseExtractorTest {
	@Test
	public void processShouldNotThrowException() {
		Document doc = DocumentBuilder.parseDocument("trololoo", "fi");
		KeyphraseExtractor target = new KeyphraseExtractor();
		assertNotNull(target.process(doc));
	}
}
