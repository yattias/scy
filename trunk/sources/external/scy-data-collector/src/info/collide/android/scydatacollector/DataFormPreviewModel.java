package info.collide.android.scydatacollector;

public class DataFormPreviewModel {

    private String Title;

    private String Description;

    private String Author;

    private String URI;

    private boolean download;

    public DataFormPreviewModel(String title, String description, String author, String uri) {
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

    public boolean is_download() {
        return download;
    }

    public void setDownload(boolean isChecked) {
        download = isChecked;
    }
}
