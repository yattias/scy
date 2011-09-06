package eu.scy.core.model.transfer;

import eu.scy.common.scyelo.ScyElo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.feb.2011
 * Time: 05:29:24
 * To change this template use File | Settings | File Templates.
 */
public class FeedbackEloTransfer extends BaseXMLTransfer {

    private String createdBy;
    private String createdByPicture;
    private String calendarDate;
    private String calendarTime;
    private String comment;
    private String uri;
    private String shown;
    private String evaluation;
    private String score;
    private String quality;

    private List feedbacks = new LinkedList();


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByPicture() {
        return createdByPicture;
    }

    public void setCreatedByPicture(String createdByPicture) {
        this.createdByPicture = createdByPicture;
    }

    public String getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(String calendarDate) {
        this.calendarDate = calendarDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCalendarTime() {
        return calendarTime;
    }

    public void setCalendarTime(String calendarTime) {
        this.calendarTime = calendarTime;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getShown() {
        return shown;
    }

    public void setShown(String shown) {
        this.shown = shown;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public List getFeedbacks() {
        if(this.feedbacks == null) this.feedbacks = new LinkedList();
        return feedbacks;
    }

    public void setFeedbacks(List feedbacks) {
        this.feedbacks = feedbacks;
    }

    public void addFeedback(FeedbackTransfer feedback) {
        getFeedbacks().add(feedback);
        
    }
}
