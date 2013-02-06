package fi.metropolia.mediaworks.juju.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fi.metropolia.mediaworks.juju.document.Document;
import fi.metropolia.mediaworks.juju.document.Token;
import fi.metropolia.mediaworks.juju.extractor.keyphrase.filter.SmartFilter;

public class EntityExtractor {
	private Document document;

	public EntityExtractor(Document document) {
		this.document = document;
	}

	public List<Gram> getEntities() {
		List<Gram> o = new ArrayList<Gram>();
		
		GramExtractor ge = new GramExtractor(document);
		ge.findGrams(1, 1);

		ge.filter(new SmartFilter());

		Set<Grams> ggs = ge.getGroupedGrams();

		for (Grams gs : ggs) {
			boolean notFirstUpper = false;
			boolean allUpper = true;
				
			for (Gram g : gs) {
				Token t = g.firstToken();
				
				if (t.getIndex() > 0 && t.getCaseChange().upperCase) {
					notFirstUpper = true;
				} else if (!t.getCaseChange().upperCase){
					allUpper = false;
					break;
				}
			}

			if (notFirstUpper && allUpper) {
				o.addAll(gs);
			}
		}

		return o;
	}
}
