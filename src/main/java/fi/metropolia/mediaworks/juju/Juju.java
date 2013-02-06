package fi.metropolia.mediaworks.juju;

import java.net.URL;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import fi.metropolia.mediaworks.juju.corpus.Corpus;

public class Juju {	
	private static LoadingCache<String, Corpus> corpusCache = CacheBuilder.newBuilder()
		.maximumSize(5)
		.build(new CacheLoader<String, Corpus>() {
			@Override
			public Corpus load(String key) throws Exception {
				URL url = getClass().getResource(String.format("/corpus/%s.corpus", key));
				if (url != null) {
					return Corpus.load(url);
				} else {
					throw new Exception("Corpus not found!");
				}
			}
		});
	
	public static Corpus accessCorpus() {
		try {
			return corpusCache.get("wikipedia");
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new Corpus();
	}
	
	public static Corpus accessCorpus(String name) throws Exception {
		try {
			return corpusCache.get(name);
		} catch (ExecutionException e) {
			e.printStackTrace();
			throw new Exception(String.format("Could not access \"%s\" corpus!", name));
		}
	}
	
	public static void main(String[] args) throws Exception {
		Corpus c = accessCorpus();
		System.out.println(c);
	}
}
