package eu.scy.tools.youtube.client;

public class VideoBean {

    private String movieID;

    private String tite;

    private String description;

    public VideoBean(String movieID, String tite, String description) {
        super();
        this.movieID = movieID;
        this.tite = tite;
        this.description = description;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getTite() {
        return tite;
    }

    public void setTite(String tite) {
        this.tite = tite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
