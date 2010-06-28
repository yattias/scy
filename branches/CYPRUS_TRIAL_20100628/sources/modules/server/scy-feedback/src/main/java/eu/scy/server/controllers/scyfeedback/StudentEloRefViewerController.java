package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.ELORefService;
import eu.scy.core.FileService;
import eu.scy.core.PlayfulAssessmentService;
import eu.scy.core.model.ELORef;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.jun.2010
 * Time: 06:31:39
 * To change this template use File | Settings | File Templates.
 */
public class StudentEloRefViewerController  extends BaseController {

    private FileService fileService;
    private ELORefService eloRefService;
    private PlayfulAssessmentService playfulAssessmentService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        logger.info("MODEL  IS: " + getModel());

        ELORefDataTransporter transporter = new ELORefDataTransporter();
        transporter.setEloRef((ELORef) getModel());
        ELORef eloRef = (ELORef) getModel();
        eloRef.setViewings(eloRef.getViewings() + 1);
        getEloRefService().save(eloRef);
        transporter.setFiles(getFileService().getFilesForELORef(eloRef));
        transporter.setTotalScore(getPlayfulAssessmentService().getScoreForELORef(eloRef));

        transporter.setAssessments(getPlayfulAssessmentService().getAssesmentsForELORef(eloRef));
        logger.info("I found " + getPlayfulAssessmentService().getAssesmentsForELORef(eloRef).size() + " ASSESSMENTS!!");

        modelAndView.addObject("transporter", transporter);
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public ELORefService getEloRefService() {
        return eloRefService;
    }

    public void setEloRefService(ELORefService eloRefService) {
        this.eloRefService = eloRefService;
    }

    public PlayfulAssessmentService getPlayfulAssessmentService() {
        return playfulAssessmentService;
    }

    public void setPlayfulAssessmentService(PlayfulAssessmentService playfulAssessmentService) {
        this.playfulAssessmentService = playfulAssessmentService;
    }
}
