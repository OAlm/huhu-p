package fi.metropolia.mediaworks.juju.rle;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.Statement;

public class CustomSelector implements Selector {
	private String check;
	
	public CustomSelector(String check) {
		this.check = check;
	}
	
	@Override
	public RDFNode getObject() {
		return null;
	}

	@Override
	public Property getPredicate() {
		return null;
	}

	@Override
	public Resource getSubject() {
		return null;
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public boolean test(Statement s) {
		return s.getPredicate().getLocalName().equals(check);
	}

}
