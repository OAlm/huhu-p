package fi.metropolia.mediaworks.juju.label;

import fi.metropolia.mediaworks.juju.extractor.Gram;

public class GramLabel extends LabelMaker<Gram> {

	@Override
	public boolean canHandle(Object object) {
		return object instanceof Gram;
	}

	@Override
	public String createLabel(Gram gram) {
		return gram.toString();
	}

}
