package eu.scy.server.controllers;

import eu.scy.core.LASService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 12:02:09
 * To change this template use File | Settings | File Templates.
 */
public class ViewLASController extends BaseController{

    private LASService lasService;




    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        String id = request.getParameter("id");
        logger.info("LAS ID: " + id);
        setModel(getLasService().getLearningActivitySpace(id));


        ModelAndView modelAndView = new ModelAndView();
        populateView(request, httpServletResponse, modelAndView);
        return modelAndView;


    }

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }
}
