package fi.metropolia.mediaworks.juju.data;

public class DataItem {
	private String id;
	private String title;
	private String text;
	
	public DataItem(String id, String title, String text) {
		this.id = id;
		this.title = title;
		this.text = text;
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return String.format("DataItem(%s): %s", id, title, text);
	}
}
