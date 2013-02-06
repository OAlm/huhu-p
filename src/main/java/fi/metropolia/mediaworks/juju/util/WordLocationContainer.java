package fi.metropolia.mediaworks.juju.util;

import java.util.ArrayList;

/**
 * Word location container, index based on the
 * sentence-token -pairs
 * @author alm
 *
 */

public class WordLocationContainer {
	private int sentence; //1-N
	private int firstToken; //0-N
	private int lastToken; // for multiword concepts. for one-word concepts, only firsttoken is used
	public WordLocationContainer(int sentence, int token) {
		this.sentence = sentence;
		this.firstToken = token;
		this.lastToken = -1;
	}
	
	public WordLocationContainer(int sentence, int first, int last) {
		this.sentence = sentence;
		this.firstToken = first;
		this.lastToken = last;
	}

	public int getSentence() {
		return this.sentence;				
	}
	
	public int getToken() {
		return this.firstToken;
	}
	
	/**
	 * use this if you want to check multiword-units
	 * @return
	 */
	public ArrayList<Integer> getTokens() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(firstToken);
		if(lastToken != -1) {
			result.add(lastToken);
		}
		return result;
	}
	
	@Override
	public String toString() {
		if(lastToken == -1) {
			return "["+sentence+", "+firstToken+"]";
		} else {
			return "["+sentence+", "+firstToken+"-"+lastToken+"]";
		}
	}
}

