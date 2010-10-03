package eu.scy.server.controllers;

import eu.scy.core.AjaxPersistenceService;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 07:10:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAjaxController extends AbstractController {

    public final static String CHECKED = "ajaxCheckBoxValue";
    public final static String CLASS = "clazz";
    public final static String ID = "id";
    public final static String PROPERTY = "property";

    private AjaxPersistenceService ajaxPersistenceService;

    public AjaxPersistenceService getAjaxPersistenceService() {
        return ajaxPersistenceService;
    }

    public void setAjaxPersistenceService(AjaxPersistenceService ajaxPersistenceService) {
        this.ajaxPersistenceService = ajaxPersistenceService;
    }
}
