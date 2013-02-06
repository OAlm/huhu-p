package net.sf.hfst;

public class NoTokenizationException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoTokenizationException(String str) {
		super("Failed to tokenize " + str);
	}
}
