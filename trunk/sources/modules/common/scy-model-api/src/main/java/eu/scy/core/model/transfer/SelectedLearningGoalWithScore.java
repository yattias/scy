package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.sep.2011
 * Time: 22:12:04
 * To change this template use File | Settings | File Templates.
 */
public class SelectedLearningGoalWithScore extends BaseXMLTransfer {

    private String learningGoalId;
    private String score;
    private String learningGoalText;

    public String getLearningGoalId() {
        return learningGoalId;
    }

    public void setLearningGoalId(String learningGoalId) {
        this.learningGoalId = learningGoalId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getLearningGoalText() {
        return learningGoalText;
    }

    public void setLearningGoalText(String learningGoalText) {
        this.learningGoalText = learningGoalText;
    }
}
