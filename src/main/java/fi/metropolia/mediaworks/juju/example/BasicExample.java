package fi.metropolia.mediaworks.juju.example;

import java.util.Map;

import fi.metropolia.mediaworks.juju.document.Document;
import fi.metropolia.mediaworks.juju.extractor.Grams;
import fi.metropolia.mediaworks.juju.extractor.keyphrase.KeyphraseExtractor;
import fi.metropolia.mediaworks.juju.syntax.parser.DocumentBuilder;

public class BasicExample {	
	public static void main(String[] args) {
		Document d = DocumentBuilder.parseDocument("Hieno dokumentti tässä on. Tässä dokumentissä on monta sanaa.", "fi");
		d.print();
		
		KeyphraseExtractor ke = new KeyphraseExtractor();
		Map<Grams, Double> result = ke.process(d);
		
		System.out.println(result);
	}
}
