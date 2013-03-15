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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.SortedSet;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;

import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import fi.metropolia.mediaworks.juju.util.Tools;

public class Example {
	public static void main(String[] args) throws Exception {
		
		Tika tika = new Tika();
		String text = tika.parseToString(new URL("http://www.kansallisbiografia.fi/kb/artikkeli/3101/"));
		
		Huhu p = new Huhu();
		LanguageIdentifier l = new LanguageIdentifier(text);
		System.out.println("LANG: " + l.getLanguage());
		
		if(l.getLanguage().equals("fi")) { // special treatment for Finnish only!
			Multiset<String> results = p.apply(text, "fi");
//			System.out.println("HIGHCOUNT: " +Multisets.copyHighestCountFirst(results));
			
			//group similar names, inflected forms of surnames will combined by string suffix similarity
			ArrayList<Multiset<String>> grouped = StringSim.groupSimilarStrings(results, 0.7);
//			System.out.println("GROUPED RESULTS:" +grouped);
			
			// normalize surnames
			Multiset<String> normalized = p.normalizeResult(grouped);
//			System.out.println("Ordered alphabetically: " + normalized);
			System.out.println("Ordered by frequency: "+ Multisets.copyHighestCountFirst(normalized));
			
		} else {
			Multiset<String> results = p.apply(text, "en");
			System.out.println(Multisets.copyHighestCountFirst(results));
		}
		
//		System.out.println("DOCUMENT: "+p.getTaggedDocument());
		
	}
}

