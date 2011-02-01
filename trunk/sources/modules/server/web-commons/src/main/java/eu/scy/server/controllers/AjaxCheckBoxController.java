package eu.scy.server.controllers;

import eu.scy.core.AjaxPersistenceService;
import eu.scy.core.ScenarioService;
import eu.scy.core.model.ScyBase;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.lang.reflect.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.mar.2010
 * Time: 13:16:04
 * To change this template use File | Settings | File Templates.
 */
public class AjaxCheckBoxController extends AbstractAjaxController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {


        Boolean checked = Boolean.FALSE;
        String checkedString = httpServletRequest.getParameter(CHECKED);
        if(checkedString != null) {
            checked = Boolean.TRUE;
        }

        String clazz = httpServletRequest.getParameter(CLASS);
        String id = httpServletRequest.getParameter(ID);
        String property = httpServletRequest.getParameter(PROPERTY);

        if(clazz != null && id != null && property != null) {
            Class c = Class.forName(clazz);
            Object scyBase= getAjaxPersistenceService().get(c, id);
            logger.info("LOaded: " + scyBase);
            executeSetter(scyBase,property, checked);
            getAjaxPersistenceService().save(scyBase);
        }



        ModelAndView modelAndView = new ModelAndView();

        return modelAndView;
    }


     private Boolean executeSetter(Object object, String property, Boolean value) {
        if(property == null) return false;
        try {
            String firstLetter = property.substring(0,1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());
            Method method = object.getClass().getMethod("set" + property, Boolean.class);

            Boolean returnValue =  (Boolean) method.invoke(object, value);
            // System.out.println(method.getName() + " " + returnValue);
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("NOOO");
    }

}
