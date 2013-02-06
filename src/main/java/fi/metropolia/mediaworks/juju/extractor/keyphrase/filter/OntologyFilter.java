package fi.metropolia.mediaworks.juju.extractor.keyphrase.filter;

import java.util.Set;

import com.google.common.base.Predicate;

import fi.metropolia.mediaworks.juju.document.Token;
import fi.metropolia.mediaworks.juju.extractor.Gram;
import fi.metropolia.mediaworks.juju.persistence.dbtrie.TrieInterface;
import fi.metropolia.mediaworks.juju.util.ResourceContainer;

// to replace totally ontology extractor: if term is contained in ontology, include it to results

public class OntologyFilter implements Predicate<Gram> {
	private TrieInterface vocabulary;
	private ResourceContainer concepts;

	public OntologyFilter(TrieInterface vocab) {
		this.vocabulary = vocab;
		concepts = new ResourceContainer();
	}

	@Override
	public boolean apply(Gram gram) {

		//NOTE, CHECK: vocabulary terms are ordered alphabetically, right?
		Set<String> uris = vocabulary.getIds(gram.getMatchString());
		if (uris != null) {
			for (Token t : gram) {
//				t.uris = new ArrayList<String>(uris);
				concepts.addURI(uris, t.getSentence().getIndex(), t.getIndex());
			}
			return true;
		}
		return false;

	}

	public ResourceContainer results() {
		return concepts;
	}
}
