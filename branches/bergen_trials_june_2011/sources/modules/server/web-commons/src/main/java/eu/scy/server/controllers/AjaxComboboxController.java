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
 * Time: 10:11:03
 */
public class AjaxComboboxController extends AbstractAjaxController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String clazz = httpServletRequest.getParameter(CLASS);
        String id = httpServletRequest.getParameter(ID);
        String property = httpServletRequest.getParameter(PROPERTY);
        String value = httpServletRequest.getParameter("value");
        String setterClass = httpServletRequest.getParameter("setterClass");

        if (clazz != null && id != null && property != null) {
            Class c = Class.forName(clazz);
            Object scyBase = getAjaxPersistenceService().get(c, id);
            executeSetter(scyBase, property, value, setterClass);
            getAjaxPersistenceService().save(scyBase);
        }

        return new ModelAndView();
    }

    private Boolean executeSetter(Object object, String property, Object value, String setterClass) {
        if (property == null) return false;
        try {
            String firstLetter = property.substring(0, 1);
            firstLetter = firstLetter.toUpperCase();

            property = firstLetter + property.substring(1, property.length());

            Class setterCl = Class.forName(setterClass);
            Method method = object.getClass().getMethod("set" + property, setterCl);

            try {
                Boolean returnValue = (Boolean) method.invoke(object, value);
                // System.out.println(method.getName() + " " + returnValue);
                return returnValue;
            } catch (IllegalArgumentException iae) {
                if (setterCl.isEnum()) {
                    Object objects[] = setterCl.getEnumConstants();
                    for (Object o : objects) {
                        // System.out.println(o);
                        if (o.toString().equals(value)) {
                            Boolean returnValue = (Boolean) method.invoke(object, o);
                            // System.out.println(method.getName() + " " + returnValue);
                            return returnValue;
                        }
                    }
                }
                throw new RuntimeException("NOOO NOOO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("NOOO");
    }
}