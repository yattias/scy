package eu.scy.modules.useradmin.webservice;

import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.Constants;
import org.springframework.ws.server.endpoint.AbstractDomPayloadEndpoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.NodeList;
import org.dom4j.Node;

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
        String userName = null;
        String password = null;
        try {
            System.out.println("ELEMENT: " + element.getNodeName());
            NodeList nodes = element.getChildNodes();


            for (int counter = 0; counter < nodes.getLength(); counter++) {
                org.w3c.dom.Element n = (Element) nodes.item(counter);
                if (n.getNodeName().equals("UserName")) userName = n.getTextContent();
                else if (n.getNodeName().equals("Password")) password = n.getTextContent();
                System.out.println("NODE: " + n.getNodeName());

                System.out.println(n.getTextContent());
                /*Node e = (Node) n.getElementsByTagNameNS(Constants.NAME_SPACE, "Name");
                if (e != null) {
                    System.out.println("VALUE: " + e.getName());
                } */

            }

            System.out.println(document.toString());
            System.out.println(element.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Element e = document.getElementById("UserName");
        //System.out.println(e);
        //System.out.println("ELEMENT IS : " + e.getTextContent());


        Element root = document.createElementNS(Constants.NAME_SPACE, "LoginResponse");
        Element myResponse = document.createElementNS(Constants.NAME_SPACE, "LoginResponse");
        root.appendChild(myResponse);
        Element message = document.createElementNS(Constants.NAME_SPACE, "Message");
        myResponse.appendChild(message);
        Text responseText = document.createTextNode(getUserManagementService().loginUser(userName, password));
        message.appendChild(responseText);
        return root;

    }
}
