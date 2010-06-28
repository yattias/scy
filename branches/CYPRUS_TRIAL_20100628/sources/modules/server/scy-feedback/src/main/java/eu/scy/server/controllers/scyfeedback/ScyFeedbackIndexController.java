package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.ELORefService;
import eu.scy.core.FileService;
import eu.scy.core.PlayfulAssessmentService;
import eu.scy.core.model.ELORef;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.jun.2010
 * Time: 07:07:25
 * To change this template use File | Settings | File Templates.
 */
public class ScyFeedbackIndexController extends BaseController {

    private ELORefService eloRefService;
    private FileService fileService;
    private PlayfulAssessmentService playfulAssessmentService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        //modelAndView.addObject("eloRefs", getEloRefService().getELORefs());

        List transporters = new LinkedList();
        List eloRefs = getEloRefService().getELORefs();
        for (int i = 0; i < eloRefs.size(); i++) {
            ELORef eloRef = (ELORef) eloRefs.get(i);
            List files = getFileService().getFilesForELORef(eloRef);
            ELORefDataTransporter transporter = new ELORefDataTransporter();
            transporter.setEloRef(eloRef);
            transporter.setFiles(files);
            transporter.setTotalScore(getPlayfulAssessmentService().getScoreForELORef(eloRef));
            transporter.setTotalAssessments(getPlayfulAssessmentService().getAssesmentsForELORef(eloRef).size());
            if(transporter.getFiles() == null) transporter.setFiles(Collections.EMPTY_LIST);
            transporters.add(transporter);
        }

        modelAndView.addObject("eloRefTransporters", transporters);


    }


    public ELORefService getEloRefService() {
        return eloRefService;
    }

    public void setEloRefService(ELORefService eloRefService) {
        this.eloRefService = eloRefService;
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public PlayfulAssessmentService getPlayfulAssessmentService() {
        return playfulAssessmentService;
    }

    public void setPlayfulAssessmentService(PlayfulAssessmentService playfulAssessmentService) {
        this.playfulAssessmentService = playfulAssessmentService;
    }
}
