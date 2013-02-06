package fi.metropolia.mediaworks.juju.extractor.person;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Ranges;
import com.google.common.io.Files;
import com.myml.gexp.chunker.common.util.RegExpUtils;

import fi.metropolia.mediaworks.juju.Juju;
import fi.metropolia.mediaworks.juju.document.Document;
import fi.metropolia.mediaworks.juju.extractor.Gram;
import fi.metropolia.mediaworks.juju.extractor.GramExtractor;
import fi.metropolia.mediaworks.juju.extractor.keyphrase.filter.CaseFilter;
import fi.metropolia.mediaworks.juju.extractor.keyphrase.filter.LengthFilter;
import fi.metropolia.mediaworks.juju.extractor.keyphrase.filter.TextLengthFilter;
import fi.metropolia.mediaworks.juju.extractor.keyphrase.filter.WordFilter;
import fi.metropolia.mediaworks.juju.syntax.parser.DocumentBuilder;
import fi.metropolia.mediaworks.juju.util.StringUtils;

public class PersonExtractor {
	private static final String MALE_FILE = Juju.class.getResource("/name/male.name").getFile();
	private static final String FEMALE_FILE = Juju.class.getResource("/name/female.name").getFile();
	private static final String SURNAME_FILE = Juju.class.getResource("/name/sur.name").getFile();

	public static final String MALE_REGEXP;
	public static final String FEMALE_REGEXP;
	public static final String NAME_REGEXP;
	public static final String SURNAME_REGEXP;
	
	private static final List<String> MALE_NAMES;
	private static final List<String> FEMALE_NAMES;
	private static final List<String> SURNAMES;
	
	static {
		MALE_NAMES = Lists.newArrayList();
		FEMALE_NAMES = Lists.newArrayList();
		SURNAMES = Lists.newArrayList();
		
		try {
			MALE_NAMES.addAll(Files.readLines(new File(MALE_FILE), Charsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FEMALE_NAMES.addAll(Files.readLines(new File(FEMALE_FILE), Charsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			SURNAMES.addAll(Files.readLines(new File(SURNAME_FILE), Charsets.UTF_8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		StringUtils.capitalizeCollection(MALE_NAMES);
		StringUtils.capitalizeCollection(FEMALE_NAMES);
		StringUtils.capitalizeCollection(SURNAMES);
		
		MALE_REGEXP = RegExpUtils.convertListToRegexp(true, MALE_NAMES.toArray(new String[0]));
		FEMALE_REGEXP = RegExpUtils.convertListToRegexp(true, FEMALE_NAMES.toArray(new String[0]));
		SURNAME_REGEXP = RegExpUtils.convertListToRegexp(true, SURNAMES.toArray(new String[0]));
		
		NAME_REGEXP = "(" + MALE_REGEXP + ")|(" + FEMALE_REGEXP + ")";
	}
	
	private final Document document;
	
	public PersonExtractor(Document document) {
		this.document = document;
	}
	
	public void process() {
		GramExtractor ge = new GramExtractor(document, 1, 3);
		System.out.println("LengthFilter...");
		ge.filter(new LengthFilter(Ranges.closed(2, 2)));
		System.out.println("TextLengthFilter...");
		ge.filter(new TextLengthFilter(Ranges.atLeast(3)));
		System.out.println("WordFilter...");
		ge.filter(new WordFilter());
		System.out.println("CaseFilter...");
		ge.filter(new CaseFilter(true));
		System.out.println("NameFilter...");
		ge.filter(new NameFilter());
		System.out.println(ge.getGrams());
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException, TikaException {
		Tika tika = new Tika();

		String text = tika.parseToString(new URL("http://fi.wikipedia.org/w/index.php?title=Espoo&printable=yes"));
		Document doc = DocumentBuilder.parseDocument(text, "fi");

		PersonExtractor pe = new PersonExtractor(doc);
		pe.process();
	}
	
	public static class NameFilter implements Predicate<Gram> {
		@Override
		public boolean apply(Gram gram) {
			if (gram.get(0).getText().matches(NAME_REGEXP)) {
				return true;
			}
			
			return false;
		}
	}
}
