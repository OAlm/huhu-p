package fi.metropolia.mediaworks.juju.document;

public class IteratorConf {
	
	public String [] includeNodes;
	public String [] skipNodes;
	public enum CASE{NONE, LOWERCASE, UPPERCASE}
	public CASE wordCase = CASE.NONE;
	
	public String [] skipConcepts;
	public String [] includeConcepts;
	
	
}
