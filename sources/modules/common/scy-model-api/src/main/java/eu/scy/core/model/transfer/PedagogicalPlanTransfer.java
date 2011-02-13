package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.feb.2011
 * Time: 22:11:57
 * To change this template use File | Settings | File Templates.
 */
public class PedagogicalPlanTransfer extends BaseXMLTransfer {

    private AssessmentSetupTransfer assessmentSetup;
    private String name;

    public AssessmentSetupTransfer getAssessmentSetup() {
        return assessmentSetup;
    }

    public void setAssessmentSetup(AssessmentSetupTransfer assessmentSetup) {
        this.assessmentSetup = assessmentSetup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
