package eu.scy.server.eportfolio.xml.utilclasses;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.okt.2010
 * Time: 11:50:35
 * To change this template use File | Settings | File Templates.
 */
public class ELOModel {

    private String EloURI;
    private String thumbnailId;
    private String EloName;
    private String createdByUserId;
    private String createdByGroupId;
    private String cratedDate;

    public String getEloURI() {
        return EloURI;
    }

    public void setEloURI(String eloURI) {
        EloURI = eloURI;
    }

    public String getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getEloName() {
        return EloName;
    }

    public void setEloName(String eloName) {
        EloName = eloName;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedByGroupId() {
        return createdByGroupId;
    }

    public void setCreatedByGroupId(String createdByGroupId) {
        this.createdByGroupId = createdByGroupId;
    }

    public String getCratedDate() {
        return cratedDate;
    }

    public void setCratedDate(String cratedDate) {
        this.cratedDate = cratedDate;
    }
}
