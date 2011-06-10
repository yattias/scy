package eu.scy.core.model.transfer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.feb.2011
 * Time: 22:11:57
 * To change this template use File | Settings | File Templates.
 */
public class PedagogicalPlanTransfer extends BaseXMLTransfer {

    private AssessmentSetupTransfer assessmentSetup;
    private MissionPlanTransfer missionPlan;
    private TechnicalInfo technicalInfo;
    private String name;
    private String pedagogicalPlanURI;

    public AssessmentSetupTransfer getAssessmentSetup() {
        if (assessmentSetup == null) setAssessmentSetup(new AssessmentSetupTransfer());
        return assessmentSetup;
    }

    public void setAssessmentSetup(AssessmentSetupTransfer assessmentSetup) {
        this.assessmentSetup = assessmentSetup;
    }

    public MissionPlanTransfer getMissionPlan() {
        if (this.missionPlan == null) setMissionPlan(new MissionPlanTransfer());
        return missionPlan;
    }

    public void setMissionPlan(MissionPlanTransfer missionPlan) {
        this.missionPlan = missionPlan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPedagogicalPlanURI() {
        return pedagogicalPlanURI;
    }

    public void setPedagogicalPlanURI(String pedagogicalPlanURI) {
        this.pedagogicalPlanURI = pedagogicalPlanURI;
    }

    public void setEncodedPedagogicalPlanURI(String boo) {

    }

    public String getEncodedPedagogicalPlanURI() {
        try {
            return URLEncoder.encode(getPedagogicalPlanURI(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return getPedagogicalPlanURI();

    }

    public String getReflectionQuestionForURI(String uri) {
        if (uri != null) {
            uri = uri.trim();
            List reflectionQuestions = getAssessmentSetup().getReflectionQuestions();
            for (int i = 0; i < reflectionQuestions.size(); i++) {
                ReflectionQuestion reflectionQuestion = (ReflectionQuestion) reflectionQuestions.get(i);
                if (reflectionQuestion.getAnchorEloURI() != null) {
                    if (reflectionQuestion.getAnchorEloURI().equals(uri))
                        return reflectionQuestion.getReflectionQuestion();
                }
            }
        }

        return "";
    }

    public TechnicalInfo getTechnicalInfo() {
        if (technicalInfo == null) setTechnicalInfo(new TechnicalInfo());
        return technicalInfo;
    }

    public void setTechnicalInfo(TechnicalInfo technicalInfo) {
        this.technicalInfo = technicalInfo;
    }

    public String obtainLasName(String lasId) {
        List<LasTransfer> lasTransfers = getMissionPlan().getLasTransfers();
        if (lasTransfers != null) {
            for (int i = 0; i < lasTransfers.size(); i++) {
                LasTransfer lasTransfer = lasTransfers.get(i);
                if (lasTransfer.getOriginalLasId().equals(lasId)) return lasTransfer.getFullName();
            }
        }


        return "";
    }
}
