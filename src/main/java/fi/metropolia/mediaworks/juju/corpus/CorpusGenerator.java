package fi.metropolia.mediaworks.juju.corpus;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ranges;

import fi.metropolia.mediaworks.juju.data.DataItem;
import fi.metropolia.mediaworks.juju.data.DataSource;
import fi.metropolia.mediaworks.juju.document.Document;
import fi.metropolia.mediaworks.juju.extractor.Gram;
import fi.metropolia.mediaworks.juju.extractor.GramExtractor;
import fi.metropolia.mediaworks.juju.extractor.Grams;
import fi.metropolia.mediaworks.juju.extractor.keyphrase.filter.TextLengthFilter;
import fi.metropolia.mediaworks.juju.extractor.keyphrase.filter.WordFilter;
import fi.metropolia.mediaworks.juju.syntax.parser.DocumentBuilder;
import fi.metropolia.mediaworks.juju.util.Tools;

public class CorpusGenerator {
	public abstract static class CreateProgressCallback {
		/**
		 * @param corpus Corpus being created
		 */
		public void handleProcess(Corpus corpus) {
			// DO NOTHING :)
		}
		
		/**
		 * @param corpus Corpus which has been created
		 */
		public void handleEnd(Corpus corpus) {
			// DO NOTHING :)
		}
	}
	
	private static final Logger log = Logger.getLogger(CorpusGenerator.class);
	
	private Corpus corpus;
	private List<DataItem> dataSource;
	private Integer limit = null;
	private File file = null;
	private int documentsParsed = 0;
	private CreateProgressCallback cpCallback = null;
	private List<Predicate<Gram>> gramFilters;
	
	public static Corpus generateCorpus(List<DataItem> documents) {
		CorpusGenerator cg = new CorpusGenerator(documents);
		Corpus c = cg.generateCorpus();
		return c;
	}

	private CorpusGenerator(List<DataItem> dataSource) {
		this.dataSource = dataSource;
		
		gramFilters = Lists.newLinkedList();
		gramFilters.add(new TextLengthFilter(Ranges.atLeast(2)));
		gramFilters.add(new WordFilter());
	}
	
	public CorpusGenerator(DataSource dataSource, CreateProgressCallback cpCallback) {
		this(dataSource);
		this.cpCallback = cpCallback;
	}

	public CorpusGenerator(File file, DataSource dataSource) {
		this(dataSource);
		this.file = file;
	}
	
	public void setLimit(int value) {
		limit = value;
	}
	
	public int getLimit() {
		return limit;
	}

	public Corpus generateCorpus() {
		corpus = new Corpus();
		
		int n = 0;
		
		int processors = Runtime.getRuntime().availableProcessors();
		log.info(String.format("Using %d cores in document parsing", processors));
		ThreadPoolExecutor es = (ThreadPoolExecutor)Executors.newFixedThreadPool(processors);
		Iterator<DataItem> iter = dataSource.iterator();

		mainLoop: while (iter.hasNext()) {
			while (iter.hasNext() && es.getQueue().size() < 1000) {
				DataItem di = iter.next();
				es.submit(new ExtractTask(di));
				n++;
			
				if (limit != null && n >= limit) {
					break mainLoop;
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				log.debug("Interrupted!", e);
			}
		}
		
		es.shutdown();
		
		while (!es.isTerminated()) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				log.debug("Interrupted!", e);
			}
		}
		
		if (cpCallback != null) {
			cpCallback.handleEnd(corpus);
		}
		
		if (file != null) {
			try {
				corpus.save(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return corpus;
	}

	private Multiset<String> parseItem(DataItem item) throws Exception {
		Document doc = null;
		
		String ripped = Tools.ripCrap(item.getText());
		doc = DocumentBuilder.parseDocument(ripped, "fi");
		
		if (doc != null) {
			GramExtractor ge = new GramExtractor(doc, 1, 3);
			for (Predicate<Gram> f : gramFilters) {
				ge.filter(f);
			}
			ge.filterByFrequency(Ranges.atLeast(2));

			Multiset<String> res = HashMultiset.create();
			
			for (Grams g : ge.getGroupedGrams()) {
				res.add(g.getMatchString(), g.size());
			}
			
			return res;
		}
		
		throw new Exception("Magic failed :(");
	}
	
	private synchronized void addResult(Multiset<String> d) {
		documentsParsed++;
		corpus.addDocument(d);
		if (cpCallback != null) {
			cpCallback.handleProcess(corpus);
		}
		if (documentsParsed % 100 == 0) {
			log.info(String.format("Documents processed: %d", documentsParsed));
		}
	}

	private class ExtractTask implements Callable<Boolean> {
		private DataItem item;

		public ExtractTask(DataItem item) {
			this.item = item;
		}

		@Override
		public Boolean call() {
			Multiset<String> r;
			try {
				r = parseItem(item);
			} catch (Exception e) {
				log.error("Could not parse document: " + item.getId(), e);
				return false;
			}
			addResult(r);
			return true;
		}
	}
}
