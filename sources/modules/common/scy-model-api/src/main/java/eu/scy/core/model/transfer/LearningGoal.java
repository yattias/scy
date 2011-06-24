package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 19:51:56
 * To change this template use File | Settings | File Templates.
 */
public class LearningGoal extends BaseXMLTransfer{

    private String goal;
    private Boolean use = Boolean.TRUE;

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }

    @Override
    public String toString() {
        return this.getGoal();
    }
}
