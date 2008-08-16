package eu.scy.modules.useradmin.webservice;

import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.Constants;
import org.springframework.ws.server.endpoint.AbstractDomPayloadEndpoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 15:03:08
 * A quick start for using the user dao from a web service.... simple stuff really - annotate methods as WebMethod and boom you go!
 */

public class UserManagementEndpoint extends AbstractDomPayloadEndpoint {



    private UserDAOHibernate userDAO;
    private UserManagementService userManagementService;

    public UserDAOHibernate getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOHibernate userDAO) {
        this.userDAO = userDAO;
    }

    public UserManagementService getUserManagementService() {
        return userManagementService;
    }

    public void setUserManagementService(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    protected Element invokeInternal(Element element, Document document) throws Exception {
        Element root = document.createElementNS(Constants.NAME_SPACE, "ServiceNameResponse");
        Element echoResponse = document.createElementNS(Constants.NAME_SPACE, "ServiceNameResponse" );
        root.appendChild(echoResponse);
        Element message =  document.createElementNS(Constants.NAME_SPACE, "Message");
        echoResponse.appendChild(message);
        Text responseText = document.createTextNode(getUserManagementService().serviceName());
        message.appendChild(responseText);
        return root;

    }
}
