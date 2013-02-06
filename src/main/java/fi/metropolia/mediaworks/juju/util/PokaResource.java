/**
 * Yksittäisen löydetyn resurssin tiedon tallettava container
 */

package fi.metropolia.mediaworks.juju.util;

import java.util.ArrayList;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public class PokaResource {

	private String uri; // toisteista tietoa, eikai haitanne?
	private int count; // frekvenssilaskuri

	private ArrayList<WordLocationContainer> location;

	public PokaResource(String uri) {
		this.uri = uri;
		this.count = 1;
		this.location = new ArrayList<WordLocationContainer>();
	}

	public String getUri() {
		return this.uri;
	}

	public void add() {
		this.count += 1;
	}

	public int getCount() {
		return this.count;
	}

	public void addLocation(int sentence, int token) {
		location.add(new WordLocationContainer(sentence, token));
	}

	public void addLocation(int sentence, int startToken, int endToken) {
		location.add(new WordLocationContainer(sentence, startToken, endToken));
	}
	public ArrayList<WordLocationContainer> getLocations() {
		return this.location;
	}

	public Multiset<Integer> getDistribution() {
		Multiset<Integer> out = TreeMultiset.create();
		for (WordLocationContainer wl : location) {
			out.add(wl.getSentence());			
		}
		return out;
	}
}