package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.sep.2011
 * Time: 01:36:47
 * To change this template use File | Settings | File Templates.
 */
public class EloAssessment {

    private String eloURI;
    private String commentHeading;
    private String comment;

    public String getEloURI() {
        return eloURI;
    }

    public void setEloURI(String eloURI) {
        this.eloURI = eloURI;
    }

    public String getCommentHeading() {
        return commentHeading;
    }

    public void setCommentHeading(String commentHeading) {
        this.commentHeading = commentHeading;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
