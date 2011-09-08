package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.sep.2011
 * Time: 09:36:23
 * To change this template use File | Settings | File Templates.
 */
public class LearningGoalCriterium extends BaseXMLTransfer {

    private String level;
    private String criterium;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCriterium() {
        return criterium;
    }

    public void setCriterium(String criterium) {
        this.criterium = criterium;
    }
}
