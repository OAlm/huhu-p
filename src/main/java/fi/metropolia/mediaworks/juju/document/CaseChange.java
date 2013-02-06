package fi.metropolia.mediaworks.juju.document;

public enum CaseChange {
	NONE("NONE", false),
	UP_FIRST("UPFIRST", true),
	UP_ALL("UPALL", true);
	
	public final String tag;
	public final boolean upperCase;
	
	private CaseChange(String tag, boolean upperCase) {
		this.tag = tag;
		this.upperCase = upperCase;
	}
	
	public static CaseChange getWithTag(String tag) {
		for (CaseChange cc : values()) {
			if (cc.tag.equals(tag)) {
				return cc;
			}
		}
		System.err.println("Missing casechange: " + tag);
		return null;
	}
}
