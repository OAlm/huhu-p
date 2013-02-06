package fi.metropolia.mediaworks.juju.syntax.parser;

import java.net.URL;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.tika.Tika;

import fi.metropolia.mediaworks.juju.document.Document;

public class DocumentBuilder {	
	public static final IDocumentParser EMPTY_PARSER = new IDocumentParser() {
		@Override
		public Document parseDocument(String content) {
			return new Document();
		}
		
		@Override
		public List<String> getSupportedLanguages() {
			return null;
		}
		
		@Override
		public int getPriority() {
			return -1;
		}
	};
	
	private static ServiceLoader<IDocumentParser> documentParserLoader = ServiceLoader.load(IDocumentParser.class);
	
	public static Document parseDocument(String content, String language) {
		return getBuilder(language).parseDocument(content);
	}
	
	public static IDocumentParser getBuilder(String language) {
		IDocumentParser selected = EMPTY_PARSER;
		
		for (IDocumentParser dp : documentParserLoader) {
			if ((dp.getSupportedLanguages() == null || dp.getSupportedLanguages().contains(language)) && dp.getPriority() > selected.getPriority()) {
				selected = dp;
			}
		}
		
		return selected;
	}
	
	public static void main(String[] args) throws Exception {
		Tika t = new Tika();
		String text = t.parseToString(new URL("http://www.kansallisbiografia.fi/kb/artikkeli/1408/"));
//		String text = "Koirissa on toivoa";
		Document d = parseDocument(text, "fi");
		d.print();
	}
}
