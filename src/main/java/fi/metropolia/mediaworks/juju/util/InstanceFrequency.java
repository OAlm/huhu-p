package fi.metropolia.mediaworks.juju.util;
/**
 * Tallentaa urin nimen ja kyseisen luokan esiintymien määrän dokumentissa
 * @author alm
 *
 */

public class InstanceFrequency implements Comparable<InstanceFrequency> {

	String uri;
	int freq;
	
	public InstanceFrequency(String uri, int freq) {
		this.uri = uri;
		this.freq = freq;
	}

	public int getFreq() {
		return this.freq;
	}
	
	@Override
	public int compareTo(InstanceFrequency another) {
		if (another != null) {
			int anotherFreq = another.getFreq();  
		    return this.freq - anotherFreq;
		} else {
			return 0;
		}
	}
	
	public String getUri() {
		return this.uri; 
	}
	
//	public String toString() {
//		return this.uri+": "+this.freq;
//	}
	//modified version for web api
	@Override
	public String toString() {
		return this.uri;
	}
}
