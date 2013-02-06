package fi.metropolia.mediaworks.juju.label;

import java.util.ServiceLoader;

import org.apache.log4j.Logger;

import fi.metropolia.mediaworks.juju.document.Document;
import fi.metropolia.mediaworks.juju.extractor.Gram;
import fi.metropolia.mediaworks.juju.syntax.parser.DocumentBuilder;

abstract public class LabelMaker<A> {
	private static final Logger log = Logger.getLogger(LabelMaker.class);
	
	private static final LabelMaker<Object> FALLBACK = new LabelMaker<Object>() {
		@Override
		public boolean canHandle(Object object) {
			return true;
		}
		
		@Override
		public String createLabel(Object object) {
			return object.toString();
		}
	};
	
	@SuppressWarnings("rawtypes")
	private static final ServiceLoader<LabelMaker> labelMakerLoader = ServiceLoader.load(LabelMaker.class);
	
	public static String getLabel(Object object) {
		int priority = Integer.MIN_VALUE;
		
		LabelMaker<Object> selectedLM = null;
		
		for (LabelMaker<Object> lm : labelMakerLoader) {
			if (lm.getPriority() > priority && lm.canHandle(object)) {
				selectedLM = lm;
				priority = lm.getPriority();
			}
		}
		
		if (selectedLM == null) {
			selectedLM = FALLBACK;
		}

		String label = selectedLM.createLabel(object); 
		log.debug(String.format("Found label \"%s\" using \"%s\"", label, selectedLM));
		return label;
	}
	
	abstract public boolean canHandle(Object object);
		
	abstract public String createLabel(A object);
	
	/**
	 * Label priority
	 * 
	 * @return Priority
	 */
	public int getPriority() {
		return 0;
	}
	
	public static void main(String[] args) {
//		String text = "arkkitehtuurin ja yhteiskunnan";
		String text = "maailman parhaan pelaajan";
		Document d = DocumentBuilder.parseDocument(text, "fi");
		Gram g = new Gram(d.get(0));
		System.out.println(getLabel(g));
	}
}
