package eu.scy.server.controllers;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.nov.2010
 * Time: 13:13:05
 * To change this template use File | Settings | File Templates.
 */
public class AjaxELOTextFieldController extends AbstractAjaxELOController{

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
    String property = httpServletRequest.getParameter(PROPERTY);
    String uri = httpServletRequest.getParameter(URI);
    String value = httpServletRequest.getParameter("value");
    logger.info("VALUE:"  + value);
    logger.info("URI: " + uri);
    logger.info("PROPERTY: " + property);

    executeSetter(uri, property, value);
/*
    if(clazz != null && id != null && property != null) {
        Class c = Class.forName(clazz);
        Object scyBase= getAjaxPersistenceService().get(c, id);
        executeSetter(scyBase,property, value);
        getAjaxPersistenceService().save(scyBase);
    }
  */
    ModelAndView modelAndView = new ModelAndView();
    return modelAndView;

}

}
