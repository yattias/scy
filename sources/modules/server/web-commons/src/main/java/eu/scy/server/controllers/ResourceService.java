package eu.scy.server.controllers;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.FileService;
import eu.scy.core.roolo.MissionELOService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 14:24:37
 * To change this template use File | Settings | File Templates.
 */
public class ResourceService extends AbstractController{

    private FileService fileService;
    private MissionELOService missionELOService;

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        FileStreamerView fileStreamerView = new FileStreamerView();
        fileStreamerView.setFileService(getFileService());

        if(httpServletRequest.getParameter("eloURI") != null) {
            logger.info("LOADING ELO IMAGE!");

            String u = httpServletRequest.getParameter("eloURI");
            u = URLDecoder.decode(u, "UTF-8");

            URI uri = new URI(u);

            ScyElo scyElo = ScyElo.loadLastVersionElo(uri, getMissionELOService());
            if(scyElo.getThumbnail() != null) {
                fileStreamerView.setEloImage(scyElo.getThumbnail());
            }
        }

        return new ModelAndView(fileStreamerView);
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
