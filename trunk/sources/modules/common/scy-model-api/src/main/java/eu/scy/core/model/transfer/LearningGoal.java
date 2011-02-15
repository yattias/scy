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

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    @Override
    public String toString() {
        return this.getGoal();
    }
}
