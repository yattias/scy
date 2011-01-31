package eu.scy.server.controllers.xml.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 31.jan.2011
 * Time: 11:11:42
 * To change this template use File | Settings | File Templates.
 */
public class PortfolioConfig extends BaseXMLTransfer{

    private String reflectionOnMissionQuestion;
    private String reflectionOnCollaborationQuestion;
    private String reflectionOnInquiryQuestion;
    private String reflectionOnEffortQuestion;

    private List portfolioEffortScale = new LinkedList();

    public String getReflectionOnMissionQuestion() {
        return reflectionOnMissionQuestion;
    }

    public void setReflectionOnMissionQuestion(String reflectionOnMissionQuestion) {
        this.reflectionOnMissionQuestion = reflectionOnMissionQuestion;
    }

    public String getReflectionOnCollaborationQuestion() {
        return reflectionOnCollaborationQuestion;
    }

    public void setReflectionOnCollaborationQuestion(String reflectionOnCollaborationQuestion) {
        this.reflectionOnCollaborationQuestion = reflectionOnCollaborationQuestion;
    }

    public String getReflectionOnInquiryQuestion() {
        return reflectionOnInquiryQuestion;
    }

    public void setReflectionOnInquiryQuestion(String reflectionOnInquiryQuestion) {
        this.reflectionOnInquiryQuestion = reflectionOnInquiryQuestion;
    }

    public String getReflectionOnEffortQuestion() {
        return reflectionOnEffortQuestion;
    }

    public void setReflectionOnEffortQuestion(String reflectionOnEffortQuestion) {
        this.reflectionOnEffortQuestion = reflectionOnEffortQuestion;
    }

    public List getPortfolioEffortScale() {
        return portfolioEffortScale;
    }

    public void setPortfolioEffortScale(List portfolioEffortScale) {
        this.portfolioEffortScale = portfolioEffortScale;
    }

    public void addPortfolioEffortScalePoint(PortfolioEffortScale portfolioEffortScale) {
        getPortfolioEffortScale().add(portfolioEffortScale);        
    }

}
