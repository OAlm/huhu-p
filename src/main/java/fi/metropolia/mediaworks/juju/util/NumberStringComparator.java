package fi.metropolia.mediaworks.juju.util;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class NumberStringComparator implements Comparator<String> {
	private static final Logger log = Logger.getLogger(NumberStringComparator.class);
	
	Pattern p = Pattern.compile("(\\d{1,})");
	
	@Override
	public int compare(String o1, String o2) {
		Matcher m1 = p.matcher(o1);
		Matcher m2 = p.matcher(o2);
		
		if (m1.find() && m2.find()) {
			Integer i1 = new Integer(Integer.parseInt(m1.group()));
			Integer i2 = new Integer(Integer.parseInt(m2.group()));
			
			return i1.compareTo(i2);
		} else {
			log.warn(String.format("Could not find number in both strings: %s, %s",o1, o2));
			return 0;
		}
	}
}
