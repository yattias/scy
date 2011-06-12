package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.feb.2011
 * Time: 06:13:37
 * To change this template use File | Settings | File Templates.
 */
public class FeedbackTransfer extends BaseXMLTransfer {

    private String createdBy;
    private String createdByPicture;
    private String calendarDate;
    private String calendarTime;
    private String comment;
    private String evalu;

    private List replies;


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

    public String getCalendarTime() {
        return calendarTime;
    }

    public void setCalendarTime(String calendarTime) {
        this.calendarTime = calendarTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List getReplies() {
        return replies;
    }

    public void setReplies(List replies) {
        this.replies = replies;
    }

    public void addReply(FeedbackReplyTransfer feedbackReplyTransfer) {
        if(getReplies() == null) setReplies(new LinkedList());
        getReplies().add(feedbackReplyTransfer);
    }

    public String getEvalu() {
        return evalu;
    }

    public void setEvalu(String evalu) {
        this.evalu = evalu;
    }
}
