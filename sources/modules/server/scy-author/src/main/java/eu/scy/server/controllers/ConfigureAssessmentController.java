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
                } else if (action.equals("removeReflectionQuestion")) {
                    String reflectionQuestionId = request.getParameter("reflectionQuestionId");
                    removeReflectionQuestion(missionSpecificationElo, pedagogicalPlanTransfer, reflectionQuestionId);
                } else if (action.equals("addTeachersQuestionToElo")) {
                    addteachersQuestionToElo(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("addRubricToElo")) {
                    addRubricToElo(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("addCriteria")) {
                    addCriteriaToRubric(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("addRubricCategory")) {
                    addCategoryToRubric(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("addTeacherQuestion")) {
                    addTeacherQuestionToMission(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("addRubricToMission")) {
                    addRubricToMission(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("setInputTypeToSlider")) {
                    setInputTypeForTab(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("setInputTypeToText")) {
                    setInputTypeForTab(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("setTeacherQuestionTypeToText")) {
                    setTeacherQuestionType(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("setTeacherQuestionTypeToSlider")) {
                    setTeacherQuestionType(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("setReflectionQuestionToText")) {
                    setReflectionQuestion(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("setReflectionQuestionToSlider")) {
                    setReflectionQuestion(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("setTeacherQuestionToEloToText")) {
                    setTeacherQuestionToElo(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                } else if (action.equals("setTeacherQuestionToEloToSlider")) {
                    setTeacherQuestionToElo(missionSpecificationElo, pedagogicalPlanTransfer, anchorEloURI, request);
                }

            }

            List<TransferElo> anchorElos = getObligatoryAnchorElos(request, missionSpecificationElo, pedagogicalPlanTransfer);
            List<AnchorEloReflectionQuestionTransporter> anchorEloReflectionQuestionTransporters = new LinkedList<AnchorEloReflectionQuestionTransporter>();
            List<AnchorEloReflectionQuestionFOrTeacherTransporter> anchorEloReflectionQuestionFOrTeacherTransporters = new LinkedList<AnchorEloReflectionQuestionFOrTeacherTransporter>();

            for (int i = 0; i < anchorElos.size(); i++) {
                TransferElo transferElo = anchorElos.get(i);
                List<ReflectionQuestion> reflectionQuestions = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionQuestionsForAnchorElo(transferElo.getUri());

                if (reflectionQuestions.size() == 0) {
                    initReflectionQuestions(pedagogicalPlanTransfer, transferElo);
                    reflectionQuestions = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionQuestionsForAnchorElo(transferElo.getUri());
                }

                AnchorEloReflectionQuestionTransporter anchorEloReflectionQuestionTransporter = new AnchorEloReflectionQuestionTransporter();
                anchorEloReflectionQuestionTransporter.setAnchorElo(transferElo);
                anchorEloReflectionQuestionTransporter.setReflectionQuestions(reflectionQuestions);
                anchorEloReflectionQuestionTransporters.add(anchorEloReflectionQuestionTransporter);

                List<TeacherQuestionToElo> teacherQuestionToElos = pedagogicalPlanTransfer.getAssessmentSetup().getTeacherQuestionToElo(transferElo.getUri());
                AnchorEloReflectionQuestionFOrTeacherTransporter anchorEloReflectionQuestionFOrTeacherTransporter = new AnchorEloReflectionQuestionFOrTeacherTransporter();
                anchorEloReflectionQuestionFOrTeacherTransporter.setAnchorElo(transferElo);
                anchorEloReflectionQuestionFOrTeacherTransporter.setTeacherQuestionToElos(teacherQuestionToElos);
                anchorEloReflectionQuestionFOrTeacherTransporters.add(anchorEloReflectionQuestionFOrTeacherTransporter);
                List<RubricForElo> rubricForElos = pedagogicalPlanTransfer.getAssessmentSetup().getRubricsForElo(transferElo.getUri());
                anchorEloReflectionQuestionFOrTeacherTransporter.setRubricForElos(rubricForElos);
            }

            if(pedagogicalPlanTransfer.getAssessmentSetup().getReflectionTabs() != null && pedagogicalPlanTransfer.getAssessmentSetup().getReflectionTabs().size() == 0) {
                initializeReflectionTabs(pedagogicalPlanTransfer);
            }

            ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
            pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
            pedagogicalPlanElo.updateElo();

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

    private void initializeReflectionTabs(PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        Tab tab1 = new Tab();
        tab1.setTitle("Reflection on mission");
        tab1.setQuestion("The most important things I learned were");
        tab1.setType("text");

        Tab tab2 = new Tab();
        tab2.setTitle("Reflection on collaboration");
        tab2.setQuestion("Did collaboration contribute to your learning?");
        tab2.setType("text");

        Tab tab3 = new Tab();
        tab3.setTitle("Reflection on inquery: ");
        tab3.setQuestion("How helpful was making all the ELOs in learning more about DNA?");
        tab3.setType("text");

        Tab tab4 = new Tab();
        tab4.setTitle("Reflection on effort: ");
        tab4.setType("slider");

    }

    private void initReflectionQuestions(PedagogicalPlanTransfer pedagogicalPlanTransfer, TransferElo anchorElo) {
        ReflectionQuestion reflectionQuestion = new ReflectionQuestion();
        reflectionQuestion.setAnchorEloURI(anchorElo.getUri());
        reflectionQuestion.setAnchorEloName(anchorElo.getMyname());
        reflectionQuestion.setReflectionQuestionTitle("Reflection in ELO");
        reflectionQuestion.setReflectionQuestion("What have you learned by making this ELO?");
        reflectionQuestion.setType("text");
        pedagogicalPlanTransfer.getAssessmentSetup().addReflectionQuestion(reflectionQuestion);

        ReflectionQuestion q2 = new ReflectionQuestion();
        q2.setAnchorEloURI(anchorElo.getUri());
        q2.setAnchorEloName(anchorElo.getMyname());
        q2.setReflectionQuestionTitle("Reflection inquery");
        q2.setReflectionQuestion("How helpful was making this ELO in understanding e.g. DNA identification?");
        q2.setType("slider");
        pedagogicalPlanTransfer.getAssessmentSetup().addReflectionQuestion(q2);
    }

    private void setTeacherQuestionToElo(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, HttpServletRequest request) {
        String teacherQuestionToEloToTextId = request.getParameter("teacherQuestionToElo");
        String aURI = getEncodedUri(anchorEloURI);
        String type = "text";
        if (request.getParameter("action").equals("setTeacherQuestionToEloToSlider")) type = "slider";
        List<TeacherQuestionToElo> teacherQuestionsToElo = pedagogicalPlanTransfer.getAssessmentSetup().getTeacherQuestionToElo(aURI);
        for (int i = 0; i < teacherQuestionsToElo.size(); i++) {
            TeacherQuestionToElo teacherQuestionToElo = teacherQuestionsToElo.get(i);
            if (teacherQuestionToElo.getId().equals(teacherQuestionToEloToTextId))
                teacherQuestionToElo.setQuestionType(type);
        }

        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();
    }

    private void setReflectionQuestion(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, HttpServletRequest request) {
        String reflectionQuestionId = request.getParameter("reflectionQuestion");
        String aURI = getEncodedUri(anchorEloURI);
        String type = "text";
        if (request.getParameter("action").equals("setReflectionQuestionToSlider")) type = "slider";
        List reflectionQuestionsForAnchorElo = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionQuestionsForAnchorElo(aURI);
        for (int i = 0; i < reflectionQuestionsForAnchorElo.size(); i++) {
            ReflectionQuestion reflectionQuestion = (ReflectionQuestion) reflectionQuestionsForAnchorElo.get(i);
            if (reflectionQuestion.getId().equals(reflectionQuestionId)) reflectionQuestion.setType(type);
        }

        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();

    }

    private void setTeacherQuestionType(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, HttpServletRequest request) {
        String teacherQuestionId = request.getParameter("teacherQuestion");
        String type = "text";
        if (request.getParameter("action").equals("setTeacherQuestionTypeToSlider")) type = "slider";
        List<TeacherQuestionToMission> teacherQuestionToMissions = pedagogicalPlanTransfer.getAssessmentSetup().getTeacherQuestionsToMission();
        for (int i = 0; i < teacherQuestionToMissions.size(); i++) {
            TeacherQuestionToMission teacherQuestionToMission = teacherQuestionToMissions.get(i);
            if (teacherQuestionToMission.getId().equals(teacherQuestionId))
                teacherQuestionToMission.setQuestionType(type);
        }

        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();

    }

    private void setInputTypeForTab(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, String anchorEloURI, HttpServletRequest request) {
        String tabId = request.getParameter("reflectionTabId");
        String action = request.getParameter("action");
        String type = "text";
        if (action.equals("setInputTypeToSlider")) type = "slider";

        List<Tab> reflectionQuestions = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionTabs();
        for (int i = 0; i < reflectionQuestions.size(); i++) {
            Tab tab = reflectionQuestions.get(i);
            if (tab.getId().equals(tabId)) tab.setType(type);
        }

        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();

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
        if (rubric != null) {
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
        ScyElo anchorElo = ScyElo.loadElo(getURI(anchorEloURI), getMissionELOService());
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
        List<ReflectionQuestion> reflectionQuestions = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionQuestions();
        ReflectionQuestion reflectionQuestionToBeDeleted = null;
        for (int i = 0; i < reflectionQuestions.size(); i++) {
            ReflectionQuestion reflectionQuestion = reflectionQuestions.get(i);
            if (reflectionQuestion.getId().equals(reflectionQuestionId))
                reflectionQuestionToBeDeleted = reflectionQuestion;
        }

        if (reflectionQuestionToBeDeleted != null) {
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
