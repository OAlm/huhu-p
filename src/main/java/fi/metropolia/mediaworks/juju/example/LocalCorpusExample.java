package fi.metropolia.mediaworks.juju.example;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tika.Tika;

import com.google.common.collect.Lists;

import fi.metropolia.mediaworks.juju.data.DataItem;
import fi.metropolia.mediaworks.juju.extractor.Grams;
import fi.metropolia.mediaworks.juju.service.KeyphraseService;

public class LocalCorpusExample {
	public static void main(String[] args) {
		String[] ids = {
			"1408",
			"4222",
			"3108",
			"3101",
			"1888",
			"1532",
			"4497",
			"4828",
			"7599"
		};
		
		Tika t = new Tika();
		
		List<DataItem> documents = Lists.newArrayList();
		
		for (String id : ids) {
			try {
				String text = t.parseToString(new URL(String.format("http://www.kansallisbiografia.fi/kb/artikkeli/%s/", id)));
				documents.add(new DataItem(id, "", text));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Map<DataItem, Map<Grams, Double>> r = KeyphraseService.getDocumentGroupKeyphrases(documents);
		
		for (Entry<DataItem, Map<Grams, Double>> e : r.entrySet()) {
			System.out.println(e.getKey());
			System.out.println("Local corpus: " + KeyphraseService.getKeyphrases(e.getKey().getText()));
			System.out.println("Normal: " + e.getValue());
			System.out.println();
		}
	}
}
