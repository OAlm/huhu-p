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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.SortedSet;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;

import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

public class Example {
	public static void main(String[] args) throws MalformedURLException, IOException, TikaException {
		
		Tika tika = new Tika();
		String text = tika.parseToString(new URL("http://www.kansallisbiografia.fi/kb/artikkeli/2816/"));
		text = tika.parseToString(new URL("http://en.wikipedia.org/wiki/Winston_Churchill"));
		
	
		Huhu p = new Huhu();
		Multiset<String> results = p.apply(text);
//		System.out.println("res:" +results);
		
		LanguageIdentifier l = new LanguageIdentifier(text);
		
		if(l.getLanguage().equals("fi")) {
			//group similar names
			SortedSet<SortedSet<String>> grouped = StringSim.groupSimilarStrings(results, 0.7);
			System.out.println("gr:" +grouped);
			
			// normalize surnames (Finnish only! Remove for English)
			Multiset<String> normalized = p.normalizeResult(grouped);
//			System.out.println("Ordered alphabetically: " + normalized);
			System.out.println("Ordered by frequency: "+ Multisets.copyHighestCountFirst(normalized));

		} else {
			System.out.println(Multisets.copyHighestCountFirst(results));
		}
		
			
		
	}
}

