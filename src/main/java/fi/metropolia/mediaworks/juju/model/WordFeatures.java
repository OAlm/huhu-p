package fi.metropolia.mediaworks.juju.model;

public class WordFeatures implements Comparable<WordFeatures> {
	private String uri;
	private int hits;
	private double tf;
	private double idf;
	private double firstOccurence;
	private double lastOccurence;
	private double concurrency;
	
	public WordFeatures(String uri, int hits, double tf, double idf, double firstOccurence, double lastOccurence, double concurrency) {
		this.uri = uri;
		this.hits = hits;
		this.tf = tf;
		this.idf = idf;
		this.firstOccurence = firstOccurence;
		this.lastOccurence = lastOccurence;
		this.concurrency = concurrency;
	}
	
	public String getUri() {
		return uri;
	}

	public int getHits() {
		return hits;
	}

	public double getTf() {
		return tf;
	}

	public double getIdf() {
		return idf;
	}

	public double getFirstOccurence() {
		return firstOccurence;
	}

	public double getLastOccurence() {
		return lastOccurence;
	}

	public double getConcurrency() {
		return concurrency;
	}
	
	public double getTfidf() {
		return tf * idf;
	}
	
	public double getSpread() {
		return lastOccurence - firstOccurence;
	}
	
	public double getScore() {
		double score = 0;
		
		score += getTfidf();
		
		double posScore = 0;
		posScore += getSpread();
		posScore += 1 - getFirstOccurence();
		
		score *= posScore / 2;
		
		return score;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Hits: %5d", hits));
		sb.append(String.format(" | TF: %f", tf));
		sb.append(String.format(" | IDF: %f", idf));
		sb.append(String.format(" | TFIDF: %f", getTfidf()));
		sb.append(String.format(" | FO: %f", firstOccurence));
		sb.append(String.format(" | LO: %f", lastOccurence));
		sb.append(String.format(" | S: %f", getSpread()));
		sb.append(String.format(" | C: %f", concurrency));
		sb.append(String.format(" | %f", getScore()));
		return sb.toString();
	}

	@Override
	public int compareTo(WordFeatures o) {
		return Double.compare(getScore(), o.getScore()) ;
	}
}
