package fi.metropolia.mediaworks.juju.extractor.person;

import static com.myml.gexp.chunker.common.GraphExpChunker.mark;
import static com.myml.gexp.chunker.common.GraphExpChunker.match;
import static com.myml.gexp.graph.matcher.GraphRegExpMatchers.or;
import static com.myml.gexp.graph.matcher.GraphRegExpMatchers.seq;
import static com.myml.gexp.graph.matcher.GraphRegExpMatchers.opt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.myml.gexp.chunker.Chunk;
import com.myml.gexp.chunker.Chunker;
import com.myml.gexp.chunker.Chunkers;
import com.myml.gexp.chunker.TextWithChunks;
import com.myml.gexp.chunker.common.GraphExpChunker;
import com.myml.gexp.chunker.common.typedef.GraphUtils;
import com.myml.gexp.chunker.common.util.RegExpUtils;
import com.myml.gexp.graph.matcher.GraphRegExp;

public class PersonRegex {
	private static final Logger log = Logger.getLogger(PersonRegex.class);
	
	public static final String MALE_FILE = PersonRegex.class.getResource("/name/male.name").getFile();
	public static final String FEMALE_FILE = PersonRegex.class.getResource("/name/female.name").getFile();
	public static final String SURNAME_FILE = PersonRegex.class.getResource("/name/sur.name").getFile();

	private static final String MALE_REGEXP;
	private static final String FEMALE_REGEXP;
	private static final String NAME_REGEXP;
	private static final String SURNAME_REGEXP;
	
	private static final List<String> MALE_NAMES;
	private static final List<String> FEMALE_NAMES;
	private static final List<String> SURNAMES;
	
	private SortedSet<String> result;
	
	static {
		List<String> male;
		List<String> female;
		List<String> surnames;
		try {
			male = Files.readLines(new File(MALE_FILE), Charsets.UTF_8);
		} catch (Exception e) {
			male = ImmutableList.of();
		}
		try {
			female = Files.readLines(new File(FEMALE_FILE), Charsets.UTF_8);
		} catch (Exception e) {
			female = ImmutableList.of();
		}
		try {
			surnames = Files.readLines(new File(SURNAME_FILE), Charsets.UTF_8);
		} catch (Exception e) {
			surnames = ImmutableList.of();
		}
		
		MALE_NAMES = capitalize(male);
		MALE_REGEXP = RegExpUtils.convertListToRegexp(true, MALE_NAMES.toArray(new String[0]));

		FEMALE_NAMES = capitalize(female);
		FEMALE_REGEXP = RegExpUtils.convertListToRegexp(true, FEMALE_NAMES.toArray(new String[0]));
		
		NAME_REGEXP = "(" + MALE_REGEXP + ")|(" + FEMALE_REGEXP + ")";
		
		SURNAMES = capitalize(surnames);
		SURNAME_REGEXP = RegExpUtils.convertListToRegexp(true, SURNAMES.toArray(new String[0]));
	}
	
	public PersonRegex() {
		result = new TreeSet<String>(); // result set init
	}
	
	private static List<String> capitalize(List<String> input) {
		for (int i = 0; i < input.size(); i++) {
			input.set(i, WordUtils.capitalize(input.get(i)));
		}
		return input;
	}

	public Chunker createPersonChunker() {
		GraphRegExp.Matcher token = match("token");

		//TODO: negative lookahead? miten kirjoitetaan sääntö joka tarkistaa että merkkijonoa ennen ei saa esiintyä
		// isolla kirjoitettu sana?
		
		//todo: kaksiosaiset sukunimet, esim. Amalia Perez Diaz?
		
		GraphRegExp.Matcher firstName = GraphUtils.regexp("^(" + MALE_REGEXP + "|" + FEMALE_REGEXP + ")(-(" + MALE_REGEXP + "|" + FEMALE_REGEXP + "))?$", token);
		GraphRegExp.Matcher firstNameParenthesis = GraphUtils.regexp("^\\((" + MALE_REGEXP + "|" + FEMALE_REGEXP + ")\\)$", token);// Lauri (Lassi) Yrjönpoika Nummi, needed?
		GraphRegExp.Matcher mid1 = GraphUtils.regexp("(af|al|mac|von|van|van|d'|D'|af|da|de|the|of|Of|De)", token); // fix case insensitive this row 
		GraphRegExp.Matcher mid2 = GraphUtils.regexp("(der|den)", token);
		GraphRegExp.Matcher capitalizedWord = GraphUtils.regexp("^\\p{Lu}'?\\p{L}+(-\\p{Lu}\\p{L}+)?$", token);
		GraphRegExp.Matcher capDash = GraphUtils.regexp("^\\p{Lu}\\p{L}+$", token);
		GraphRegExp.Matcher letter = GraphUtils.regexp("^\\p{Upper}\\.$", token);
		GraphRegExp.Matcher noName = GraphUtils.regexp("^(?!(" + MALE_REGEXP + "|" + FEMALE_REGEXP + "))", token);
				//--> negative lookeahead: tarkoitus olla hyväksymättä nimiä joita edeltää joku nimen kaltainen ei-nimisana
		
// 		--> OLD, to be removed soon		
//		Chunker chunker = Chunkers.pipeline(
//				Chunkers.regexp("token", "(\\p{L}+|[^\\s]+|\\n|)"), // p{L}+| non-whitespace+ | \n    
//				new GraphExpChunker(null, seq( mark("person", or(
//						seq(firstName,  opt(firstName), opt(firstName),opt(mid1), opt(mid2), capitalizedWord)
//						//seq(opt(noFirstName), firstName, opt(firstName),opt(firstNameParenthesis), opt(firstName), opt(midPart), capitalizedWord)
//				))))
//		);
		
		
		Chunker chunker = Chunkers.pipeline(
				Chunkers.regexp("token", "\\p{L}+-?\\p{L}+|[^\\s]+|\\n|"), // --> TODO: check this token. Person-regex have to first pass token-pattern. Why [^\s]+? and \n 
				new GraphExpChunker(null, seq( mark("person", or(
						seq(firstName,  opt(or(firstName,letter)), opt(firstName),opt(mid1), opt(mid2), capitalizedWord)
				))))
		);
		

		return chunker;
	}
	
	/**
	 * --> empty result set 
	 */
	public void clearResults() {
		this.result = new TreeSet<String>();
	}

	public SortedSet<String> apply(String text) {
		Chunker personChunker = createPersonChunker();
		
		TextWithChunks twc = new TextWithChunks(text);
		
		Collection<Chunk> chunks = personChunker.chunk(twc);
		
		for (Chunk ch : chunks) {
//			log.debug("CHUNK: "+ ch.type+": "+text.substring(ch.start,ch.end));
			if (ch.type.equals("person")) {
//				log.debug("CHUNK: "+ ch.type+": "+text.substring(ch.start,ch.end));
				result.add(ch.getContent());
			}
		}
		
		return result;
	}
	/**
	 * return the first occurence matching, for testing purposes
	 * @param text
	 * @return
	 */
	public String applyOne(String text) {
		try {
			return this.suffixStripper(this.apply(text).first()); // with name normalizer
		} catch(NoSuchElementException e) {
			return null;
		}
	}
	
	private SortedSet<String> normalizeResult(SortedSet<SortedSet<String>> result) {
		SortedSet<String> normalized = new TreeSet<String>();
		
		for (SortedSet<String> set : result) {
			String shortest = null;
			for (String s : set) {
				if (shortest == null || s.length() < shortest.length()) {
					shortest = s;
				}
			}
			
			if (shortest != null) {
				if (shortest.matches(SURNAME_REGEXP)) {
					normalized.add(shortest);
				} else {
					String[] parts = shortest.split("\\s");
					String[] outParts = new String[parts.length];
					for (int i = 0; i<parts.length; i++) {
						if (!parts[i].matches(NAME_REGEXP) && (i == 0 || i == parts.length-1)) {
							outParts[i] = suffixStripper(parts[i]);
						} else {
							outParts[i] = parts[i];
						}
					}
					normalized.add(StringUtils.join(outParts, " "));
				}
			}
		}
		
		return normalized;
	}

	/*
	 * sijamuodot:  1. nominatiivi (alm, almit), 2. akkusatiivi ([he näkivät] almin, almit) , 3. genetiivi (almin, almien), 
	 * 4. partitiivi (almia, almeja), 5. essiivi (almina, almeina), 6. translatiivi (almiksi, almeiksi), 7. inessiivi (almissa, almeissa), 
	 * 8. illatiivi (almiin, almeihin), 9. elatiivi (almista, almeista), 10. adessiivi (almilla, almeilla), 11. allatiivi (almille, almeille), 
	 * 12. ablatiivi (almilta, almeilta), 13. abessiivi (almitta, almeitta), 14. instruktiivi (almin, almein), 
	 * 15. [paljain jaloin] ja 16. komitatiivi (almeineen). + 17. eksessiivi
	 * --> huom. taivutusluokan käsite on toinen: tietty luokka käyttäytyy samalla tavalla
	 * 
	 * 
	 * 1. nom	-   , -t
	 * 2. akk	-in , -it
	 * 3. gen	-in , -ien
	 * 4. par	-ia , -eja
	 * 5. ess   -ina, -eina
	 * 6. tra	-ksi, -eiksi
	 * 7. ine	-ssa, -issa
	 * 8. ill	-iin, -eihin
	 * 9. ela	-sta, -eista
	 * 10. ade	-lla, -eilla
	 * 11. all	-lle, -eille
	 * 12. abl	-lta, -eilta
	 * 13. abe 	-tta, -eitta [EI ESIINNY]
	 * 14. ins	-min, -mein [EI ESIINNY]
	 * 15. kom  -eineen [EI ESIINNY]
	 */
	
	private String suffixStripper(String s) { //TODO: monikko ei esiinny nimissä?
		log.debug("parsing '"+s+"'");
		if (s.matches(".*lt[aä]")) { // 12. ABL
			log.debug("ABLATIVE");
			if(s.matches(".*ilt[aä]")) {
				return s.substring(0, s.length() - 4);
			}
			return s.substring(0, s.length() - 3);
			
		} else if (s.matches(".*lle")) { // 11. ALL
			log.debug("ALLATIVE");
			if(s.matches(".*ille")) {
				return s.substring(0, s.length() - 4);
			} else if(s.matches(".*kselle")) {
				return s.substring(0, s.length() - 6)+"s"; // Paciukselle --> Pacius
			} else if(s.matches(".*selle")) { // Räsäselle, Järviselle
				return s.substring(0, s.length() - 5)+"nen";
			} else {
				return s.substring(0, s.length() - 3);
			}
		} else if (s.matches(".*lla")) { // 10. ADE
			log.debug("ADESSIVE");
			if(s.matches(".*illa")) {
				return s.substring(0, s.length() - 4);
			} else if(s.matches(".*ghella")) { // garghella
				return s;
			} else {
				return s.substring(0, s.length() - 3);
			}
		} else if (s.matches(".*st[aä]")) { // 9. ELA
			log.debug("ELATIVE");
			if(s.matches(".*rist[aä]")) {
				return s.substring(0, s.length() - 3);
			} else if(s.matches(".*ist[aä]")) {
				return s.substring(0, s.length() - 4);
			} 
			return s.substring(0, s.length() - 3);
			
		} else if (s.matches(".*(aa|ii)n")) { // 8 ILL
			log.debug("ILLATIVE");
			
			return s.substring(0, s.length() - 3);
		} else if (s.matches(".*ss[aä]")) { // 7. INE
			log.debug("INESSIVE");
			
			if(s.matches(".*iss[aä]")) {
				return s.substring(0, s.length() - 4);
			}
			return s.substring(0, s.length() - 3);
		} else if (s.matches(".*ksi")) { // 6. TRA
			log.debug("TRANSLATIVE");
			
			if(s.matches(".*iksi")) {
				return s.substring(0, s.length() - 4);
			}
			return s.substring(0, s.length() - 3);
		} else if (s.matches(".*in[aä]")) { // 5. ESS
			log.debug("ESSIVE");
			return s.substring(0, s.length() - 3);
		} else if (s.matches(".*ia")) { // 4. PAR
			log.debug("PARTITIVE");
			if (s.matches(".*(ria)")) { // lontoon
				return s;
			}
			return s.substring(0, s.length() - 2);
		} else if (s.matches(".*ien")) { //3. GEN PL [EI ESIINNY?]
			log.debug("GENETIVE PLURAL");
			return s.substring(0, s.length() - 2);
		} else if (s.matches(".*n")) { //3. GEN SG, 2. AKK SG
			log.debug("GENETIVE, ACCUSATIVE");
			
			if (s.matches(".*(con|oon)")) { // lontoon
				return s.substring(0, s.length() - 1);
			} else if (s.matches(".*non")) {
				return s.substring(0, s.length() - 1);
			} else if (s.matches(".*nen")) { // manninen
					return s;
			} else if (s.matches(".*ian")) {
				return s.substring(0, s.length() - 1);
			} else if (s.matches(".*[(nn)(ström)(tt)hlnr]in")) { // Rennerin --> Renner (vrt alla *in --> *i), Arteronin --> Arteron, Scottin, Pavlovitshin
				return s.substring(0, s.length() - 2);
			} else if (s.matches(".*laisen")) {
				return s.substring(0, s.length() - 6) + "lainen";
			} else if (s.matches(".*ren")) { //wahren --> wahren
				return s;
			} else if(s.matches(".*[aeiouö]sen")) { // järvisen, ylösen, kelasen 
				return s.substring(0, s.length() - 3) + "nen";
			} else if (s.matches(".*(xon|oln|sen|son|hn|ean|pton|ron|upin|illan|green|stein)")) { //henriksson, henriksen, john, ocean, frampton, cuaron, lupin, mcmillan
				return s;
			} else if (s.matches(".*man")) { //snellman --> snellman, ocean
				return s;
			} else if (s.matches(".*hden")) { //lehden --> lehti
				return s.substring(0, s.length()-3)+"ti";
			} else if(s.matches(".*en")) { //Kiven --> Kivi
			    return s.substring(0, s.length()-2)+"i";
			} else if(s.matches(".*(lan|eyn|donan)")) { //Vesalan, Weasleyn, Maradonan
			    return s.substring(0, s.length()-1);
			} else if(s.matches(".*[aiäö]n")) {
				return s.substring(0, s.length() - 1);
			} else {
				return s.substring(0, s.length() - 2);
			}
		} else {
			log.debug("--> no match");
		}
		
		/* else if (s.matches(".*t")) { // 2. AKK PL, 1. NOM PL --> ei esiinny?
			return s.substring(0, s.length() - 2);
		} */

		return s;
	}

	/* wikipedia extract test from main
	  
	  		File f = new File("/home/alm/Documents/names2.list");
		Scanner s = new Scanner(f);	
		
		PersonRegex p = new PersonRegex();
		int min = 1000;
		int max = 2000;
		int loop = 0;
		int fullnames = 0;
		int notFound = 0;
		
		TreeSet<String> notfoundNames = new TreeSet<String>();
		
		while(s.hasNextLine()) {
			loop++;
			String name = s.nextLine().trim();			
			if(loop < min) {
				continue;
			}

			SortedSet<String> results = p.apply(name);
			if(results.isEmpty()) {
				if(name.indexOf(" ")!= -1) { 
//					System.out.println("Not found: '"+name+"'");
					notfoundNames.add(name);
					notFound++;
				}
			} else {
//				System.out.println("Found: '"+name+"'");
				fullnames++;
			}
			if(loop>max) {
				break;
			}
		}
		System.out.println("Notfound: ");
		for(String ss: notfoundNames) {
			System.out.println(ss);
		}
		System.out.println("names total: "+loop+", fullnames: "+fullnames+", notFound fullnames: "+notFound);
		
		s.close();
	 */
	
	/*
	 * URLs from file, to ease up testing
	 */
	public static ArrayList<String> getURLs() {
		ArrayList<String> res = new ArrayList<String>();
		
		try {
			Scanner s = new Scanner(new File("urls.txt"));
			while(s.hasNextLine()) {
				res.add(s.nextLine());
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException, TikaException {		
		Logger.getLogger(PersonRegex.class).setLevel(Level.DEBUG);
		Tika tika = new Tika();

//		SortedSet<String> results = new TreeSet<String>();
		
		// TODO: loop URLs here from file
		
		String text = tika.parseToString(new URL("http://www.kansallisbiografia.fi/kb/artikkeli/2816/"));
//		text = "Toni Spännärin, Tuomas Kyrön, Eero Anhavan, Sisko Jaskaran";
		PersonRegex p = new PersonRegex();
		SortedSet<String> results = p.apply(text);
		
//		System.out.println(results.size());
		System.out.println("Original                : "+results);
		
		//fi: uncorrected + normalize
		
		SortedSet<SortedSet<String>> uncorrected = StringSim.groupSimilarStrings(results, 0.7);
		//System.out.println(uncorrected.size());
		System.out.println(uncorrected);
		SortedSet<String> normalized = p.normalizeResult(uncorrected);
//		System.out.println(normalized.size());
		System.out.println("Normalized              : "+normalized);
//		
		normalized.removeAll(results);
//		System.out.println("Changed in normalization: "+normalized);
	}

}
