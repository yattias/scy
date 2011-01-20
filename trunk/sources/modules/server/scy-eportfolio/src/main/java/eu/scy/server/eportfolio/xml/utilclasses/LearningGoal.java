package eu.scy.server.eportfolio.xml.utilclasses;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jan.2011
 * Time: 05:41:06
 * To change this template use File | Settings | File Templates.
 */
public class LearningGoal {

    private String goal;

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    @Override
    public String toString() {
        return getGoal();
    }
}
