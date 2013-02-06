package fi.metropolia.mediaworks.juju.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import fi.metropolia.mediaworks.juju.persistence.dbtrie.TrieIndex;

public class Vocabulary {
	private TrieIndex trie;
	private Map<String, String[]> labels;
	
	public Vocabulary(File file) throws Exception {
		trie = new TrieIndex(file, true);
		
		labels = new TreeMap<String, String[]>();
		
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(fis), "UTF-8"));
		
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(":", 2);
				if (parts.length == 2) {
					String key = parts[0].trim();
					String[] props = parts[1].trim().split("[ ]*,[ ]*");
					labels.put(key, props);
				}
			}
			br.close();
		} catch (Exception e) {
			throw new Exception("Could not parse file!", e);
		}
	}
	
	public int size() {
		return labels.keySet().size();
	}
	
	public TrieIndex getTrie() {
		return trie;
	}
	
	public String getLabels(String id) {
		String[] l = labels.get(id);
		if (l != null) {
			return StringUtils.join(l, ", ");
		} else {
			return "NOT FOUND";
		}
	}
	
	public String getLabel(String id) {
		return this.getLabel(id, 0);
	}
	
	public String getLabel(String id, int index) {
		String[] l = labels.get(id);
		if (l != null) {
			int i = Math.min(index, l.length-1);
			return l[i];
		} else {
			return "NOT FOUND";
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		//example
		String path = "C://data/bench/ontology/";
		Vocabulary v = new Vocabulary(new File(path+"magazine-labels.fi"));
		System.out.println(v.getLabel("m00006"));
		
	
		
	}
}
