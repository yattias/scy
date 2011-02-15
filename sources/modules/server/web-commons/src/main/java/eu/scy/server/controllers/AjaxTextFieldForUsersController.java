package eu.scy.server.controllers;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.feb.2011
 * Time: 06:11:27
 * To change this template use File | Settings | File Templates.
 */
public class AjaxTextFieldForUsersController extends AbstractAjaxController {

    private UserService userService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String clazz = httpServletRequest.getParameter(CLASS);
        String id = httpServletRequest.getParameter(ID);
        String property = httpServletRequest.getParameter(PROPERTY);
        String value = httpServletRequest.getParameter("value");
        logger.info("VALUE:"  + value);
        logger.info("ID:" + id);


        if(clazz != null && id != null && property != null) {
            Class c = Class.forName(clazz);
            User user = getUserService().getUser(id);
            executeSetter(user.getUserDetails() ,property, value);
            getUserService().save(user);
        }

        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;

    }

    private Boolean executeSetter(Object object, String property, String value) {
       if(property == null) return false;
       try {
           String firstLetter = property.substring(0,1);
           firstLetter = firstLetter.toUpperCase();

           property = firstLetter + property.substring(1, property.length());
           Method method = object.getClass().getMethod("set" + property, String.class);

           Boolean returnValue =  (Boolean) method.invoke(object, value);
           // System.out.println(method.getName() + " " + returnValue);
           return returnValue;
       } catch (Exception e) {
           e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
       }
       throw new RuntimeException("NOOO");
   }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
