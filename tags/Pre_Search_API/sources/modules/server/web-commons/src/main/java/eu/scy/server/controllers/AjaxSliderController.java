package eu.scy.server.controllers;

import eu.scy.core.model.ScyBase;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 11:02:07
 */
public class AjaxSliderController extends AbstractAjaxController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String clazz = httpServletRequest.getParameter(CLASS);
        String id = httpServletRequest.getParameter(ID);
        String property = httpServletRequest.getParameter(PROPERTY);
        String value = httpServletRequest.getParameter("value");

        if (clazz != null && id != null && property != null) {
            Class c = Class.forName(clazz);
            Object scyBase = getAjaxPersistenceService().get(c, id);
            executeSetter(scyBase, property, value);
            getAjaxPersistenceService().save(scyBase);
        }

        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    private Boolean executeSetter(Object object, String property, String value) {
        if (property == null) return false;
        try {
            String firstLetter = property.substring(0, 1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());
            Method method = object.getClass().getMethod("set" + property, String.class);

            Boolean returnValue = (Boolean) method.invoke(object, value);
            // System.out.println(method.getName() + " " + returnValue);
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("NOOO");
    }
}