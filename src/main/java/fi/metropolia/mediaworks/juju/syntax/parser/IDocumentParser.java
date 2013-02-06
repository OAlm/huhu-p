package fi.metropolia.mediaworks.juju.syntax.parser;

import java.util.List;

import fi.metropolia.mediaworks.juju.document.Document;

public interface IDocumentParser {
	public Document parseDocument(String content);
	public List<String> getSupportedLanguages();
	public int getPriority();
}
