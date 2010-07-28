package eu.scy.server.controllers.json.util;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mai.2010
 * Time: 15:26:34
 * To change this template use File | Settings | File Templates.
 */
public class UserLearningActivitySpaceConnectionUtil {

    private String userName;
    private String lasId;
    private String lasName;
    private String pedagogicalPlanId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLasId() {
        return lasId;
    }

    public void setLasId(String lasId) {
        this.lasId = lasId;
    }

    public String getLasName() {
        return lasName;
    }

    public void setLasName(String lasName) {
        this.lasName = lasName;
    }

    public String getPedagogicalPlanId() {
        return pedagogicalPlanId;
    }

    public void setPedagogicalPlanId(String pedagogicalPlanId) {
        this.pedagogicalPlanId = pedagogicalPlanId;
    }
}
