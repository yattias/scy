package eu.scy.core.model.transfer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    private String pedagogicalPlanURI;

    public AssessmentSetupTransfer getAssessmentSetup() {
        if(assessmentSetup == null) setAssessmentSetup(new AssessmentSetupTransfer());
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
}
