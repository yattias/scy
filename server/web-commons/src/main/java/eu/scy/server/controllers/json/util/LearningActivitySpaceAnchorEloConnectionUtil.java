package eu.scy.server.controllers.json.util;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mai.2010
 * Time: 12:38:27
 * To change this template use File | Settings | File Templates.
 */
public class LearningActivitySpaceAnchorEloConnectionUtil extends Object{

    private String from;
    private String to;
    private String direction;
    private String pedagogicalPlanId;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPedagogicalPlanId() {
        return pedagogicalPlanId;
    }

    public void setPedagogicalPlanId(String pedagogicalPlanId) {
        this.pedagogicalPlanId = pedagogicalPlanId;
    }
}
