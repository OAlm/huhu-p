package fi.metropolia.mediaworks.juju.data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.Lists;

import fi.metropolia.mediaworks.juju.corpus.CorpusGenerator;

public class WikipediaDataSource extends DataSource {
	private WikipediaHandler handler;
	
	private static final Pattern REMOVE_CBRACES = Pattern.compile("\\{\\{ [^\\}\\{]* \\}\\}", Pattern.DOTALL + Pattern.COMMENTS);
	private static final Pattern REMOVE_IMAGES = Pattern.compile("\\[\\[  [^\\|\\]]* \\| [^\\|\\]]* \\| [^\\|\\]]* \\| [^\\]]*  \\]\\]", Pattern.COMMENTS + Pattern.CASE_INSENSITIVE);
	private static final Pattern REMOVE_LANGUAGE_LINKS = Pattern.compile("\\[\\[  [^\\:]*:[^\\]]* \\]\\]", Pattern.DOTALL + Pattern.COMMENTS);
	private static final Pattern LINK_PATTERN = Pattern.compile("\\[\\[  ([ ^\\]\\| ]*\\|)? ([^\\]]*) \\]\\]", Pattern.DOTALL + Pattern.COMMENTS);
	private static final Pattern REMOVE_LINKS = Pattern.compile("\\[ [^ \\] \\[ ]* \\]", Pattern.COMMENTS);
	private static final Pattern REMOVE_TAGS = Pattern.compile("< [^<>]* >", Pattern.COMMENTS);
	
	private List<DataItem> articles = Lists.newArrayListWithExpectedSize(600000);
	
	public WikipediaDataSource(File wikipediaDump) throws Exception {
		handler = new WikipediaHandler();
		SAXParser p = SAXParserFactory.newInstance().newSAXParser();
		System.out.println("Parsing...");
		p.parse(wikipediaDump, handler);
		System.out.println("Parsing done!");
	}
	
	@Override
	public DataItem get(int index) {
		return articles.get(index);
	}

	@Override
	public int size() {
		return articles.size();
	}

	public static void main(String[] args) throws Exception {
		File file = new File("/home/jonime/Data/wikipedia/fiwiki-20121113-pages-articles.xml");
		WikipediaDataSource ds = new WikipediaDataSource(file);
		CorpusGenerator cg = new CorpusGenerator(new File("wikipedia.corpus"), ds);
		cg.generateCorpus();
	}
	
	public static String cleanWikipediaText(String text) {
		String last = "";
		while (last.length() != text.length()) {
			last = text;
			text = REMOVE_CBRACES.matcher(text).replaceAll("");	
		}
		
		text = REMOVE_IMAGES.matcher(text).replaceAll("");
		text = REMOVE_LANGUAGE_LINKS.matcher(text).replaceAll("");
		text = LINK_PATTERN.matcher(text).replaceAll("$2");
		text = REMOVE_LINKS.matcher(text).replaceAll("");
		text = REMOVE_TAGS.matcher(text).replaceAll("");
		text = text.replaceAll("'", "");
		text = text.replaceAll("\"", "");
		
		return text.trim();
	}
	
	private class WikipediaHandler extends DefaultHandler {
		private LinkedList<String> path = Lists.newLinkedList();
		
		private StringBuilder content = new StringBuilder();
		private String id;
		private String title;
		private String text;
			
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			path.add(qName);
			if (qName.equals("page")) {
				title = null;
				id = null;
				text = null;
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			content.append(ch, start, length);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (path.size() >= 2) {
				if (path.get(path.size()-2).equals("page")) {
					if (path.getLast().equals("title")) {
						title = content.toString().trim();
					} else if (path.getLast().equals("id")) {
						id = content.toString().trim();
					}
				} else if (path.getLast().equals("text")) {
					text = cleanWikipediaText(content.toString().trim());
				} else if (path.getLast().equals("page")) {
					if (id != null && title != null && text != null) {
						articles.add(new DataItem(id, title, text));
					}
				}
			}
			
			path.removeLast();
			content.setLength(0);
		}
		
	}
}
