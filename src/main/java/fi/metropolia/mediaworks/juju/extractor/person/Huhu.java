/*******************************************************************************
 * Copyright (c) 2013 Olli Alm / Metropolia www.metropolia.fi
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
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
import org.apache.tika.language.LanguageIdentifier;

import com.google.common.base.Charsets;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import com.google.common.io.Files;
import com.myml.gexp.chunker.Chunk;
import com.myml.gexp.chunker.Chunker;
import com.myml.gexp.chunker.Chunkers;
import com.myml.gexp.chunker.TextWithChunks;
import com.myml.gexp.chunker.common.GraphExpChunker;
import com.myml.gexp.chunker.common.typedef.GraphUtils;
import com.myml.gexp.chunker.common.util.RegExpUtils;
import com.myml.gexp.graph.matcher.GraphRegExp;

import fi.metropolia.mediaworks.juju.extractor.person.fi.FiLemmatizer;

public class Huhu {
	private static final Logger log = Logger.getLogger(Huhu.class);
	
	public static final String MALE_FILE = Huhu.class.getResource("/name/male.name").getFile();
	public static final String FEMALE_FILE = Huhu.class.getResource("/name/female.name").getFile();
	public static final String SURNAME_FILE = Huhu.class.getResource("/name/sur.name").getFile();

	public static final String MALE_REGEXP;
	public static final String FEMALE_REGEXP;
	private static final String NAME_REGEXP;
	private static final String SURNAME_REGEXP;
	
	private static final List<String> MALE_NAMES;
	private static final List<String> FEMALE_NAMES;
	private static final List<String> SURNAMES;
	
	private Multiset<String> result;
	
	private StringBuilder taggedDoc; //store doc with names tagged 
	
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
		
		NAME_REGEXP = "(" + Huhu.MALE_REGEXP + ")|(" + Huhu.FEMALE_REGEXP + ")";
		
		SURNAMES = capitalize(surnames);
		SURNAME_REGEXP = RegExpUtils.convertListToRegexp(true, SURNAMES.toArray(new String[0]));
	}
	
	public Huhu() {
		result = TreeMultiset.create(); // result set init
		taggedDoc = new StringBuilder();
	}
	
	private static List<String> capitalize(List<String> input) {
		for (int i = 0; i < input.size(); i++) {
			input.set(i, WordUtils.capitalize(input.get(i)));
		}
		return input;
	}
	
	/**
	 * --> empty result set 
	 * 
	 */
	public void clearResults() {
		this.result = TreeMultiset.create();
	}

	public Multiset<String> apply(String text, String lang) {
		
		Chunker personChunker;
		
		if(lang.equals("fi")) {
			personChunker = NameChunker.createFinnishChunker();
		} else {
			personChunker = NameChunker.createEnglishChunker();
		}
		
		TextWithChunks twc = new TextWithChunks(text);
		
		Collection<Chunk> chunks = personChunker.chunk(twc);
		int i = 0;
		int pointer = 0;
		
		for (Chunk ch : chunks) {
			String chunk = ch.getContent();
			log.info("CHUNK "+ i+": "+ ch.type+": "+ch.start+", " +ch.end+": "+chunk);
			if (ch.type.equals("person")) {
				taggedDoc.append(text.substring(pointer, ch.start)); // prev text
				taggedDoc.append("<name>"+chunk+"</name>");
				pointer = ch.end;
				
				result.add(chunk);
				
			} 
			i++;
		}
		taggedDoc.append(text.substring(pointer));
				
		
		return result;
	}
	/**
	 * return the first occurence matching, for testing purposes
	 * @param text
	 * @return
	 */
	public String applyOne(String text, String lang) {
		try {
			return FiLemmatizer.apply(this.apply(text, lang).elementSet().iterator().next()); // with name normalizer
		} catch(NoSuchElementException e) {
			return null;
		}
	}
	
	public String getTaggedDocument() {
		return this.taggedDoc.toString();
	}
	
	public Multiset<String> normalizeResult(ArrayList<Multiset<String>> result) {
		Multiset<String> normalized = TreeMultiset.create();
		
		for (Multiset<String> set : result) {
			String shortest = null;
			for (String s : set) {
				if (shortest == null || s.length() < shortest.length()) {
					shortest = s;
				}
			}
			
			if (shortest != null) {
				if (shortest.matches(SURNAME_REGEXP)) {
					normalized.add(shortest,set.size());
				} else {
					String[] parts = shortest.split("\\s");
					String[] outParts = new String[parts.length];
					for (int i = 0; i<parts.length; i++) {
						log.debug("part "+i+": "+parts[i]);
						if (i == parts.length-1) {
//							log.debug("Apply lemmatizer: "+parts[i]);
							outParts[i] = FiLemmatizer.apply(parts[i]);
						} else {
//							log.debug("No lemmatizer "+parts[i]);
							outParts[i] = parts[i];
						}
					}
					normalized.add(StringUtils.join(outParts, " "),set.size());
				}
			}
		}
		
		return normalized;
	}

}
