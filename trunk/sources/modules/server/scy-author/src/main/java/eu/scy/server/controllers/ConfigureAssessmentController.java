package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.LearningGoal;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.XMLTransferObjectServiceImpl;
import eu.scy.server.util.TransferObjectServiceCollection;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.feb.2011
 * Time: 05:23:25
 * To change this template use File | Settings | File Templates.
 */
public class ConfigureAssessmentController extends BaseController {

    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private TransferObjectServiceCollection transferObjectServiceCollection;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        try {
            String uriParam = request.getParameter("eloURI");
            String action = request.getParameter("action");

            logger.info("ELO URI: " + uriParam);

            URI uri = new URI(uriParam);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());
            modelAndView.addObject("missionSpecificationEloURI ", URLEncoder.encode(uriParam, "UTF-8"));

            PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanTransfer(missionSpecificationElo);

            if(action != null) {
                if(action.equals("addGeneralLearningGoal")) addGeneralLearningGoal(missionSpecificationElo, pedagogicalPlanTransfer);
            }



            modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);
            modelAndView.addObject("transferObjectServiceCollection", getTransferObjectServiceCollection());



        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void addGeneralLearningGoal(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        LearningGoal learningGoal = new LearningGoal();
        pedagogicalPlanTransfer.getAssessmentSetup().addGeneralLearningGoal(learningGoal);
        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();
    }

    private PedagogicalPlanTransfer getPedagogicalPlanTransfer(MissionSpecificationElo missionSpecificationElo) {
        URI pedagogicalPlanUri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        logger.info("**** PEDAGOGICAL PLAN URI: " + pedagogicalPlanUri);

        ScyElo pedagogicalPlanELO = ScyElo.loadLastVersionElo(pedagogicalPlanUri, getMissionELOService());
        String pedagogicalPlanXML = pedagogicalPlanELO.getContent().getXmlString();
        return (PedagogicalPlanTransfer) getXmlTransferObjectService().getObject(pedagogicalPlanXML);
    }

    private ScyElo getPedagogicalPlanEloForMission(MissionSpecificationElo missionSpecificationElo) {
        URI pedagogicalPlanUri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        logger.info("**** PEDAGOGICAL PLAN URI: " + pedagogicalPlanUri);
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
}
