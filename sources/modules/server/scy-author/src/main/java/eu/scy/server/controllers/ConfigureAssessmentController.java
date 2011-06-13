package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.*;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.server.util.TransferObjectServiceCollection;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.feb.2011
 * Time: 05:23:25
 * To change this template use File | Settings | File Templates.
 */
public class ConfigureAssessmentController extends BaseController {

    private MissionELOService missionELOService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private TransferObjectServiceCollection transferObjectServiceCollection;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        try {
            String uriParam = request.getParameter("eloURI");
            String action = request.getParameter("action");
            String anchorEloURI = request.getParameter("anchorEloURI");

            logger.info("ELO URI: " + uriParam);

            URI uri = new URI(uriParam);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());

            PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanTransfer(missionSpecificationElo);
            syncAssessmentWithAnchorElos(pedagogicalPlanTransfer, getMissionELOService().getAnchorELOs(missionSpecificationElo), missionSpecificationElo);


            if (action != null) {
                if (action.equals("addReflectionQuestion"))
                    addReflectionQuestion(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, "");
                else if (action.equals("clearReflectionQuestions"))
                    clearReflectionQuestions(missionSpecificationElo, pedagogicalPlanTransfer);
            }

            logger.info("Ich habe die parameteren gesatt: " + uriParam);

            modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);
            modelAndView.addObject("transferObjectServiceCollection", getTransferObjectServiceCollection());
            modelAndView.addObject("missionSpecificationEloURI", URLEncoder.encode(uriParam, "UTF-8"));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearReflectionQuestions(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        logger.info("CLEARING REFLECTION QUESTIONS!");
        if (pedagogicalPlanTransfer != null) {
            pedagogicalPlanTransfer.getAssessmentSetup().setReflectionQuestions(Collections.EMPTY_LIST);
            ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
            pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
            pedagogicalPlanElo.updateElo();
        }
    }

    private void syncAssessmentWithAnchorElos(PedagogicalPlanTransfer pedagogicalPlanTransfer, List anchorELOs, MissionSpecificationElo missionSpecificationElo) {

        Map reflectionMap = new HashMap();
        List syncedList = new LinkedList();


        if(pedagogicalPlanTransfer.getAssessmentSetup().getReflectionTabs() == null){
            pedagogicalPlanTransfer.getAssessmentSetup().setReflectionTabs(new LinkedList());
        }

        if(pedagogicalPlanTransfer.getAssessmentSetup().getReflectionTabs().size() == 0) {
            pedagogicalPlanTransfer.getAssessmentSetup().addReflectionTab(createTab("Reflection on Mission", "The most important things I learned about CO2 neural houses are...", "text"));
            pedagogicalPlanTransfer.getAssessmentSetup().addReflectionTab(createTab("Reflection on Collaboration", "Did your group agreed about how to work before you started and were there any problems during the work?", "text"));
            pedagogicalPlanTransfer.getAssessmentSetup().addReflectionTab(createTab("Reflection on Inquiry", "Did you keep the hypotheses that you created early in the inquiry or did you change it?", "text"));
            pedagogicalPlanTransfer.getAssessmentSetup().addReflectionTab(createTab("Reflection on Effort", "Are you satisfied with your own work on this Mission?", "slider"));


        }

        List reflectionQuestions = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionQuestions();
        for (int j = 0; j < reflectionQuestions.size(); j++) {
            ReflectionQuestion reflectionQuestion = (ReflectionQuestion) reflectionQuestions.get(j);
            reflectionMap.put(reflectionQuestion, reflectionQuestion.getAnchorEloURI());
        }

        for (int i = 0; i < anchorELOs.size(); i++) {
            ScyElo scyElo = (ScyElo) anchorELOs.get(i);
            if (scyElo.getObligatoryInPortfolio() != null && scyElo.getObligatoryInPortfolio()) {
                ReflectionQuestion question = (ReflectionQuestion) reflectionMap.get(String.valueOf(scyElo.getUri()));
                if (question != null) {
                    syncedList.add(question);
                } else {
                    addReflectionQuestion(missionSpecificationElo, pedagogicalPlanTransfer, String.valueOf(scyElo.getUri()), scyElo.getTitle());
                }
            }


        }


    }

    private Tab createTab(String title, String question, String type) {
        Tab returnTab = new Tab();
        returnTab.setTitle(title);
        returnTab.setQuestion(question);
        returnTab.setType(type);

        return returnTab;
    }



    private void addReflectionQuestion(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, String title) {
        logger.info("ADDING REFLECTION QUESTION " + anchorEloURI + " " + title);

        if (getSetupContainsQuestion(pedagogicalPlanTransfer.getAssessmentSetup(), title)) {
            logger.info("SETUP ALREADY CONTAINS QUESTION FOR " + title);
            return;
        }

        ReflectionQuestion reflectionQuestion = new ReflectionQuestion();
        reflectionQuestion.setAnchorEloURI(anchorEloURI);
        reflectionQuestion.setAnchorEloName(title);
        if (pedagogicalPlanTransfer != null) {
            pedagogicalPlanTransfer.getAssessmentSetup().addReflectionQuestion(reflectionQuestion);
            ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
            pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
            pedagogicalPlanElo.updateElo();
        } else {
            logger.info("PEDAGOGICAL PLAN TRANSFER IS NULL");
        }


    }

    private boolean getSetupContainsQuestion(AssessmentSetupTransfer assessmentSetup, String title) {
        List questions = assessmentSetup.getReflectionQuestions();
        for (int i = 0; i < questions.size(); i++) {
            ReflectionQuestion reflectionQuestion = (ReflectionQuestion) questions.get(i);
            if (reflectionQuestion.getAnchorEloName() != null && reflectionQuestion.getAnchorEloName().equals(title))
                return true;

        }
        return false;
    }

    private PedagogicalPlanTransfer getPedagogicalPlanTransfer(MissionSpecificationElo missionSpecificationElo) {
        return getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationElo);
    }

    private ScyElo getPedagogicalPlanEloForMission(MissionSpecificationElo missionSpecificationElo) {
        URI pedagogicalPlanUri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        ScyElo pedagogicalPlanELO = ScyElo.loadLastVersionElo(pedagogicalPlanUri, getMissionELOService());
        return pedagogicalPlanELO;

    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }

    public TransferObjectServiceCollection getTransferObjectServiceCollection() {
        return transferObjectServiceCollection;
    }

    public void setTransferObjectServiceCollection(TransferObjectServiceCollection transferObjectServiceCollection) {
        this.transferObjectServiceCollection = transferObjectServiceCollection;
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }
}
