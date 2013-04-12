package fi.metropolia.mediaworks.juju.extractor.person;

import static com.myml.gexp.chunker.common.GraphExpChunker.mark;
import static com.myml.gexp.chunker.common.GraphExpChunker.match;
import static com.myml.gexp.graph.matcher.GraphRegExpMatchers.opt;
import static com.myml.gexp.graph.matcher.GraphRegExpMatchers.or;
import static com.myml.gexp.graph.matcher.GraphRegExpMatchers.seq;

import com.myml.gexp.chunker.Chunker;
import com.myml.gexp.chunker.Chunkers;
import com.myml.gexp.chunker.common.GraphExpChunker;
import com.myml.gexp.chunker.common.typedef.GraphUtils;
import com.myml.gexp.graph.matcher.GraphRegExp;

public class NameChunker {

	public static final String SURNAMECOMMONPART =  "^(\\p{Lu}'?\\p{L}+(-\\p{Lu}\\p{L}+)?|I)";
	public static final String TOKENPATTERN =  "\\p{L}+('|-)?\\p{L}+|\n|\\p{L}+|\\d+|\\p{Punct}";
	
	
	public static Chunker createFinnishChunker() {
		GraphRegExp.Matcher token = match("token");
		
		GraphRegExp.Matcher firstName = GraphUtils.regexp("^(" + Huhu.MALE_REGEXP + "|" + Huhu.FEMALE_REGEXP + ")(-(" + Huhu.MALE_REGEXP + "|" + Huhu.FEMALE_REGEXP + "))?$", token);
		GraphRegExp.Matcher mid1 = GraphUtils.regexp("(?i)(af|mac|von|van|van|d'|af|da|de|the|of)", token); // fix case insensitive this row 
		GraphRegExp.Matcher mid2 = GraphUtils.regexp("(der|den)", token);
		GraphRegExp.Matcher capitalizedWord = GraphUtils.regexp(SURNAMECOMMONPART+"(?<!ss[aä])$", token); //(?<!ssa --> not ending with -ssa (for Finnish!)
		GraphRegExp.Matcher letter = GraphUtils.regexp("^\\p{Upper}\\.$", token);		
		
		
		Chunker chunker = Chunkers.pipeline(
//				Chunkers.regexp("token", "\\p{L}+-?\\p{L}+|[^\\s]+|\\n|"), // --> TODO: check this token. Person-regex have to first pass token-pattern. Why [^\s]+? and \n
				
				//Chunkers.regexp("token", "(\\p{L}|-)+|\\p{Punct}|\n"),
				Chunkers.regexp("token", TOKENPATTERN),
				new GraphExpChunker(null, seq( mark("person", or(
						seq(firstName,  opt(or(firstName,letter)), opt(firstName),opt(mid1), opt(mid2), capitalizedWord)
				))))//.setDebugString(true)
		);
		

		return chunker;

	}
	
	public static Chunker createEnglishChunker() {
		GraphRegExp.Matcher token = match("token");
		
		GraphRegExp.Matcher firstName = GraphUtils.regexp("^(" + Huhu.MALE_REGEXP + "|" + Huhu.FEMALE_REGEXP + ")(-(" + Huhu.MALE_REGEXP + "|" + Huhu.FEMALE_REGEXP + "))?$", token);
		GraphRegExp.Matcher mid1 = GraphUtils.regexp("(af|al|mac|von|van|van|d'|D'|af|da|de|the|of|Of|De)", token); // fix case insensitive this row 
		GraphRegExp.Matcher mid2 = GraphUtils.regexp("(der|den)", token);
		GraphRegExp.Matcher capitalizedWord = GraphUtils.regexp(SURNAMECOMMONPART+"$", token); 
		GraphRegExp.Matcher letter = GraphUtils.regexp("^\\p{Upper}\\.$", token);		
		
		
		Chunker chunker = Chunkers.pipeline(
//				Chunkers.regexp("token", "\\p{L}+-?\\p{L}+|[^\\s]+|\\n|"), // --> TODO: check this token. Person-regex have to first pass token-pattern. Why [^\s]+? and \n 
				Chunkers.regexp("token", TOKENPATTERN),
				new GraphExpChunker(null, seq( mark("person", or(
						seq(firstName,  opt(or(firstName,letter)), opt(firstName),opt(mid1), opt(mid2), capitalizedWord)
				))))//.setDebugString(true)
		);
		

		return chunker;

	}
	
}
