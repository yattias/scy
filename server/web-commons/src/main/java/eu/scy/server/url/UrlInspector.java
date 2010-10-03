package eu.scy.server.url;

import eu.scy.core.AjaxPersistenceService;
import eu.scy.core.model.ScyBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.apr.2010
 * Time: 09:45:20
 * To change this template use File | Settings | File Templates.
 */
public class UrlInspector {

    private AjaxPersistenceService service;

    public Object instpectRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) {
         String modelString = request.getParameter("model");
        if(modelString != null) {
            String type = modelString.substring(0, modelString.indexOf("_"));
            String id = modelString.substring(modelString.indexOf("_")+1, modelString.length());
            try {
                Class clazz = Class.forName(type);
                Object model =  getService().get(clazz, id);

                request.setAttribute("modelObject", model);

                return model;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  
            }
        }
        return null;
    }

    public AjaxPersistenceService getService() {
        return service;
    }

    public void setService(AjaxPersistenceService service) {
        this.service = service;
    }
}
