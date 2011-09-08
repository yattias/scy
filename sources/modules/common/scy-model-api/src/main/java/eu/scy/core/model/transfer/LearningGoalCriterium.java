package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.sep.2011
 * Time: 09:36:23
 * To change this template use File | Settings | File Templates.
 */
public class LearningGoalCriterium extends BaseXMLTransfer {

    public static final String LOW = "LOW";
    public static final String MEDIUM = "MEDIUM";
    public static final String HIGH = "HIGH";

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
