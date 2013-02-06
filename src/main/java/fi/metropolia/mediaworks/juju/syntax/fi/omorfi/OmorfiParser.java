package fi.metropolia.mediaworks.juju.syntax.fi.omorfi;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.hfst.NoTokenizationException;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.collect.Lists;

import fi.metropolia.mediaworks.juju.document.CaseChange;
import fi.metropolia.mediaworks.juju.document.Document;
import fi.metropolia.mediaworks.juju.document.GrammaticalCase;
import fi.metropolia.mediaworks.juju.document.GrammaticalNumber;
import fi.metropolia.mediaworks.juju.document.OmorfiToken;
import fi.metropolia.mediaworks.juju.document.Sentence;
import fi.metropolia.mediaworks.juju.syntax.parser.IDocumentParser;

public class OmorfiParser implements IDocumentParser {
	private static final List<String> SUPPORTED_LANGUAGES = Lists.newArrayList("fi"); 
	
	private static final Pattern CASE_PATTERN = Pattern.compile("\\[CASE=([^\\]]*)\\]");
	private static final Pattern NUM_PATTERN = Pattern.compile("\\[NUM=([^\\]]*)\\]");
	private static final Pattern POS_PATTERN = Pattern.compile("\\[POS=([^\\]]*)\\]");
	private static final Pattern SUBCAT_PATTERN = Pattern.compile("\\[SUBCAT=([^\\]]*)\\]");
	private static final Pattern LEMMA_PATTERN = Pattern.compile("\\[LEMMA='([^\\]]*)'\\]");
	private static final Pattern CASECHANGE_PATTERN = Pattern.compile("\\[CASECHANGE=([^\\]]*)\\]");
	private static final Pattern SYMBOL_PATTERN = Pattern.compile("^\\W*$");
	private static final String BOUNDARY_SENTENCE = "[BOUNDARY=SENTENCE]";

	private Lemmatizer lemmatizer = null;
	
	private Lemmatizer getLemmatizer() {
		if (lemmatizer == null) {
			lemmatizer = new Lemmatizer();
		}
		return lemmatizer;
	}
	
	private synchronized List<String> getTokens(String text) {
		List<String> tokens = Lists.newArrayList();

		try {
			tokens = getLemmatizer().lemmaWithOriginal(text);
		} catch (NoTokenizationException e) {
			e.printStackTrace();
		}

		return tokens;
	}

	private static GrammaticalCase getTokenCase(String token) {
		Matcher m = CASE_PATTERN.matcher(token);
		if (m.find()) {
			String caseTag = m.group(1);
			return GrammaticalCase.getWithTag(caseTag);
		}
		return null;
	}

	private static GrammaticalNumber getTokenNum(String token) {
		Matcher m = NUM_PATTERN.matcher(token);
		if (m.find()) {
			String numTag = m.group(1);
			return GrammaticalNumber.getByTag(numTag);
		}
		return null;
	}
	
	private static String getTokenPos(String token) {
		Matcher m = POS_PATTERN.matcher(token);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}
	
	private static String getTokenSubcat(String token) {
		Matcher m = SUBCAT_PATTERN.matcher(token);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	private static OmorfiPartOfSpeech getTokenPartOfSpeech(String token) {
		String pos = getTokenPos(token);
		String subcat = getTokenSubcat(token);
		return OmorfiPartOfSpeech.getWithPosAndSubcat(pos, subcat);
	}

	private static String getLemma(String token) {
		Matcher m = LEMMA_PATTERN.matcher(token);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	private static CaseChange getCaseChange(String token) {
		Matcher m = CASECHANGE_PATTERN.matcher(token);
		if (m.find()) {
			String caseChange = m.group(1);
			return CaseChange.getWithTag(caseChange);
		}
		return null;
	}

	private static boolean checkSymbol(String text) {
		Matcher m = SYMBOL_PATTERN.matcher(text);
		return m.find();
	}

	@Override
	public Document parseDocument(String content) {
		Document document = new Document();
		Sentence sentence = null;

		for (String token : getTokens(content)) {
			if (sentence == null) {
				sentence = new Sentence();
				document.add(sentence);
			}

			String text;
			OmorfiPartOfSpeech tokenPos = OmorfiPartOfSpeech.UNIDENTIFIED;
			GrammaticalCase tokenCase = null;
			GrammaticalNumber tokenNumber = null;
			CaseChange tokenCaseChange = null;
			String lemma = null;
			boolean changeSentence = false;

			if (!token.contains(Lemmatizer.NORESULT)) {
				text = token.substring(0, token.indexOf('[')).trim();
				tokenPos = getTokenPartOfSpeech(token);
				tokenNumber = getTokenNum(token);
				tokenCase = getTokenCase(token);
				tokenCaseChange = getCaseChange(token);

				if (token.contains(BOUNDARY_SENTENCE)) {
					changeSentence = true;
				}

				lemma = getLemma(token);
			} else {
				text = token.replace(Lemmatizer.NORESULT, "").trim();
				text = StringEscapeUtils.unescapeHtml4(text);
					
				if (checkSymbol(text)) {
					tokenPos = OmorfiPartOfSpeech.PUNCTUATION;
				}
			}
			
			if (text.length() > 0) {
				if (tokenPos == OmorfiPartOfSpeech.PROPER_NOUN && tokenCaseChange != null && !tokenCaseChange.upperCase) {
					// Proper nouns have CaseChange.NONE (why?)
					tokenCaseChange = CaseChange.UP_FIRST;
				}
				
				OmorfiToken t = new OmorfiToken(text, lemma, tokenPos, tokenCase, tokenNumber, tokenCaseChange);
				sentence.add(t);
			}

			if (changeSentence) {
				sentence = null;
			}
		}

		return document;
	}

	@Override
	public List<String> getSupportedLanguages() {
		return SUPPORTED_LANGUAGES;
	}
	
	@Override
	public int getPriority() {
		return 1;
	}
}
