package eu.scy.server.controllers;

import eu.scy.core.FileService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 14:24:37
 * To change this template use File | Settings | File Templates.
 */
public class ResourceService extends AbstractController{

    private FileService fileService;

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        FileStreamerView fileStreamerView = new FileStreamerView();
        fileStreamerView.setFileService(getFileService());
        return new ModelAndView(fileStreamerView);
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
