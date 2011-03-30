package eu.scy.server.controllers;

import eu.scy.core.model.ScyBase;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind
 * Date: 21.apr.2010
 * Time: 07:09:55
 */
public class AjaxDatePickerController extends AbstractAjaxController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String clazz = httpServletRequest.getParameter(CLASS);
        String id = httpServletRequest.getParameter(ID);
        String property = httpServletRequest.getParameter(PROPERTY);
        String value = httpServletRequest.getParameter("date");

        if(clazz != null && id != null && property != null) {
            Class c = Class.forName(clazz);
            ScyBase scyBase= getAjaxPersistenceService().get(c, id);
            executeSetter(scyBase,property, value);
            getAjaxPersistenceService().save(scyBase);
        }

        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;

    }

    private Boolean executeSetter(Object object, String property, String value) {
       if(property == null) return null;
       try {
           String firstLetter = property.substring(0,1);
           firstLetter = firstLetter.toUpperCase();
           property = firstLetter + property.substring(1, property.length());
           Method method = object.getClass().getMethod("set" + property, Date.class);

           String year = value.substring(0,4);
           String month = value.substring(5,7);
           String day = value.substring(8,10);
           System.out.println(year);
           System.out.println(month);
           System.out.println(day);
           if (month.startsWith("0")) {
               month = month.substring(1,2);
           }
        GregorianCalendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));

           if (day.startsWith("0")) {
               day = day.substring(1,2);
           }

           System.out.println(method.getName() + String.valueOf(calendar.getTime()));

           Boolean returnValue =  (Boolean) method.invoke(object, calendar.getTime());

           return returnValue;
       } catch (Exception e) {
           e.printStackTrace();
       }
       throw new RuntimeException("NOOO");
   }

}