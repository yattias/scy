package eu.scy.server.controllers;

import eu.scy.core.model.ScyBase;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.sql.Time;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind
 * Date: 21.apr.2010
 * Time: 07:09:55
 */
public class AjaxTimePickerController extends AbstractAjaxController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String clazz = httpServletRequest.getParameter(CLASS);
        String id = httpServletRequest.getParameter(ID);
        String property = httpServletRequest.getParameter(PROPERTY);
        String value = httpServletRequest.getParameter("time");

        if(clazz != null && id != null && property != null) {
            Class c = Class.forName(clazz);
            Object scyBase= getAjaxPersistenceService().get(c, id);
            executeSetter(scyBase,property, value);
            getAjaxPersistenceService().save(scyBase);
        }

        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;

    }

    private Boolean executeSetter(Object object, String property, String value) {


       if(property == null || value == null) return null;
        value = value.replace(".", ":") + ":00";
        // System.out.println("VVVAAAALLUUUE: " + value);
       try {
           String firstLetter = property.substring(0,1);
           firstLetter = firstLetter.toUpperCase();
           property = firstLetter + property.substring(1, property.length());
           Method method = object.getClass().getMethod("set" + property, Time.class);

           // System.out.println(method.getName() + String.valueOf(value) + " TIME: " + Time.valueOf(value.substring(1, value.length())));
           
           Boolean returnValue =  (Boolean) method.invoke(object, Time.valueOf(value.substring(1, value.length())));

           return returnValue;
       } catch (Exception e) {
           e.printStackTrace();
       }
       throw new RuntimeException("NOOO");
   }

}