package fi.metropolia.mediaworks.juju.service;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import fi.metropolia.mediaworks.juju.corpus.Corpus;
import fi.metropolia.mediaworks.juju.corpus.CorpusGenerator;
import fi.metropolia.mediaworks.juju.data.DataItem;
import fi.metropolia.mediaworks.juju.document.Document;
import fi.metropolia.mediaworks.juju.extractor.Grams;
import fi.metropolia.mediaworks.juju.extractor.keyphrase.KeyphraseExtractor;
import fi.metropolia.mediaworks.juju.syntax.parser.DocumentBuilder;

public class KeyphraseService {
	public static Map<DataItem, Map<Grams, Double>> getDocumentGroupKeyphrases(List<DataItem> documents) {
		Corpus c = CorpusGenerator.generateCorpus(documents);
		KeyphraseExtractor ke = new KeyphraseExtractor(c);
		
		Map<DataItem, Map<Grams, Double>> result = Maps.newLinkedHashMap();
		
		for (DataItem d : documents) {
			Document doc = DocumentBuilder.parseDocument(d.getText(), "fi");
			result.put(d, ke.process(doc));
		}
		
		return result;
	}
	
	public static Map<Grams, Double> getKeyphrases(String document) {
		Document d = DocumentBuilder.parseDocument(document, "fi");
		KeyphraseExtractor ke = new KeyphraseExtractor();
		return ke.process(d);
	}
}
