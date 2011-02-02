package eu.scy.client.tools.formauthor;

public class DataFormPreviewModel {
	private String Title;
	private String Description;
	private String Author;
	private String URI;

	public DataFormPreviewModel(String title, String description,
			String author, String uri) {
		Title = title;
		Description = description;
		Author = author;
		URI = uri;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uri) {
		URI = uri;
	}
}
