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
            //syncAssessmentWithAnchorElos(pedagogicalPlanTransfer, getMissionELOService().getAnchorELOs(missionSpecificationElo), missionSpecificationElo);


            if (action != null) {
                if (action.equals("addReflectionQuestion"))
                    addReflectionQuestion(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, "");
                else if (action.equals("clearReflectionQuestions")) {
                    clearReflectionQuestions(missionSpecificationElo, pedagogicalPlanTransfer);
                } else if (action.equals("addReflectionQuestionOnMission")) {
                    addReflectionQuestionToMission(missionSpecificationElo, pedagogicalPlanTransfer);
                } else if(action.equals("removeReflectionQuestion")) {
                    String reflectionQuestionId = request.getParameter("reflectionQuestionId");
                    removeReflectionQuestion(missionSpecificationElo, pedagogicalPlanTransfer, reflectionQuestionId);
                } else if(action.equals("addTeachersQuestionToElo")) {
                    addteachersQuestionToElo(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if(action.equals("addRubricToElo")) {
                    addRubricToElo(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if(action.equals("addCriteria")) {
                    addCriteriaToRubric(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if(action.equals("addRubricCategory")) {
                    addCategoryToRubric(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if(action.equals("addTeacherQuestion")) {
                    addTeacherQuestionToMission(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if(action.equals("addRubricToMission")) {
                    addRubricToMission(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                }

            }

            List <TransferElo> anchorElos = getObligatoryAnchorElos(request, missionSpecificationElo, pedagogicalPlanTransfer);//getMissionELOService().getObligatoryAnchorELOs(missionSpecificationElo, pedagogicalPlanTransfer);
            List <AnchorEloReflectionQuestionTransporter> anchorEloReflectionQuestionTransporters = new LinkedList<AnchorEloReflectionQuestionTransporter>();
            List <AnchorEloReflectionQuestionFOrTeacherTransporter> anchorEloReflectionQuestionFOrTeacherTransporters = new LinkedList<AnchorEloReflectionQuestionFOrTeacherTransporter>();

            for (int i = 0; i < anchorElos.size(); i++) {
                TransferElo transferElo = anchorElos.get(i);
                List <ReflectionQuestion> reflectionQuestions = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionQuestionsForAnchorElo(transferElo.getUri());
                AnchorEloReflectionQuestionTransporter anchorEloReflectionQuestionTransporter = new AnchorEloReflectionQuestionTransporter();
                anchorEloReflectionQuestionTransporter.setAnchorElo(transferElo);
                anchorEloReflectionQuestionTransporter.setReflectionQuestions(reflectionQuestions);
                anchorEloReflectionQuestionTransporters.add(anchorEloReflectionQuestionTransporter);

                List <TeacherQuestionToElo> teacherQuestionToElos = pedagogicalPlanTransfer.getAssessmentSetup().getTeacherQuestionToElo(transferElo.getUri());
                AnchorEloReflectionQuestionFOrTeacherTransporter anchorEloReflectionQuestionFOrTeacherTransporter = new AnchorEloReflectionQuestionFOrTeacherTransporter();
                anchorEloReflectionQuestionFOrTeacherTransporter.setAnchorElo(transferElo);
                anchorEloReflectionQuestionFOrTeacherTransporter.setTeacherQuestionToElos(teacherQuestionToElos);
                anchorEloReflectionQuestionFOrTeacherTransporters.add(anchorEloReflectionQuestionFOrTeacherTransporter);
                List <RubricForElo> rubricForElos = pedagogicalPlanTransfer.getAssessmentSetup().getRubricsForElo(transferElo.getUri());
                anchorEloReflectionQuestionFOrTeacherTransporter.setRubricForElos(rubricForElos);


            }

            modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);
            modelAndView.addObject("transferObjectServiceCollection", getTransferObjectServiceCollection());
            modelAndView.addObject("missionSpecificationEloURI", URLEncoder.encode(uriParam, "UTF-8"));
            modelAndView.addObject("anchorElos", anchorEloReflectionQuestionTransporters);
            modelAndView.addObject("anchorEloReflectionQuestionForTeacherTransporters", anchorEloReflectionQuestionFOrTeacherTransporters);
            modelAndView.addObject("teacherQuestionsToMission", pedagogicalPlanTransfer.getAssessmentSetup().getTeacherQuestionsToMission());
            modelAndView.addObject("teacherRubricsForMission", pedagogicalPlanTransfer.getAssessmentSetup().getRubricForMission());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addRubricToMission(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, HttpServletRequest request) {
        RubricForMission rubricForMission = new RubricForMission();

        RubricCategory category = new RubricCategory();
        category.setName("Click to add category name");
        rubricForMission.addRubricCategory(category);

        RubricAssessmentCriteria assessmentCriteria = new RubricAssessmentCriteria();
        assessmentCriteria.setAssessmentCriteria("Click to edit assessment criteria");
        category.addRubricAssessmentCriteria(assessmentCriteria);

        pedagogicalPlanTransfer.getAssessmentSetup().addRubric(rubricForMission);

        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();

    }

    private void addTeacherQuestionToMission(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, HttpServletRequest request) {
        TeacherQuestionToMission teacherQuestionToMission = new TeacherQuestionToMission();
        teacherQuestionToMission.setQuestionType("text");
        pedagogicalPlanTransfer.getAssessmentSetup().addTeacherQuestiontoMission(teacherQuestionToMission);
        
        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();

    }

    private void addCategoryToRubric(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, HttpServletRequest request) {
        ScyElo anchorElo = ScyElo.loadLastVersionElo(getURI(anchorEloURI), getMissionELOService());
        TransferElo anchorEloTransfer = new TransferElo(anchorElo);
        String rubricId = request.getParameter("rubricId");
        Rubric rubric = pedagogicalPlanTransfer.getAssessmentSetup().getRubric(rubricId);
        if(rubric != null) {
            RubricCategory rubricCategory = new RubricCategory();
            rubric.addRubricCategory(rubricCategory);
        }

        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();

    }

    private void addCriteriaToRubric(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, HttpServletRequest request) {
        ScyElo anchorElo = ScyElo.loadLastVersionElo(getURI(anchorEloURI), getMissionELOService());
        TransferElo anchorEloTransfer = new TransferElo(anchorElo);
        String categoryId = request.getParameter("categoryId");
        RubricCategory category = pedagogicalPlanTransfer.getAssessmentSetup().getRubricCategory(categoryId);
        category.addRubricAssessmentCriteria(new RubricAssessmentCriteria());

        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();

    }

    private void addRubricToElo(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, HttpServletRequest request) {
        ScyElo anchorElo = ScyElo.loadLastVersionElo(getURI(anchorEloURI), getMissionELOService());
        TransferElo anchorEloTransfer = new TransferElo(anchorElo);
        logger.info("*** **** **** **** Adding rubrik to anchor elo: " + anchorEloTransfer.getMyname());

        RubricForElo rubricForElo = new RubricForElo();
        rubricForElo.setAnchorElo(anchorEloTransfer.getUri());

        RubricCategory category = new RubricCategory();
        category.setName("Click to add category name");
        rubricForElo.addRubricCategory(category);

        RubricAssessmentCriteria assessmentCriteria = new RubricAssessmentCriteria();
        assessmentCriteria.setAssessmentCriteria("Click to edit assessment criteria");
        category.addRubricAssessmentCriteria(assessmentCriteria);

        pedagogicalPlanTransfer.getAssessmentSetup().addRubric(rubricForElo);

        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();

    }

    private void addteachersQuestionToElo(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, HttpServletRequest request) {
        ScyElo anchorElo = ScyElo.loadLastVersionElo(getURI(anchorEloURI), getMissionELOService());
        TransferElo anchorEloTransfer = new TransferElo(anchorElo);
        logger.info("*** **** **** **** Adding teacher question to anchor elo: " + anchorEloTransfer.getMyname());
        TeacherQuestionToElo teacherQuestionToElo = new TeacherQuestionToElo();
        teacherQuestionToElo.setEloURI(anchorEloTransfer.getUri());
        teacherQuestionToElo.setQuestionType("text");
        pedagogicalPlanTransfer.getAssessmentSetup().addTeacherQuestionToElo(teacherQuestionToElo);
        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();

    }

    private void removeReflectionQuestion(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String reflectionQuestionId) {
        List <ReflectionQuestion> reflectionQuestions = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionQuestions();
        ReflectionQuestion reflectionQuestionToBeDeleted = null;
        for (int i = 0; i < reflectionQuestions.size(); i++) {
            ReflectionQuestion reflectionQuestion = reflectionQuestions.get(i);
            if(reflectionQuestion.getId().equals(reflectionQuestionId)) reflectionQuestionToBeDeleted = reflectionQuestion;
        }

        if(reflectionQuestionToBeDeleted != null) {
            reflectionQuestions.remove(reflectionQuestionToBeDeleted);
            pedagogicalPlanTransfer.getAssessmentSetup().setReflectionQuestions(reflectionQuestions);
        }

        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();
    }

    private void addReflectionQuestionToMission(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        pedagogicalPlanTransfer.getAssessmentSetup().addReflectionTab(createTab("Edit tab name", "Edit Question", "text"));
        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();
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


        if (pedagogicalPlanTransfer.getAssessmentSetup().getReflectionTabs() == null) {
            pedagogicalPlanTransfer.getAssessmentSetup().setReflectionTabs(new LinkedList());
        }

        /*if(pedagogicalPlanTransfer.getAssessmentSetup().getReflectionTabs().size() == 0) {
            pedagogicalPlanTransfer.getAssessmentSetup().addReflectionTab(createTab("Reflection on Mission", "The most important things I learned about CO2 neural houses are...", "text"));
            pedagogicalPlanTransfer.getAssessmentSetup().addReflectionTab(createTab("Reflection on Collaboration", "Did your group agreed about how to work before you started and were there any problems during the work?", "text"));
            pedagogicalPlanTransfer.getAssessmentSetup().addReflectionTab(createTab("Reflection on Inquiry", "Did you keep the hypotheses that you created early in the inquiry or did you change it?", "text"));
            pedagogicalPlanTransfer.getAssessmentSetup().addReflectionTab(createTab("Reflection on Effort", "Are you satisfied with your own work on this Mission?", "slider"));


        } */

        List reflectionQuestions = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionQuestions();
        for (int j = 0; j < reflectionQuestions.size(); j++) {
            ReflectionQuestion reflectionQuestion = (ReflectionQuestion) reflectionQuestions.get(j);
            reflectionMap.put(reflectionQuestion, reflectionQuestion.getAnchorEloURI());
        }

        /*List obligatoryAnchorElos = getMissionELOService().getObligatoryAnchorELOs(missionSpecificationElo, pedagogicalPlanTransfer);
        for (int i = 0; i < obligatoryAnchorElos.size(); i++) {
            TransferElo transferElo = (TransferElo) obligatoryAnchorElos.get(i);
            ReflectionQuestion question = (ReflectionQuestion) reflectionMap.get(String.valueOf(transferElo.getUri()));
            addReflectionQuestion(missionSpecificationElo, pedagogicalPlanTransfer, String.valueOf(transferElo.getUri()), transferElo.getMyname());

        } */

    }

    private Tab createTab(String title, String question, String type) {
        Tab returnTab = new Tab();
        returnTab.setTitle(title);
        returnTab.setQuestion(question);
        returnTab.setType(type);

        return returnTab;
    }


    private void addReflectionQuestion(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, String title) {
        logger.info("ADDING REFLECTION QUESTION " + getEncodedUri(anchorEloURI) + " " + title);

        ReflectionQuestion reflectionQuestion = new ReflectionQuestion();
        reflectionQuestion.setAnchorEloURI(getEncodedUri(anchorEloURI));
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
